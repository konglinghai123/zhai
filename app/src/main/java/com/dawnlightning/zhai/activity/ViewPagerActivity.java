package com.dawnlightning.zhai.activity;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dawnlightning.zhai.R;
import com.dawnlightning.zhai.base.BaseActivity;
import com.dawnlightning.zhai.bean.ImageDetailedBean;
import com.dawnlightning.zhai.utils.Options;
import com.dawnlightning.zhai.widget.HackyViewPager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by Administrator on 2016/5/6.
 */
public class ViewPagerActivity extends BaseActivity {
    private HackyViewPager mViewPager;
    private List<ImageDetailedBean> list;
    private int currentposition=0;
    private Toolbar toolbar;
    private  TextView tv_currentposition;
    private ImageView iv_nextpage;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager);
        list=(List<ImageDetailedBean>)getIntent().getSerializableExtra("list");
        currentposition=getIntent().getIntExtra("position",0);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        tv_currentposition=(TextView)toolbar.findViewById(R.id.toolbar_title);
        iv_nextpage=(ImageView)toolbar.findViewById(R.id.iv_nextpage);
        iv_nextpage.setVisibility(View.VISIBLE);
        toolbar.setNavigationIcon(R.mipmap.ic_back);
        tv_currentposition.setText(String.format("%s/%s", String.valueOf(currentposition + 1), list.size()));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentposition > 0) {
                    currentposition = currentposition - 1;

                    mViewPager.setCurrentItem(currentposition);
                    tv_currentposition.setText(String.format("%s/%s", String.valueOf(currentposition + 1), list.size()));
                }

            }
        });
        iv_nextpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentposition + 1 < list.size()) {
                    currentposition = currentposition + 1;
                    mViewPager.setCurrentItem(currentposition);
                    tv_currentposition.setText(String.format("%s/%s", String.valueOf(currentposition + 1), list.size()));
                }
            }
        });
        mViewPager = (HackyViewPager) findViewById(R.id.vp_viewpicture);
        mViewPager.setAdapter(new SamplePagerAdapter());
        mViewPager.setCurrentItem(currentposition);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentposition=position;
                tv_currentposition.setText(String.format("%s/%s", String.valueOf(currentposition + 1), list.size()));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
     class SamplePagerAdapter extends PagerAdapter {
         private ImageLoader imageLoader = ImageLoader.getInstance();
         private DisplayImageOptions options;

        public SamplePagerAdapter(){
            options = Options.getHdListOptions();
        }
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(container.getContext());
            imageLoader.displayImage(list.get(position).getSrc(),photoView,options);

            // Now just add PhotoView to ViewPager and return it
            container.addView(photoView, ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT);

            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }

}
