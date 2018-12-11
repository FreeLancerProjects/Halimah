package com.appzone.halimah.models;

import java.io.Serializable;
import java.util.List;

public class UserModel implements Serializable {
    private String user_id;
    private String user_full_name;
    private String user_name;
    private String user_phone;
    private String user_image;
    private String person_responsible_name;
    private String from_hour;
    private String to_hour;
    private String user_type;
    private String user_address;
    private String hour_cost;
    private String user_google_lat;
    private String user_google_long;
    private String person_responsible_phone;
    private String user_token_id;
    private int stars_num;
    private List <Nursery_Service> kindergarten_service;
    private List<Nursery_Gallery> gallary;
    private int success_signup;
    private int success_login;
    private int success_update;
    private int success_update_pass;
    private int success_delete;


    public String getUser_id() {
        return user_id;
    }

    public int getStars_num() {
        return stars_num;
    }

    public String getUser_full_name() {
        return user_full_name;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public String getUser_image() {
        return user_image;
    }

    public String getPerson_responsible_name() {
        return person_responsible_name;
    }

    public String getFrom_hour() {
        return from_hour;
    }

    public String getTo_hour() {
        return to_hour;
    }

    public String getUser_type() {
        return user_type;
    }

    public String getUser_address() {
        return user_address;
    }

    public String getUser_google_lat() {
        return user_google_lat;
    }

    public String getUser_google_long() {
        return user_google_long;
    }

    public String getPerson_responsible_phone() {
        return person_responsible_phone;
    }

    public String getUser_token_id() {
        return user_token_id;
    }

    public String getHour_cost() {
        return hour_cost;
    }

    public int getSuccess_update() {
        return success_update;
    }

    public int getSuccess_update_pass() {
        return success_update_pass;
    }

    public int getSuccess_delete() {
        return success_delete;
    }

    public List<Nursery_Service> getKindergarten_service() {
        return kindergarten_service;
    }

    public List<Nursery_Gallery> getGallary() {
        return gallary;
    }

    public int getSuccess_signup() {
        return success_signup;
    }

    public int getSuccess_login() {
        return success_login;
    }

    public class Nursery_Service implements Serializable
    {
        private String service_id_fk;
        private String id_service;
        private String ar_service_title;
        private String en_service_title;

        public String getService_id_fk() {
            return service_id_fk;
        }

        public String getId_service() {
            return id_service;
        }

        public String getAr_service_title() {
            return ar_service_title;
        }

        public String getEn_service_title() {
            return en_service_title;
        }
    }

    public class Nursery_Gallery implements Serializable
    {
        private String id_photo;
        private String photo_name;

        public String getId_photo() {
            return id_photo;
        }

        public String getPhoto_name() {
            return photo_name;
        }
    }
}
