package org.sixpetals.ibeacon_app.activities;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;

import org.sixpetals.ibeacon_app.R;

public class MainActivityFragment extends Fragment{

    private MainActivity parent;

    @Override
    public void onAttach(Activity activity) {
        parent = (MainActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        Button btnMove = (Button) rootView.findViewById(R.id.viewInfomaitonButton);
        btnMove.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                parent.showInfomation();
            }
        });

        return rootView;
    }
}
