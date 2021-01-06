package recommender

import io.Source.fromFile
import scala.collection.mutable.ListBuffer

object StarbucksLocationData {

  def locationToList(fileName: String): ListBuffer[String] = {
    
    val file = fromFile(fileName)
    val lines = file.getLines().toList
    var reducedAddress = ListBuffer[String]()
   for(i <- lines){
        val addressWithCount = i.split(" ")
        val addressWithoutCount = addressWithCount.take(addressWithCount.length-1)
        var line: String = ""
        for(j <- addressWithoutCount){
            line = line + j + " "
        }
        reducedAddress += line
    }
    return reducedAddress
  }
}