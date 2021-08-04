package com.example.virtuallab;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    Button signupbtn, signinBtn,aboutUsBTN,googlebtn;
    GoogleSignInClient mGoogleSigninClient;
    TextInputEditText usernameEt, passwordEt;
    TextView text;
    static final int GOOGLE_SIGN=123;
    // Firebase Authentication
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        googlebtn =findViewById(R.id.gsignin);
        // initialize
        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
                .Builder()
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSigninClient = GoogleSignIn.getClient(this, googleSignInOptions);
        googlebtn.setOnClickListener(V->SignInGoogle());

        if(mAuth.getCurrentUser()!=null){
            FirebaseUser user = mAuth.getCurrentUser();
            updateUI(user);
        }
        signupbtn = findViewById(R.id.signup);
        signinBtn = findViewById(R.id.signin_id);
        aboutUsBTN = findViewById(R.id.aboutUsBTN);
       // google auth


        //
        // edit text
        usernameEt = findViewById(R.id.username_id); //email
        passwordEt = findViewById(R.id.password_id);


        // sign in button click
        signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // collected email and password
                String emailStr = usernameEt.getText().toString();
                String passStr = passwordEt.getText().toString();

                // check is empty or not
                if (!TextUtils.isEmpty(emailStr) && !TextUtils.isEmpty(passStr)){
                    // all is okay
                    // firebase login
                    mAuth.signInWithEmailAndPassword(emailStr, passStr).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                // sign in successfull
                                Log.d(TAG, "onComplete: sign in  success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                            }else{
                                Log.d(TAG, "onComplete: sign in failed");
                                Toast.makeText(LoginActivity.this, "Sign in Failed!Please try again!", Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }
                            
                        }
                    });

                }else{
                    Toast.makeText(LoginActivity.this, "Empty string not allowed!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(intent);

            }
        });


       //about us button er kaj
        aboutUsBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,AboutUs.class);
                startActivity(intent);
            }
        });

    }

    //function for google signin
    void SignInGoogle(){
        Intent intent = mGoogleSigninClient.getSignInIntent();
        startActivityForResult(intent, GOOGLE_SIGN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GOOGLE_SIGN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try{
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if(account!=null) {
                    firebaseAuthWithGoogle(account);
                }
            }catch (ApiException e){
                e.getStackTrace();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d("TAG","firebaseAuthWithGoogle:"+account.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this,task->{
                    if(task.isSuccessful()){
                        Log.d("TAG", "signin sucessful");
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    }else
                    {
                        Log.w("TAG", "sign failure",task.getException());

                        Toast.makeText(this,"signIn failed", Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                });
    }
    //

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null){

          String name= currentUser.getDisplayName();
          System.out.println(name);
          String email= currentUser.getEmail();
          System.out.println(email);
//            text.append("Info:\n");
//            text.append(name+"\n");
//            text.append(email);

            //already login ...
            Toast.makeText(this, "Thank you!", Toast.LENGTH_SHORT).show();
            Intent goHome = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(goHome);
            finish();

            
        }else{
            // no login found ..

        }
    }
}
