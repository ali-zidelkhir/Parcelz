package com.example.parcelz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.jetbrains.annotations.NotNull;

public class OTP extends AppCompatActivity {
    EditText editText;
    String otp;
    private FirebaseAuth mAuth;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            //get to main frame
            Intent mainI = new Intent(OTP.this, MainFrame.class);
            startActivity(mainI);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
          mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_otp);
        editText = findViewById(R.id.editTextPhone);
        otp= getIntent().getStringExtra("auth");
    }

    public void VerifyPhone(View view) {
        String verification = editText.getText().toString();
        if(!verification.isEmpty()){
            PhoneAuthCredential credential= PhoneAuthProvider.getCredential(otp, verification);
            SignIn(credential);

        }else{
            Toast.makeText(OTP.this, "enter otp", Toast.LENGTH_LONG).show();
        }
    }
    private void SignIn(PhoneAuthCredential phoneAuthCredential){
        mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent mainI = new Intent(OTP.this, MainFrame.class);
                    startActivity(mainI);
                    finish();
                }else{

                }
            }
        });
    }
}