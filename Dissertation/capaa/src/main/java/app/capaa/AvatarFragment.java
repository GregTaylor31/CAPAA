package app.capaa;

import android.app.Activity;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.text.style.BackgroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AvatarFragment extends Fragment {
    public ImageView IV;
    public Boolean GreenTorso = false;
    public Boolean loaded = false;

    public int TorsoColour;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_avatar, container, false);




       if (GreenTorso == true) {
           IV = (ImageView) rootView.findViewById(R.id.Torso);
           IV.setImageResource(R.drawable.torso2green);

       }





       return rootView;
    }

    public void greenTorso()
    {
        GreenTorso = true;
    }
}
