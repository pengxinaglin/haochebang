package com.haoche51.checker.activity.widget;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.haoche51.checker.R;
import com.haoche51.checker.listener.ConcreteWatched;
import com.haoche51.checker.util.ToastUtil;
import com.haoche51.checker.widget.calendar.CalendarDay;
import com.haoche51.checker.widget.calendar.MaterialCalendarView;
import com.haoche51.checker.widget.calendar.OnDateChangedListener;
import com.haoche51.checker.widget.calendar.OnMonthChangedListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by mac on 15/9/27.
 */
public class CalendarActivity extends CommonTitleBaseActivity implements OnDateChangedListener, OnMonthChangedListener {

    private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();

    MaterialCalendarView widget;
    TextView min, max;

    int minTime, maxTime;

    @Override
    public void initContentView(Bundle saveInstanceState) {
        widget = (MaterialCalendarView) findViewById(R.id.calendarView);
        min = (TextView) findViewById(R.id.min);
        max = (TextView) findViewById(R.id.max);

        widget.setOnDateChangedListener(this);
        widget.setOnMonthChangedListener(this);
    }

    @Override
    public void initTitleBar(TextView mReturn, TextView mTitle, TextView mRightFaction) {
        mTitle.setText(getString(R.string.select_time));
    }

    @Override
    public View getHCContentView() {
        return View.inflate(this, R.layout.activity_calendar, null);
    }

    @Override
    public void onHCReturn(TextView mReturn) {
        mReturn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ConcreteWatched.getInstance().notifyWatchers(0, 0);
                finish();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            ConcreteWatched.getInstance().notifyWatchers(0, 0);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onDateChanged(@NonNull MaterialCalendarView widget, @Nullable CalendarDay date) {
        if (date == null) {
            return;
        }
        if (minTime == 0) {
            min.setText(FORMATTER.format(date.getDate().getTime()));
            minTime = (int) (date.getDate().getTime() / 1000);
            ToastUtil.showInfo(getString(R.string.select_end_time));
        } else {
            if (date.getDate().getTime() / 1000 <= minTime)
                ToastUtil.showInfo(getString(R.string.end_time_error));
            else {
                maxTime = (int) (date.getDate().getTime() / 1000);
                max.setText(FORMATTER.format(date.getDate().getTime()));
                ConcreteWatched.getInstance().notifyWatchers(minTime, maxTime);
                finish();
            }
        }
    }

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
        ToastUtil.showInfo(FORMATTER.format(date.getDate()));
    }
}
