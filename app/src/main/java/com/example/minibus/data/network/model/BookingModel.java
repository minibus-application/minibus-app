package com.example.minibus.data.network.model;

import com.example.minibus.data.network.AppApiClient;
import com.example.minibus.data.network.pojo.booking.BookingRequest;
import com.example.minibus.data.network.pojo.booking.BookingResponse;

import io.reactivex.Single;

public class BookingModel {

    public Single<BookingResponse> doDeleteBookingData(String authToken, int bookingId) {
        return AppApiClient.getApiService().deleteBookingData("Basic ".concat(authToken), Integer.toString(bookingId));
    }

    public Single<BookingResponse> doPostBookingData(String authToken, BookingRequest booking) {
        return AppApiClient.getApiService().postBookingData("Basic ".concat(authToken), booking);
    }
}
