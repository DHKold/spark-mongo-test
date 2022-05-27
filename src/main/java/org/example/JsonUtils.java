package org.example;

import org.apache.commons.io.IOUtils;
import org.bson.BsonArray;
import org.bson.Document;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class to manipulate Json files and documents
 */
public final class JsonUtils {

    public static final Charset DECODING_CHARSET = StandardCharsets.UTF_8;

    /**
     * Read a resource file.
     *
     * @param path Path of the resource.
     * @return The content of the file decoded as an UTF8 string.
     * @throws IOException Error when trying to read the file.
     */
    public static String loadResource(String path) throws IOException {
        InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
        if (input == null) {
            return null;
        }
        return IOUtils.toString(input, DECODING_CHARSET);
    }

    /**
     * Load a JSON file as a list of documents.
     *
     * @param path The path of the JSON file.
     * @return The list of documents contained in the JSON file.
     * @throws IOException Error when trying to read the file.
     */
    public static List<Document> getJsonDocuments(String path) throws IOException {
        return BsonArray
                .parse(loadResource(path))
                .stream()
                .map(bsonValue -> Document.parse(bsonValue.toString()))
                .collect(Collectors.toList());
    }

    /**
     * The utility class only contains static methods, it cannot be instantiated.
     */
    private JsonUtils() {
    }

}
