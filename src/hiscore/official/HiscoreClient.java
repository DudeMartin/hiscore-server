package hiscore.official;

import hiscore.utils.StreamUtilities;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class HiscoreClient {

    public String lookup(String name, HiscoreType type) throws IOException {
        return new String(
                StreamUtilities.readStream(
                        new URL(type.endpoint + "?player=" + URLEncoder.encode(name, StandardCharsets.UTF_8.name())).openStream()),
                StandardCharsets.UTF_8);
    }
}