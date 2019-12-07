package com.example.harmit.swooshcar;

public class GetDataAdapter1 {

    String rideid, drivername,driverimage, pickup, destination,price,date,time;

    public GetDataAdapter1(String rideid, String drivername,String driverimage, String pickup, String destination, String price, String date, String time) {
        this.rideid = rideid;
        this.drivername = drivername;
        this.driverimage = driverimage;
        this.pickup = pickup;
        this.destination = destination;
        this.price = price;
        this.date = date;
        this.time = time;

    }

    public String getRideid() {
        return rideid;
    }

    public String getDriver_name() {
        return drivername;
    }

    public String getPickup() {
        return pickup;
    }

    public String getDestination() {
        return destination;
    }

    public String getDriverimage() { return driverimage; }

    public String getPrice() { return price;}

    public String getDeparturedate(){ return date; }

    public String getDeparturetime() { return time; }

}


