package hiscore.storage.file;

import hiscore.official.HiscoreType;
import hiscore.storage.PlayerRecord;
import hiscore.storage.RecordAccessor;
import hiscore.utils.NameUtilities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class FileRecordAccessor implements RecordAccessor {

    private final Map<String, PlayerRecord> records = new ConcurrentHashMap<>();
    private final Path storagePath;

    public FileRecordAccessor(Path storagePath) throws IOException {
        Files.createDirectories(storagePath);
        this.storagePath = storagePath;
    }

    @Override
    public PlayerRecord getRecord(String name, HiscoreType type) {
        return records.computeIfAbsent(NameUtilities.toConsistentFormat(name, type), formattedName -> {
            try {
                return new FilePlayerRecord(storagePath.resolve(formattedName));
            } catch (IOException e) {
                System.err.println("Failed to initialize a player record for \"" + name + "\".");
                e.printStackTrace();
                return null;
            }
        });
    }
}