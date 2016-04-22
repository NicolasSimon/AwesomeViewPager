package ns.com.horizontalscrollerapp.fragment;

import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.InputDevice;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import ns.com.horizontalscrollerapp.R;
import ns.com.horizontalscrollerapp.view.MyBouncingView;

/**
 * Created by Nicolas on 20/04/2016.
 * (c) Touchnote Ltd., 2015
 */
public class Fragment0 extends Fragment {
    private int[]                   mColors = new int[]{Color.BLACK, Color.BLUE, Color.RED, Color.GREEN };
    private int                     mCurrentColor = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_0_layout, container, false);

        final MyBouncingView bouncingView = (MyBouncingView) layout.findViewById(R.id.funView);
        if (bouncingView != null) {
            bouncingView.getPaint().setColor(mColors[mCurrentColor]);
            bouncingView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            mCurrentColor += 1;
                            if (mCurrentColor >= mColors.length) {
                                mCurrentColor = 0;
                            }
                            bouncingView.setPaintColor(mColors[mCurrentColor], 600, new Point((int)event.getX(), (int)event.getY()), 50);
                            bouncingView.startAnim();
                            return (true);
                    }
                    return false;
                }
            });
        }

        return (layout);
    }
}
