package wws.db.connection

import net.liftweb._
import net.liftweb.common._
import net.liftweb.mongodb._
import net.liftweb.util.Props
import com.mongodb.{ ServerAddress, Mongo }
import com.mongodb.MongoException

/**
 * MongoDb connection provider.
 */
object MongoConfig {

  val hostname = Props.get("mongo.host", "127.0.0.1")
  val port = Props.getInt("mongo.port", 27017)
  val dbName = Props.get("mongo.dbname", "test")

  /**
   * Define default Mongo connection provider based on props.
   */
  def init() {
    val server = new ServerAddress(hostname, port)
    MongoDB.defineDb(DefaultMongoIdentifier, new Mongo(server), dbName)
  }

  /**
   * Checks if there is connectivity to MongoDB server by
   * checking default DB connection and calling getLastError.
   */
  def isConnected = {
    val db = MongoDB.getDb(DefaultMongoIdentifier)
    if (db.isEmpty)
      false
    else
      try {
        db.get.getLastError
        true
      } catch {
        case t: MongoException => false
      }
  }
  
  def getDb = MongoDB.getDb(DefaultMongoIdentifier)
  
  def getCollection(name: String) = getDb.get.getCollection(name)
}
