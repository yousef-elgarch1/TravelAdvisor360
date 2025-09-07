# Travel Advisor360 📱✈️

<div align="center">
  <img src="https://i.imgur.com/5Zo5MiQ.png" alt="Travel Advisor360 Logo" width="200"/>
  
  <p><em>Your intelligent travel companion for seamless journey planning</em></p>

  [![Platform](https://img.shields.io/badge/Platform-Android-green.svg)](https://www.android.com)
  [![Language](https://img.shields.io/badge/Language-Java-orange.svg)](https://www.java.com)
  [![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
  [![Version](https://img.shields.io/badge/Version-1.0.0-purple.svg)](https://github.com/yourusername/traveladvisor360)
</div>

---

## 📑 Table of Contents
- [Overview](#overview)
- [Features](#features)
- [Project Architecture](#project-architecture)
- [Screenshots](#screenshots)
- [Installation](#installation)
- [Dependencies](#dependencies)
- [API Integrations](#api-integrations)
- [Team Members](#team-members)
- [Supervision](#supervision)
- [License](#license)

---

## 🌟 Overview

**Travel Advisor360** is a comprehensive Android mobile application designed to revolutionize the way travel enthusiasts plan and experience their journeys. The app serves as an all-in-one travel companion, offering intelligent recommendations, itinerary planning, booking services, and social features to share travel experiences.

Our application empowers users to:
- Plan detailed itineraries with customizable activities and timeframes
- Discover unique destinations with AI-powered recommendations
- Book flights and accommodations directly through the app
- Connect with fellow travelers and share experiences
- Access travel tips and cultural insights for destinations
- Manage budgets and track expenses during trips

Whether you're a solo traveler, planning a family vacation, or organizing a group adventure, Travel Advisor360 provides the tools and insights to create memorable journeys tailored to your preferences.

---

## ✨ Features
 <img src="https://i.imgur.com/ZaSiHLb.png" alt="Travel Advisor360 Logo" width="600"/>

### 🗺️ Destination Discovery
- Browse curated destinations with detailed information
- Filter options based on preferences, budget, and season
- AI-powered destination recommendations based on user profile

### 📝 Itinerary Planning
- Create detailed day-by-day itineraries
- Add and organize activities with time slots
- Manual and AI-assisted planning options
- Flexible editing and rearranging of itinerary items

### 🧳 Travel Experience Management
- Document and share travel experiences
- Photo integration with experiences
- Rate and review attractions, hotels, and activities
- Experience recommendations based on user preferences

### 🤖 AI Integration
- Smart recommendations for destinations and activities
- Automated itinerary generation based on preferences
- Personalized travel tips and cultural insights
- Chatbot assistance for travel queries

### 🏨 Booking Services
- Seamless flight and hotel booking integration
- Price comparison for best deals
- Booking management and confirmation tracking
- Calendar integration for travel dates

### 👥 Social Features
- Add and manage travel companions
- Group trip planning tools
- Experience sharing with the community
- Comment and interaction on shared experiences

### 🧭 Navigation & Exploration
- "Chemin" feature for off-the-beaten-path discoveries
- Attraction exploration with detailed information
- Integrated maps and directions
- City tour planning with optimized routes

### 👤 User Profile & Preferences
- Customizable user profiles
- Travel preference settings
- Trip history and statistics
- Authentication and secure account management

---

## 🏗️ Project Architecture

Travel Advisor360 follows a modular, clean architecture pattern that separates concerns and promotes maintainability:

```
com.example.traveladvisor360/
├── activities/           # Main UI screens
├── adapters/             # RecyclerView and data adapters
├── callbacks/            # Interface implementations
├── database/             # Local data persistence
├── fragments/            # UI components within activities
├── interfaces/           # Contract definitions
├── models/               # Data models
├── network/              # API communication
├── services/             # Background services
└── utils/                # Helper classes
```

### Key Components:

#### Activities
The app includes specialized activities for each major function:
- `SplashActivity`: App entry point with branding
- `MainActivity`: Central navigation hub
- `AuthActivity`: User authentication flow
- `DestinationsActivity`: Destination browsing and selection
- `ItinerariesActivity`: Itinerary management
- `ExperiencesActivity`: Travel experience sharing
- `AIRecommendationsActivity`: AI-powered suggestions
- `FlightBookingActivity`: Flight search and booking
- `HotelBookingActivity`: Accommodation booking
- `ProfileActivity`: User profile management

#### Architecture Pattern
The application implements the MVVM (Model-View-ViewModel) architecture pattern with:
- **Model**: Data classes in the `models` package
- **View**: Activities and Fragments
- **ViewModel**: Data handling and business logic
- **Repository**: Data access abstraction

#### Data Flow
```
┌─────────────┐    ┌─────────────┐    ┌─────────────┐    ┌─────────────┐
│   UI Layer  │◄──►│  ViewModel  │◄──►│ Repository  │◄──►│  Data Source│
│ (Activities │    │    Layer    │    │    Layer    │    │  (API/Local)│
│  Fragments) │    │             │    │             │    │             │
└─────────────┘    └─────────────┘    └─────────────┘    └─────────────┘
```

 <img src="https://i.imgur.com/838mSR5.png" alt="Travel Advisor360 Logo" width="600"/>
#### Database Structure
The app uses a local SQLite database with Room Persistence Library:
- User profiles and preferences
- Saved itineraries and experiences
- Cached destination data
- Travel companion information

#### Network Layer
API integration with travel services:
- Amadeus for flight booking
- Hotel booking APIs
- Geoapify for location services
- Aviation Stack for flight information

---

## 📱 Screenshots

<div align="center">
  <table>
    <tr>
      <td><img src="/api/placeholder/200/400" alt="Splash Screen" width="200"/></td>
      <td><img src="/api/placeholder/200/400" alt="Home Screen" width="200"/></td>
      <td><img src="/api/placeholder/200/400" alt="Destination Details" width="200"/></td>
    </tr>
    <tr>
      <td align="center"><strong>Welcome Screen</strong></td>
      <td align="center"><strong>Home Dashboard</strong></td>
      <td align="center"><strong>Destination Details</strong></td>
    </tr>
    <tr>
      <td><img src="/api/placeholder/200/400" alt="Itinerary Planning" width="200"/></td>
      <td><img src="/api/placeholder/200/400" alt="Flight Booking" width="200"/></td>
      <td><img src="/api/placeholder/200/400" alt="AI Recommendations" width="200"/></td>
    </tr>
    <tr>
      <td align="center"><strong>Itinerary Planning</strong></td>
      <td align="center"><strong>Flight Booking</strong></td>
      <td align="center"><strong>AI Recommendations</strong></td>
    </tr>
  </table>
</div>

---

## 🚀 Installation

### Prerequisites
- Android Studio Arctic Fox (2021.3.1) or higher
- JDK 11 or higher
- Android SDK 31 (Android 12) or higher
- Gradle 7.0.2 or higher

### Setup Instructions
1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/traveladvisor360.git
   ```

2. Open the project in Android Studio:
   ```bash
   cd traveladvisor360
   ```

3. Configure API keys:
   - Create a `secrets.xml` file in the `res/values` directory
   - Add your API keys:
     ```xml
     <?xml version="1.0" encoding="utf-8"?>
     <resources>
         <string name="amadeus_api_key">YOUR_AMADEUS_API_KEY</string>
         <string name="geoapify_api_key">YOUR_GEOAPIFY_API_KEY</string>
         <string name="aviation_stack_api_key">YOUR_AVIATION_STACK_API_KEY</string>
     </resources>
     ```

4. Sync Gradle and run the app:
   - Click "Sync Project with Gradle Files"
   - Select a device or emulator (minimum API level 26 recommended)
   - Click "Run app" (▶️)

---

## 📦 Dependencies

### Core Dependencies
```gradle
// Android Architecture Components
implementation 'androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1'
implementation 'androidx.lifecycle:lifecycle-livedata-ktx:2.5.1'
implementation 'androidx.room:room-runtime:2.4.3'
kapt 'androidx.room:room-compiler:2.4.3'

// Networking
implementation 'com.squareup.retrofit2:retrofit:2.9.0'
implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
implementation 'com.squareup.okhttp3:okhttp:4.9.3'
implementation 'com.squareup.okhttp3:logging-interceptor:4.9.3'

// UI Components
implementation 'androidx.recyclerview:recyclerview:1.2.1'
implementation 'com.google.android.material:material:1.6.1'
implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
implementation 'com.github.bumptech.glide:glide:4.13.2'

// Maps and Location
implementation 'com.google.android.gms:play-services-maps:18.1.0'
implementation 'com.google.android.gms:play-services-location:20.0.0'

// Animation
implementation 'com.airbnb.android:lottie:5.2.0'

// AI and ML
implementation 'org.tensorflow:tensorflow-lite:2.9.0'
```

### Testing Dependencies
```gradle
// Unit Testing
testImplementation 'junit:junit:4.13.2'
testImplementation 'androidx.arch.core:core-testing:2.1.0'
testImplementation 'org.mockito:mockito-core:4.0.0'

// UI Testing
androidTestImplementation 'androidx.test.ext:junit:1.1.3'
androidTestImplementation 'androidx.test.espresso:espresso-core:3.4.0'
```

---

## 🔌 API Integrations

Travel Advisor360 leverages several external APIs to provide comprehensive travel services:

### 🛫 Amadeus Travel API
- Flight search and booking
- Hotel availability and booking
- Points of interest data

### 🗺️ Geoapify
- Location services and geocoding
- Place details and recommendations
- Route optimization for itineraries

### ✈️ Aviation Stack
- Real-time flight tracking
- Airport information
- Flight status updates

### 🤖 Custom AI Recommendation Service
- Personalized destination recommendations
- Activity suggestions based on user preferences
- Smart itinerary generation

### 🏨 Hotel Booking Services
- Room availability and pricing
- Booking management
- Property details and amenities

---

## 👥 Team Members

<div align="center">
  <table>
    <tr>
      <td align="center"><img src="https://avatars.githubusercontent.com/u/146676271?v=4" alt="Youssef ELGARCH" width="100" style="border-radius:50%"/><br/><strong>Youssef ELGARCH</strong><br/>
      <td align="center"><img src="https://media.licdn.com/dms/image/v2/D4E03AQE-6UajLV1hMA/profile-displayphoto-scale_400_400/B4EZhcype.HIAg-/0/1753903439714?e=1759968000&v=beta&t=_CCBYnq2T_J5yUkfep4jLCSMjGOV3Y6mQy8SCklwN6U" alt="Hajar ELKASIRI" width="100" style="border-radius:50%"/><br/><strong>Hajar ELKASIRI</strong><br/>
    </tr>
    <tr>
      <td align="center"><img src="https://media.licdn.com/dms/image/v2/D4E03AQGgo4PLeDDxtQ/profile-displayphoto-shrink_400_400/profile-displayphoto-shrink_400_400/0/1729197093114?e=1759968000&v=beta&t=VSs9U4zOZ8FYOaH8hZPGBxRV8iLbkPImOfgrR0P_dtI" alt="Nisrine IBNOU-KADY" width="100" style="border-radius:50%"/><br/><strong>Nisrine IBNOU-KADY</strong><br/>
      <td align="center"><img src="/api/placeholder/100/100" alt="Hiba EL OUERKHAOUI" width="100" style="border-radius:50%"/><br/><strong>Hiba EL OUERKHAOUI</strong><br/>
    </tr>
  </table>
</div>

---

## 👨‍🏫 Supervision

<div align="center">
  <div style="padding: 20px; border: 2px solid #009688; border-radius: 10px; max-width: 500px; margin: 0 auto;">
    <img src="https://i.imgur.com/Lumjpqh.png" alt="Professor Hatim GUERMAH" width="120" style="border-radius:50%"/>
    <h3>Professor Hatim GUERMAH</h3>
    <p><em>Project Supervisor</em></p>
    <p>Head of Software Engineering- ENSIAS</p>
  </div>
</div>

---

## 📊 Project Stats

<div align="center">
  <table>
    <tr>
      <td align="center">
        <strong>20+</strong><br/>
        <span>Activities</span>
      </td>
      <td align="center">
        <strong>30+</strong><br/>
        <span>Fragments</span>
      </td>
      <td align="center">
        <strong>40+</strong><br/>
        <span>Models</span>
      </td>
      <td align="center">
        <strong>5+</strong><br/>
        <span>API Integrations</span>
      </td>
    </tr>
  </table>
</div>

---

## 📝 License

Travel Advisor360 is released under the MIT License. See the [LICENSE](LICENSE) file for details.

---

<div align="center">
  <p>© 2025 Travel Advisor360 Team. All Rights Reserved.</p>
  <p>
    <a href="mailto:contact@traveladvisor360.com">📧 Contact Us</a> |
    <a href="https://github.com/yourusername/traveladvisor360">🌐 GitHub</a> |
    <a href="https://traveladvisor360.com">🌍 Website</a>
  </p>
</div>
