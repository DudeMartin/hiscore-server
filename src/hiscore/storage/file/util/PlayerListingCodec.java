package hiscore.storage.file.util;

import hiscore.model.PlayerListing;

import java.nio.ByteBuffer;

public final class PlayerListingCodec {

    public static final int BYTES_PER_LISTING = BufferedPlayerListing.LISTING_BYTE_COUNT;

    private PlayerListingCodec() {

    }

    public static PlayerListing fromBytes(ByteBuffer listingBytes) {
        return new BufferedPlayerListing(listingBytes);
    }

    public static ByteBuffer toBytes(PlayerListing listing) {
        return new BufferedPlayerListing(listing).getBytes();
    }
}