package client

object URLGenerator{

    var base_URL = "https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial"

    def generateURL(origins: String, destination: String): String = {

        var originInFormat = "origins=" + origins
        var destinationInFormat = "destinations=" + destination
        val departureTime = "departure_time=now"
        val APIKey = "key=" + "AIzaSyCE6ElymIpN4HowHK2_4-1_o-MvaE8Vy6Q"

        return base_URL + "&" + originInFormat + "&" + destinationInFormat + "&" + departureTime + "&" + APIKey
    }

}