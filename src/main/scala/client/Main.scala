package client

import scala.io.Source._
import java.io.FileNotFoundException
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import io.StdIn._
import scala.util.Random
import scala.math._
import scala.util.{Failure, Success}
import scala.concurrent.duration._
import scala.concurrent.Await
import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.Map
import scala.collection.immutable.ListMap

object Main{
  
  def main(args: Array[String]): Unit = {
    var sum = 0.0
    var myMap = Map[String, Double]()

  for(i <- StarbucksLocationData.locationToList("Starbucks.txt")){
    for(j <- GPSAddressData.addressToList("GPS.txt")){
      var mile: Future[Double] = mileExtractor(j, i)

    mile.onComplete{
    case Success(mile) => {
      sum += mile
    }
    case Failure(t) => println("Could not process file: " + t.getMessage)
      }
    }
    Thread.sleep(2000)
    myMap(i) = sum/5
    //println(s"Starbucks Location: ${i}\nAverage distance calculated: ${sum/5}\n")
    //println("Now Recommending based on the distance calculated ...")
  }
  Thread.sleep(4000)
  println
  mapSort(myMap).keys.foreach(key => 
    println(s"Starbucks Location: ${key}\nAverage distance calculated: ${myMap(key)}\n")
  )
  }

  def mileExtractor(origin: String, destination: String): Future[Double] = Future{
    var mile = ArrayBuffer[Double]()
    val lines = fromURL(
      URLGenerator.generateURL(origin.replaceAll(" ", ""), destination.replaceAll(" ", ""))
    ).mkString.split("[:&\"]").filter(_.contains("mi"))

    for(i <- lines; if(i.substring(i.length - 2).equals("mi"))){
        mile += i.replaceAll(" mi", "").toDouble
    }
    mile(0)
  }

  def mapSort(contentToMap: Map[String, Double]): ListMap[String, Double] = {
    val sortedMap: ListMap[String, Double] = ListMap(contentToMap.toSeq.sortWith(_._2 < _._2): _*)
    return sortedMap
  }
}