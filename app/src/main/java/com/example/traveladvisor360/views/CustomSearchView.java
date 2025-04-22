package com.example.traveladvisor360.views;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;

import com.example.traveladvisor360.R;

public class CustomSearchView extends LinearLayout {

    private EditText searchEditText;
    private ImageButton clearButton;
    private ProgressBar progressBar;
    private OnSearchListener searchListener;

    public interface OnSearchListener {
        void onSearch(String query);
        void onClear();
    }

    public CustomSearchView(Context context) {
        super(context);
        init(context);
    }

    public CustomSearchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomSearchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_custom_search, this, true);

        searchEditText = findViewById(R.id.search_edit_text);
        clearButton = findViewById(R.id.clear_button);
        progressBar = findViewById(R.id.progress_bar);

        clearButton.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                clearButton.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
                if (searchListener != null) {
                    searchListener.onSearch(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        clearButton.setOnClickListener(v -> {
            searchEditText.setText("");
            if (searchListener != null) {
                searchListener.onClear();
            }
        });
    }

    public void setOnSearchListener(OnSearchListener listener) {
        this.searchListener = listener;
    }

    public void setSearchText(String text) {
        searchEditText.setText(text);
    }

    public String getSearchText() {
        return searchEditText.getText().toString();
    }

    public void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        clearButton.setVisibility(show ? View.GONE : (searchEditText.getText().length() > 0 ? View.VISIBLE : View.GONE));
    }
}
