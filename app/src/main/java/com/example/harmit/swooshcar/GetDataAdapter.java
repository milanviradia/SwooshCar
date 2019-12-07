package com.example.harmit.swooshcar;

public class GetDataAdapter {

    String rideid, drivername, pickup, destination;
    String departuredate, departuretime, seats;
    String paymenttype, distance, duration, price;
    String profileimage;

    public GetDataAdapter(String rideid, String drivername, String pickup, String destination, String departuredate, String departuretime, String seats, String paymenttype, String distance, String duration, String price, String profileimage) {
        this.rideid = rideid;
        this.drivername = drivername;
        this.pickup = pickup;
        this.destination = destination;
        this.departuredate = departuredate;
        this.departuretime = departuretime;
        this.seats = seats;
        this.paymenttype = paymenttype;
        this.distance = distance;
        this.duration = duration;
        this.price = price;
        this.profileimage = profileimage;
    }

    public String getRideid() {
        return rideid;
    }

    public String getDrivername() {
        return drivername;
    }

    public String getPickup() {
        return pickup;
    }

    public String getDestination() {
        return destination;
    }

    public String getDeparturedate() {
        return departuredate;
    }

    public String getDeparturetime() {
        return departuretime;
    }

    public String getSeats() {
        return seats;
    }

    public String getPaymenttype() {
        return paymenttype;
    }

    public String getDistance() {
        return distance;
    }

    public String getDuration() {
        return duration;
    }

    public String getPrice() {
        return price;
    }

    public String getProfileimage() {
        return profileimage;
    }

}