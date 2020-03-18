package org.minibus.app.data.network;

import org.minibus.app.data.network.pojo.booking.BookingRequest;
import org.minibus.app.data.network.pojo.booking.BookingResponse;
import org.minibus.app.data.network.pojo.schedule.BusScheduleResponse;
import org.minibus.app.data.network.pojo.user.UserRequest;
import org.minibus.app.data.network.pojo.user.UserResponse;
import org.minibus.app.data.network.pojo.city.CityResponse;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AppApiService {

    @GET("schedule/{stopId}")
    Single<BusScheduleResponse> getScheduleData(@Path(value = "stopId") String stopId,
                                                @Header("CurrentDate") String date);

    @GET("cities")
    Single<List<CityResponse>> getCitiesData();

    @GET("users?old_trips=true")
    Single<UserResponse> getUserData(@Header("Authorization") String authToken);

    @Headers("Content-Type: application/json")
    @POST("users")
    Single<UserResponse> postUserData(@Body UserRequest newUser);

    @POST("bookings")
    Single<BookingResponse> postBookingData(@Header("Authorization") String authToken,
                                            @Body BookingRequest booking);

    @DELETE("bookings/{bookingId}")
    Single<BookingResponse> deleteBookingData(@Header("Authorization") String authToken,
                                             @Path(value = "bookingId") String bookingId);
}
