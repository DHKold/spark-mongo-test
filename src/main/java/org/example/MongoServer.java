package org.example;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.ImmutableMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfig;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import org.bson.Document;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

/**
 * Mongo embedded server for testing purpose.
 */
public class MongoServer implements Closeable {

    private final String mongoUri;

    private final MongodExecutable mongoExe;

    private final MongodProcess mongoDaemon;

    private final MongoClient client;

    private final String databaseName;

    /**
     * Create and start a new MongoDB server for testing purpose on a free local port.
     *
     * @param databaseName The Mongo Database to use.
     * @throws IOException Error during the creation of the server.
     */
    public MongoServer(String databaseName) throws IOException {
        this.databaseName = databaseName;
        int port = Network.getFreeServerPort();
        mongoUri = "mongodb://localhost:" + port;
        ImmutableMongodConfig mongoConfig = MongodConfig.builder()
                                                        .version(Version.Main.PRODUCTION)
                                                        .net(new Net(port, Network.localhostIsIPv6())).build();
        mongoExe = MongodStarter.getDefaultInstance().prepare(mongoConfig);
        mongoDaemon = mongoExe.start();
        client = MongoClients.create(mongoUri);
    }

    /**
     * @return The URI of the server.
     */
    public String getUri() {
        return mongoUri;
    }

    /**
     * @return The name of the database.
     */
    public String getDatabaseName() {
        return databaseName;
    }

    /**
     * Stop and close the server.
     */
    public void close() {
        client.close();
        mongoDaemon.stop();
        mongoExe.stop();
    }

    /**
     * @return A Mongo client connection to the server.
     */
    public MongoClient getClient() {
        return client;
    }

    /**
     * Create or replace a collection content.
     *
     * @param collectionName The name of the collection.
     * @param documents      The list of documents to insert.
     * @return The current mock server.
     */
    public MongoServer setCollection(String collectionName, List<Document> documents) {
        dropCollection(collectionName);
        insert(collectionName, documents);
        return this;
    }

    /**
     * Insert documents into a collection.
     *
     * @param collectionName The name of the collection.
     * @param documents      The list of documents to insert.
     * @return The current mock server.
     */
    public MongoServer insert(String collectionName, List<Document> documents) {
        client.getDatabase(databaseName)
              .getCollection(collectionName)
              .insertMany(documents);
        return this;
    }

    /**
     * Delete a collection.
     *
     * @param collectionName The name of the collection.
     * @return The current mock server.
     */
    public MongoServer dropCollection(String collectionName) {
        MongoDatabase database = client.getDatabase(databaseName);
        database.getCollection(collectionName).drop();
        return this;
    }

}
