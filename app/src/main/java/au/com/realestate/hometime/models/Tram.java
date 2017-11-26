package au.com.realestate.hometime.models;

import com.squareup.moshi.Json;

public class Tram {
    @Json(name = "Destination")
    private String destination;
    @Json(name = "PredictedArrivalDateTime")
    private String predictedArrival;
    @Json(name = "RouteNo")
    private String routeNo;

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getPredictedArrival() {
        return predictedArrival;
    }

    public void setPredictedArrival(String predictedArrival) {
        this.predictedArrival = predictedArrival;
    }

    public String getRouteNo() {
        return routeNo;
    }

    public void setRouteNo(String routeNo) {
        this.routeNo = routeNo;
    }
}
