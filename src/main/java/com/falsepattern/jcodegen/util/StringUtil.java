package com.falsepattern.jcodegen.util;

import lombok.val;

public class StringUtil {
    private static final int HIGHEST_GENERATED_PREFIX = 16;
    private static final String[] prefixes = new String[HIGHEST_GENERATED_PREFIX + 1];
    static {
        val pfb = new StringBuilder();
        for (int i = 0; i <= HIGHEST_GENERATED_PREFIX; i++) {
            prefixes[i] = pfb.toString();
            pfb.append(' ');
        }
    }
    private static String getPrefix(int indent) {
        if (indent <= HIGHEST_GENERATED_PREFIX) {
            return prefixes[indent];
        } else {
            StringBuilder result = new StringBuilder(indent);
            while (indent > HIGHEST_GENERATED_PREFIX) {
                result.append(prefixes[HIGHEST_GENERATED_PREFIX]);
                indent -= HIGHEST_GENERATED_PREFIX;
            }
            result.append(prefixes[indent]);
            return result.toString();
        }
    }
    public static String indent(String str, int indent) {
        val prefix = getPrefix(indent);
        val lines = str.split("\r|\r\n|\n");
        val b = new StringBuilder(str.length() + indent * lines.length);
        for (val line: lines) {
            b.append(prefix);
            b.append(line);
            b.append('\n');
        }
        return b.toString();
    }
}
