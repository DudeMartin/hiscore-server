package hiscore.storage;

import hiscore.model.PlayerListing;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface PlayerRecord {

    Optional<PlayerListing> firstListing();

    Optional<PlayerListing> mostRecentListing();

    List<PlayerListing> listingsBetween(Instant start, Instant end);

    void addListing(PlayerListing listing);
}