package recommender

import io.Source.fromFile
import scala.collection.mutable.ListBuffer

object GPSAddressData {

  def addressToList(fileName: String): ListBuffer[String] = {
    
    val file = fromFile(fileName)
    val lines = file.getLines().toList
    val firstFiveLines = lines.take(5)
    var reducedAddress = ListBuffer[String]()
    for(i <- firstFiveLines){
        val addressWithCount = i.split(" ")
        val addressWithoutCount = addressWithCount.take(addressWithCount.length-1)
        var line: String = ""
        for(j <- addressWithoutCount){
            line += j
        }
        reducedAddress += line
    }
    return reducedAddress
  }
}
