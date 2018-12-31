package hiscore.official;

import hiscore.utils.StreamUtilities;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public final class HiscoreClient {

    public String lookupRaw(String name, HiscoreType type) throws IOException {
        return new String(
                StreamUtilities.readStream(
                        new URL(type.endpoint + "?player=" + URLEncoder.encode(name, StandardCharsets.UTF_8.name())).openStream()),
                StandardCharsets.UTF_8);
    }

    public HiscorePlayerListing lookup(String name, HiscoreType type) throws IOException {
        return new HiscorePlayerListing(lookupRaw(name, type));
    }
}