package com.appzone.halimah.models;

import java.io.Serializable;

public class ServiceModel implements Serializable {

    private String id_service;
    private String ar_service_title;
    private String en_service_title;

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
