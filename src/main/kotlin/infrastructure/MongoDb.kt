package infrastructure

import com.mongodb.kotlin.client.coroutine.MongoClient;

object MongoDb {
    val database = MongoClient.create("mongodb://mongodb:27017").getDatabase("ChargeAndTrackDB")
}
