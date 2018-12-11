package com.appzone.halimah.models;

import java.io.Serializable;

public class ReserveModel implements Serializable{
    private long reservation_date;
    private long reservation_from_hour;
    private long reservation_to_hour;
    private double total_hour_cost;

    public ReserveModel(long reservation_date, long reservation_from_hour, long reservation_to_hour,double total_hour_cost) {
        this.reservation_date = reservation_date;
        this.reservation_from_hour = reservation_from_hour;
        this.reservation_to_hour = reservation_to_hour;
        this.total_hour_cost = total_hour_cost;
    }

    public long getReservation_date() {
        return reservation_date;
    }

    public long getReservation_from_hour() {
        return reservation_from_hour;
    }

    public long getReservation_to_hour() {
        return reservation_to_hour;
    }

    public double getTotal_hour_cost() {
        return total_hour_cost;
    }
}
