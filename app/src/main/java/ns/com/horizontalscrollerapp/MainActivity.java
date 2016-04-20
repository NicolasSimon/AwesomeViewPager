package ns.com.horizontalscrollerapp;

import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

import ns.com.horizontalscrollerapp.adapter.MyPagerAdapter;
import ns.com.horizontalscrollerapp.fragment.Fragment0;
import ns.com.horizontalscrollerapp.fragment.Fragment1;
import ns.com.horizontalscrollerapp.fragment.Fragment2;
import ns.com.horizontalscrollerapp.fragment.Fragment3;
import ns.com.horizontalscrollerapp.view.MyPagerTransformer;

public class MainActivity extends AppCompatActivity {
    private Fragment1               mFrag1;
    private Fragment2               mFrag2;
    private Fragment3               mFrag3;

    private ViewPager               mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        mViewPager = (ViewPager) findViewById(R.id.main_view_pager);

        CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.indicator);

        if (mViewPager != null) {
            mViewPager.setPageTransformer(true, new MyPagerTransformer(.2f, 50.0f));

            //vp.addOnPageChangeListener(listener);
            List<Fragment> fragments = new ArrayList<>();

            mFrag1 = new Fragment1();
            mFrag2 = new Fragment2();
            mFrag3 = new Fragment3();
            fragments.add(mFrag1);
            fragments.add(mFrag2);
            fragments.add(mFrag3);
            fragments.add(new Fragment0());

            PagerAdapter realViewPagerAdapter = new MyPagerAdapter(super.getSupportFragmentManager(), fragments);

            mViewPager.setAdapter(realViewPagerAdapter);
            if (indicator != null) {
                indicator.setViewPager(mViewPager);
            }
        }
    }

    public void changeScene(View v) {
        switch (mViewPager.getCurrentItem()) {
            case 0:
                mFrag1.changeScene(v);
                break;
            case 1:
                mFrag2.changeScene(v);
                break;
            case 2:
                mFrag3.changeScene(v);
                break;
            default:
                break;
        }
    }
}
