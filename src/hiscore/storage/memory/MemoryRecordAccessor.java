package hiscore.storage.memory;

import hiscore.official.HiscoreType;
import hiscore.storage.PlayerRecord;
import hiscore.storage.RecordAccessor;
import hiscore.utils.NameUtilities;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class MemoryRecordAccessor implements RecordAccessor {

    private final Map<String, PlayerRecord> records = new ConcurrentHashMap<>();

    @Override
    public PlayerRecord getRecord(String name, HiscoreType type) {
        return records.computeIfAbsent(NameUtilities.toConsistentFormat(name, type), key -> new MemoryPlayerRecord());
    }
}