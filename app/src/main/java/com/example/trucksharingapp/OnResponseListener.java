package com.example.trucksharingapp;

/**
 * Request callbacks
 */
public interface OnResponseListener {

    void onSuccess(String response);

    void onError(String error);
}