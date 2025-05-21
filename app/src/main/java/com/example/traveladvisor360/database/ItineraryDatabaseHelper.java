package com.example.traveladvisor360.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.traveladvisor360.models.SavedItinerary;
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

public class ItineraryDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "itineraries.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_ITINERARIES = "itineraries";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TRIP_TYPE = "trip_type";
    private static final String COLUMN_DESTINATION = "destination";
    private static final String COLUMN_DESTINATION_CITY = "destination_city";
    private static final String COLUMN_DEPARTURE = "departure";
    private static final String COLUMN_BUDGET = "budget";
    private static final String COLUMN_CURRENCY = "currency";
    private static final String COLUMN_START_DATE = "start_date";
    private static final String COLUMN_RETURN_DATE = "return_date";
    private static final String COLUMN_FLIGHT = "flight";
    private static final String COLUMN_FLIGHT_DETAILS = "flight_details";
    private static final String COLUMN_HOTEL = "hotel";
    private static final String COLUMN_HOTEL_DETAILS = "hotel_details";
    private static final String COLUMN_ACTIVITIES = "activities";
    private static final String COLUMN_COMPANIONS = "companions";
    private static final String COLUMN_CREATED_AT = "created_at";

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    private final Gson gson = new Gson();

    public ItineraryDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_ITINERARIES + " (" +
                COLUMN_ID + " TEXT PRIMARY KEY," +
                COLUMN_TRIP_TYPE + " TEXT," +
                COLUMN_DESTINATION + " TEXT," +
                COLUMN_DESTINATION_CITY + " TEXT," +
                COLUMN_DEPARTURE + " TEXT," +
                COLUMN_BUDGET + " REAL," +
                COLUMN_CURRENCY + " TEXT," +
                COLUMN_START_DATE + " TEXT," +
                COLUMN_RETURN_DATE + " TEXT," +
                COLUMN_FLIGHT + " TEXT," +
                COLUMN_FLIGHT_DETAILS + " TEXT," +
                COLUMN_HOTEL + " TEXT," +
                COLUMN_HOTEL_DETAILS + " TEXT," +
                COLUMN_ACTIVITIES + " TEXT," +
                COLUMN_COMPANIONS + " TEXT," +
                COLUMN_CREATED_AT + " TEXT" +
                ")";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITINERARIES);
        onCreate(db);
    }

    public void saveItinerary(SavedItinerary itinerary) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_ID, itinerary.getId());
        values.put(COLUMN_TRIP_TYPE, itinerary.getTripType());
        values.put(COLUMN_DESTINATION, itinerary.getDestination());
        values.put(COLUMN_DESTINATION_CITY, itinerary.getDestinationCityName());
        values.put(COLUMN_DEPARTURE, itinerary.getDeparture());
        values.put(COLUMN_BUDGET, itinerary.getBudget());
        values.put(COLUMN_CURRENCY, itinerary.getCurrency());
        values.put(COLUMN_START_DATE, dateFormat.format(itinerary.getStartDate()));
        values.put(COLUMN_RETURN_DATE, dateFormat.format(itinerary.getReturnDate()));
        
        // Handle flight details from API
        if (itinerary.getSelectedFlight() != null) {
            values.put(COLUMN_FLIGHT, itinerary.getSelectedFlight());
            if (itinerary.getFlightDetails() != null) {
                // Ensure all required flight details are present
                Map<String, Object> flightDetails = itinerary.getFlightDetails();
                if (!flightDetails.containsKey("airline")) flightDetails.put("airline", "Unknown");
                if (!flightDetails.containsKey("class")) flightDetails.put("class", "Economy");
                if (!flightDetails.containsKey("stops")) flightDetails.put("stops", 0);
                if (!flightDetails.containsKey("departure")) flightDetails.put("departure", "TBD");
                if (!flightDetails.containsKey("arrival")) flightDetails.put("arrival", "TBD");
                if (!flightDetails.containsKey("price")) flightDetails.put("price", 0.0);
                values.put(COLUMN_FLIGHT_DETAILS, gson.toJson(flightDetails));
            }
        }

        // Handle hotel details from API
        if (itinerary.getSelectedHotel() != null) {
            values.put(COLUMN_HOTEL, itinerary.getSelectedHotel());
            if (itinerary.getHotelDetails() != null) {
                // Ensure all required hotel details are present
                Map<String, Object> hotelDetails = itinerary.getHotelDetails();
                if (!hotelDetails.containsKey("location")) hotelDetails.put("location", "TBD");
                if (!hotelDetails.containsKey("description")) hotelDetails.put("description", "No description available");
                if (!hotelDetails.containsKey("rating")) hotelDetails.put("rating", 0.0);
                if (!hotelDetails.containsKey("stars")) hotelDetails.put("stars", 0);
                if (!hotelDetails.containsKey("price")) hotelDetails.put("price", 0.0);
                values.put(COLUMN_HOTEL_DETAILS, gson.toJson(hotelDetails));
            }
        }

        // Handle activities
        if (itinerary.getSelectedActivities() != null) {
            values.put(COLUMN_ACTIVITIES, gson.toJson(itinerary.getSelectedActivities()));
        }

        // Handle companions
        if (itinerary.getCompanions() != null) {
            values.put(COLUMN_COMPANIONS, gson.toJson(itinerary.getCompanions()));
        }

        values.put(COLUMN_CREATED_AT, dateFormat.format(itinerary.getCreatedAt()));

        db.insertWithOnConflict(TABLE_ITINERARIES, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public List<SavedItinerary> getAllItineraries() {
        List<SavedItinerary> itineraries = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_ITINERARIES + " ORDER BY " + COLUMN_START_DATE + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                SavedItinerary itinerary = cursorToItinerary(cursor);
                itineraries.add(itinerary);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return itineraries;
    }

    public List<SavedItinerary> getItinerariesByType(String type) {
        List<SavedItinerary> itineraries = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_ITINERARIES + 
                           " WHERE " + COLUMN_TRIP_TYPE + " = ?" +
                           " ORDER BY " + COLUMN_START_DATE + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{type});

        if (cursor.moveToFirst()) {
            do {
                SavedItinerary itinerary = cursorToItinerary(cursor);
                itineraries.add(itinerary);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return itineraries;
    }

    private SavedItinerary cursorToItinerary(Cursor cursor) {
        SavedItinerary itinerary = new SavedItinerary();
        try {
            itinerary.setId(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID)));
            itinerary.setTripType(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TRIP_TYPE)));
            itinerary.setDestination(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESTINATION)));
            itinerary.setDestinationCityName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESTINATION_CITY)));
            itinerary.setDeparture(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DEPARTURE)));
            itinerary.setBudget(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_BUDGET)));
            itinerary.setCurrency(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CURRENCY)));
            itinerary.setStartDate(dateFormat.parse(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_START_DATE))));
            itinerary.setReturnDate(dateFormat.parse(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RETURN_DATE))));
            
            // Parse flight details
            String flight = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FLIGHT));
            if (flight != null) {
                itinerary.setSelectedFlight(flight);
                String flightDetailsJson = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FLIGHT_DETAILS));
                if (flightDetailsJson != null) {
                    Type mapType = new TypeToken<Map<String, Object>>(){}.getType();
                    itinerary.setFlightDetails(gson.fromJson(flightDetailsJson, mapType));
                }
            }

            // Parse hotel details
            String hotel = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HOTEL));
            if (hotel != null) {
                itinerary.setSelectedHotel(hotel);
                String hotelDetailsJson = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HOTEL_DETAILS));
                if (hotelDetailsJson != null) {
                    Type mapType = new TypeToken<Map<String, Object>>(){}.getType();
                    itinerary.setHotelDetails(gson.fromJson(hotelDetailsJson, mapType));
                }
            }

            // Parse activities and companions
            String activitiesJson = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ACTIVITIES));
            if (activitiesJson != null) {
                Type listType = new TypeToken<List<String>>(){}.getType();
                itinerary.setSelectedActivities(gson.fromJson(activitiesJson, listType));
            }

            String companionsJson = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COMPANIONS));
            if (companionsJson != null) {
                Type companionType = new TypeToken<List<com.example.traveladvisor360.models.TravelCompanion>>(){}.getType();
                itinerary.setCompanions(gson.fromJson(companionsJson, companionType));
            }

            itinerary.setCreatedAt(dateFormat.parse(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CREATED_AT))));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return itinerary;
    }
} 