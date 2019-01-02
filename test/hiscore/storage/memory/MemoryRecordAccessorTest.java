package hiscore.storage.memory;

import hiscore.model.PlayerListing;
import hiscore.official.HiscoreType;
import hiscore.storage.PlayerRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Instant;
import java.util.Collections;

class MemoryRecordAccessorTest {

    private static final String TEST_NAME = "scape_xp";
    private static final HiscoreType TEST_TYPE = HiscoreType.NORMAL;
    private final MemoryRecordAccessor recordAccessor = new MemoryRecordAccessor();

    @Test
    void testEmptyRecord() {
        assertEmptyRecord(getRecord());
    }

    @Test
    void testUpdateRecord() throws IOException {
        PlayerRecord record = getRecord();
        assertEmptyRecord(record);
        updateRecord();
        record = getRecord();
        Assertions.assertEquals(1, record.listingsBetween(Instant.MIN, Instant.MAX).size());
    }

    @Test
    void testRecordOrdering() throws Exception {
        updateRecord();
        PlayerListing firstListing = getRecord().listingsBetween(Instant.MIN, Instant.now()).get(0);
        Thread.sleep(2000);
        updateRecord();
        PlayerRecord record = getRecord();
        Assertions.assertEquals(Collections.singletonList(firstListing), record.listingsBetween(Instant.MIN, firstListing.timestamp().plusSeconds(1)));
    }

    private PlayerRecord getRecord() {
        return recordAccessor.getRecord(TEST_NAME, TEST_TYPE);
    }

    private void updateRecord() throws IOException {
        recordAccessor.updateRecord(TEST_NAME, TEST_TYPE);
    }

    private void assertEmptyRecord(PlayerRecord record) {
        Assertions.assertTrue(record.listingsBetween(Instant.MIN, Instant.MAX).isEmpty());
    }
}