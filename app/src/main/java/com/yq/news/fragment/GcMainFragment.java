package com.yq.news.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.yq.news.R;
import com.yq.news.adapter.MainOutGridAdapter;
import com.yq.news.entity.MySection;
import com.yq.news.model.GridItem;
import com.yq.news.view.GridItemDecoration;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GcMainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GcMainFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private List<MySection> mData;

    private List<GridItem> values;

    private GridItemDecoration itemDecoration;

    private MainOutGridAdapter mainNestGridAdapter ;


    private RelativeLayout mHeaderGroup ;

    private ImageView imageView ;

    private HeaderAndFooterWrapper headerAndFooterWrapper ;

    public GcMainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GcMainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GcMainFragment newInstance(String param1, String param2) {
        GcMainFragment fragment = new GcMainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gc_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRcy(view);
        initToolbar(view);
        toolbar_title.setText("首页");
        iv_back.setVisibility(View.GONE);
        mainNestGridAdapter = new MainOutGridAdapter(getActivity());
        mHeaderGroup =  (RelativeLayout) LayoutInflater.from(getActivity()).inflate(R.layout.fragment_gc_header, null);
        imageView = mHeaderGroup.findViewById(R.id.image);
        //Glide.with(getActivity()).load("https://hbimg.huabanimg.com/beaa0006c6256915db02cfc2dbf9a094ccb9c59a6f3d3-HXfNMk_fw658").into(imageView);
        headerAndFooterWrapper = new HeaderAndFooterWrapper(mainNestGridAdapter);
        headerAndFooterWrapper.addHeaderView(mHeaderGroup);
       /* GridLayoutManager gll = new GridLayoutManager(getActivity(), 12);
        gll.setSpanSizeLookup(new SpecialSpanSizeLookup());*/
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(headerAndFooterWrapper);
        refreshLayout.setEnableAutoLoadMore(false);
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setEnableRefresh(false);
       /* values = initData();
        itemDecoration = new GridItemDecoration.Builder(getActivity(),values, 3)
                .setTitleTextColor(Color.parseColor("#4e5864"))
                //.setTitleBgColor(Color.parseColor("#008577"))
                .setTitleFontSize(22)
                .setTitleHeight(52)
                .build();
        recyclerView.addItemDecoration(itemDecoration);
        MainGridPageAdapter mainGridPageAdapter = new MainGridPageAdapter(getActivity(),values);
        recyclerView.setAdapter(mainGridPageAdapter);*/

    }

    class SpecialSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {

        @Override
        public int getSpanSize(int i) {
            GridItem gridItem = values.get(i);
            return gridItem.getSpanSize();
        }
    }
    private List<GridItem> initData() {
        List<GridItem> values = new ArrayList<>();
        values.add(new GridItem("我很忙", "", R.drawable.person_icon,"最近常听",3,GridItem.TYPE_SMALL));
        values.add(new GridItem("治愈：有些歌比闺蜜更懂你", "", R.drawable.person_icon,"最近常听",3,GridItem.TYPE_SMALL));
        values.add(new GridItem("「华语」90后的青春纪念手册", "", R.drawable.person_icon,"最近常听",3,GridItem.TYPE_SMALL));
        values.add(new GridItem("「华语」90后的青春纪念手册", "", R.drawable.person_icon,"最近常听",3,GridItem.TYPE_SMALL));

        //values.add(new GridItem("我很忙", "", R.drawable.grid_head_1,"最近常听",3,GridItem.TYPE_SMALL));
        /*values.add(new GridItem("治愈：有些歌比闺蜜更懂你", "", R.drawable.grid_head_2,"最近常听2",4,GridItem.TYPE_SMALL));
        values.add(new GridItem("「华语」90后的青春纪念手册", "", R.drawable.grid_head_3,"最近常听2",4,GridItem.TYPE_SMALL));
        values.add(new GridItem("「华语」90后的青春纪念手册", "", R.drawable.grid_head_3,"最近常听2",4,GridItem.TYPE_SMALL));*/
        values.add(new GridItem("流行创作女神你霉，泰勒斯威夫特的创作历程", "", R.drawable.person_icon
                ,"更多为你推荐",4,GridItem.TYPE_NORMAL));
        values.add(new GridItem("行走的CD写给别人的歌", "给「跟我走吧」几分，试试这些", R.drawable.person_icon
                ,"更多为你推荐",4,GridItem.TYPE_NORMAL));
        values.add(new GridItem("爱情里的酸甜苦辣，让人捉摸不透", "听完「靠近一点点」，他们等你翻牌", R.drawable.person_icon
                ,"更多为你推荐",4,GridItem.TYPE_NORMAL));
        values.add(new GridItem("关于喜欢你这件事，我都写在了歌里", "「好想你」听罢，听它们吧", R.drawable.person_icon
                ,"更多为你推荐",4,GridItem.TYPE_NORMAL));
        values.add(new GridItem("周杰伦暖心混剪，短短几分钟是多少人的青春", "", R.drawable.person_icon
                ,"更多为你推荐",4, GridItem.TYPE_NORMAL));
        values.add(new GridItem("我好想和你一起听雨滴", "给「发如雪」几分，那这些呢", R.drawable.person_icon
                ,"更多为你推荐",4,GridItem.TYPE_NORMAL));
        values.add(new GridItem("油管周杰伦热门单曲Top20", "「周杰伦」的这些哥，你听了吗", R.drawable.person_icon
                ,"更多为你推荐",4,GridItem.TYPE_NORMAL));

        return values;

    }

}
