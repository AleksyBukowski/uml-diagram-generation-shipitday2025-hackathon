package fetching;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.Deflater;

import static com.aisupport.AIUMLFetchingService.getUMLResponse;

public class PlantUMLFetchingService{

    private static final String PLANTUML_SERVER = "http://www.plantuml.com/plantuml/png/";
    private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz-_";

    public static void main(String[] args) throws Exception {
        String uml = getUMLResponse("C:\\Users\\albukows\\Downloads\\the7javafiles");
        System.out.println(uml);

        byte[] image = getDiagramAsPng(uml);

        try (FileOutputStream fos = new FileOutputStream("diagram.png")) {
            fos.write(image);
        }

        System.out.println("✅ Diagram saved as diagram.png");
    }

    public static byte[] getDiagramAsPng(String umlSource) throws Exception {
        String encoded = encode(umlSource);
        String url = PLANTUML_SERVER + encoded;

        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("GET");

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed to get diagram: HTTP " + conn.getResponseCode());
        }

        try (InputStream in = conn.getInputStream();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[4096];
            int n;
            while ((n = in.read(buffer)) != -1) {
                out.write(buffer, 0, n);
            }
            return out.toByteArray();
        }
    }

    // --- PlantUML encoding (same logic as Python version) ---
    private static String encode(String text) throws UnsupportedEncodingException {
        // Compress with zlib (deflate)
        byte[] input = text.getBytes("UTF-8");
        Deflater deflater = new Deflater();
        deflater.setInput(input);
        deflater.finish();

        byte[] compressed = new byte[2048];
        int len = deflater.deflate(compressed);
        deflater.end();

        // Strip header (2 bytes) and checksum (4 bytes)
        byte[] data = new byte[len - 6];
        System.arraycopy(compressed, 2, data, 0, len - 6);

        // Encode using PlantUML's custom base64
        return encode64(data);
    }

    private static String encode64(byte[] data) {
        StringBuilder res = new StringBuilder();
        int i = 0;
        while (i < data.length) {
            int b1 = data[i++] & 0xFF;
            int b2 = i < data.length ? data[i++] & 0xFF : 0;
            int b3 = i < data.length ? data[i++] & 0xFF : 0;

            int c1 = b1 >> 2;
            int c2 = ((b1 & 0x3) << 4) | (b2 >> 4);
            int c3 = ((b2 & 0xF) << 2) | (b3 >> 6);
            int c4 = b3 & 0x3F;

            res.append(ALPHABET.charAt(c1));
            res.append(ALPHABET.charAt(c2));
            res.append(ALPHABET.charAt(c3));
            res.append(ALPHABET.charAt(c4));
        }
        return res.toString();
    }
}
