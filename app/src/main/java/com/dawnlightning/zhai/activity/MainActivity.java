package com.dawnlightning.zhai.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.dawnlightning.zhai.R;

import com.dawnlightning.zhai.base.Actions;
import com.dawnlightning.zhai.base.AppApplication;
import com.dawnlightning.zhai.base.BaseActivity;
import com.dawnlightning.zhai.base.Classify;
import com.dawnlightning.zhai.channel.ChannelItem;
import com.dawnlightning.zhai.channel.ChannelManage;
import com.dawnlightning.zhai.fragment.MainFragment;
import com.dawnlightning.zhai.fragment.imagelist.BeautifyLegImagesFragement;
import com.dawnlightning.zhai.fragment.imagelist.BeiLaQiImagesFragment;
import com.dawnlightning.zhai.fragment.imagelist.TiangouImagesFragment;
import com.dawnlightning.zhai.model.ImageListModel;
import com.dawnlightning.zhai.widget.ColumnHorizontalScrollView;



import android.support.v7.widget.Toolbar;

import android.view.Gravity;

import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import com.dawnlightning.zhai.utils.BaseTools;
import com.dawnlightning.zhai.widget.lvp.LazyFragmentPagerAdapter;
import com.dawnlightning.zhai.widget.lvp.LazyViewPager;

/**
 * @author Leo
 */
public class MainActivity extends BaseActivity {

    private  Toolbar toolbar;

    /** 自定义HorizontalScrollView */
    private ColumnHorizontalScrollView mColumnHorizontalScrollView;
    LinearLayout mRadioGroup_content;
    LinearLayout ll_more_columns;
    RelativeLayout rl_column;
    private ImageView button_more_columns;
    private LazyViewPager mViewPager;
    /** 屏幕宽度 */
    private int mScreenWidth = 0;
    /** Item宽度 */
    private int mItemWidth = 0;
    /** 左阴影部分*/
    public ImageView shade_left;
    /** 右阴影部分 */
    public ImageView shade_right;
    /** 当前选中的栏目*/
    private int columnSelectIndex = 0;
    /** 请求CODE */
    public final static int CHANNELREQUEST = 1;
    /** 调整返回的RESULTCODE */
    public final static int CHANNELRESULT = 10;
    private  android.support.v4.app.FragmentManager fragmentManager;
    private  ArrayList<Fragment> fragmentList=new ArrayList<Fragment>();
    /** 用户选择的分类列表*/
    private ArrayList<ChannelItem> userChannelList=new ArrayList<ChannelItem>();
    private long mExitTime;//点击返回键的时间
   private CustomLazyFragmentPagerAdapter customLazyFragmentPagerAdapter;
    private ImageListModel model=new ImageListModel();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mScreenWidth = BaseTools.getWindowsWidth(this);
        mItemWidth = mScreenWidth / 6;// 一个Item宽度为屏幕的1/6
        initView();
        initData();
    }


    public void initView() {
        toolbar=(Toolbar)findViewById(R.id.toolbar);

        mViewPager=(LazyViewPager)findViewById(R.id.lvp_main_viewpager);
        mColumnHorizontalScrollView =  (ColumnHorizontalScrollView)findViewById(R.id.mColumnHorizontalScrollView);
        mRadioGroup_content = (LinearLayout) findViewById(R.id.mRadioGroup_content);
        ll_more_columns = (LinearLayout) findViewById(R.id.ll_more_columns);
        rl_column = (RelativeLayout) findViewById(R.id.rl_column);
        button_more_columns = (ImageView) findViewById(R.id.button_more_columns);
        shade_left = (ImageView) findViewById(R.id.shade_left);
        shade_right = (ImageView) findViewById(R.id.shade_right);


        toolbar.setTitleTextColor(getResources().getColor(R.color.jianshured));
        toolbar.setTitle("");
        ((TextView)toolbar.findViewById(R.id.toolbar_title)).setText("首页");

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
        button_more_columns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_channel = new  Intent(getApplicationContext(), ChannelActivity.class);
                startActivityForResult(intent_channel, CHANNELREQUEST);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });


    }

    private void initData(){
        initColumnData();
        initTabColumn();
        initFragment();
    }
    /** 获取Column栏目 数据*/
    private void initColumnData() {
        userChannelList = ((ArrayList<ChannelItem>) ChannelManage.getManage(AppApplication.getApp().getSQLHelper()).getUserChannel());

    }

    /**
     *  初始化Column栏目项
     * */
    private void initTabColumn() {
        mRadioGroup_content.removeAllViews();
        int count =  userChannelList.size();
        mColumnHorizontalScrollView.setParam(this, mScreenWidth, mRadioGroup_content, shade_left, shade_right, ll_more_columns, rl_column);
        for(int i = 0; i< count; i++){
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mItemWidth , ViewGroup.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 5;
            params.rightMargin = 5;
//			TextView localTextView = (TextView) mInflater.inflate(R.layout.column_radio_item, null);
            TextView columnTextView = new TextView(this);
            columnTextView.setTextAppearance(this, R.style.top_category_scroll_view_item_text);
//			localTextView.setBackground(getResources().getDrawable(R.drawable.top_category_scroll_text_view_bg));
//            columnTextView.setBackgroundResource(R.drawable.radio_buttong_bg);
            columnTextView.setGravity(Gravity.CENTER);
            columnTextView.setPadding(5, 5, 5, 5);
            columnTextView.setId(i);
            columnTextView.setText(userChannelList.get(i).getName());
            columnTextView.setTextColor(getResources().getColorStateList(R.color.grey));

            if(columnSelectIndex == i){
                columnTextView.setTextColor(getResources().getColor(R.color.jianshured));
                columnTextView.setSelected(true);
            }
            columnTextView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    for(int i = 0;i < mRadioGroup_content.getChildCount();i++){
                        View localView = mRadioGroup_content.getChildAt(i);
                        if (localView != v)
                            localView.setSelected(false);
                        else{
                            localView.setSelected(true);
                            mViewPager.setCurrentItem(i);
                        }
                    }
                    Toast.makeText(getApplicationContext(), userChannelList.get(v.getId()).getName(), Toast.LENGTH_SHORT).show();
                }
            });
            mRadioGroup_content.addView(columnTextView, i ,params);
        }
    }

    /**
     *  选择的Column里面的Tab
     * */
    private void selectTab(int tab_postion) {
        columnSelectIndex = tab_postion;
        for (int i = 0; i < mRadioGroup_content.getChildCount(); i++) {
            View checkView = mRadioGroup_content.getChildAt(tab_postion);
            int k = checkView.getMeasuredWidth();
            int l = checkView.getLeft();
            int i2 = l + k / 2 - mScreenWidth / 2;
            // rg_nav_content.getParent()).smoothScrollTo(i2, 0);
            mColumnHorizontalScrollView.smoothScrollTo(i2, 0);
            // mColumnHorizontalScrollView.smoothScrollTo((position - 2) *
            // mItemWidth , 0);
        }
        //判断是否选中
        for (int j = 0; j <  mRadioGroup_content.getChildCount(); j++) {
            View checkView = mRadioGroup_content.getChildAt(j);
            boolean ischeck;
            if (j == tab_postion) {
                TextView textView=(TextView)mRadioGroup_content.getChildAt(j);
                textView.setTextColor(getResources().getColorStateList(R.color.jianshured));
                ischeck = true;
            } else {
                TextView textView=(TextView)mRadioGroup_content.getChildAt(j);
                textView.setTextColor(getResources().getColorStateList(R.color.grey));
                ischeck = false;
            }
            checkView.setSelected(ischeck);
        }
    }

    /**
     *  初始化Fragment
     * */
    private void initFragment() {
        fragmentList.clear();//清空
        fragmentManager =getSupportFragmentManager();

      customLazyFragmentPagerAdapter=new CustomLazyFragmentPagerAdapter(fragmentManager);
		mViewPager.setOffscreenPageLimit(0);
        mViewPager.setAdapter(customLazyFragmentPagerAdapter);
        mViewPager.setOnPageChangeListener(pageListener);

    }
    /**
     *  ViewPager切换监听方法
     * */
    public ViewPager.OnPageChangeListener pageListener= new ViewPager.OnPageChangeListener(){

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int position) {

            mViewPager.setCurrentItem(position);
            selectTab(position);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        switch (requestCode) {
            case CHANNELREQUEST:
                if(resultCode == CHANNELRESULT){
                   initData();
                }
                break;

            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {

                if ((System.currentTimeMillis() - mExitTime) > 2000) {
                    Toast.makeText(this, "在按一次退出",
                            Toast.LENGTH_SHORT).show();
                    mExitTime = System.currentTimeMillis();
                } else {
                    finish();
                }

            return true;
        }
        //拦截MENU按钮点击事件，让他无任何操作
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    private  class CustomLazyFragmentPagerAdapter extends LazyFragmentPagerAdapter {

        private CustomLazyFragmentPagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(ViewGroup container, int position) {
            ChannelItem item=userChannelList.get(position);

            if (item.getClassify().equals(Classify.ApiGrils)){
                Bundle data = new Bundle();
                data.putString("type","ApiGrils");
                data.putString("text", userChannelList.get(position).getName());
                data.putInt("id", userChannelList.get(position).getId());
                return TiangouImagesFragment.newInstance(data);
            }else if (item.getClassify().equals(Classify.BeautyLeg)){
                Bundle data = new Bundle();
                data.putString("type","BeautyLeg");
                data.putString("text", userChannelList.get(position).getName());
                data.putInt("id", userChannelList.get(position).getId());
                return BeautifyLegImagesFragement.newInstance(data);
            }else if(item.getClassify().equals(Classify.NewApiGrils)){
                Bundle data = new Bundle();
                data.putString("type","BeautyLeg");
                data.putString("text", userChannelList.get(position).getName());
                data.putInt("id", userChannelList.get(position).getId());
                return BeiLaQiImagesFragment.newInstance(data);
            }else if (item.getClassify().equals(Classify.Home)){
                return MainFragment.newInstance();
            }

          return  null;

        }

        @Override
        public int getCount() {
            return userChannelList.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }
    }
    public void backhomepage(){
        mViewPager.setCurrentItem(0);
    }
}



