package org.minibus.app.ui.cities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.minibus.app.AppConstants;
import org.minibus.app.data.network.pojo.city.City;
import org.minibus.app.ui.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CitiesAdapter extends RecyclerView.Adapter<CitiesAdapter.BusStopsViewHolder> {

    public interface OnItemClickListener {
        void onCityClick(View view, City city, int pos);
    }

    private Context context;
    private List<City> cities = new ArrayList<>();
    private CitiesAdapter.OnItemClickListener clickListener;
    private long lastSelectedItemId;

    public CitiesAdapter(Context context) {
        this.context = context;
    }

    public void setOnItemClickListener(CitiesAdapter.OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setData(List<City> cities, long lastSelectedItemId) {
        this.lastSelectedItemId = lastSelectedItemId;
        this.cities.clear();
        this.cities.addAll(cities);

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CitiesAdapter.BusStopsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.view_bus_stop, viewGroup, false);

        return new CitiesAdapter.BusStopsViewHolder(view);
    }

    @Override
    public long getItemId(int position) {
        return cities.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return cities == null ? 0 : cities.size();
    }

    @Override
    public void onBindViewHolder(@NonNull final CitiesAdapter.BusStopsViewHolder viewHolder, final int position) {
        City city = cities.get(position);
        viewHolder.bind(city, lastSelectedItemId == AppConstants.DEFAULT_SELECTED_CITY_ID
                ? cities.get(0).getId()
                : lastSelectedItemId);
    }

    class BusStopsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_city_title) TextView textCityName;
        @BindView(R.id.tv_region_subtitle) TextView textCityRegionName;
        @BindView(R.id.rl_city_location) RelativeLayout cityLocationIcon;

        private City city;
        private long cityId;

        public BusStopsViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(City city, long lastSelectedCityId) {
            this.city = city;
            this.cityId = city.getId();

            textCityRegionName.setText(city.getRegion());
            textCityName.setText(city.getName());
            textCityName.setTextColor(ContextCompat.getColor(context,
                    cityId == lastSelectedCityId ? R.color.colorAccent : R.color.colorOnPrimary));
        }

        @OnClick
        public void onItemClick(View itemView) {
            // have to get position by invoking getAdapterPosition()
            // because this is where sometimes the "position" value is not the proper one
            // and sometimes returns an exception regarding inconsistent items state.
            int itemPos = getAdapterPosition();

            lastSelectedItemId = cityId;
            clickListener.onCityClick(itemView, city, itemPos);
        }
    }
}
