package com.appzone.halimah.models;

import java.io.Serializable;
import java.util.List;

public class NotificationModel implements Serializable {
    private String id_reservation;
    private String user_id_fk;
    private String reservation_cost;
    private String approved;
    private String notification_date;
    private String transformated;
    private String transformation_amount;
    private String transformation_date;
    private String transformation_image;
    private String transformation_person;
    private String transformation_phone;
    private String user_type;
    private String user_full_name;
    private String user_phone;
    private String user_image;
    private List<Reservation_Details_Model> reservation_details;

    public String getId_reservation() {
        return id_reservation;
    }

    public String getUser_id_fk() {
        return user_id_fk;
    }

    public String getReservation_cost() {
        return reservation_cost;
    }

    public String getApproved() {
        return approved;
    }

    public String getNotification_date() {
        return notification_date;
    }

    public String getTransformated() {
        return transformated;
    }

    public String getTransformation_amount() {
        return transformation_amount;
    }

    public String getTransformation_date() {
        return transformation_date;
    }

    public String getTransformation_image() {
        return transformation_image;
    }

    public String getTransformation_person() {
        return transformation_person;
    }

    public String getTransformation_phone() {
        return transformation_phone;
    }

    public String getUser_full_name() {
        return user_full_name;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public String getUser_image() {
        return user_image;
    }

    public String getUser_type() {
        return user_type;
    }

    public List<Reservation_Details_Model> getReservation_details() {
        return reservation_details;
    }

    public class Reservation_Details_Model implements Serializable
    {
        private String id;
        private String reservation_date;
        private String reservation_from_hour;
        private String reservation_to_hour;
        private String reservation_approved;
        private String total_hour_cost;


        public String getId() {
            return id;
        }

        public String getReservation_date() {
            return reservation_date;
        }

        public String getReservation_from_hour() {
            return reservation_from_hour;
        }

        public String getReservation_to_hour() {
            return reservation_to_hour;
        }

        public String getReservation_approved() {
            return reservation_approved;
        }

        public String getTotal_hour_cost() {
            return total_hour_cost;
        }
    }

}
