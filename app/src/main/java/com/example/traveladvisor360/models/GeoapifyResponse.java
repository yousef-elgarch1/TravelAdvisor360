package com.example.traveladvisor360.models;

import java.util.List;

public class GeoapifyResponse {
    public List<Feature> features;

    public static class Feature {
        public Properties properties;
    }

    public static class Properties {
        public String formatted;
        public String city;
        public String country;
        // Add more fields as needed
    }
}