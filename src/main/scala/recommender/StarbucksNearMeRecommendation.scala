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
import java.util.Scanner
import java.lang.System

object StarbucksNearMeRecommendation {

  def main(args: Array[String]): Unit = {
    println("\n\n")

    println(s"Starting the [Starbucks Near Me Recoomendation] program...\n")
    loading()
    
    print(
      "[STEP 1] - Show top 5 most visited addresses, based on a GPS tracker\n\n" +
      "Which file you want to import to proceed? Type [ ls ] to see the list of files.\n"
    )
    var stringInput = stringInputReader()
    println()

    while(!stringInput.equals("ls")){
      if(stringInput.equalsIgnoreCase("exit")){
        System.exit(0)
      }else{
        print("No such file/keyword is found. Please type in again: ")
        stringInput = stringInputReader()
      }
    }

    userInputExecutor(stringInput)
    println("\n\n")

    println(
    "File imported successfully\n"+
    "Hit [ Enter ] to proceed."
    )
    stringInputReader()
    println()
    print(
      "[STEP 2] - Show Starbucks stores that are only located in the same city and state as appeared in the addresses from [Step 1]\n\n" +
      "Which file you want to import to proceed? Type [ ls ] to see the list of files.\n"
    )
    var stringInput1 = stringInputReader()
    println()
    
     while(!stringInput1.equals("ls")){
      if(stringInput1.equalsIgnoreCase("exit")){
        System.exit(0)
      }else{
        print("No such file/keyword is found. Please type in again: ")
        stringInput1 = stringInputReader()
      }
    }
    userInputExecutor(stringInput1)
    println("\n\n")
    println(
    "File imported successfully\n"+
    "Hit [ Enter ] to proceed."
    )
    stringInputReader()
    println()

    println(
      "[STEP 3] - Compute travel distances between 5 addresses from [Step 1] and each Starbucks store location from [Step 2]\n" +
      "Hit [ Enter ] to proceed."
    )
    stringInputReader()
    val mapWithFinalValue = travelDistanceCalculator("q2sortedGPSoutput.txt", "q2sortedStarbucksoutput.txt")

    println()
    println(
      "[STEP 4] - Rocommend top 3 Starbucks Near Me locations using an algorithm (Shortest distance to Farthest distance)\n"+
      "Hit [ Enter ] to proceed."
    )
    stringInputReader()
    loading()

    var count = 1
    mapSort(mapWithFinalValue).keys.take(3).foreach{key =>
    println(s"Starbucks Near Me Recommendation [${count}]: ${key}\n" +
      f"Travel Distance based on your driving data: ${mapWithFinalValue(key)}%.2f mi\n")
    count += 1
    Thread.sleep(500)
    }
    println("\n\n")
    println(
    "Program Completed\n"+
    "Hit [ Enter ] to exit."
    )
    stringInputReader()

  }

  def travelDistanceCalculator(gpsfile: String, starbucksfile: String): Map[String, Double] = {
    var myMap = Map[String, Double]()
    var starbucksCount = 'A'
    

    for(i <- StarbucksLocationData.locationToList(starbucksfile)){
      var sum = 0.0
      var addressCount = 1
      print(
        s"Travel Distance between\n" +
        s"\t* Starbucks Store [$starbucksCount] ${i}\n"
      )
      for(j <- GPSAddressData.addressToList(gpsfile)){
        var mile: Future[Double] = mileExtractor(j, i)
        Thread.sleep(500)
      mile.onComplete{
      case Success(mile) => {
        sum += mile
        print(s"\t  ** Address ($addressCount) : $mile mi ${j}\n")
        addressCount += 1
      }
      case Failure(t) => println("Could not process file: " + t.getMessage)
        }
      }
      Thread.sleep(500)
      println("\n\n")

      
    starbucksCount = (starbucksCount+1).toChar
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
      Thread.sleep(300)
    }
    println()
  }

  def showList(): List[String] ={
    print(
      "Showing only .txt and .csv files in your current working directory  -->  "
    )
    var fileListArray = ("ls" !!).split("\\s").filter(x => x.endsWith("txt") || x.endsWith("csv"))
    fileListArray.foreach(x => print(s"| $x "))
    return fileListArray.toList
  }

  def stringInputReader(): String = {
    var scanner = new Scanner(System.in)
    var input = ""
    input = scanner.nextLine().toLowerCase()
    return input
  }

  def userInputExecutor(input: String): Unit = {
    if(input.equalsIgnoreCase("ls")){
      showList()
      println()
      print("Select a file from the list above: ")
      val stringInput = stringInputReader()
      userInputExecutor(stringInput)
    }
    else if(input.endsWith("txt")){
      println()
      loading()
      input match{
        case a if(a.contains("starbucks")) => {
          println()
          val file = fromFile("q2sortedStarbucksoutput.txt")
          file.getLines().toList.foreach(println)
        }
        case b if(b.contains("gps")) => {
          println()
          val file = fromFile("q2sortedGPSoutput.txt")
          file.getLines().toList.take(5).foreach(println)
        }
        case _ => {
          print("No such file/keyword is found. Please type in again: ")
          val stringInput1 = stringInputReader()
          userInputExecutor(stringInput1)
        }
      }
    }
    else if(input.equalsIgnoreCase("exit")){
        System.exit(0)
    }
    else{
    print("No such file/keyword is found. Please type in again: ")
    val stringInput2 = stringInputReader()
    userInputExecutor(stringInput2)
    }
  }
}
