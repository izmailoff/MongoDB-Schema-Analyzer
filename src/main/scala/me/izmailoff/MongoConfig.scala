package wws.db.connection

import java.io.{File, FileInputStream}

import com.mongodb.{MongoClient, MongoException, ServerAddress}
import net.liftweb.common.Full
import net.liftweb.mongodb._
import net.liftweb.util.{DefaultConnectionIdentifier, Props}

import scala.util.{Failure, Success, Try}

/**
 * MongoDb connection provider.
 */
object MongoConfig {
  val filename = "default.props"
  if (new File(filename).exists)
    Props.whereToLook = () => ((filename, () => Full(new FileInputStream(filename))) :: Nil)

  val hostname = Props.get("mongo.host", "127.0.0.1")
  val port = Props.getInt("mongo.port", 27017)
  val dbName = Props.get("mongo.dbname", "test")

  /**
   * Define default Mongo connection provider based on props.
   */
  def init(): Unit = {
    val server = new ServerAddress(hostname, port)
    MongoDB.defineDb(DefaultConnectionIdentifier, new MongoClient(server), dbName)
    if(!isConnected())
      sys.exit(1)
  }

  /**
   * Checks if there is connectivity to MongoDB server by
   * checking default DB connection and calling some db command.
   */
  def isConnected() = Try {
    getDb.map(_.getStats()).isDefined
  } match {
    case Success(dbDefined) =>
      dbDefined
    case Failure(err) =>
      println(s"Could NOT connect to DB. Got: $err")
      false
  }
  
  def getDb = MongoDB.getDb(DefaultConnectionIdentifier)
  
  def getCollection(name: String) = getDb.get.getCollection(name)
}
