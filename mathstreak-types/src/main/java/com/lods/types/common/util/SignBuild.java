package com.lods.types.common.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeSet;

public final class SignBuild {

    private static final String SECRET = "e275f1af-1dda-4b47-9d87-afcbe1f96dca";

    /**
     * 根据参数生成 sign
     * 规则与 Python 版本保持一致：
     * 1. 过滤 null / 空串 / sign 字段
     * 2. key 按 case-insensitive 排序，若忽略大小写相同，则按原 key 排序
     * 3. baseString = key=value 以 & 拼接
     * 4. choice = token 优先，否则 dateTime
     * 5. AndroidVersionName 去掉前缀 V/v，再去掉所有点号
     * 6. canonical = baseString & choice & SECRET & cleanedVersion
     * 7. SHA-256 hex
     */
    public static String generateSign(Map<String, String> params) {
        String canonical = buildCanonicalString(params);
        return sha256Hex(canonical);
    }

    /**
     * 如需调试可直接查看规范化字符串
     */
    public static String buildCanonicalString(Map<String, String> params) {
        if (params == null || params.isEmpty()) {
            return buildCanonicalStringFromParts("", "", "", "");
        }

        TreeSet<String> keysToSign = new TreeSet<>(new Comparator<>() {
            @Override
            public int compare(String a, String b) {
                int c = a.compareToIgnoreCase(b);
                if (c != 0) return c;
                return a.compareTo(b);
            }
        });

        for (Map.Entry<String, String> entry : params.entrySet()) {
            String k = entry.getKey();
            String v = entry.getValue();
            if (k == null) continue;
            if ("sign".equals(k)) continue;
            if (v == null || v.trim().isEmpty()) continue;
            keysToSign.add(k);
        }

        StringBuilder base = new StringBuilder();
        boolean first = true;
        for (String k : keysToSign) {
            if (!first) {
                base.append("&");
            }
            base.append(k).append("=").append(params.get(k));
            first = false;
        }

        String token = safeGet(params, "token");
        String dateTime = safeGet(params, "dateTime");
        String choice = !token.isEmpty() ? token : dateTime;

        String versionName = safeGet(params, "AndroidVersionName");
        String cleanedVersion = cleanAndroidVersionName(versionName);

        return buildCanonicalStringFromParts(base.toString(), choice, SECRET, cleanedVersion);
    }

    private static String buildCanonicalStringFromParts(String baseString, String choice, String secret, String cleanedVersion) {
        return baseString + "&" + choice + "&" + secret + "&" + cleanedVersion;
    }

    private static String safeGet(Map<String, String> params, String key) {
        String v = params.get(key);
        return v == null ? "" : v;
    }

    /**
     * 等价于 Python 的:
     * version_name.lstrip("Vv").replace(".", "")
     * 注意：lstrip("Vv") 会连续去掉前缀里的 V/v，不只是一个字符。
     */
    private static String cleanAndroidVersionName(String versionName) {
        if (versionName == null || versionName.isEmpty()) {
            return "";
        }
        int i = 0;
        while (i < versionName.length()) {
            char ch = versionName.charAt(i);
            if (ch == 'V' || ch == 'v') {
                i++;
            } else {
                break;
            }
        }
        String trimmed = versionName.substring(i);
        return trimmed.replace(".", "");
    }

    private static String sha256Hex(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder(hash.length * 2);
            for (byte b : hash) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 不可用", e);
        }
    }
}
