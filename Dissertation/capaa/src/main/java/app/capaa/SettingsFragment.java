package app.capaa;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.SensorManager;
import android.media.Image;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class SettingsFragment extends Fragment {

    public Button button;
    public ImageView Head;
    public ImageView Torso;
    public ImageView ShopGreenTorso;
    private Communicator interfaceImplementor;
    public Context context;


//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//    }
//
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//
//        button = (Button) getView().findViewById(R.id.button1);
//        Torso = (ImageView) getView().findViewById(R.id.Torso);
//        ShopGreenTorso= (ImageView) getView().findViewById(R.id.ShopGreenTorso);
//
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//               // Toast.makeText(getActivity(), "Purchased", Toast.LENGTH_SHORT).show();
//
//            }
//        });
//
//    }

    public interface Communicator
    {
        public void communicateWith();

    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        this.interfaceImplementor = (Communicator)context;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ImageView head = (ImageView) getActivity().findViewById(R.id.item2);


        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Button button = (Button) getActivity().findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // ImageView torso = (ImageView) getActivity().findViewById(R.id.Torso);
                interfaceImplementor.communicateWith();

            }
        });
    }
}
