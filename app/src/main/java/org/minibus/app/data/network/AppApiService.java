package org.minibus.app.data.network;

import org.minibus.app.data.network.pojo.city.City;
import org.minibus.app.data.network.pojo.route.Route;
import org.minibus.app.data.network.pojo.schedule.RouteScheduleResponse;
import org.minibus.app.data.network.pojo.schedule.RouteTrip;
import org.minibus.app.data.network.pojo.user.UserResponse;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AppApiService {

    @GET("/cities")
    Single<List<City>> getAllCitiesData();

    @GET("/cities/exclude")
    Single<List<City>> getFilteredCitiesData(@Query("id") String cityId);

    @GET("/routes")
    Single<List<Route>> getAllRoutesData();

    @GET("/routes/filterBy")
    Single<Route> getRouteData(@Query("fromId") String depCityId, @Query("toId") String arrCityId);

    @GET("/user")
    Single<UserResponse> getUserData(@Header("Authorization") String authToken, @Query("history") boolean withBookingsHistory);

    @POST("/user/auth")
    Single<UserResponse> authUserData(@Body HashMap<String, Object> body);

    @POST("/user/create")
    Single<UserResponse> createUserData(@Body HashMap<String, Object> body);

    @DELETE("/user/revokeBooking")
    Single<UserResponse> deleteBookingData(@Header("Authorization") String authToken, @Query("id") String bookingId);

    @GET("/schedule/filterBy")
    Single<RouteScheduleResponse> getRouteScheduleData(@Query("tripDate") String date, @Query("routeId") String routeId);

    @POST("/schedule")
    Single<UserResponse> postRouteTripData(@Header("Authorization") String authToken,
                                              @Query("tripDate") String depDate,
                                              @Query("routeId") String routeId,
                                              @Query("tripId") String tripId,
                                              @Query("seats") int seatsCount);
}
