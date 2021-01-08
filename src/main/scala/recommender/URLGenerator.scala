package recommender

object URLGenerator{

    var base_URL = "https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial"

    def generateURL(origins: String, destination: String): String = {

        var originInFormat = "origins=" + origins
        var destinationInFormat = "destinations=" + destination
        val departureTime = "departure_time=now"
        val APIKey = "key=" + "SampleAPI"

        return base_URL + "&" + originInFormat + "&" + destinationInFormat + "&" + departureTime + "&" + APIKey
    }

}