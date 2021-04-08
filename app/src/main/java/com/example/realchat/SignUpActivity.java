package com.example.realchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference userReference;
    EditText email,pass,confirmPass,userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        userReference=database.getReference("user");
        setContentView(R.layout.activity_sign_up);
        email=findViewById(R.id.emailEdText);
        userName=findViewById(R.id.userNameEdText);
        pass=findViewById(R.id.passwordEdText);
        confirmPass=findViewById(R.id.confirmPassEdText);
    }
    public void signUpClick(View view){
        if(pass.getText().toString().equals(confirmPass.getText().toString())){
            auth.createUserWithEmailAndPassword(email.getText().toString().trim(),pass.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        userReference.child(auth.getCurrentUser().getUid().toString()).setValue(userName.getText().toString().trim());
                        Intent intent= new Intent(SignUpActivity.this,MainActivity.class);
                        Toast.makeText(SignUpActivity.this, "Sign Up Successful", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                        finish();
                    }else{
                        Log.i("SignUpError",task.getException().toString());
                        Toast.makeText(SignUpActivity.this,"Something went wrong Please try again",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }else{
            Toast.makeText(SignUpActivity.this,"Password not matching!",Toast.LENGTH_LONG).show();
        }

    }
}