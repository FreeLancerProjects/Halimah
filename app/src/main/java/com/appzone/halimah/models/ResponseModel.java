package com.appzone.halimah.models;

import java.io.Serializable;

public class ResponseModel implements Serializable{
    private int success_contact;
    private int success_location;
    private int success_token_id;
    private int success_logout;
    private int success_resevation;
    private int success_read;
    private int success_payment;
    private int success_rest;
    private int success_confirm;
    private int success_transformation;
    private boolean status;

    public int getSuccess_contact() {
        return success_contact;
    }
    public int getSuccess_location() {
        return success_location;
    }
    public int getSuccess_token_id() {
        return success_token_id;
    }
    public int getSuccess_logout() {
        return success_logout;
    }
    public int getSuccess_resevation() {
        return success_resevation;
    }
    public int getSuccess_read() {
        return success_read;
    }
    public int getSuccess_payment() {
        return success_payment;
    }


    public int getSuccess_rest() {
        return success_rest;
    }

    public int getSuccess_confirm() {
        return success_confirm;
    }

    public int getSuccess_transformation() {
        return success_transformation;
    }

    public boolean isStatus() {
        return status;
    }
}
