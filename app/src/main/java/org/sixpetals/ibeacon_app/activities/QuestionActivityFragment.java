package org.sixpetals.ibeacon_app.activities;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.sixpetals.ibeacon_app.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class QuestionActivityFragment extends Fragment {

    public QuestionActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_question, container, false);
    }
}
