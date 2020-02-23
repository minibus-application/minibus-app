package com.example.minibus.ui.stops;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.minibus.data.network.pojo.city.CityResponse;
import com.example.minibus.data.network.pojo.city.BusStop;
import com.example.minibus.ui.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class BusStopsAdapter extends BaseExpandableListAdapter {

    public interface OnItemClickListener {
        void onBusStopClick(View view, BusStop stop);
    }

    private Context context;
    private List<CityResponse> cities = new ArrayList<>();
    private List<BusStop> busStops = new ArrayList<>();
    private Map<CityResponse, List<BusStop>> citiesToBusStopsMap = new HashMap<>();
    private TreeMap<CityResponse, List<BusStop>> citiesToBusStopsSortedMap;
    private int prevSelectedBusStopId;
    private OnItemClickListener clickListener;

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public BusStopsAdapter(Context context, List<CityResponse> cities, int prevSelectedBusStopId) {
        this.context = context;
        this.prevSelectedBusStopId = prevSelectedBusStopId;
        setData(cities);
    }

    private BusStop getStop(int cityPos, int stopPos) {
        return citiesToBusStopsSortedMap.get(cities.get(cityPos)).get(stopPos);
    }

    private CityResponse getCity(int cityPos) {
        return cities.get(cityPos);
    }

    private void clearData() {
        cities.clear();
        busStops.clear();
        citiesToBusStopsMap.clear();
    }

    public void setData(List<CityResponse> cities) {
        clearData();

        this.cities.addAll(cities);
        this.cities.forEach(city -> {
            busStops.addAll(city.getBusStops());
            citiesToBusStopsMap.put(city, city.getBusStops());
        });

        citiesToBusStopsSortedMap = new TreeMap<>(citiesToBusStopsMap);
        notifyDataSetChanged();
    }

    @Override
    public View getGroupView(int groupPos, boolean isExpanded, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.view_city, null);
        }

        TextView cityNameView = view.findViewById(R.id.text_city_name);
        ImageView checkMark = view.findViewById(R.id.image_city_selected);

        checkMark.setVisibility(View.GONE);
        cityNameView.setText(getGroup(groupPos).toString());

        if (getCity(groupPos).getBusStopsIds().contains(prevSelectedBusStopId)) {
            checkMark.setVisibility(View.VISIBLE);
        }

        return view;
    }

    @Override
    public View getChildView(int groupPos, int childPos, boolean isExpanded, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.view_bus_stop, null);
        }

        TextView stopNameView = view.findViewById(R.id.text_bus_stop_name);
        ImageView checkMark = view.findViewById(R.id.image_bus_stop_selected);

        checkMark.setVisibility(View.GONE);
        stopNameView.setText(getChild(groupPos, childPos).toString());

        if (getStop(groupPos, childPos).getId() == prevSelectedBusStopId) {
            checkMark.setVisibility(View.VISIBLE);
        }

        view.setOnClickListener(v -> clickListener.onBusStopClick(v, getStop(groupPos, childPos)));

        return view;
    }

    @Override
    public int getGroupCount() {
        return cities.size();
    }

    @Override
    public Object getGroup(int groupPos) {
        return getCity(groupPos);
    }

    @Override
    public long getGroupId(int groupPos) {
        return getCity(groupPos).getId();
    }

    @Override
    public int getChildrenCount(int groupPos) {
        return citiesToBusStopsMap.get(cities.get(groupPos)).size();
    }

    @Override
    public Object getChild(int groupPos, int childPos) {
        return getStop(groupPos, childPos);
    }

    @Override
    public long getChildId(int groupPos, int childPos) {
        return getStop(groupPos, childPos).getId();
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPos, int childPos) {
        return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }
}
