package ns.com.horizontalscrollerapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ns.com.horizontalscrollerapp.R;

/**
 * Created by Nicolas on 21/04/2016.
 * (c) Touchnote Ltd., 2015
 */
public class FragmentFocus extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return (inflater.inflate(R.layout.fragment_focusview, container, false));
    }
}
