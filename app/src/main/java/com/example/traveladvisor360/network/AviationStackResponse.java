package com.example.traveladvisor360.network;

import java.util.List;

public class AviationStackResponse {
    public List<CityData> data;

    public static class CityData {
        public String city_name;
        public String country_name;
        public String iata_code;
    }
}