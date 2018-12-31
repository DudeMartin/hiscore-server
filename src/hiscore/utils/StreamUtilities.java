package hiscore.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public final class StreamUtilities {

    private StreamUtilities() {

    }

    public static byte[] readStream(InputStream stream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] transferBuffer = new byte[1024];
        int bytesRead;
        try {
            while ((bytesRead = stream.read(transferBuffer)) >= 0) {
                outputStream.write(transferBuffer, 0, bytesRead);
            }
        } finally {
            stream.close();
        }
        return outputStream.toByteArray();
    }
}