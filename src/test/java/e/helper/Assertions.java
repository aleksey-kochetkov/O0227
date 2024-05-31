package e.helper; 

import java.math.BigDecimal;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.fail;

public class Assertions {

    public static void assertEquals(BigDecimal expected, BigDecimal actual) {
        assertEquals(expected, actual, (String)null);
    }

    static void assertEquals(BigDecimal expected, BigDecimal actual, String message) {
        if (expected == null ? actual != null : actual == null || expected.compareTo(actual) != 0) {
            failNotEqual(expected, actual, message);
        }
    }

    static void failNotEqual(Object expected, Object actual, String message) {
        fail(() -> format(expected, actual, message));
    }

    static String format(Object expected, Object actual, String message) {
        return buildPrefix(message) + formatValues(expected, actual);
    }

    static String formatValues(Object expected, Object actual) {
        String expectedString = Objects.toString(expected);
        String actualString = Objects.toString(actual);
        if (expectedString.equals(actualString)) {
            return String.format("expected: %s but was: %s", formatClassAndValue(expected, expectedString),
                    formatClassAndValue(actual, actualString));
        }
        return String.format("expected: <%s> but was: <%s>", expectedString, actualString);
    }

    static String buildPrefix(String message) {
//        return (StringUtils.isNotBlank(message) ? message + " ==> " : "");
        return "";
    }

    private static String formatClassAndValue(Object value, String valueString) {
        // If the value is null, return <null> instead of null<null>.
        if (value == null) {
            return "<null>";
        }
        String classAndHash = getClassName(value) + toHash(value);
        // if it's a class, there's no need to repeat the class name contained in the valueString.
        return (value instanceof Class ? "<" + classAndHash + ">" : classAndHash + "<" + valueString + ">");
    }

    private static String getClassName(Object obj) {
        return (obj == null ? "null"
          : obj instanceof Class ? getCanonicalName((Class<?>) obj) : obj.getClass().getName());
    }

    private static String toHash(Object obj) {
        return (obj == null ? "" : "@" + Integer.toHexString(System.identityHashCode(obj)));
    }

    static String getCanonicalName(Class<?> clazz) {
        try {
            String canonicalName = clazz.getCanonicalName();
            return (canonicalName != null ? canonicalName : clazz.getName());
        }
        catch (Throwable t) {
//            UnrecoverableExceptions.rethrowIfUnrecoverable(t);
            return clazz.getName();
        }
    }
}