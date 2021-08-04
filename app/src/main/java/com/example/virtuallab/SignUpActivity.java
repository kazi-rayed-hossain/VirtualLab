package com.example.virtuallab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "SignUpActivity";
    public String phoneStr;

    //Declare an instance of FirebaseAuth
    private FirebaseAuth mAuth1;

    // FireStore initialize
    private  FirebaseFirestore db ;


    Button goButton,signInButton;
    TextInputEditText name,email,password, phoneEt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //initialize the FirebaseAuth instance
        mAuth1 = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        goButton = findViewById(R.id.go);
        signInButton = findViewById(R.id.signin);

        name = findViewById(R.id.name_id);
        email = findViewById(R.id.email_id);
        password = findViewById(R.id.password_id);
        phoneEt = findViewById(R.id.phone_id);

        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String nameStr = name.getText().toString();
                String emailStr = email.getText().toString();
                String passStr = password.getText().toString();
                       phoneStr = phoneEt.getText().toString();

                if (!TextUtils.isEmpty(nameStr) && ! TextUtils.isEmpty(emailStr) && !TextUtils.isEmpty(passStr)){
                    // all is okay
                    Toast.makeText(SignUpActivity.this, "Name: "+nameStr+" Email: "+emailStr+" pass: "+passStr, Toast.LENGTH_SHORT).show();
                    // firebase sign up
                     mAuth1.createUserWithEmailAndPassword(emailStr, passStr)
                             .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                 @Override
                                 public void onComplete(@NonNull Task<AuthResult> task) {
                                     if (task.isSuccessful()){
                                         // sign up success
                                         FirebaseUser currentUser = mAuth1.getCurrentUser();
                                         // add userdata to Firebase
                                         Map<String, Object> user = new HashMap<>();
                                         user.put("name", nameStr);
                                         user.put("phone",phoneStr);
                                         //
                                         db.collection("users")
                                                 .document(currentUser.getUid())
                                                 .set(user)
                                                 .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                     @Override
                                                     public void onSuccess(Void aVoid) {
                                                         Log.d(TAG, "DocumentSnapshot successfully written!");
                                                     }
                                                 })
                                                 .addOnFailureListener(new OnFailureListener() {
                                                     @Override
                                                     public void onFailure(@NonNull Exception e) {
                                                         Log.w(TAG, "Error writing document", e);
                                                     }
                                                 });


                                         updateUI(currentUser);
                                     }else{
                                         // sign up failed
                                         Log.d(TAG, "onComplete: failed: "+task.getException());
                                         Toast.makeText(SignUpActivity.this, "Failed to signup:", Toast.LENGTH_SHORT).show();
                                         updateUI(null);
                                     }
                                 }
                             });
                }else{
                    // empty string
                    Toast.makeText(SignUpActivity.this, "Invalid information!!", Toast.LENGTH_SHORT).show();
                }


            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();

            }
        });

    }

    private void updateUI(FirebaseUser currentUser) {

               //already Registration
                if(currentUser !=null){
                    Toast.makeText(this,"Thank you for Signup!",Toast.LENGTH_LONG ).show();
                    // go to another activity
                    Intent goHome = new Intent(SignUpActivity.this, HomeActivity.class);
                    startActivity(goHome);
                    finish();

                }
                else {
                    // user null

                }

    }
}
