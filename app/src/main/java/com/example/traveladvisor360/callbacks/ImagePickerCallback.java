package com.example.traveladvisor360.callbacks;

import android.net.Uri;

public interface ImagePickerCallback {
    void onImagePicked(Uri imageUri);
    void onImagePickError(String error);
}
