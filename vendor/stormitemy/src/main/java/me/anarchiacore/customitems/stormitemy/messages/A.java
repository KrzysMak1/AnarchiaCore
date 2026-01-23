/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  org.bukkit.configuration.ConfigurationSection
 */
package me.anarchiacore.customitems.stormitemy.messages;

import org.bukkit.configuration.ConfigurationSection;

public class A {
    private String D;
    private String G;
    private String E;
    private String C;
    private int B;
    private int A;
    private int F;

    public A(String string) {
        this.D = string;
        this.B = 10;
        this.A = 70;
        this.F = 20;
    }

    public A(ConfigurationSection configurationSection) {
        this.D = configurationSection.getString("chat");
        this.G = configurationSection.getString("title");
        this.E = configurationSection.getString("subtitle");
        this.C = configurationSection.getString("actionbar");
        this.B = configurationSection.getInt("fade-in", 10);
        this.A = configurationSection.getInt("stay", 70);
        this.F = configurationSection.getInt("fade-out", 20);
    }

    public String F() {
        return this.D;
    }

    public String E() {
        return this.G;
    }

    public String G() {
        return this.E;
    }

    public String C() {
        return this.C;
    }

    public int I() {
        return this.B;
    }

    public int A() {
        return this.A;
    }

    public int B() {
        return this.F;
    }

    public boolean D() {
        return this.D != null && !this.D.isEmpty();
    }

    public boolean H() {
        return this.G != null && !this.G.isEmpty();
    }

    public boolean K() {
        return this.E != null && !this.E.isEmpty();
    }

    public boolean J() {
        return this.C != null && !this.C.isEmpty();
    }

    public static class _A {
        private String D;
        private String G;
        private String E;
        private String C;
        private int B = 10;
        private int A = 70;
        private int F = 20;

        public _A D(String string) {
            this.D = string;
            return this;
        }

        public _A A(String string) {
            this.G = string;
            return this;
        }

        public _A B(String string) {
            this.E = string;
            return this;
        }

        public _A C(String string) {
            this.C = string;
            return this;
        }

        public _A A(int n2) {
            this.B = n2;
            return this;
        }

        public _A B(int n2) {
            this.A = n2;
            return this;
        }

        public _A C(int n2) {
            this.F = n2;
            return this;
        }

        public A A() {
            A a2 = new A("");
            a2.D = this.D;
            a2.G = this.G;
            a2.E = this.E;
            a2.C = this.C;
            a2.B = this.B;
            a2.A = this.A;
            a2.F = this.F;
            return a2;
        }
    }
}

