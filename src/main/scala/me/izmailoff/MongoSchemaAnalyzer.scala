package me.izmailoff

import com.mongodb.DBObject
import net.liftweb.json._
import wws.db.connection.MongoConfig

import scala.util.Try
import scala.util.Success
import scala.util.Failure
import scala.collection.JavaConverters._

object MongoSchemaAnalyzer extends App {

  val collectionName = Try {
    require(!args.isEmpty && !args(0).isEmpty, "Collection name must be provided.")
    args(0)
  } match {
    case Success(name) => name
    case Failure(e) =>
      println(e)
      sys.exit(1)
  }

  /**
   * Conversion function. Converts real to default values to ignore document differences.
   * Other non-matched values will be either empty or recursively traversed by 'transform' function.
   */
  val defaults: PartialFunction[JValue, JValue] = {
    case JInt(_) => JInt(0)
    case blob@JString("<Binary Data>") => blob
    case JString(_) => JString("...")
    case JDouble(_) => JDouble(0)
    case JBool(_) => JBool(true)
    case JArray(elems) => JArray((elems map { e => e.transform(defaults) } toSet) toList)
  }

  /**
   * Transforms JSON docs to print their original data types as field values.
   */
  val printTransform: PartialFunction[JValue, JValue] = {
    case JInt(_) => JString("Integer")
    case blob@JString("<Binary Data>") => blob
    case JString(_) => JString("String")
    case JDouble(_) => JString("Double")
    case JBool(_) => JString("Boolean")
  }

  /**
    * A workaround to handle binary data fields such as file chunks which break proper JSON representation.
    * Better options are possible such as converting from DBObject to JSON directly without string replace.
    * @param doc
    * @return
    */
  def toJsonStr(doc: DBObject) = doc.toString.replace("<Binary Data>","\"<Binary Data>\"")

  MongoConfig.init()
  val collection = MongoConfig.getCollection(collectionName)
  val cursor = collection.find()
  val uniqueASTs = cursor.iterator.asScala.collect { case doc: DBObject => parse(toJsonStr(doc)).transform(defaults) }.toSet
  uniqueASTs foreach { doc => println(prettyRender(doc.transform(printTransform))) }
}