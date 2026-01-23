package cc.dreamcode.antylogout.libs.com.google.gson;

import java.math.BigDecimal;
import cc.dreamcode.antylogout.libs.com.google.gson.stream.MalformedJsonException;
import cc.dreamcode.antylogout.libs.com.google.gson.internal.LazilyParsedNumber;
import java.io.IOException;
import cc.dreamcode.antylogout.libs.com.google.gson.stream.JsonReader;

public enum ToNumberPolicy implements ToNumberStrategy
{
    DOUBLE {
        public Double readNumber(final JsonReader in) throws IOException {
            return in.nextDouble();
        }
    }, 
    LAZILY_PARSED_NUMBER {
        public Number readNumber(final JsonReader in) throws IOException {
            return new LazilyParsedNumber(in.nextString());
        }
    }, 
    LONG_OR_DOUBLE {
        public Number readNumber(final JsonReader in) throws IOException, JsonParseException {
            final String value = in.nextString();
            try {
                return (Number)Long.valueOf(Long.parseLong(value));
            }
            catch (final NumberFormatException longE) {
                try {
                    final Double d = Double.valueOf(value);
                    if ((d.isInfinite() || d.isNaN()) && !in.isLenient()) {
                        throw new MalformedJsonException("JSON forbids NaN and infinities: " + (Object)d + "; at path " + in.getPreviousPath());
                    }
                    return (Number)d;
                }
                catch (final NumberFormatException doubleE) {
                    throw new JsonParseException("Cannot parse " + value + "; at path " + in.getPreviousPath(), (Throwable)doubleE);
                }
            }
        }
    }, 
    BIG_DECIMAL {
        public BigDecimal readNumber(final JsonReader in) throws IOException {
            final String value = in.nextString();
            try {
                return new BigDecimal(value);
            }
            catch (final NumberFormatException e) {
                throw new JsonParseException("Cannot parse " + value + "; at path " + in.getPreviousPath(), (Throwable)e);
            }
        }
    };
}
