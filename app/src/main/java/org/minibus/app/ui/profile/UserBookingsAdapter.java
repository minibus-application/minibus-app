package org.minibus.app.ui.profile;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.minibus.app.data.network.pojo.booking.Booking;
import org.minibus.app.data.network.pojo.schedule.RouteTrip;
import org.minibus.app.ui.R;
import org.minibus.app.helpers.AppDatesHelper;
import org.minibus.app.ui.schedule.RouteScheduleAdapter;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

import static org.minibus.app.helpers.AppDatesHelper.DatePattern.BOOKING;
import static org.minibus.app.helpers.AppDatesHelper.DatePattern.ISO;


public class UserBookingsAdapter extends RecyclerView.Adapter<UserBookingsAdapter.BookingsViewHolder> {

    private Context context;
    private List<Booking> bookings = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    public UserBookingsAdapter(Context context) {
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.onItemClickListener = clickListener;
    }

    public void setData(List<Booking> bookings) {
        this.bookings.clear();
        if (bookings != null) {
            this.bookings.addAll(bookings);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BookingsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new BookingsViewHolder(inflater.inflate(R.layout.view_user_booking, viewGroup, false), onItemClickListener);
    }

    @Override
    public long getItemId(int position) {
        return new BigInteger(bookings.get(position).getId(), 16).longValue();
    }

    @Override
    public int getItemCount() {
        return bookings == null ? 0 : bookings.size();
    }

    @Override
    public void onBindViewHolder(@NonNull final BookingsViewHolder viewHolder, int position) {
        Timber.d("Bind booking with id = %d, position = %d", getItemId(position), position);

        Booking booking = bookings.get(position);
        viewHolder.bind(context, booking);
    }

    public interface OnItemClickListener {
        void onBookingActionButtonClick(View view, String itemId);
    }

    static class BookingsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_booking_trip_duration) TextView textTripDuration;
        @BindView(R.id.btn_booking_action) MaterialButton buttonAction;
        @BindView(R.id.tv_booking_dep_date) TextView textDepDate;
        @BindView(R.id.tv_booking_dep_time) TextView textDepTime;
        @BindView(R.id.tv_booking_dep_city) TextView textDepCity;
        @BindView(R.id.tv_booking_dep_station) TextView textDepCityStation;
        @BindView(R.id.tv_booking_arr_station) TextView textArrCityStation;
        @BindView(R.id.tv_booking_arr_time) TextView textArrTime;
        @BindView(R.id.tv_booking_arr_city) TextView textArrCity;
        @BindView(R.id.tv_booking_details) TextView textDetails;
        @BindView(R.id.tv_booking_cost) TextView textCost;
        @BindView(R.id.fl_trip) FrameLayout layoutBooking;

        private String itemId;
        private OnItemClickListener onItemClickListener;

        private BookingsViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            this.onItemClickListener = onItemClickListener;
            ButterKnife.bind(this, itemView);
        }

        private void bind(Context context, Booking booking) {
            String formattedDate = AppDatesHelper.formatDate(booking.getDepartureDate(), ISO, BOOKING);
            this.itemId = booking.getId();

            RouteTrip routeTrip = booking.getRouteTrip();

            String details = String.format("%s - %s %s, %s",
                    routeTrip.getVehicle().getCarrier().getName(),
                    routeTrip.getVehicle().getMake(),
                    routeTrip.getVehicle().getModel(),
                    routeTrip.getVehicle().getPlateNumber());

            textCost.setText(String.format("%s %s", routeTrip.getPrice(), routeTrip.getCurrency()));
            textDepDate.setText(formattedDate.substring(0, 1).toUpperCase().concat(formattedDate.substring(1)));
            textDetails.setText(details);
            textDepTime.setText(routeTrip.getDepartureTime());
            textDepCity.setText(routeTrip.getRoute().getDepartureCity().getName());
            textDepCityStation.setText(routeTrip.getRoute().getDepartureCity().getStation());
            textTripDuration.setText(routeTrip.getDuration());

            textArrTime.setText(routeTrip.getArrivalTime());
            textArrCity.setText(routeTrip.getRoute().getArrivalCity().getName());
            textArrCityStation.setText(routeTrip.getRoute().getArrivalCity().getStation());

            buttonAction.setVisibility(View.VISIBLE);
            buttonAction.setText(context.getText(R.string.cancel));
            buttonAction.setStrokeColor(ContextCompat.getColorStateList(context, R.color.selector_neg_outlined_btn));
            buttonAction.setTextColor(ContextCompat.getColorStateList(context, R.color.selector_neg_outlined_btn_text));
            buttonAction.setRippleColor(null);
            buttonAction.setEnabled(true);
            buttonAction.setClickable(true);
            buttonAction.setFocusable(true);

            if (booking.isEnRoute()) {
                buttonAction.setText(context.getText(R.string.en_route));
                buttonAction.setStrokeColorResource(R.color.colorGreen);
                buttonAction.setTextColor(context.getColor(R.color.colorGreen));
                buttonAction.setEnabled(false);
                buttonAction.setClickable(false);
                buttonAction.setFocusable(false);
            } else {
                if (!booking.isActive()) {
                    buttonAction.setVisibility(View.GONE);
                }
            }
        }

        @OnClick(R.id.btn_booking_action)
        public void onCancelButtonClick(View itemView) {
            onItemClickListener.onBookingActionButtonClick(itemView, itemId);
        }
    }
}
