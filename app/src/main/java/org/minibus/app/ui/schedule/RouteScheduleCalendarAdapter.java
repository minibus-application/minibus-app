package org.minibus.app.ui.schedule;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import org.minibus.app.AppConstants;
import org.minibus.app.ui.R;
import org.minibus.app.helpers.AppDatesHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class RouteScheduleCalendarAdapter extends RecyclerView.Adapter<RouteScheduleCalendarAdapter.RouteScheduleCalendarViewHolder> {

    public interface OnCalendarItemClickListener {
        void onDateClick(View view, int position);
    }

    private final int DEFAULT_CALENDAR_DATE_POSITION = AppConstants.DEFAULT_CALENDAR_DATE_POSITION;
    private final int DEFAULT_CALENDAR_SIZE = AppConstants.DEFAULT_CALENDAR_SIZE;

    private Context context;
    private List<String> dates = new ArrayList<>();
    private int lastSelectedPos;
    private int selectedPos;
    private OnCalendarItemClickListener onCalendarItemClickListener;

    public RouteScheduleCalendarAdapter(Context context) {
        this.context = context;
        setupSelectedDatePosition(DEFAULT_CALENDAR_DATE_POSITION);
        setupCalendar(DEFAULT_CALENDAR_SIZE);
    }

    public void setOnItemClickListener(OnCalendarItemClickListener clickListener) {
        this.onCalendarItemClickListener = clickListener;
    }

    private void setupSelectedDatePosition(int position) {
        this.selectedPos = position;
        this.lastSelectedPos = selectedPos;
    }

    private void setupCalendar(int calendarSize) {
        if (!dates.isEmpty()) clearCalendar();
        dates.addAll(AppDatesHelper.createWeekdays(calendarSize, AppDatesHelper.DatePattern.API_SCHEDULE_REQUEST));
    }

    private void clearCalendar() {
        dates.clear();
    }

    public String getSelectedDate(AppDatesHelper.DatePattern pattern) {
        return AppDatesHelper.formatDate(getSelectedDate(), AppDatesHelper.DatePattern.API_SCHEDULE_REQUEST, pattern);
    }

    public String getSelectedDate() {
        return getDate(selectedPos);
    }

    public String getDate(AppDatesHelper.DatePattern pattern, int position) {
        return AppDatesHelper.formatDate(getDate(position), AppDatesHelper.DatePattern.API_SCHEDULE_REQUEST, pattern);
    }

    public String getDate(int position) {
        String oldCurrentDate = dates.get(DEFAULT_CALENDAR_DATE_POSITION);
        String currentDate = AppDatesHelper.getTimestamp();

        // calculate days difference before update and after
        // to re-draw calendar if the date changes during the app session
        int diff = AppDatesHelper.getDaysDifference(oldCurrentDate, currentDate, AppDatesHelper.DatePattern.API_SCHEDULE_REQUEST);

        if (diff != 0) {
            Timber.d("The day has passed");

            setupCalendar(DEFAULT_CALENDAR_SIZE);
            lastSelectedPos = lastSelectedPos != 0 ? lastSelectedPos - 1 : 0;
            notifyDataSetChanged();
        } else {
            dates.set(DEFAULT_CALENDAR_DATE_POSITION, currentDate);
        }

        return dates.get(position);
    }

    @NonNull
    @Override
    public RouteScheduleCalendarViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        int layoutIdForDatePickerItem = R.layout.view_calendar_date;

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForDatePickerItem, viewGroup, false);

        return new RouteScheduleCalendarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RouteScheduleCalendarViewHolder viewHolder, int position) {
        viewHolder.bind(getDate(AppDatesHelper.DatePattern.CALENDAR, position).toUpperCase());

        viewHolder.itemView.setOnClickListener((View v) -> {
            lastSelectedPos = position;
            onCalendarItemClickListener.onDateClick(v, position);

            notifyDataSetChanged();
        });

        if (lastSelectedPos == position) {
            selectedPos = position;
            viewHolder.radioButtonMonthDay.setChecked(true);
        } else {
            viewHolder.radioButtonMonthDay.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        return dates.size();
    }

    static class RouteScheduleCalendarViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.radio_button_month_day) RadioButton radioButtonMonthDay;
        @BindView(R.id.text_week_day) TextView textWeekDay;

        public RouteScheduleCalendarViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(String date) {
            String[] parts = date.split(" ");
            textWeekDay.setText(parts[0]);
            radioButtonMonthDay.setText(parts[1]);
        }
    }
}
