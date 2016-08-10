package com.mcarr.officialsmooviesapp.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.mcarr.officialsmooviesapp.R;
import com.mcarr.officialsmooviesapp.bus.BusProvider;
import com.mcarr.officialsmooviesapp.events.CreateEventEvent;
import com.mcarr.officialsmooviesapp.object.Event;
import com.mcarr.officialsmooviesapp.object.request.CreateEventRequest;
import com.mcarr.officialsmooviesapp.object.response.CreateEventResponse;
import com.mcarr.officialsmooviesapp.util.Util;
import com.squareup.otto.Subscribe;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.Icepick;
import icepick.State;

/**********************************
 * Created by Mikel on 08-Jun-16.
 *********************************/
public class CreateEventActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {

    /** Static **/
    public static final String ARGS_GROUP_ID = "ARGS_GROUP_ID";
    public static final String ARGS_GROUP_IMAGE = "ARGS_GROUP_IMAGE";
    public static final String ARGS_EXISTING_EVENT = "ARGS_EXISTING_EVENT";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormat.forPattern("EEEE, dd MMM");
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormat.forPattern("h:mma");

    /** Views **/
    @BindView(R.id.activity_create_event_name) EditText mNameView;
    @BindView(R.id.activity_create_event_date) TextView mDateView;
    @BindView(R.id.activity_create_event_time) TextView mTimeView;
    @BindView(R.id.activity_create_event_location) EditText mLocationView;
    @BindView(R.id.activity_create_event_theme) EditText mThemeView;
    @BindView(R.id.activity_create_event_notes) EditText mNotesView;

    /** Instance **/
    @State protected LocalDateTime mDateTime = LocalDateTime.now().plusDays(1);
    @State protected int mGroupID;
    @State protected String mGroupImage;
    private Event mEvent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
        setContentView(R.layout.activity_create_event);
        ButterKnife.bind(this);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent() != null) {
            mGroupID = getIntent().getIntExtra(ARGS_GROUP_ID, -1);
            mGroupImage = getIntent().getStringExtra(ARGS_GROUP_IMAGE);
            mEvent = (Event) getIntent().getSerializableExtra(ARGS_EXISTING_EVENT);
        }

        if (mEvent == null) {
            mDateView.setText(mDateTime.toString(DATE_FORMAT));
            mTimeView.setText(mDateTime.toString(TIME_FORMAT));
        } else {
            mDateView.setText(mEvent.getEventDateTime().toString(DATE_FORMAT));
            mTimeView.setText(mEvent.getEventDateTime().toString(TIME_FORMAT));
            mNameView.setText(mEvent.getName());
            mLocationView.setText(mEvent.getLocation());
            mThemeView.setText(mEvent.getTheme());
            mNotesView.setText(mEvent.getNotes());
        }
    }

    @OnClick(R.id.activity_create_event_date) protected void onDateClicked() {
        DatePickerDialog dialog = new DatePickerDialog(
                this,
                this,
                mDateTime.getYear(),
                mDateTime.getMonthOfYear(),
                mDateTime.getDayOfMonth());
        dialog.show();
    }

    @OnClick(R.id.activity_create_event_time) protected void onTimeClicked() {
        TimePickerDialog dialog = new TimePickerDialog(
                this,
                this,
                mDateTime.getHourOfDay(),
                mDateTime.getMinuteOfHour(),
                false);
        dialog.show();
    }

    @Override public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_create_event_check:
                done();
                return true;
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_event, menu);
        return true;
    }

    private void done() {

        ArrayList<EditText> views = new ArrayList<>();
        views.add(mThemeView);
        views.add(mLocationView);
        views.add(mNameView);

        boolean cancel = false;

        for (EditText field : views) {
            String input = field.getText().toString();
            if (TextUtils.isEmpty(input)) {
                cancel = true;
                field.setError(getString(R.string.error_invalid_input));
                field.requestFocus();
            }
        }

        if (!cancel) {
            if (mEvent == null) {
                CreateEventRequest tokenRequest = new CreateEventRequest(
                        Util.getToken(this),
                        mNameView.getText().toString(),
                        mGroupID,
                        mThemeView.getText().toString(),
                        mLocationView.getText().toString(),
                        mNotesView.getText().toString(),
                        mDateTime.toString());
                BusProvider.getInstance().post(new CreateEventEvent.OnLoadingStart(tokenRequest));
            } else {
                Intent returnIntent = getIntent();
                returnIntent.putExtra(EventDetailActivity.ARGS_EVENT_THEME, mThemeView.getText().toString());
                returnIntent.putExtra(EventDetailActivity.ARGS_EVENT_NAME, mNameView.getText().toString());
                returnIntent.putExtra(EventDetailActivity.ARGS_EVENT_DATE, mDateTime);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        }
    }

    @Subscribe
    public void onEventCreated(CreateEventEvent.OnLoaded onLoaded) {
        CreateEventResponse response = onLoaded.getResponse();
        Intent intent = new Intent(this, EventDetailActivity.class);
        intent.putExtra(EventDetailActivity.ARGS_EVENT_ID, response.getEventID());
        intent.putExtra(EventDetailActivity.ARGS_EVENT_NAME, mNameView.getText().toString());
        intent.putExtra(EventDetailActivity.ARGS_EVENT_DATE, mDateTime);
        intent.putExtra(EventDetailActivity.ARGS_GROUP_IMAGE, mGroupImage);
        startActivity(intent);
        finish();
    }

    @Subscribe
    public void onEventCreateFailed(CreateEventEvent.OnLoadingError onLoadingFailed) {
        Toast.makeText(this, "load failed", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        mDateTime = mDateTime.withYear(year).withMonthOfYear(monthOfYear).withDayOfMonth(dayOfMonth);
        mDateView.setText(mDateTime.toString(DATE_FORMAT));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        mDateTime = mDateTime.withHourOfDay(hourOfDay).withMinuteOfHour(minute);
        mTimeView.setText(mDateTime.toString(TIME_FORMAT));
    }
}
