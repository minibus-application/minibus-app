package org.minibus.app.data.network.model;

import org.minibus.app.data.network.AppApiClient;
import org.minibus.app.data.network.pojo.booking.BookingRequest;
import org.minibus.app.data.network.pojo.booking.BookingResponse;

import io.reactivex.Single;

public class BookingModel {

    public Single<BookingResponse> doDeleteBookingData(String authToken, int bookingId) {
        return AppApiClient.getApiService().deleteBookingData("Basic ".concat(authToken), Integer.toString(bookingId));
    }

    public Single<BookingResponse> doPostBookingData(String authToken, BookingRequest booking) {
        return AppApiClient.getApiService().postBookingData("Basic ".concat(authToken), booking);
    }
}
