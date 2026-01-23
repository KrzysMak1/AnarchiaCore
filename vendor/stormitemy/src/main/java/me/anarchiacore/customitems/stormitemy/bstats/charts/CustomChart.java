/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Exception
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 *  java.util.function.BiConsumer
 */
package me.anarchiacore.customitems.stormitemy.bstats.charts;

import java.util.function.BiConsumer;
import me.anarchiacore.customitems.stormitemy.bstats.json.JsonObjectBuilder;

public abstract class CustomChart {
    private final String A;

    protected CustomChart(String string) {
        if (string == null) {
            throw new IllegalArgumentException("chartId must not be null");
        }
        this.A = string;
    }

    public JsonObjectBuilder._A getRequestJsonObject(BiConsumer<String, Throwable> biConsumer, boolean bl) {
        JsonObjectBuilder jsonObjectBuilder = new JsonObjectBuilder();
        jsonObjectBuilder.appendField("chartId", this.A);
        try {
            JsonObjectBuilder._A _A2 = this.A();
            if (_A2 == null) {
                return null;
            }
            jsonObjectBuilder.appendField("data", _A2);
        }
        catch (Throwable throwable) {
            if (bl) {
                biConsumer.accept((Object)("Failed to get data for custom chart with id " + this.A), (Object)throwable);
            }
            return null;
        }
        return jsonObjectBuilder.build();
    }

    protected abstract JsonObjectBuilder._A A() throws Exception;
}

