package hiscore.utils;

import hiscore.official.HiscoreType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class NameUtilitiesTest {

    @Test
    void testToConsistentFormat() {
        Assertions.assertEquals("test__username", NameUtilities.toConsistentFormat("test_!username"));
        Assertions.assertEquals("test__username", NameUtilities.toConsistentFormat("TEST  USERNAME"));
        Assertions.assertEquals("test__username", NameUtilities.toConsistentFormat("   tEst_@USERname "));
    }

    @Test
    void testToConsistentFormatWithType() {
        Assertions.assertEquals(
                "test_username_NORMAL",
                NameUtilities.toConsistentFormat("test_username", HiscoreType.NORMAL));
        Assertions.assertEquals(
                "test_username_HARDCORE_IRONMAN",
                NameUtilities.toConsistentFormat("TEST USERNAME", HiscoreType.HARDCORE_IRONMAN));
        Assertions.assertEquals(
                "test_username_TOURNAMENT",
                NameUtilities.toConsistentFormat("   tEst@USERname ", HiscoreType.TOURNAMENT));
    }
}