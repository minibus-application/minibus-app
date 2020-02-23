package com.example.minibus.ui.schedule;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.minibus.ui.R;
import com.example.minibus.data.network.pojo.schedule.BusTrip;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class BusScheduleAdapter extends RecyclerView.Adapter<BusScheduleAdapter.BusScheduleViewHolder> {

    public interface OnItemClickListener {
        void onBusTripClick(View view, int id, int pos);
    }

    private Context context;
    private List<BusTrip> busTrips = new ArrayList<>();
    private OnItemClickListener clickListener;
    private boolean isItemClickable = true;
    private boolean isItemLoading = false;
    private int lastClickedItemPos = -1;

    public BusScheduleAdapter(Context context) {
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setData(List<BusTrip> newBusTrips) {
        DiffUtil.DiffResult diffResult =
                DiffUtil.calculateDiff(new BusScheduleDiffUtilCallback(this.busTrips, newBusTrips), true);

        this.busTrips.clear();
        this.busTrips.addAll(newBusTrips);

        diffResult.dispatchUpdatesTo(this);
    }

    public void setLoading(boolean isLoading) {
        isItemLoading = isLoading;
        isItemClickable = !isLoading;

        if (lastClickedItemPos >= 0) notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BusScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        int layoutIdForBusTrip = R.layout.view_bus_trip;

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForBusTrip, viewGroup, false);

        return new BusScheduleViewHolder(view);
    }

    @Override
    public long getItemId(int position) {
        return busTrips.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return busTrips == null ? 0 : busTrips.size();
    }

    @Override
    public void onBindViewHolder(@NonNull final BusScheduleViewHolder viewHolder, final int position) {
        BusTrip busTrip = busTrips.get(position);
        viewHolder.bind(busTrip);

        Timber.d("Bind bus trip with id = %d, position = %d", getItemId(position), position);

        // if not first item, check if item above has the same header
        viewHolder.showHeader(position > 0
                && busTrips.get(position - 1).getDepartureHours().equals(busTrip.getDepartureHours()));

        // set loading indicator only for clicked item
        viewHolder.setLoading(lastClickedItemPos == position && isItemLoading);
    }

    class BusScheduleViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_bus_trip_dep_time) TextView textDepartureTime;
        @BindView(R.id.text_bus_trip_arr_time) TextView textArrivalTime;
        @BindView(R.id.text_bus_trip_seats_count) TextView textSeatsCount;
        @BindView(R.id.text_bus_trip_header) TextView textHeader;
        @BindView(R.id.layout_bus_trip) RelativeLayout layoutBusTrip;
        @BindView(R.id.progress_bus_trip) ProgressBar progress;
        @BindView(R.id.image_bus_trip_forward) ImageView imageForward;

        private int itemId;

        public BusScheduleViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(BusTrip busTrip) {
            itemId = busTrip.getId();

            textDepartureTime.setText(busTrip.getDepartureTime());
            textArrivalTime.setText(busTrip.getArrivalTime());
            textHeader.setText(busTrip.getDepartureHours());
            textSeatsCount.setText(context.getResources()
                    .getString(R.string.label_available_seats_count, busTrip.getSeatsCount()));

            layoutBusTrip.setAlpha(busTrip.getSeatsCount() == 0 ? 0.5f : 1.0f);
        }

        @OnClick
        public void onItemClick(View itemView) {
            // have to get position by invoking getAdapterPosition()
            // because this is where sometimes the "position" value is not the proper one
            // and sometimes returns an exception regarding inconsistent items state.
            int itemPos = getAdapterPosition();

            if (isItemClickable && itemPos >= 0) {
                lastClickedItemPos = itemPos;
                clickListener.onBusTripClick(itemView, itemId, itemPos);

                Timber.d("Select bus trip with id = %d, position = %d", itemId, itemPos);
            }
        }

        protected void setLoading(boolean isLoading) {
            progress.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            imageForward.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        }

        protected void showHeader(boolean isAboveTheSame) {
            textHeader.setVisibility(isAboveTheSame ? View.GONE : View.VISIBLE);
        }
    }
}
