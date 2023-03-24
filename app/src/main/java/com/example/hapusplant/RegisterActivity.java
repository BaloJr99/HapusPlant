package com.example.hapusplant;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity {

    EditText etBirthdate;
    DatePickerDialog picker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etBirthdate = findViewById(R.id.etBirthdate);

        etBirthdate.setOnClickListener(view -> showDatePicker());
    }

    private void showDatePicker(){
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog
        picker = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> etBirthdate.setText(getString(R.string.dateformat, selectedDay, (selectedMonth + 1) ,selectedYear)), year, month, day);
        picker.show();
    }
}