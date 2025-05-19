package com.example.traveladvisor360.network;

import java.util.List;

public class HotelApiResponse {
    public List<HotelData> data;

    public static class HotelData {
        public String name;
        public Address address;
        public Description description;
        public double rating;
        public Price price;
        // Add other fields as needed

        public static class Address {
            public String cityName;
        }
        public static class Description {
            public String text;
        }
        public static class Price {
            public double total;
        }
    }
}