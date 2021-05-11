package com.example.parcelz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.parcelz.Send.Frame_A_Details;

public class MainFrame extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_frame);
    }

    public void TEST(View view) {
        Intent mainI = new Intent(MainFrame.this, Frame_A_Details.class);
        startActivity(mainI);
        finish();
    }
}