package com.example.parcelz.Send;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.parcelz.MainFrame;
import com.example.parcelz.R;
import com.shuhart.stepview.StepView;

import java.util.Arrays;

public class Frame_A_Details extends AppCompatActivity {

    StepView stepView;
    int stepIndex = 0;
    String[] stepsText = {"Pass 1", "Pass 2", "Pass 3"};
    String[] description = {"Details", "GeoLocation", "Confirmation"};

    //SPINNER
    String[] TypeParcel = {"Envelope", "Package", "Livres", "Glass", "Other"};
    Spinner j_spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame_adetails);
        j_spinner = findViewById(R.id.spinner);
        stepView = findViewById(R.id.spb);

        //***********************************************edit stepView
        stepView.getState()
                .animationType(StepView.ANIMATION_ALL)
                .stepsNumber(3)
                .animationDuration(getResources().getInteger(android.R.integer.config_shortAnimTime))
                .commit();
        stepView.setSteps(Arrays.asList(description));

        //***********************************************edit spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                TypeParcel
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        j_spinner.setAdapter(adapter);
        j_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(
                        Frame_A_Details.this,
                        j_spinner.getSelectedItem().toString(),
                        Toast.LENGTH_SHORT
                ).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    public void Next(View view) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    stepIndex++;
                                    stepView.go(stepIndex, true);
                                    Intent mainI = new Intent(Frame_A_Details.this, Frame_B_Details.class);
                                    startActivity(mainI);
                                    finish();
                                }
                            }, 3000
        );


    }
}