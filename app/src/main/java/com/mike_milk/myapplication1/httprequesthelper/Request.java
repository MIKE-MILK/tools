package com.mike_milk.myapplication1.httprequesthelper;

public class Request  {
    private String URL;
    private String[]key;
    private String[]value;
    private String Method;

    public String getURL() {
        return URL;
    }

    public String[] getKey() {
        return key;
    }

    public String[] getValue() {
        return value;
    }

    public String getMethod() {
        return Method;
    }

    Request(Builder builder){
        this.URL=builder.URL;
        this.key=builder.key;
        this.value=builder.value;
        this.Method=builder.Method;
    }
    public static class Builder{
         String URL;
         String[]key;
         String[]value;
         String Method;
        public Builder(String URL){
            this.URL=URL;
        }
        public Builder key(String[]key){
            this.key=key;
           return this;
        }
        public Builder value(String[]value){
            this.value=value;
            return this;
        }
        public Builder Method(String Method){
            this.Method=Method;
            return this;
        }
        public Request builder(){
            return new Request(this);
        }
    }
}
