package ns.com.horizontalscrollerapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.transition.ChangeBounds;
import android.transition.Scene;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import ns.com.horizontalscrollerapp.R;
import ns.com.horizontalscrollerapp.view.MyBounceInterpolator;

/**
 * Created by Nicolas on 18/04/2016.
 * (c) Touchnote Ltd., 2015
 */
public class Fragment1 extends Fragment {
    private Scene                       mStartScene;
    private Scene                       mInfoScene;
    private Transition                  mTransition;

    private boolean                     mIsShowingDetails = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.view_pager_root, container, false);

        FrameLayout rootLayout = (FrameLayout) v.findViewById(R.id.root);

        mTransition = new ChangeBounds();
        mTransition.setInterpolator(new MyBounceInterpolator());
        mTransition.setDuration(400);

        mStartScene = Scene.getSceneForLayout(rootLayout, R.layout.view_pager_item_default_pc, getContext());
        mInfoScene = Scene.getSceneForLayout(rootLayout, R.layout.view_pager_item_details_pc, getContext());

        mStartScene.enter();

        return (v);
    }

    public void changeScene(View v) {
        Scene tmp = mInfoScene;
        mInfoScene = mStartScene;
        mStartScene = tmp;
        mIsShowingDetails = !mIsShowingDetails;
        TransitionManager.go(mStartScene, mTransition);
    }
}