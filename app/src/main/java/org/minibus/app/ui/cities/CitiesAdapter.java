package org.minibus.app.ui.cities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.minibus.app.data.network.pojo.city.City;
import org.minibus.app.ui.R;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CitiesAdapter extends RecyclerView.Adapter<CitiesAdapter.CitiesViewHolder> {

    private Context context;
    private List<City> cities = new ArrayList<>();
    private OnCityItemClickListener clickListener;
    private String lastSelectedItemId;

    public CitiesAdapter(Context context) {
        this.context = context;
    }

    public void setOnItemClickListener(OnCityItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setData(List<City> cities, final String lastSelectedItemId) {
        this.lastSelectedItemId = lastSelectedItemId;
        this.cities.clear();
        this.cities.addAll(getSortedBySelected(cities));

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CitiesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.view_city, viewGroup, false);
        return new CitiesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CitiesViewHolder viewHolder, final int position) {
        City city = cities.get(position);

        if (lastSelectedItemId == null) viewHolder.bind(city);
        else viewHolder.bind(city, lastSelectedItemId);
    }

    @Override
    public long getItemId(int position) {
        return new BigInteger(cities.get(position).getId(), 16).longValue();
    }

    @Override
    public int getItemCount() {
        return cities == null ? 0 : cities.size();
    }

    private List<City> getSortedBySelected(List<City> unsorted) {
        return unsorted.stream()
                .sorted((o1, o2) -> o1.getId().equals(lastSelectedItemId) ? -1 : o2.getId().equals(lastSelectedItemId) ? 1 : 0)
                .collect(Collectors.toList());
    }

    public interface OnCityItemClickListener {
        void onCityClick(View view, City city, int pos);
        void onCityLocationClick(View view, City city, int pos);
    }

    class CitiesViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_city_title) TextView textCityName;
        @BindView(R.id.tv_region_subtitle) TextView textCityRegionName;
        @BindView(R.id.iv_city_location) ImageView cityLocationIcon;

        private City city;
        private String cityId;

        public CitiesViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(City city, String lastSelectedCityId) {
            this.city = city;
            this.cityId = city.getId();

            textCityRegionName.setText(city.getRegion());
            textCityName.setText(city.getName());
            textCityName.setTextColor(ContextCompat.getColor(context,
                    cityId.equals(lastSelectedCityId) ? R.color.colorSecondary : R.color.colorTextPrimary));
        }

        public void bind(City city) {
            this.city = city;
            this.cityId = city.getId();

            textCityRegionName.setText(city.getRegion());
            textCityName.setText(city.getName());
        }

        @OnClick(R.id.ll_city_container)
        public void onCityClick(View itemView) {
            int itemPos = getAdapterPosition();

            lastSelectedItemId = cityId;
            clickListener.onCityClick(itemView, city, itemPos);
        }

        @OnClick(R.id.iv_city_location)
        public void onCityLocationClick(View itemView) {
            clickListener.onCityLocationClick(itemView, city, getAdapterPosition());
        }
    }
}
