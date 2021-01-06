package recommender

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
import scala.sys.process._
import scala.language.postfixOps

object StarbucksNearMeRecommendation {

  def main(args: Array[String]): Unit = {
    println("\n\n")

    println(s"Starting the [Starbucks Near Me Recoomendation] program...\n")
    loading()
    
    print(
      "[STEP 1] - Show top 5 most visited addresses, based on a GPS tracker\n\n" +
      "Which file you want to import? Type [ ls ] to see the list of files.\n"
    )
    //prompt the user
    println()

    //Showing the list of files in the folder
    showList()
    println("\n\n")
    println("Select a file from the list above: \n")
    //prompt the user

    loading()

    //show the file content


    println()
    print(
      "[STEP 2] - Show Starbucks stores that are only located in the same city and state as in the addresses from [Step 1]\n\n" +
      "Which file you want to import? Type [ ls ] to see the list of files.\n"
    )
    //prompt the user
    println()
    
    //Showing the list of files in the folder
    showList()
    println("\n\n")
    println("Select a file from the list above: \n")
    //prompt the user

    loading()

    //show the file content

    println()
    println(
      "[STEP 3] - Compute travel distances between 5 addresses from [Step 1] and each Starbucks store location from [Step 2]\n"
    )
    loading()
    val mapWithFinalValue = travelDistanceCalculator("GPS.txt", "Starbucks.txt")

    println()
    println(
      "[STEP 4] - Top 3 Starbucks Near Me Recommendation using an algorithm (Shortest distance to Farthest distance)\n"
    )
    loading()

    var count = 1
    mapSort(mapWithFinalValue).keys.take(3).foreach{key =>
    println(s"Starbucks Near Me Recommendation [${count}]: ${key}\n" +
      f"Travel Distance based on your driving data: ${mapWithFinalValue(key)}%.2f mi\n")
    count += 1
    Thread.sleep(500)
    }

  }

  def travelDistanceCalculator(gpsfile: String, starbucksfile: String): Map[String, Double] = {
    var sum = 0.0
    var myMap = Map[String, Double]()
    var starbucksCount = 'A'
    var addressCount = 1
    

    for(i <- StarbucksLocationData.locationToList(starbucksfile)){
      print(
        s"Travel Distance between\n" +
        s"\t* Starbucks Store [$starbucksCount]\n"
      )
      for(j <- GPSAddressData.addressToList(gpsfile)){
        var mile: Future[Double] = mileExtractor(j, i)
        Thread.sleep(500)
      mile.onComplete{
      case Success(mile) => {
        sum += mile
        print(s"\t  ** Address ($addressCount) : $mile mi\n")
        addressCount += 1
      }
      case Failure(t) => println("Could not process file: " + t.getMessage)
        }
      }
      Thread.sleep(1000)
      println("\n\n")

      
    starbucksCount = (starbucksCount+1).toChar
    addressCount = 1
    myMap(i) = sum/5
    }
    Thread.sleep(1000)
    return myMap
  }

  def mileExtractor(origin: String, destination: String): Future[Double] =
    Future {
      var mile = ArrayBuffer[Double]()
      val lines = fromURL(
        URLGenerator.generateURL(
          origin.replaceAll(" ", ""),
          destination.replaceAll(" ", "")
        )
      ).mkString.split("[:&\"]").filter(_.contains("mi"))

      for (i <- lines; if (i.substring(i.length - 2).equals("mi"))) {
        mile += i.replaceAll(" mi", "").toDouble
      }
      mile(0)
    }

  def mapSort(contentToMap: Map[String, Double]): ListMap[String, Double] = {
    val sortedMap: ListMap[String, Double] = ListMap(
      contentToMap.toSeq.sortWith(_._2 < _._2): _*
    )
    return sortedMap
  }

  def loading(): Unit ={
      for (i <- 1 to 6) {
      val dot = "_"
      println(f"[${dot*i}%-6s]")
      Thread.sleep(500)
    }
    println()
  }

  def showList(): Unit ={
    print(
      "Showing only .txt and .csv files in your current working directory  -->  "
    )
    var fileListArray = ("ls" !!).split("\\s").filter(x => x.endsWith("txt") || x.endsWith("csv"))
    fileListArray.foreach(x => print(s"| $x "))
  }
}
