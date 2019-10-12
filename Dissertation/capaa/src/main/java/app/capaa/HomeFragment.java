package app.capaa;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.Activity;
import android.view.View;
import android.widget.Toast;
import android.app.Activity;
import android.content.Context;
import android.hardware.*;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment implements SensorEventListener {

    private SensorManager sensorManager;
    private TextView count;
    boolean activityRunning;
    private Context ctx;

    private Button ClearSteps;

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        count = (TextView) getView().findViewById(R.id.StepCounter);
//    }
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        count = (TextView) getView().findViewById(R.id.StepCounter);
        sensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);

//        ClearSteps = (Button) getView().findViewById(R.id.ClearSteps);
//        ClearSteps.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                count.setText("");
//
//            }
//        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);

    }

    @Override
    public void onResume(){
        super.onResume();
        activityRunning = true;
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if(countSensor != null){
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
        }else{
             // Toast.makeText(ctx, "Count sensor not available", Toast.LENGTH_LONG).show();
                Toast.makeText(getActivity(), "Count sensor not available",
                    Toast.LENGTH_LONG).show();
//            Toast.makeText(getApplicationContext(),"Incorrect email or password",Toast.LENGTH_SHORT).show();


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
            count.setText(String.valueOf(sensorEvent.values[0]));
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
