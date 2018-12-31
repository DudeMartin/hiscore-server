package hiscore.storage;

import hiscore.official.HiscoreClient;
import hiscore.official.HiscoreType;

import java.io.IOException;

public interface RecordAccessor {

    HiscoreClient HISCORE_CLIENT = new HiscoreClient();

    PlayerRecord getRecord(String name, HiscoreType type) throws IOException;

    default void updateRecord(String name, HiscoreType type) throws IOException {
        getRecord(name, type).addListing(HISCORE_CLIENT.lookup(name, type));
    }
}