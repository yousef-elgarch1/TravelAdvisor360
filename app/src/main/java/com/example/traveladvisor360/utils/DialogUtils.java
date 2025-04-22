package com.example.traveladvisor360.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class DialogUtils {

    public static void showErrorDialog(Context context, String message) {
        new MaterialAlertDialogBuilder(context)
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

    public static void showSuccessDialog(Context context, String message) {
        new MaterialAlertDialogBuilder(context)
                .setTitle("Success")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

    public static void showConfirmationDialog(Context context, String title, String message,
                                              DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener) {
        new MaterialAlertDialogBuilder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Yes", positiveListener)
                .setNegativeButton("No", negativeListener)
                .show();
    }

    public static void showProgressDialog(Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setCancelable(false)
                .setMessage(message);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}