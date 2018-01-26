package wws.db.connection

import java.io.FileInputStream

import com.mongodb.{MongoClient, MongoException, ServerAddress}
import net.liftweb.common.Full
import net.liftweb.mongodb._
import net.liftweb.util.{DefaultConnectionIdentifier, Props}

/**
 * MongoDb connection provider.
 */
object MongoConfig {
  val filename = "default.props"
  Props.whereToLook = () => ((filename, () => Full(new FileInputStream(filename))) :: Nil)

  val hostname = Props.get("mongo.host", "127.0.0.1")
  val port = Props.getInt("mongo.port", 27017)
  val dbName = Props.get("mongo.dbname", "test")

  /**
   * Define default Mongo connection provider based on props.
   */
  def init() {
    val server = new ServerAddress(hostname, port)
    MongoDB.defineDb(DefaultConnectionIdentifier, new MongoClient(server), dbName)
  }

  /**
   * Checks if there is connectivity to MongoDB server by
   * checking default DB connection and calling some db command.
   */
  def isConnected = {
    val db = MongoDB.getDb(DefaultConnectionIdentifier)
    if (db.isEmpty)
      false
    else
      try {
        db.get.getStats()
        true
      } catch {
        case _: MongoException => false
      }
  }
  
  def getDb = MongoDB.getDb(DefaultConnectionIdentifier)
  
  def getCollection(name: String) = getDb.get.getCollection(name)
}
