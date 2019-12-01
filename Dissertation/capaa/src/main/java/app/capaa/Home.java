package app.capaa;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class Home extends AppCompatActivity implements SettingsFragment.Communicator, SensorEventListener {

    private TextView name, email;
    Button LogoutButton;
    UserSessionManager sessionManager;
    Button button;
    ImageView Torso;
    AvatarFragment avatarFragment = new AvatarFragment();
    SettingsFragment settingsFragment = new SettingsFragment();
    HomeFragment homeFragment = new HomeFragment();

    private SensorManager sensorManager;
    //private TextView count;
    boolean activityRunning;
    private Context ctx;
    private TextView stepsView;
    private int steps =0;
    private int Initials;
    private Button ClearSteps;
    DatabaseHelper db;

    public void communicateWith() {
        if (getStepsFromDB()>= 20){
            avatarFragment.greenTorso();
            updateDatabaseSteps(20,0);
            setStepstoTextView(getStepsFromDB());
            Toast.makeText(getApplicationContext(), "Purchase successful!", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getApplicationContext(), "Not enough steps to purchase!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        getWindow().setBackgroundDrawableResource(R.drawable.background2); // sets background to background2.png
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); // hides keyboard on boot

        sessionManager = new UserSessionManager(getApplicationContext());

        TextView lblName = (TextView) findViewById(R.id.lblName);
        TextView lblEmail = (TextView) findViewById(R.id.lblEmail);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new HomeFragment()).commit();


        if (sessionManager.checkLogin())
            finish();

        // get user data from session
        HashMap<String, String> user = sessionManager.getUserDetails();

        // get name
        String name = user.get(UserSessionManager.KEY_NAME);

        // get email
        String email = user.get(UserSessionManager.KEY_EMAIL);


        //lblName.setText(Html.fromHtml("Name: <b>" + name + "</b>"));
        //lblEmail.setText(Html.fromHtml("Welcome: <b>" + email + "</b>"));

       // email;
       // int currentSteps = getSteps();
        //db = new DatabaseHelper(this);
        //Boolean insert = db.updateSteps(email, currentSteps);
        //Toast.makeText(getApplicationContext(), "Steps updated", Toast.LENGTH_SHORT).show();
        //stepsView.setText(String.valueOf(sensorEvent.values[0]));


        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepsView = (TextView) findViewById(R.id.steps);

        //totalStepsText = (TextView) findViewById(R.id.totalStepsText);
       // db = new DatabaseHelper(this);
      //  db.getTorsoFromDb(getEmail());


       // Picasso.get().load("http://i.imgur.com/DvpvklR.png").into(imageView);


//        sessionManager.checkLogin();
//        HashMap<String, String> user = sessionManager.getUserDetail();
//        String mName = user.get(sessionManager.NAME);
//        String mEmail = user.get(sessionManager.EMAIL);

//        name.setText(mName);
//        email.setText(mEmail);

        LogoutButton = (Button) findViewById(R.id.LogoutButton);
        Toast.makeText(getApplicationContext(),
                "User Login Status: " + sessionManager.isUserLoggedIn(),
                Toast.LENGTH_LONG).show();

        LogoutButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                // Clear the User session data
                // and redirect user to LoginActivity
                sessionManager.logoutUser();
            }
        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;
                    switch (menuItem.getItemId()) {
                        case R.id.nav_steps:
                            //HomeFragment homeFragment = new HomeFragment();
                            selectedFragment = homeFragment;
                            break;
                        case R.id.nav_avatar:
                            //HomeFragment avatarFragment = new HomeFragment();
                            selectedFragment = avatarFragment;
                            break;
                        case R.id.nav_settings:
                            //HomeFragment settingsFragment = new HomeFragment();
                            selectedFragment = settingsFragment;
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();
                    return true;
                }


            };


    @Override
    public void onResume(){
        super.onResume();
        activityRunning = true;
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if(countSensor != null){
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
        }else{
            // Toast.makeText(ctx, "Count sensor not available", Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(), "Count sensor not available",
                    Toast.LENGTH_LONG).show();
            // Toast.makeText(getApplicationContext(),"Incorrect email or password",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        activityRunning = false;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(activityRunning){
           // getStepsFromDB();
            //setStepstoTextView(getStepsFromDB());
            //stepsView.setText(String.valueOf(sensorEvent.values[0]));
            //steps = steps + 1; //Integer.valueOf((int)sensorEvent.values[0]);
           // setSteps(steps);
            updateDatabaseSteps(0,1);
            //getStepsFromDB();
            setStepstoTextView(getStepsFromDB());
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }


    public int getSteps(){
        return steps;
    }

    public void setSteps(int step){
        steps = steps;
    }

    public String getEmail(){
        sessionManager = new UserSessionManager(getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        String email = user.get(UserSessionManager.KEY_EMAIL);
        return email;
    }

    public void updateDatabaseSteps(int Itemcost, int steps) {
        String currentEmail = getEmail();
        int currentSteps = getStepsFromDB();
        currentSteps = currentSteps - Itemcost;
        currentSteps = currentSteps + steps;
        db = new DatabaseHelper(this);
        Boolean insert = db.updateSteps(currentEmail, currentSteps);
        Toast.makeText(getApplicationContext(), "Steps updated", Toast.LENGTH_SHORT).show();
        //getStepsFromDB();
    }

    public int getStepsFromDB(){
        //stepsView = (TextView) findViewById(R.id.steps);
        db = new DatabaseHelper(this);
        int newSteps = db.getStepsFromDataBase(getEmail());
        //stepsView.setText(String.valueOf(newSteps));
        //stepsView.setText(newSteps);
        return newSteps;
    }

    public void setStepstoTextView(int newSteps){
        stepsView.setText(String.valueOf(newSteps));
    }
}
