package hiscore.storage;

import hiscore.model.PlayerListing;

import java.time.Instant;
import java.util.List;

public interface PlayerRecord {

    List<PlayerListing> listingsBetween(Instant start, Instant end);

    void addListing(PlayerListing listing);
}