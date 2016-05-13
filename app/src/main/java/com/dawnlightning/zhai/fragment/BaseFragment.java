package com.dawnlightning.zhai.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.dawnlightning.zhai.R;
import com.dawnlightning.zhai.adapter.imagelistadapter.BaseImageListAdapter;
import com.dawnlightning.zhai.base.Actions;
import com.dawnlightning.zhai.base.Classify;
import com.dawnlightning.zhai.base.IRefreshAndLoadmore;
import com.dawnlightning.zhai.bean.ImageListBean;
import com.dawnlightning.zhai.base.ErrorCode;
import com.dawnlightning.zhai.presenter.ImageListPresenter;
import com.dawnlightning.zhai.view.IBaseFragmentView;
import com.dawnlightning.zhai.widget.lvp.LazyFragmentPagerAdapter;

import java.util.List;

/**
 * Created by Administrator on 2016/5/9.
 */
public abstract class BaseFragment extends Fragment implements IBaseFragmentView,IRefreshAndLoadmore,LazyFragmentPagerAdapter.Laziable{

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    boolean isLoading=false;
    public BaseImageListAdapter adapter;
    private Handler handler = new Handler();
    public int channel_id;
    public  String text;
    public  String type;
    public static int Page=1;
    public ImageListPresenter imageListPresenter;
    private TextView tv_totalpage;
    private EditText ed_currentpage;
    private ImageView iv_goto;
    public Classify classify;
    public abstract void initAdapter();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view =inflater.inflate(R.layout.fragment_main, container,false);
        swipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh_widget);
        recyclerView= (RecyclerView)view.findViewById(R.id.recyclerView);
        tv_totalpage=(TextView)view.findViewById(R.id.tv_page_totle);
        ed_currentpage=(EditText)view.findViewById(R.id.et_page_current);
        ed_currentpage.setText("1");
        iv_goto=(ImageView)view.findViewById(R.id.iv_page_goto);
        iv_goto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(ed_currentpage.getText().toString()) > Integer.parseInt(tv_totalpage.getText().toString().substring(1))) {
                    BaseFragment.Page = Integer.parseInt(tv_totalpage.getText().toString().substring(1));
                } else {
                    BaseFragment.Page = Integer.parseInt(ed_currentpage.getText().toString());
                }
                ed_currentpage.setText(String.valueOf(BaseFragment.Page));
                hidekeyboard();
                swipeRefreshLayout.setRefreshing(true);
                GoTo(BaseFragment.Page, Actions.GoTo);
            }
        });
        initview();
        return view;
    }

    public void initData() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                BaseFragment.Page = 1;
                Refresh(BaseFragment.Page, Actions.Refresh);
            }
        }, 1500);

    }
    private void initview(){
        swipeRefreshLayout.setColorSchemeResources(R.color.jianshured);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        BaseFragment.Page = 1;
                        Refresh(BaseFragment.Page, Actions.Refresh);
                    }
                }, 2000);
            }
        });
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                if (lastVisibleItemPosition + 1 == adapter.getItemCount()) {

                    boolean isRefreshing = swipeRefreshLayout.isRefreshing();
                    if (isRefreshing) {
                        adapter.notifyItemRemoved(adapter.getItemCount());
                        return;
                    }
                    if (!isLoading) {
                        isLoading = true;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                BaseFragment.Page = BaseFragment.Page + 1;
                                LoadMore(BaseFragment.Page, Actions.LoadMore);
                            }
                        }, 1000);
                    }
                }
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        Bundle args = getArguments();
        text = args != null ? args.getString("text") : "";
        channel_id = args != null ? args.getInt("id", 0) : 0;
        type=args!=null?args.getString("type"):"";
        switch (type){
            case "BeautyLeg":
                classify=Classify.BeautyLeg;
                break;
            case "ApiGrils":
                classify=Classify.ApiGrils;
                break;
        }
        imageListPresenter=new ImageListPresenter(this);
        initAdapter();
        initData();
        super.onCreate(savedInstanceState);
    }

    @Override
    public void showImageList(List<ImageListBean> list, Actions action, int totalpage) {
        if (action.equals(Actions.LoadMore)){
            adapter.addAll(list);
            isLoading = false;
            adapter.notifyItemRemoved(adapter.getItemCount());

        }else if(action.equals(Actions.Refresh)){
            adapter.headinsert(list);
            swipeRefreshLayout.setRefreshing(false);

        }else if (action.equals(Actions.GoTo)){
            adapter.setList(list);
            swipeRefreshLayout.setRefreshing(false);
        }
        ed_currentpage.setText(String.valueOf(BaseFragment.Page));
        tv_totalpage.setText("/" + String.valueOf(totalpage));
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showError(int code, String msg,Actions action) {
        switch (code){
            case  ErrorCode.NoNextPage:
                adapter.showend();
                break;
            case ErrorCode.GetImageListError:
                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                break;
        }
    }
    private void hidekeyboard() {
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(ed_currentpage.getWindowToken(), 0);
    }
}
