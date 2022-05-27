package org.example;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.io.IOException;

public class Main {


    public static void main(String[] args) throws IOException {
        new Main();
    }

    private final MongoServer mongoServer;

    private Main() throws IOException {
        // Setup an embedded MongoDB
        mongoServer = new MongoServer("tests")
                .setCollection("users", JsonUtils.getJsonDocuments("users.json"))
                .setCollection("pets", JsonUtils.getJsonDocuments("pets.json"));
        // Setup a local Spark
        SparkServer sparkServer = new SparkServer();
        // Execute a simple join
        Dataset<Row> users = read(sparkServer.getSession(), "users");
        Dataset<Row> pets = read(sparkServer.getSession(), "pets");
        Dataset<Row> results = users.join(pets, users.col("id").equalTo(pets.col("ownerId")));
        results.show();
        // End
        sparkServer.close();
        mongoServer.close();
        System.exit(0);
    }

    public Dataset<Row> read(SparkSession sparkSession, String collection) {
        return sparkSession.read()
                .format("mongodb")
                .option("connection.uri", mongoServer.getUri())
                .option("database", mongoServer.getDatabaseName())
                .option("collection", collection)
                .load();
    }

}