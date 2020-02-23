package com.example.minibus.helpers;

import com.example.minibus.data.network.pojo.errors.ErrorResponse;
import com.google.gson.Gson;

import okhttp3.ResponseBody;
import retrofit2.HttpException;

public class ApiErrorHelper {

    public static String parseResponseMessage(Throwable throwable) {
        String errorMessage;

        try {
            ResponseBody body = ((HttpException) throwable).response().errorBody();
            Gson gson = new Gson();
            ErrorResponse errorResponse = gson.fromJson(body.charStream(), ErrorResponse.class);
            errorMessage = errorResponse.getMessage();
        } catch (Exception e) {
            errorMessage = throwable.getMessage();
        }

        return errorMessage;
    }
}
