package com.example.traveladvisor360.fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.traveladvisor360.R;
import com.example.traveladvisor360.models.TravelCompanion;
import com.google.android.material.snackbar.Snackbar;

public class AddCompanionDialog extends DialogFragment {

    private EditText nameEdit;
    private EditText emailEdit;
    private EditText preferencesEdit;
    private Button addButton;
    private Button cancelButton;

    public interface CompanionAddListener {
        void onCompanionAdded(TravelCompanion companion);
    }

    private CompanionAddListener listener;

    public void setCompanionAddListener(CompanionAddListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_add_companion, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nameEdit = view.findViewById(R.id.edit_companion_name);
        emailEdit = view.findViewById(R.id.edit_companion_email);
        preferencesEdit = view.findViewById(R.id.edit_companion_preferences);
        addButton = view.findViewById(R.id.btn_add_companion);
        cancelButton = view.findViewById(R.id.btn_cancel);

        addButton.setOnClickListener(v -> {
            if (validateInputs()) {
                String name = nameEdit.getText().toString().trim();
                String email = emailEdit.getText().toString().trim();
                String preferences = preferencesEdit.getText().toString().trim();

                TravelCompanion companion = new TravelCompanion(name, email, preferences);

                if (listener != null) {
                    listener.onCompanionAdded(companion);
                }

                dismiss();
            }
        });

        cancelButton.setOnClickListener(v -> dismiss());
    }

    private boolean validateInputs() {
        if (nameEdit.getText().toString().trim().isEmpty()) {
            Snackbar.make(requireView(), "Please enter companion's name", Snackbar.LENGTH_SHORT).show();
            return false;
        }

        if (emailEdit.getText().toString().trim().isEmpty()) {
            Snackbar.make(requireView(), "Please enter companion's email", Snackbar.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = (int)(getResources().getDisplayMetrics().widthPixels * 0.9);
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setWindowAnimations(R.style.DialogAnimation);
        }
    }
}