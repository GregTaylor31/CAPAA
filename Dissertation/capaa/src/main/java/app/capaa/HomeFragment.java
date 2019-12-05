package app.capaa;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.Activity;
import android.view.View;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment  {

    private SensorManager sensorManager;
    private TextView count;
    boolean activityRunning;
    private Context ctx;
    private TextView totalSteps;
    private int steps =1;
    private int Initials;

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        count = (TextView) getView().findViewById(R.id.StepCounter);
        sensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);

        totalSteps = (TextView) getView().findViewById(R.id.totalSteps);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
       // totalSteps = (TextView) getView().findViewById(R.id.totalSteps);
        //totalSteps.setText(String.valueOf(sensorEvent.values[0]));
       //totalSteps.setText(steps);
        totalSteps = (TextView) rootView.findViewById(R.id.totalSteps);
        //totalSteps.setText(getSteps());
        //totalSteps.setText(steps);
        //IV = (ImageView) rootView.findViewById(R.id.Torso);
        return rootView;
    }
}