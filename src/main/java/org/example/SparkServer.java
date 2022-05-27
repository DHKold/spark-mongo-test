package org.example;

import org.apache.spark.sql.SparkSession;

import java.io.Closeable;

/**
 * Spark mock server with minimal configuration.
 */
public class SparkServer implements Closeable {

    private final SparkSession sparkSession;

    /**
     * Create and start a new Spark server for testing purpose.
     */
    public SparkServer() {
        sparkSession = SparkSession
                .builder()
                .appName("SparkTest")
                .config("spark.sql.shuffle.partitions", 8)
                .master("local[*]").getOrCreate();
        sparkSession.sparkContext().setLogLevel("ERROR");
    }

    /**
     * Stop & close the spark test server.
     */
    @Override
    public void close() {
        sparkSession.stop();
        sparkSession.close();
    }

    /**
     * @return The spark session for the test server.
     */
    public SparkSession getSession() {
        return sparkSession;
    }

}
