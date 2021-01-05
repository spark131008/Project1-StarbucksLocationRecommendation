package client

import io.Source.fromFile
import scala.collection.mutable.ListBuffer

object GPSAddressData {

  def addressToList(fileName: String): ListBuffer[String] = {
    
    val file = fromFile(fileName)
    val lines = file.getLines().toList
    var reducedAddress = ListBuffer[String]()
    for(i <- lines){
        val address = i.split(" ")
        var line: String = ""
        for(j <- 0 until address.length-2){
            line = line + address(j) + " "
        }
        reducedAddress += line
    }
    return reducedAddress
  }
}
