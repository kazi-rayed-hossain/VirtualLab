package com.example.virtuallab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";

    FirebaseAuth amuth ;
    DocumentReference docref;
    Button button;

    //
    TextView showText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        final Button startButton=(Button) findViewById(R.id.chemistry_button_id);
        final Button startButton1=(Button) findViewById(R.id.physic_button_id);
        final Button startButton2=(Button) findViewById(R.id.biology_button_id);

        startButton.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startButton.getBackground().setAlpha(100);

                        break;
                    case MotionEvent.ACTION_UP:
                        Intent intent=new Intent(HomeActivity.this, ChemistryActivity.class);
                        startActivity(intent);
                        startButton.getBackground().setAlpha(255);


                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;

                    default:
                        break;
                }
                return false;
            }
        });

        startButton1.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startButton1.getBackground().setAlpha(100);

                        break;
                    case MotionEvent.ACTION_UP:
                        Intent intent=new Intent(HomeActivity.this, PhysicsActivity.class);
                        startActivity(intent);
                        startButton1.getBackground().setAlpha(255);


                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;

                    default:
                        break;
                }
                return false;
            }
        });




        startButton2.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        startButton2.getBackground().setAlpha(100);

                        break;
                    case MotionEvent.ACTION_UP:
                        Intent intent=new Intent(HomeActivity.this, BiologyActivity.class);
                        startActivity(intent);
                        startButton2.getBackground().setAlpha(255);


                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;

                    default:
                        break;
                }
                return false;
            }
        });


        // firbase initalize
        amuth = FirebaseAuth.getInstance();



        button = findViewById(R.id.signout_id);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                amuth.getInstance().signOut();

                Intent intent = new Intent(HomeActivity.this,LoginActivity.class);

                startActivity(intent);
                finish();


            }
        });



        showText = findViewById(R.id.textView);





        FirebaseFirestore db = FirebaseFirestore.getInstance();


        // read from firestore and get the string and set it to showText

        docref = db.collection("users").document(amuth.getCurrentUser().getUid());

        docref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                   // System.out.println("gese");
                    if (documentSnapshot.exists()){
                        //found
                        Map<String, Object> userInfo = documentSnapshot.getData();
                        Toast.makeText(HomeActivity.this, "Name: "+userInfo.get("name"), Toast.LENGTH_SHORT).show();
                        String userName = (String) userInfo.get("name");
                        String phone = (String) userInfo.get("phone");
                        String showMessage = "WELCOME, \n"+userName+"\nto Virtual Laboratory !";
                        showText.setText(showMessage);
                    }
                }else{

                    //Toast.makeText(HomeActivity.this, "Failed to read!", Toast.LENGTH_SHORT).show();
                    Toast.makeText(HomeActivity.this, "WELCOME to Virtual Laboratory !", Toast.LENGTH_SHORT).show();


                }

            }
        });
        // Thats it






    }
}
