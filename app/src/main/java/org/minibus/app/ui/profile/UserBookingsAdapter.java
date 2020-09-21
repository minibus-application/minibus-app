package org.minibus.app.ui.profile;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.minibus.app.data.network.pojo.booking.Booking;
import org.minibus.app.data.network.pojo.schedule.RouteTrip;
import org.minibus.app.ui.R;
import org.minibus.app.helpers.AppDatesHelper;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static org.minibus.app.helpers.AppDatesHelper.DatePattern.BOOKING;
import static org.minibus.app.helpers.AppDatesHelper.DatePattern.ISO;


public class UserBookingsAdapter extends RecyclerView.Adapter<UserBookingsAdapter.BookingsViewHolder> {

    public interface OnItemClickListener {
        void onBookingActionButtonClick(View view, String id);
    }

    private Context context;
    private List<Booking> bookings = new ArrayList<>();
    private OnItemClickListener clickListener;

    public UserBookingsAdapter(Context context) {
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setData(List<Booking> bookings) {
        this.bookings.clear();
        if (bookings != null) this.bookings.addAll(bookings);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BookingsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        int bookingItemLayoutId = R.layout.view_user_booking;
        View layoutView = LayoutInflater.from(context).inflate(bookingItemLayoutId, viewGroup, false);
        return new BookingsViewHolder(layoutView);
    }

    @Override
    public long getItemId(int position) {
        return bookings.get(position).getLongId();
    }

    @Override
    public void onBindViewHolder(@NonNull BookingsViewHolder viewHolder, int position) {
        Booking booking = bookings.get(position);
        viewHolder.bind(booking);
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    class BookingsViewHolder extends RecyclerView.ViewHolder {

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

        private String itemId;

        private BookingsViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void bind(Booking booking) {
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

            if (booking.isEnRoute()) {
                buttonAction.setStrokeColorResource(R.color.colorAccent);
                buttonAction.setText(context.getText(R.string.en_route));
                buttonAction.setTextColor(context.getColor(R.color.colorAccent));
                buttonAction.setEnabled(false);
                buttonAction.setClickable(false);
            } else {
                buttonAction.setStrokeColorResource(R.color.colorControl);
                buttonAction.setText(context.getText(R.string.cancel));
                buttonAction.setEnabled(true);
                buttonAction.setClickable(true);
            }
        }

        @OnClick(R.id.btn_booking_action)
        public void onCancelButtonClick(View itemView) {
            clickListener.onBookingActionButtonClick(itemView, itemId);
        }
    }
}
