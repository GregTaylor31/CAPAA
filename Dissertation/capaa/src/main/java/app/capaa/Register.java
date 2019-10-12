package app.capaa;

import app.capaa.R;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.ObjectInputValidation;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Register
public class Register extends AppCompatActivity
{
    DatabaseHelper db;
    EditText Email,Password,ConfirmPassword;
    Button RegisterButton;
    TextView AlreadyHaveAccount;
    RadioButton TCbutton;
    boolean TCbuttonClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setBackgroundDrawableResource(R.drawable.background2);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        db = new DatabaseHelper(this);
        Email =(EditText)findViewById(R.id.userEmail);
        Password=(EditText)findViewById(R.id.userPassword);
        ConfirmPassword =(EditText)findViewById(R.id.userConfirmPassword);
        TCbutton =(RadioButton)findViewById(R.id.TCradio);
        TCbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                TCbuttonClicked  = true;
            }
        });

        AlreadyHaveAccount=(TextView)findViewById(R.id.AlreadyHaveAccount);
        AlreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent accountsIntent = new Intent(Register.this, Login.class);
                startActivity(accountsIntent);

            }
        });

        RegisterButton=(Button)findViewById(R.id.RegisterBtn);
        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1 = Email.getText().toString();
                String s2 = Password.getText().toString();
                String s3 = ConfirmPassword.getText().toString();

                if(s1.equals("")||s2.equals("")||s3.equals("")){
                    Toast.makeText(getApplicationContext(),"Fields are empty!", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (isEmailValid(s1)) {

                        if (s2.equals(s3)) {  //passwords match
                            Boolean checkEmail = db.checkEmail(s1);

                            if (TCbuttonClicked) {

                                if (checkEmail) {
                                    Boolean insert = db.insert(s1, s2);
                                    Toast.makeText(getApplicationContext(), "Registration Successful!", Toast.LENGTH_SHORT).show();
                                    Intent accountsIntent = new Intent(Register.this, Home.class);
                                    startActivity(accountsIntent);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Email Already exists", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(getApplicationContext(), "Please agree to the Terms and Conditions", Toast.LENGTH_SHORT).show();
                            }

                            } else {
                                Toast.makeText(getApplicationContext(), "Passwords do not match!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Not a valid Email Address!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }




    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}