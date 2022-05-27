plugins {
    id("java")
}

repositories {
    mavenCentral()
}

val sparkVersion = "3.2.1"
val mongodbVersion = "4.6.0"
val mongodbSparkVersion = "10.0.1"

dependencies {
    implementation("org.apache.spark:spark-catalyst_2.12:$sparkVersion")
    implementation("org.apache.spark:spark-core_2.12:$sparkVersion")
    implementation("org.apache.spark:spark-sql_2.12:$sparkVersion")
    implementation("de.flapdoodle.embed:de.flapdoodle.embed.mongo:3.4.6")
    implementation("org.mongodb:bson:$mongodbVersion")
    implementation("org.mongodb:mongodb-driver-core:$mongodbVersion")
    implementation("org.mongodb:mongodb-driver-sync:$mongodbVersion")
    implementation("org.mongodb.spark:mongo-spark-connector:$mongodbSparkVersion")
    implementation("ch.qos.logback:logback-classic:1.2.11")
}
