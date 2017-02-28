package com.haoche51.checker.fragment.offerrefer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.haoche51.checker.R;
import com.haoche51.checker.activity.offerrefer.SecondHandCarOfferActivity;
import com.haoche51.checker.adapter.OrderAdapter;

/**
 * "排序"
 * Created by wfx on 2016/8/5.
 */
public class OrderFragment extends Fragment {
    private View contentView;
    private ListView lv_order;
    private OrderAdapter orderPopupAdapter;
    private String[] orders = {"默认排序", "价格由高到底", "价格由低到高", "上牌时间降序", "上牌时间升序"};


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = View.inflate(getActivity(), R.layout.fragment_order, null);
        return contentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        lv_order = (ListView) contentView.findViewById(R.id.lv_order);
        initData();
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * 初始化"排序"界面的数据
     */
    private void initData() {

        if (orderPopupAdapter == null) {
            orderPopupAdapter = new OrderAdapter(getActivity(), orders);
            lv_order.setAdapter(orderPopupAdapter);
            lv_order.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    SecondHandCarOfferActivity activity = ((SecondHandCarOfferActivity) getActivity());
                    if (position == 0) {
                        activity.tv_order.setText("排序");
                    } else {
                        activity.tv_order.setText(orders[position]);
                    }
                    activity.getOfferReferEntity().setOrder(position);
                    activity.changeOrderStyle(false);
                    getFragmentManager().beginTransaction().hide(OrderFragment.this).commitAllowingStateLoss();
                    activity.onPullDownRefresh();
                }
            });
        } else {
            orderPopupAdapter.notifyDataSetChanged();
        }
    }

}
