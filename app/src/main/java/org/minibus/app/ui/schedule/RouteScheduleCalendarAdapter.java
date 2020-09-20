package org.minibus.app.ui.schedule;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import org.minibus.app.AppConstants;
import org.minibus.app.ui.R;
import org.minibus.app.helpers.AppDatesHelper;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;


public class RouteScheduleCalendarAdapter extends RecyclerView.Adapter<RouteScheduleCalendarAdapter.RouteScheduleCalendarViewHolder> {

    private Context context;
    private List<LocalDate> dates = new ArrayList<>();
    public List<Integer> activeDaysOfWeek;
    private int selectedPos;
    private OnCalendarItemClickListener onCalendarItemClickListener;

    public RouteScheduleCalendarAdapter(Context context) {
        this.context = context;
        this.activeDaysOfWeek = AppDatesHelper.getDaysOfWeek();
        this.selectedPos = AppConstants.DEFAULT_CALENDAR_DATE_POSITION;

        setupCalendar();
    }

    public void setOnItemClickListener(OnCalendarItemClickListener clickListener) {
        this.onCalendarItemClickListener = clickListener;
    }

    public void setActiveDays(List<Integer> daysOfWeek) {
        this.activeDaysOfWeek = daysOfWeek;
        notifyDataSetChanged();
    }

    public LocalDate getSelectedDate() {
        return getDate(selectedPos);
    }

    public LocalDate getDate(int position) {
        LocalDate previousDay = dates.get(0);
        LocalDate currentDay = LocalDate.now();

        // calculate days difference before and after an update
        // to re-draw calendar if the day has changed during the app session
        int diff = (int) ChronoUnit.DAYS.between(previousDay, currentDay);

        if (diff != 0) {
            Timber.d("The day has changed: %s", previousDay.toString());

            setupCalendar();
            selectedPos = selectedPos != 0 ? selectedPos - 1 : 0;
            notifyDataSetChanged();
        } else {
            dates.set(0, currentDay);
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
        LocalDate date = getDate(position);
        boolean isOperational = activeDaysOfWeek.contains(AppDatesHelper.getDayOfWeek(date));

        Timber.d("Bind date=%s, enabled=%s", date.toString(), isOperational);

        viewHolder.bind(date, isOperational);
    }

    @Override
    public int getItemCount() {
        return dates.size();
    }

    public interface OnCalendarItemClickListener {
        void onDateItemClick(View view, int position);
    }

    class RouteScheduleCalendarViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.rb_month_day) RadioButton radioButtonMonthDay;
        @BindView(R.id.tv_day_of_week) TextView textDayOfWeek;
        @BindView(R.id.ll_date_container) LinearLayout layoutDate;

        public RouteScheduleCalendarViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(LocalDate date, boolean isOperationalDay) {
            String formattedDate = AppDatesHelper.formatDate(date, AppDatesHelper.DatePattern.CALENDAR);
            String[] parts = formattedDate.split(" ");
            textDayOfWeek.setText(parts[0]);
            radioButtonMonthDay.setText(parts[1]);
            layoutDate.setEnabled(isOperationalDay);
            layoutDate.setAlpha(isOperationalDay ? 1.0f : 0.3f);

            if (selectedPos == getAdapterPosition()) {
                radioButtonMonthDay.setChecked(true);
            } else {
                radioButtonMonthDay.setChecked(false);
            }
        }

        @OnClick
        public void onItemClick(View itemView) {
            radioButtonMonthDay.setChecked(true);
            int itemPos = getAdapterPosition();
            if (selectedPos != itemPos) {
                onCalendarItemClickListener.onDateItemClick(itemView, itemPos);
                notifyDataSetChanged();
                selectedPos = itemPos;
            }
        }
    }

    private void setupCalendar() {
        if (!dates.isEmpty()) clearCalendar();
        dates.addAll(AppDatesHelper.getWeek());
    }

    private void clearCalendar() {
        dates.clear();
    }
}
