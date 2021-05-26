package com.example.parcelz.Send;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.parcelz.MainActivity;
import com.example.parcelz.MainFrame;
import com.example.parcelz.Models.DetailsFrameA;
import com.example.parcelz.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.shuhart.stepview.StepView;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class Frame_A_Details extends AppCompatActivity {
    private FirebaseAuth mAuth;
    StepView stepView;
    int stepIndex = 0;
    String[] stepsText = {"Pass 1", "Pass 2", "Pass 3"};
    String[] description = {"Details", "GeoLocation", "Confirmation"};

    //SPINNER
    String[] TypeParcel = {"Envelope", "Package", "Livres", "Glass", "Other"};
    Spinner j_spinner;
    EditText Title, H, W, L, Description;

    //DB Real Time Database
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    String Key = "";
    String UID = "";

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        UID = currentFirebaseUser.getUid();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame_adetails);
        j_spinner = findViewById(R.id.TypeParcelSpinner);
        stepView = findViewById(R.id.spb);
        Title = (EditText) findViewById(R.id.TitleEditText);
        H = (EditText) findViewById(R.id.HEditText);
        W = (EditText) findViewById(R.id.WEditText);
        L = (EditText) findViewById(R.id.LEditText);
        Description = (EditText) findViewById(R.id.DescriptionEditText);

        //***********************************************edit stepView
        stepView.getState()
                .animationType(StepView.ANIMATION_ALL)
                .stepsNumber(3)
                .animationDuration(getResources().getInteger(android.R.integer.config_shortAnimTime))
                .commit();
        stepView.setSteps(Arrays.asList(description));

        //*********************************************** edit spinner
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
                //action here
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Send_Details_A");

    }


    public void insertData() {

        String TitleT = Title.getText().toString();
        String HT = H.getText().toString();
        String WT = W.getText().toString();
        String LT = L.getText().toString();
        String TypeT = j_spinner.getSelectedItem().toString();
        String DescriptionT = Description.getText().toString();

        //create Model Data
        DetailsFrameA ModelA = new DetailsFrameA(
                TitleT,
                HT,
                WT,
                LT,
                TypeT,
                DescriptionT,
                0, 0, 0, 0,
                UID, "A", "B"
        );
        databaseReference.push().

                setValue(ModelA);

        //get Last inserted Key
        Query query = databaseReference.orderByKey().limitToLast(1);
        query.addListenerForSingleValueEvent(new

                                                     ValueEventListener() {
                                                         @Override
                                                         public void onDataChange(DataSnapshot dataSnapshot) {
                                                             for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                                                 Key = (childSnapshot.getKey());
                                                             }
                                                         }

                                                         @Override
                                                         public void onCancelled(DatabaseError databaseError) {
                                                             throw databaseError.toException();
                                                         }
                                                     });

    }

    public void Next(View view) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    insertData();
                                    stepIndex++;
                                    stepView.go(stepIndex, true);

                                    Intent mainI = new Intent(Frame_A_Details.this, Frame_B_Details.class);
                                    //mainI.putExtra("Key", Key);
                                    startActivity(mainI);
                                    finish();


                                }
                            }, 2000
        );


    }
}