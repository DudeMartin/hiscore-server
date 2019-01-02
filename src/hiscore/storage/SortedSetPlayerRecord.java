package hiscore.storage;

import hiscore.model.PlayerListing;

import java.time.Instant;
import java.util.List;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;

public class SortedSetPlayerRecord implements PlayerRecord {

    protected final SortedSet<PlayerListing> listings = new ConcurrentSkipListSet<>();

    @Override
    public final List<PlayerListing> listingsBetween(Instant start, Instant end) {
        return listings.stream()
                .filter(listing -> listing.timestamp().isAfter(start) && listing.timestamp().isBefore(end))
                .collect(Collectors.toList());
    }

    @Override
    public void addListing(PlayerListing listing) {
        listings.add(listing);
    }
}