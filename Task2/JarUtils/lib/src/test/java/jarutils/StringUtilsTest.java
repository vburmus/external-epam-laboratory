package jarutils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class StringUtilsTest {

    @Test
    void isPositiveNumber() {

        Assertions.assertFalse(StringUtils.isPositiveNumber("Volcano"));
        Assertions.assertFalse(StringUtils.isPositiveNumber(null));
        Assertions.assertFalse(StringUtils.isPositiveNumber("-999"));
        Assertions.assertFalse(StringUtils.isPositiveNumber(""));
        Assertions.assertTrue(StringUtils.isPositiveNumber("1"));
    }
}