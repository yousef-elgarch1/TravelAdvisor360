package com.example.traveladvisor360.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.traveladvisor360.models.SavedItinerary;
import com.example.traveladvisor360.models.TravelCompanion;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class ItineraryRepository {
    private DatabaseHelper dbHelper;
    private static ItineraryRepository instance;
    private Gson gson;
    private SimpleDateFormat dateFormat;

    private ItineraryRepository(Context context) {
        dbHelper = DatabaseHelper.getInstance(context);
        gson = new Gson();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    }

    public static synchronized ItineraryRepository getInstance(Context context) {
        if (instance == null) {
            instance = new ItineraryRepository(context.getApplicationContext());
        }
        return instance;
    }

    public void saveItinerary(SavedItinerary itinerary, String userId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Generate a unique ID if not provided
        if (itinerary.getId() == null || itinerary.getId().isEmpty()) {
            itinerary.setId(UUID.randomUUID().toString());
        }

        values.put("id", itinerary.getId());
        values.put("user_id", userId);
        values.put("trip_type", itinerary.getTripType());
        values.put("destination", itinerary.getDestination());
        values.put("destination_city", itinerary.getDestinationCityName());
        values.put("departure", itinerary.getDeparture());
        values.put("budget", itinerary.getBudget());
        values.put("currency", itinerary.getCurrency());
        values.put("start_date", dateFormat.format(itinerary.getStartDate()));
        values.put("return_date", dateFormat.format(itinerary.getReturnDate()));
        values.put("flight", itinerary.getSelectedFlight());
        values.put("flight_details", gson.toJson(itinerary.getFlightDetails()));
        values.put("hotel", itinerary.getSelectedHotel());
        values.put("hotel_details", gson.toJson(itinerary.getHotelDetails()));
        values.put("activities", gson.toJson(itinerary.getSelectedActivities()));
        values.put("companions", gson.toJson(itinerary.getCompanions()));

        db.insert("itineraries", null, values);
    }

    public List<SavedItinerary> getAllItineraries(String userId) {
        List<SavedItinerary> itineraries = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query("itineraries",
                null,
                "user_id = ?",
                new String[]{userId},
                null, null, "created_at DESC");

        if (cursor != null) {
            while (cursor.moveToNext()) {
                SavedItinerary itinerary = new SavedItinerary();
                itinerary.setId(cursor.getString(cursor.getColumnIndex("id")));
                itinerary.setTripType(cursor.getString(cursor.getColumnIndex("trip_type")));
                itinerary.setDestination(cursor.getString(cursor.getColumnIndex("destination")));
                itinerary.setDestinationCityName(cursor.getString(cursor.getColumnIndex("destination_city")));
                itinerary.setDeparture(cursor.getString(cursor.getColumnIndex("departure")));
                itinerary.setBudget(cursor.getDouble(cursor.getColumnIndex("budget")));
                itinerary.setCurrency(cursor.getString(cursor.getColumnIndex("currency")));
                try {
                    String startDateStr = cursor.getString(cursor.getColumnIndex("start_date"));
                    String returnDateStr = cursor.getString(cursor.getColumnIndex("return_date"));
                    if (startDateStr != null) {
                        itinerary.setStartDate(dateFormat.parse(startDateStr));
                    }
                    if (returnDateStr != null) {
                        itinerary.setReturnDate(dateFormat.parse(returnDateStr));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                itinerary.setSelectedFlight(cursor.getString(cursor.getColumnIndex("flight")));
                
                String flightDetailsJson = cursor.getString(cursor.getColumnIndex("flight_details"));
                if (flightDetailsJson != null) {
                    Type mapType = new TypeToken<Map<String, Object>>(){}.getType();
                    Map<String, Object> flightDetails = gson.fromJson(flightDetailsJson, mapType);
                    itinerary.setFlightDetails(flightDetails);
                }

                itinerary.setSelectedHotel(cursor.getString(cursor.getColumnIndex("hotel")));
                
                String hotelDetailsJson = cursor.getString(cursor.getColumnIndex("hotel_details"));
                if (hotelDetailsJson != null) {
                    Type mapType = new TypeToken<Map<String, Object>>(){}.getType();
                    Map<String, Object> hotelDetails = gson.fromJson(hotelDetailsJson, mapType);
                    itinerary.setHotelDetails(hotelDetails);
                }

                String activitiesJson = cursor.getString(cursor.getColumnIndex("activities"));
                if (activitiesJson != null) {
                    Type listType = new TypeToken<List<Map<String, Object>>>(){}.getType();
                    List<Map<String, Object>> activitiesMaps = gson.fromJson(activitiesJson, listType);
                    List<String> activities = new ArrayList<>();
                    for (Map<String, Object> activityMap : activitiesMaps) {
                        if (activityMap.containsKey("name")) {
                            activities.add(activityMap.get("name").toString());
                        }
                    }
                    itinerary.setSelectedActivities(activities);
                }

                String companionsJson = cursor.getString(cursor.getColumnIndex("companions"));
                if (companionsJson != null) {
                    Type listType = new TypeToken<List<Map<String, Object>>>(){}.getType();
                    List<Map<String, Object>> companionsMaps = gson.fromJson(companionsJson, listType);
                    List<TravelCompanion> companions = new ArrayList<>();
                    for (Map<String, Object> companionMap : companionsMaps) {
                        String name = companionMap.containsKey("name") ? companionMap.get("name").toString() : "";
                        String email = companionMap.containsKey("email") ? companionMap.get("email").toString() : "";
                        String phone = companionMap.containsKey("phone") ? companionMap.get("phone").toString() : "";
                        TravelCompanion companion = new TravelCompanion(name, email, phone);
                        companions.add(companion);
                    }
                    itinerary.setCompanions(companions);
                }

                itineraries.add(itinerary);
            }
            cursor.close();
        }

        return itineraries;
    }

    public List<SavedItinerary> getItinerariesByType(String userId, String tripType) {
        List<SavedItinerary> itineraries = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query("itineraries",
                null,
                "user_id = ? AND trip_type = ?",
                new String[]{userId, tripType},
                null, null, "created_at DESC");

        if (cursor != null) {
            while (cursor.moveToNext()) {
                SavedItinerary itinerary = new SavedItinerary();
                itinerary.setId(cursor.getString(cursor.getColumnIndex("id")));
                itinerary.setTripType(cursor.getString(cursor.getColumnIndex("trip_type")));
                itinerary.setDestination(cursor.getString(cursor.getColumnIndex("destination")));
                itinerary.setDestinationCityName(cursor.getString(cursor.getColumnIndex("destination_city")));
                itinerary.setDeparture(cursor.getString(cursor.getColumnIndex("departure")));
                itinerary.setBudget(cursor.getDouble(cursor.getColumnIndex("budget")));
                itinerary.setCurrency(cursor.getString(cursor.getColumnIndex("currency")));
                try {
                    String startDateStr = cursor.getString(cursor.getColumnIndex("start_date"));
                    String returnDateStr = cursor.getString(cursor.getColumnIndex("return_date"));
                    if (startDateStr != null) {
                        itinerary.setStartDate(dateFormat.parse(startDateStr));
                    }
                    if (returnDateStr != null) {
                        itinerary.setReturnDate(dateFormat.parse(returnDateStr));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                itinerary.setSelectedFlight(cursor.getString(cursor.getColumnIndex("flight")));
                
                String flightDetailsJson = cursor.getString(cursor.getColumnIndex("flight_details"));
                if (flightDetailsJson != null) {
                    Type mapType = new TypeToken<Map<String, Object>>(){}.getType();
                    Map<String, Object> flightDetails = gson.fromJson(flightDetailsJson, mapType);
                    itinerary.setFlightDetails(flightDetails);
                }

                itinerary.setSelectedHotel(cursor.getString(cursor.getColumnIndex("hotel")));
                
                String hotelDetailsJson = cursor.getString(cursor.getColumnIndex("hotel_details"));
                if (hotelDetailsJson != null) {
                    Type mapType = new TypeToken<Map<String, Object>>(){}.getType();
                    Map<String, Object> hotelDetails = gson.fromJson(hotelDetailsJson, mapType);
                    itinerary.setHotelDetails(hotelDetails);
                }

                String activitiesJson = cursor.getString(cursor.getColumnIndex("activities"));
                if (activitiesJson != null) {
                    Type listType = new TypeToken<List<Map<String, Object>>>(){}.getType();
                    List<Map<String, Object>> activitiesMaps = gson.fromJson(activitiesJson, listType);
                    List<String> activities = new ArrayList<>();
                    for (Map<String, Object> activityMap : activitiesMaps) {
                        if (activityMap.containsKey("name")) {
                            activities.add(activityMap.get("name").toString());
                        }
                    }
                    itinerary.setSelectedActivities(activities);
                }

                String companionsJson = cursor.getString(cursor.getColumnIndex("companions"));
                if (companionsJson != null) {
                    Type listType = new TypeToken<List<Map<String, Object>>>(){}.getType();
                    List<Map<String, Object>> companionsMaps = gson.fromJson(companionsJson, listType);
                    List<TravelCompanion> companions = new ArrayList<>();
                    for (Map<String, Object> companionMap : companionsMaps) {
                        String name = companionMap.containsKey("name") ? companionMap.get("name").toString() : "";
                        String email = companionMap.containsKey("email") ? companionMap.get("email").toString() : "";
                        String phone = companionMap.containsKey("phone") ? companionMap.get("phone").toString() : "";
                        TravelCompanion companion = new TravelCompanion(name, email, phone);
                        companions.add(companion);
                    }
                    itinerary.setCompanions(companions);
                }

                itineraries.add(itinerary);
            }
            cursor.close();
        }

        return itineraries;
    }
} 