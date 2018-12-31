package hiscore.storage.file;

import hiscore.model.PlayerListing;
import hiscore.storage.SortedSetPlayerRecord;
import hiscore.storage.file.util.PlayerListingCodec;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

final class FilePlayerRecord extends SortedSetPlayerRecord {

    private final AsynchronousFileChannel recordChannel;
    private final AtomicLong recordFilePosition;

    FilePlayerRecord(Path recordFile) throws IOException {
        if (Files.exists(recordFile)) {
            Collection<PlayerListing> initialListings = readListings(Files.readAllBytes(recordFile));
            recordChannel = AsynchronousFileChannel.open(recordFile, StandardOpenOption.WRITE);
            listings.addAll(initialListings);
        } else {
            recordChannel = AsynchronousFileChannel.open(recordFile, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
        }
        recordFilePosition = new AtomicLong(recordChannel.size());
    }

    @Override
    public void addListing(PlayerListing listing) {
        long startPosition = recordFilePosition.getAndAdd(PlayerListingCodec.BYTES_PER_LISTING);
        ByteBuffer listingBytes = PlayerListingCodec.toBytes(listing);
        recordChannel.write(listingBytes, startPosition, listingBytes, new WriteHandler(listing, startPosition));
    }

    private static Collection<PlayerListing> readListings(byte[] recordBytes) {
        List<PlayerListing> listings = new ArrayList<>();
        for (int offset = 0, byteCount = recordBytes.length; offset < byteCount; offset += PlayerListingCodec.BYTES_PER_LISTING) {
            listings.add(PlayerListingCodec.fromBytes(ByteBuffer.wrap(recordBytes, offset, PlayerListingCodec.BYTES_PER_LISTING)));
        }
        return listings;
    }

    private final class WriteHandler implements CompletionHandler<Integer, ByteBuffer> {

        private final PlayerListing listing;
        private final long startPosition;

        private WriteHandler(PlayerListing listing, long startPosition) {
            this.listing = listing;
            this.startPosition = startPosition;
        }

        @Override
        public void completed(Integer result, ByteBuffer writeBuffer) {
            if (writeBuffer.hasRemaining()) {
                recordChannel.write(writeBuffer, startPosition + writeBuffer.position(), writeBuffer, this);
            } else {
                listings.add(listing);
            }
        }

        @Override
        public void failed(Throwable exception, ByteBuffer writeBuffer) {
            exception.printStackTrace();
        }
    }
}