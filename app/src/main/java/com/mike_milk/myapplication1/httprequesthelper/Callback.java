package com.mike_milk.myapplication1.httprequesthelper;

public interface Callback {
    void onResponse(String response);
    void onFailed(Throwable t);
}
