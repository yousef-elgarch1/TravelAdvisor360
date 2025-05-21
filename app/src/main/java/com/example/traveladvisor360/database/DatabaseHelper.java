package com.example.traveladvisor360.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "TravelAdvisor.db";
    private static final int DATABASE_VERSION = 2; // Increment version for schema update
    private static DatabaseHelper instance;
    private SQLiteDatabase database;

    // Table names
    public static final String TABLE_USERS = "users";
    public static final String TABLE_ITINERARIES = "itineraries";
    public static final String TABLE_TRAVEL_PLANS = "travel_plans";

    // Common columns
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CREATED_AT = "created_at";

    // Users table columns
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";

    // Itineraries table columns
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_TRIP_TYPE = "trip_type";
    public static final String COLUMN_DESTINATION = "destination";
    public static final String COLUMN_DESTINATION_CITY = "destination_city";
    public static final String COLUMN_DEPARTURE = "departure";
    public static final String COLUMN_BUDGET = "budget";
    public static final String COLUMN_CURRENCY = "currency";
    public static final String COLUMN_START_DATE = "start_date";
    public static final String COLUMN_RETURN_DATE = "return_date";
    public static final String COLUMN_FLIGHT = "flight";
    public static final String COLUMN_FLIGHT_DETAILS = "flight_details";
    public static final String COLUMN_HOTEL = "hotel";
    public static final String COLUMN_HOTEL_DETAILS = "hotel_details";
    public static final String COLUMN_ACTIVITIES = "activities";
    public static final String COLUMN_COMPANIONS = "companions";

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Enable foreign key support
        db.execSQL("PRAGMA foreign_keys = ON;");

        // Create users table
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_EMAIL + " TEXT UNIQUE, " +
                COLUMN_PASSWORD + " TEXT, " +
                COLUMN_CREATED_AT + " TEXT DEFAULT CURRENT_TIMESTAMP)";
        db.execSQL(createUsersTable);

        // Create itineraries table
        String createItinerariesTable = "CREATE TABLE " + TABLE_ITINERARIES + " (" +
                COLUMN_ID + " TEXT PRIMARY KEY, " +
                COLUMN_USER_ID + " INTEGER, " +
                COLUMN_TRIP_TYPE + " TEXT, " +
                COLUMN_DESTINATION + " TEXT, " +
                COLUMN_DESTINATION_CITY + " TEXT, " +
                COLUMN_DEPARTURE + " TEXT, " +
                COLUMN_BUDGET + " REAL, " +
                COLUMN_CURRENCY + " TEXT, " +
                COLUMN_START_DATE + " TEXT, " +
                COLUMN_RETURN_DATE + " TEXT, " +
                COLUMN_FLIGHT + " TEXT, " +
                COLUMN_FLIGHT_DETAILS + " TEXT, " +
                COLUMN_HOTEL + " TEXT, " +
                COLUMN_HOTEL_DETAILS + " TEXT, " +
                COLUMN_ACTIVITIES + " TEXT, " +
                COLUMN_COMPANIONS + " TEXT, " +
                COLUMN_CREATED_AT + " TEXT DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " + 
                TABLE_USERS + "(" + COLUMN_ID + ") ON DELETE CASCADE)";
        db.execSQL(createItinerariesTable);

        // Create travel_plan table
        String createTravelPlanTable = "CREATE TABLE " + TABLE_TRAVEL_PLANS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USER_ID + " INTEGER, " +
                COLUMN_TRIP_TYPE + " TEXT, " +
                COLUMN_DESTINATION + " TEXT, " +
                COLUMN_DESTINATION_CITY + " TEXT, " +
                COLUMN_DEPARTURE + " TEXT, " +
                COLUMN_BUDGET + " REAL, " +
                COLUMN_CURRENCY + " TEXT, " +
                COLUMN_START_DATE + " TEXT, " +
                COLUMN_RETURN_DATE + " TEXT, " +
                COLUMN_FLIGHT_DETAILS + " TEXT, " +
                COLUMN_HOTEL_DETAILS + " TEXT, " +
                COLUMN_ACTIVITIES + " TEXT, " +
                COLUMN_COMPANIONS + " TEXT, " +
                COLUMN_CREATED_AT + " TEXT DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " + 
                TABLE_USERS + "(" + COLUMN_ID + ") ON DELETE CASCADE)";
        db.execSQL(createTravelPlanTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Enable foreign key support
        db.execSQL("PRAGMA foreign_keys = ON;");

        if (oldVersion < 2) {
            // Create itineraries table if upgrading from version 1
            String createItinerariesTable = "CREATE TABLE IF NOT EXISTS " + TABLE_ITINERARIES + " (" +
                    COLUMN_ID + " TEXT PRIMARY KEY, " +
                    COLUMN_USER_ID + " INTEGER, " +
                    COLUMN_TRIP_TYPE + " TEXT, " +
                    COLUMN_DESTINATION + " TEXT, " +
                    COLUMN_DESTINATION_CITY + " TEXT, " +
                    COLUMN_DEPARTURE + " TEXT, " +
                    COLUMN_BUDGET + " REAL, " +
                    COLUMN_CURRENCY + " TEXT, " +
                    COLUMN_START_DATE + " TEXT, " +
                    COLUMN_RETURN_DATE + " TEXT, " +
                    COLUMN_FLIGHT + " TEXT, " +
                    COLUMN_FLIGHT_DETAILS + " TEXT, " +
                    COLUMN_HOTEL + " TEXT, " +
                    COLUMN_HOTEL_DETAILS + " TEXT, " +
                    COLUMN_ACTIVITIES + " TEXT, " +
                    COLUMN_COMPANIONS + " TEXT, " +
                    COLUMN_CREATED_AT + " TEXT DEFAULT CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " + 
                    TABLE_USERS + "(" + COLUMN_ID + ") ON DELETE CASCADE)";
            db.execSQL(createItinerariesTable);
        }
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        // Enable foreign key support
        db.setForeignKeyConstraintsEnabled(true);
    }

    public synchronized SQLiteDatabase getWritableDatabase() {
        if (database == null || !database.isOpen()) {
            database = super.getWritableDatabase();
        }
        return database;
    }

    public synchronized SQLiteDatabase getReadableDatabase() {
        if (database == null || !database.isOpen()) {
            database = super.getReadableDatabase();
        }
        return database;
    }

    public synchronized void closeDatabase() {
        if (database != null && database.isOpen()) {
            database.close();
            database = null;
        }
    }
}