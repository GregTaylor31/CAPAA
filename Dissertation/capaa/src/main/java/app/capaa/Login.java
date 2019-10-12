package app.capaa;

import app.capaa.R;

import android.provider.ContactsContract;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity {

     EditText Email;
     EditText Password;
     Button LoginButton;
     DatabaseHelper db;
     TextView NoAccount;
     UserSessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        getWindow().setBackgroundDrawableResource(R.drawable.background2); // sets background to background2.png
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); // hides keyboard on boot


        // User Session Manager
        sessionManager = new UserSessionManager(getApplicationContext());


        db = new DatabaseHelper(this);
        Email=(EditText) findViewById(R.id.userEmail);
        Password=(EditText)findViewById(R.id.userPassword);
        LoginButton=(Button)findViewById(R.id.LoginBtn);

        NoAccount=(TextView)findViewById(R.id.NoAccount);
        NoAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent accountsIntent = new Intent(Login.this, Register.class);
                startActivity(accountsIntent);
            }
        });

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = Email.getText().toString();
                String password = Password.getText().toString();
                Boolean CheckEmailAndPassword = db.emailAndPasswordCheckInDB(email,password);
                if(CheckEmailAndPassword==true) {
                    Toast.makeText(getApplicationContext(), "Successfully logged in", Toast.LENGTH_SHORT).show();
                    sessionManager.createUserLoginSession(email, email);
                    Intent accountsIntent = new Intent(Login.this, Home.class);
                    startActivity(accountsIntent);
                }
                else
                    Toast.makeText(getApplicationContext(),"Incorrect email or password",Toast.LENGTH_SHORT).show();

            }
        });
    }
}



        // initViews();


//    private void Validate(String userEmail, String userPassword) {
//        if ((userEmail == "admin") && (userPassword == "1234")) {
//
//            Email = findViewById(R.id.userEmail);
//            Password = findViewById(R.id.userPassword);
//            //LoginButton=findViewById(R.id.LoginBtn);
//        }
//    }
//            private void initViews() {
//                Button LoginButton = findViewById(R.id.LoginBtn);
//                LoginButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        //Starts an intent of the sign up  activity
//                        Intent accountsIntent = new Intent(Login.this, Home.class);
//                        startActivity(accountsIntent);
//                    }
//                });
//            }
//        }
//
//
//
//
//
////    private initObjects()
////    {
////        inputValidation = new InputValidation(activity);
////        databaseHelper = new DatabaseHelper(activity);
////        user = new User();
////    }
////
////    @Override
////    public void onClick(View v)
////    {
////        switch (v.getId())
////        {
////            case R.id.RegisterBtn:
////                postDataToSQL();
////                break;
////        }
////    }
//}