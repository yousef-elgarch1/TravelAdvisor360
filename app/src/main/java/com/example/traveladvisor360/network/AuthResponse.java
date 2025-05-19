package com.example.traveladvisor360.network;

public class AuthResponse {
    public String access_token;
    public String token_type;
    public int expires_in;
    public String state;
}
//package com.example.traveladvisor360.network;
//
//
//import com.google.gson.annotations.SerializedName;
//
//public class AuthResponse {
//    @SerializedName("access_token")
//    private String accessToken;
//
//    @SerializedName("refresh_token")
//    private String refreshToken;
//
//    @SerializedName("expires_in")
//    private int expiresIn;
//
//    @SerializedName("user_id")
//    private String userId;
//
//    @SerializedName("email")
//    private String email;
//
//    @SerializedName("name")
//    private String name;
//
//    @SerializedName("profile_pic_url")
//    private String profilePicUrl;
//
//    @SerializedName("email_verified")
//    private boolean emailVerified;
//
//    public AuthResponse() {
//    }
//
//    public String getAccessToken() {
//        return accessToken;
//    }
//
//    public void setAccessToken(String accessToken) {
//        this.accessToken = accessToken;
//    }
//
//    public String getRefreshToken() {
//        return refreshToken;
//    }
//
//    public void setRefreshToken(String refreshToken) {
//        this.refreshToken = refreshToken;
//    }
//
//    public int getExpiresIn() {
//        return expiresIn;
//    }
//
//    public void setExpiresIn(int expiresIn) {
//        this.expiresIn = expiresIn;
//    }
//
//    public String getUserId() {
//        return userId;
//    }
//
//    public void setUserId(String userId) {
//        this.userId = userId;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getProfilePicUrl() {
//        return profilePicUrl;
//    }
//
//    public void setProfilePicUrl(String profilePicUrl) {
//        this.profilePicUrl = profilePicUrl;
//    }
//
//    public boolean isEmailVerified() {
//        return emailVerified;
//    }
//
//    public void setEmailVerified(boolean emailVerified) {
//        this.emailVerified = emailVerified;
//    }
//}