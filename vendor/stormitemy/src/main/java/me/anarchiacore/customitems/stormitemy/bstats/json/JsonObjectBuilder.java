/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.CharSequence
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.util.Arrays
 *  java.util.stream.Collectors
 */
package me.anarchiacore.customitems.stormitemy.bstats.json;

import java.util.Arrays;
import java.util.stream.Collectors;

public class JsonObjectBuilder {
    private StringBuilder B = new StringBuilder();
    private boolean A = false;

    public JsonObjectBuilder() {
        this.B.append("{");
    }

    public JsonObjectBuilder appendNull(String string) {
        this.A(string, "null");
        return this;
    }

    public JsonObjectBuilder appendField(String string, String string2) {
        if (string2 == null) {
            throw new IllegalArgumentException("JSON value must not be null");
        }
        this.A(string, "\"" + JsonObjectBuilder.B(string2) + "\"");
        return this;
    }

    public JsonObjectBuilder appendField(String string, int n2) {
        this.A(string, String.valueOf((int)n2));
        return this;
    }

    public JsonObjectBuilder appendField(String string, _A _A2) {
        if (_A2 == null) {
            throw new IllegalArgumentException("JSON object must not be null");
        }
        this.A(string, _A2.toString());
        return this;
    }

    public JsonObjectBuilder appendField(String string2, String[] stringArray) {
        if (stringArray == null) {
            throw new IllegalArgumentException("JSON values must not be null");
        }
        String string3 = (String)Arrays.stream((Object[])stringArray).map(string -> "\"" + JsonObjectBuilder.B(string) + "\"").collect(Collectors.joining((CharSequence)","));
        this.A(string2, "[" + string3 + "]");
        return this;
    }

    public JsonObjectBuilder appendField(String string, int[] nArray) {
        if (nArray == null) {
            throw new IllegalArgumentException("JSON values must not be null");
        }
        String string2 = (String)Arrays.stream((int[])nArray).mapToObj(String::valueOf).collect(Collectors.joining((CharSequence)","));
        this.A(string, "[" + string2 + "]");
        return this;
    }

    public JsonObjectBuilder appendField(String string, _A[] _AArray) {
        if (_AArray == null) {
            throw new IllegalArgumentException("JSON values must not be null");
        }
        String string2 = (String)Arrays.stream((Object[])_AArray).map(_A::toString).collect(Collectors.joining((CharSequence)","));
        this.A(string, "[" + string2 + "]");
        return this;
    }

    private void A(String string, String string2) {
        if (this.B == null) {
            throw new IllegalStateException("JSON has already been built");
        }
        if (string == null) {
            throw new IllegalArgumentException("JSON key must not be null");
        }
        if (this.A) {
            this.B.append(",");
        }
        this.B.append("\"").append(JsonObjectBuilder.B(string)).append("\":").append(string2);
        this.A = true;
    }

    public _A build() {
        if (this.B == null) {
            throw new IllegalStateException("JSON has already been built");
        }
        _A _A2 = new _A(this.B.append("}").toString());
        this.B = null;
        return _A2;
    }

    private static String B(String string) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i2 = 0; i2 < string.length(); ++i2) {
            char c2 = string.charAt(i2);
            if (c2 == '\"') {
                stringBuilder.append("\\\"");
                continue;
            }
            if (c2 == '\\') {
                stringBuilder.append("\\\\");
                continue;
            }
            if (c2 <= '\u000f') {
                stringBuilder.append("\\u000").append(Integer.toHexString((int)c2));
                continue;
            }
            if (c2 <= '\u001f') {
                stringBuilder.append("\\u00").append(Integer.toHexString((int)c2));
                continue;
            }
            stringBuilder.append(c2);
        }
        return stringBuilder.toString();
    }

    public static class _A {
        private final String A;

        private _A(String string) {
            this.A = string;
        }

        public String toString() {
            return this.A;
        }
    }
}

