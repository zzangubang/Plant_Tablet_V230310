package com.example.plant_iot_tablet2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignActivity_Fin#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignActivity_Fin extends Fragment {
    // 애니메이션.
    Animation fadeIn_anim;

    ImageView image_fin;
    TextView titleB, titleS;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SignActivity_Fin() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignActivity_Fin.
     */
    // TODO: Rename and change types and number of parameters
    public static SignActivity_Fin newInstance(String param1, String param2) {
        SignActivity_Fin fragment = new SignActivity_Fin();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sign_fin, container, false);

        // 애니메이션.
        fadeIn_anim = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);

        image_fin = (ImageView) v.findViewById(R.id.image_fin);
        titleB = (TextView) v.findViewById(R.id.titleB);
        titleS = (TextView) v.findViewById(R.id.titleS);
        image_fin.startAnimation(fadeIn_anim);
        titleB.startAnimation(fadeIn_anim);
        titleS.startAnimation(fadeIn_anim);

        return v;
    }
}