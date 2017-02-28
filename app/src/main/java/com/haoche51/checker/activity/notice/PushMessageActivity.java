package com.haoche51.checker.activity.notice;

import android.support.v4.app.FragmentTransaction;

import com.haoche51.checker.R;
import com.haoche51.checker.activity.widget.CommonBaseFragmentActivity;
import com.haoche51.checker.fragment.push.MessageListFragment;

/**
 * Created by yangming on 2015/12/1.
 */
public class PushMessageActivity extends CommonBaseFragmentActivity {

  @Override
  protected void initView() {
    super.initView();
    setContentView(R.layout.activity_message_list);
    registerTitleBack();
    setScreenTitle(getString(R.string.hc_message_activity_title));
    FragmentTransaction ft = this.getSupportFragmentManager()
      .beginTransaction();
    ft.add(R.id.fl_activity_message_list_container, new MessageListFragment());
    ft.commitAllowingStateLoss();
  }

  @Override
  protected void initData() {
    super.initData();
  }


}
