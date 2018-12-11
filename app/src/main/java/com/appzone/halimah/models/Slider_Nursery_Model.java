package com.appzone.halimah.models;

import java.io.Serializable;
import java.util.List;

public class Slider_Nursery_Model implements Serializable {

    private List<SliderModel> slider;
    private List<NurseryModel> list;

    public List<SliderModel> getSlider() {
        return slider;
    }

    public List<NurseryModel> getList() {
        return list;
    }

    public class SliderModel implements Serializable
    {
        private String show_iamge;

        public String getShow_iamge() {
            return show_iamge;
        }
    }

    public class NurseryModel implements Serializable
    {
        private String user_id;
        private String user_type;
        private String user_full_name;
        private String user_phone;
        private String user_image;
        private String user_google_lat;
        private String user_google_long;
        private String user_address;
        private String person_responsible_name;
        private String person_responsible_phone;
        private String hour_cost;
        private String dst;
        private int stars_num;
        private String to_hour;
        private String from_hour;
        private List<ServiceModel> service;
        private List<GalleryModel> gallary;
        public String getUser_id() {
            return user_id;
        }

        public String getUser_type() {
            return user_type;
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

        public String getUser_google_lat() {
            return user_google_lat;
        }

        public String getUser_google_long() {
            return user_google_long;
        }

        public String getUser_address() {
            return user_address;
        }

        public String getPerson_responsible_name() {
            return person_responsible_name;
        }

        public String getPerson_responsible_phone() {
            return person_responsible_phone;
        }

        public String getHour_cost() {
            return hour_cost;
        }

        public int getStars_num() {
            return stars_num;
        }

        public String getTo_hour() {
            return to_hour;
        }

        public String getDst() {
            return dst;
        }

        public String getFrom_hour() {
            return from_hour;
        }

        public List<ServiceModel> getService() {
            return service;
        }

        public List<GalleryModel> getGallary() {
            return gallary;
        }


        public class ServiceModel implements Serializable
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

        public class GalleryModel implements Serializable
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




}
