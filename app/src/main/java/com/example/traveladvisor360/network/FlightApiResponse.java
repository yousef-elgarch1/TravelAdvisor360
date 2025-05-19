package com.example.traveladvisor360.network;

import java.util.List;

public class FlightApiResponse {
    public List<FlightOffer> data;

    public static class FlightOffer {
        public Price price;
        public List<Itinerary> itineraries;
        public List<TravelerPricing> travelerPricings;
    }

    public static class Price {
        public String total;
        public String currency;
    }

    public static class Itinerary {
        public List<Segment> segments;
    }

    public static class Segment {
        public String carrierCode;
        public String number;
        public DepartureArrival departure;
        public DepartureArrival arrival;
    }

    public static class DepartureArrival {
        public String iataCode;
        public String at; // ISO date-time string
    }

    public static class TravelerPricing {
        public String travelerId;
        public String fareOption;
        public String travelerType;
        public List<FareDetailsBySegment> fareDetailsBySegment;
    }

    public static class FareDetailsBySegment {
        public String cabin;
        public String fareBasis;
        public String brandedFare;
        public String segmentId;
        public String class_;
    }
}