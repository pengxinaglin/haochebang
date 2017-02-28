package com.haoche51.checker.adapter;

import android.app.Activity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haoche51.checker.R;
import com.haoche51.checker.activity.hostling.HostlingCompleteActivity;
import com.haoche51.checker.constants.TaskConstants;
import com.haoche51.checker.custom.CGridView;
import com.haoche51.checker.entity.HostlingTaskEntity;

import java.util.ArrayList;
import java.util.List;

import me.iwf.photopicker.utils.PhotoPickerIntent;

/**
 * 完成整备
 */
public class HostlingCompleteAdapter extends BaseAdapter {
  private List<HostlingTaskEntity.ProjectEntity> hostlingList;
  private Activity mActivity;
  private HostlingCompletePhotoAdapter gridAdpater;
  private View view;
  private ViewHolder holder;
  private MyTextWatcher myTextWatcher;
  private int index;

  public HostlingCompleteAdapter(Activity activity, List<HostlingTaskEntity.ProjectEntity> hostlingList, int index) {
    this.mActivity = activity;
    this.hostlingList = hostlingList;
    this.index = index;
  }

  @Override
  public int getCount() {
    return hostlingList.size();
  }

  @Override
  public Object getItem(int position) {
    return hostlingList.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(final int position, View convertView, ViewGroup parent) {
    if (convertView == null) {
      holder = new ViewHolder();
      view = LayoutInflater.from(mActivity).inflate(R.layout.item_hostling_complete, parent, false);
      holder.tv_project_number = (TextView) view.findViewById(R.id.tv_project_number);
      holder.tv_project_name = (TextView) view.findViewById(R.id.tv_project_name);
      holder.cb_not_finish = (CheckBox) view.findViewById(R.id.cb_not_finish);
      holder.ed_hostling_money = (EditText) view.findViewById(R.id.ed_hostling_money);
      holder.gridView = (CGridView) view.findViewById(R.id.cgv_hostling);
      holder.ll_hostling_add = (LinearLayout) view.findViewById(R.id.ll_hostling_add);
      holder.ll_hostling_project = (LinearLayout) view.findViewById(R.id.ll_hostling_project);
      view.setTag(holder);
    } else {
      view = convertView;
      holder = (ViewHolder) view.getTag();
    }

    final HostlingTaskEntity.ProjectEntity hostlingProject = hostlingList.get(position);
    //整备编号
    holder.tv_project_number.setText(String.valueOf(position + 1 + index));
    //整备名称
    holder.tv_project_name.setText(hostlingProject.getName());
    //是否整备
    holder.cb_not_finish.setTag(position);
    holder.cb_not_finish.setChecked(hostlingProject.getNo_repair() == 1);
    holder.cb_not_finish.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int position = (int) buttonView.getTag();
        HostlingTaskEntity.ProjectEntity tempProject = hostlingList.get(position);
        tempProject.setNo_repair(isChecked ? 1 : 0);
        if (isChecked) {
          tempProject.setReal_price("");
          tempProject.setAfter_image(new ArrayList<String>());
        }
        notifyDataSetChanged();
      }
    });


    //整备金额（元）
    Object obj = holder.ed_hostling_money.getTag();
    if (obj instanceof TextWatcher) {
      holder.ed_hostling_money.removeTextChangedListener((TextWatcher) obj);
    }

    if (TextUtils.isEmpty(hostlingProject.getReal_price())) {
      holder.ed_hostling_money.setText("");
    } else {
      holder.ed_hostling_money.setText(hostlingProject.getReal_price());
      holder.ed_hostling_money.setSelection(hostlingProject.getReal_price().length());
    }

    holder.ed_hostling_money.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
          if (!hostlingProject.isFocus() && !v.isFocused()) {
            v.requestFocus();
            v.onWindowFocusChanged(true);
          }
          setCheckFocus(position);
        }
        return false;
      }
    });

    myTextWatcher = new MyTextWatcher(hostlingProject);
    holder.ed_hostling_money.addTextChangedListener(myTextWatcher);
    holder.ed_hostling_money.setTag(myTextWatcher);

    //添加符号
    holder.ll_hostling_add.setTag(position);
    holder.ll_hostling_add.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        openPhotoPicker((int) v.getTag());
      }
    });


    //整备后照片
    if (hostlingProject.getAfter_image() != null && hostlingProject.getAfter_image().size() > 0) {
      gridAdpater = new HostlingCompletePhotoAdapter(mActivity, hostlingProject.getAfter_image());
      holder.gridView.setAdapter(gridAdpater);
    } else {
      holder.gridView.setAdapter(null);
    }

    //整备项目内容是否显示
    holder.ll_hostling_project.setVisibility(hostlingProject.getNo_repair() == 1 ? View.GONE : View.VISIBLE);
    return view;
  }

  /**
   * 设置选中焦点
   *
   * @param position 索引位置
   */
  private void setCheckFocus(int position) {
    int size = hostlingList.size();
    HostlingTaskEntity.ProjectEntity projectEntity;
    for (int i = 0; i < size; i++) {
      projectEntity = hostlingList.get(position);
      projectEntity.setFocus(false);
    }
    hostlingList.get(position).setFocus(true);
  }

  /**
   * 打开照片选择器
   *
   * @param position 整备项目的位置
   */
  private void openPhotoPicker(int position) {
    PhotoPickerIntent intent = new PhotoPickerIntent(mActivity);
    //设置每次只可以选择10张图片
    intent.setPhotoCount(10);
    intent.setShowCamera(false);
    ((HostlingCompleteActivity) this.mActivity).currPosition = position;
    mActivity.startActivityForResult(intent, TaskConstants.REQUEST_SELECT_PHOTO);
  }

  private class MyTextWatcher implements TextWatcher {
    private HostlingTaskEntity.ProjectEntity projectEntity;

    public MyTextWatcher(HostlingTaskEntity.ProjectEntity projectEntity) {
      this.projectEntity = projectEntity;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
      if (TextUtils.isEmpty(s)) {
        projectEntity.setReal_price("");
      } else {
        projectEntity.setReal_price(s.toString());
      }
    }
  }

  private class ViewHolder {
    private TextView tv_project_number;
    private TextView tv_project_name;
    private CheckBox cb_not_finish;
    private EditText ed_hostling_money;
    private CGridView gridView;
    private LinearLayout ll_hostling_add;
    private LinearLayout ll_hostling_project;
  }

}
