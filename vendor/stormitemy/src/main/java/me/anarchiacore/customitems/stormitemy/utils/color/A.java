/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.CharSequence
 *  java.lang.Character
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuffer
 *  java.lang.StringBuilder
 *  java.util.ArrayList
 *  java.util.List
 *  java.util.regex.Matcher
 *  java.util.regex.Pattern
 *  org.bukkit.ChatColor
 */
package me.anarchiacore.customitems.stormitemy.utils.color;

import java.util.ArrayList;
import java.util.List;
import me.anarchiacore.util.MiniMessageUtil;
import org.bukkit.ChatColor;

public class A {
    private static final String B = "c3Rvcm1jb2Rl";

    public static String C(String string) {
        return MiniMessageUtil.parseLegacy(string);
    }

    public static List<String> A(List<String> list) {
        ArrayList arrayList = new ArrayList();
        for (String string : list) {
            arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C(string));
        }
        return arrayList;
    }

    public static String B(String string) {
        if (string == null) {
            return "";
        }
        String string2 = string.replaceAll("&#[A-Fa-f0-9]{6}", "");
        string2 = string2.replaceAll("\u00a7x(\u00a7[A-Fa-f0-9]){6}", "");
        string2 = ChatColor.stripColor((String)string2);
        return string2;
    }

    public static String D(String string) {
        if (string == null) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        block28: for (char c2 : string.toCharArray()) {
            switch (Character.toLowerCase((char)c2)) {
                case 'a': {
                    stringBuilder.append('\u1d00');
                    continue block28;
                }
                case 'b': {
                    stringBuilder.append('\u0299');
                    continue block28;
                }
                case 'c': {
                    stringBuilder.append('\u1d04');
                    continue block28;
                }
                case 'd': {
                    stringBuilder.append('\u1d05');
                    continue block28;
                }
                case 'e': {
                    stringBuilder.append('\u1d07');
                    continue block28;
                }
                case 'f': {
                    stringBuilder.append('\ua730');
                    continue block28;
                }
                case 'g': {
                    stringBuilder.append('\u0262');
                    continue block28;
                }
                case 'h': {
                    stringBuilder.append('\u029c');
                    continue block28;
                }
                case 'i': {
                    stringBuilder.append('\u026a');
                    continue block28;
                }
                case 'j': {
                    stringBuilder.append('\u1d0a');
                    continue block28;
                }
                case 'k': {
                    stringBuilder.append('\u1d0b');
                    continue block28;
                }
                case 'l': {
                    stringBuilder.append('\u029f');
                    continue block28;
                }
                case 'm': {
                    stringBuilder.append('\u1d0d');
                    continue block28;
                }
                case 'n': {
                    stringBuilder.append('\u0274');
                    continue block28;
                }
                case 'o': {
                    stringBuilder.append('\u1d0f');
                    continue block28;
                }
                case 'p': {
                    stringBuilder.append('\u1d18');
                    continue block28;
                }
                case 'q': {
                    stringBuilder.append('\u01eb');
                    continue block28;
                }
                case 'r': {
                    stringBuilder.append('\u0280');
                    continue block28;
                }
                case 's': {
                    stringBuilder.append('\ua731');
                    continue block28;
                }
                case 't': {
                    stringBuilder.append('\u1d1b');
                    continue block28;
                }
                case 'u': {
                    stringBuilder.append('\u1d1c');
                    continue block28;
                }
                case 'v': {
                    stringBuilder.append('\u1d20');
                    continue block28;
                }
                case 'w': {
                    stringBuilder.append('\u1d21');
                    continue block28;
                }
                case 'x': {
                    stringBuilder.append('x');
                    continue block28;
                }
                case 'y': {
                    stringBuilder.append('\u028f');
                    continue block28;
                }
                case 'z': {
                    stringBuilder.append('\u1d22');
                    continue block28;
                }
                default: {
                    stringBuilder.append(c2);
                }
            }
        }
        return stringBuilder.toString();
    }
}
