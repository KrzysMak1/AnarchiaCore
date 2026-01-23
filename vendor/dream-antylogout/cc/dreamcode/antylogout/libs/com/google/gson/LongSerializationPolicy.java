package cc.dreamcode.antylogout.libs.com.google.gson;

public enum LongSerializationPolicy
{
    DEFAULT {
        @Override
        public JsonElement serialize(final Long value) {
            if (value == null) {
                return JsonNull.INSTANCE;
            }
            return new JsonPrimitive((Number)value);
        }
    }, 
    STRING {
        @Override
        public JsonElement serialize(final Long value) {
            if (value == null) {
                return JsonNull.INSTANCE;
            }
            return new JsonPrimitive(value.toString());
        }
    };
    
    public abstract JsonElement serialize(final Long p0);
}
