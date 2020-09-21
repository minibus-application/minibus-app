package org.minibus.app.ui.profile;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.minibus.app.data.network.pojo.booking.Booking;
import org.minibus.app.ui.R;
import org.minibus.app.helpers.AppDatesHelper;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class UserBookingsAdapter extends RecyclerView.Adapter<UserBookingsAdapter.BookingsViewHolder> {

    public interface OnItemClickListener {
        void onBookingCancelButtonClick(View view, String id);
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

        @BindView(R.id.text_booking_dep_date) TextView textDepartureDate;
        @BindView(R.id.text_booking_dep_time) TextView textDepartureTime;
        @BindView(R.id.text_booking_dep_city) TextView textDepartureCity;
        @BindView(R.id.text_booking_dep_station) TextView textDepartureBusStop;
        @BindView(R.id.text_booking_arr_time) TextView textArrivalTime;
        @BindView(R.id.text_booking_arr_city) TextView textArrivalCity;
        @BindView(R.id.button_booking_action) MaterialButton buttonCancel;

        private String itemId;

        private BookingsViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void bind(Booking booking) {
            itemId = booking.getId();

            String date = AppDatesHelper.formatDate(booking.getDepartureDate(),
                    AppDatesHelper.DatePattern.ISO,
                    AppDatesHelper.DatePattern.BOOKING);

            textDepartureDate.setText(date.substring(0, 1).toUpperCase().concat(date.substring(1)));

            textDepartureTime.setText(booking.getRouteTrip().getDepartureTime());
            textDepartureCity.setText(booking.getRouteTrip().getRoute().getDepartureCity().getName());
//            textDepartureBusStop.setText(context.getResources()
//                    .getString(R.string.label_bus_stop_prefix, booking.getCity().getName()));

            textArrivalTime.setText(booking.getRouteTrip().getArrivalTime());
            textArrivalCity.setText(booking.getRouteTrip().getRoute().getArrivalCity().getName());
        }

        @OnClick(R.id.button_booking_action)
        public void onCancelButtonClick(View itemView) {
            clickListener.onBookingCancelButtonClick(itemView, itemId);
        }
    }
}
