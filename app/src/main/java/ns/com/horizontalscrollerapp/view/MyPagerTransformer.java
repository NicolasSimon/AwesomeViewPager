package ns.com.horizontalscrollerapp.view;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Nicolas on 18/04/2016.
 * (c) Touchnote Ltd., 2015
 */
public class MyPagerTransformer implements ViewPager.PageTransformer {
    private float                           mParallaxCoeff;
    private float                           mDistanceCoeff;

    public MyPagerTransformer(float parallax, float distance) {
        mParallaxCoeff = parallax;
        mDistanceCoeff = distance;
    }

    @Override
    public void transformPage(View page, float position) {
        float coefficient = page.getWidth() * mParallaxCoeff;
        ViewGroup vG = (ViewGroup) page;
        if (vG.getChildAt(0) instanceof ViewGroup) {
            vG = (ViewGroup) vG.getChildAt(0);
            for (int i = vG.getChildCount() - 1; i >= 0; --i) {
                View v = vG.getChildAt(i);
                if (v != null) {
                    v.setTranslationX(coefficient * (position * position * position));
                }
                coefficient *= mDistanceCoeff;
            }
        }
    }
}
