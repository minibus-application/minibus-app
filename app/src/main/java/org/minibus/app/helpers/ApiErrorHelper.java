package org.minibus.app.helpers;

import android.annotation.SuppressLint;

import org.minibus.app.data.network.pojo.errors.ErrorResponse;
import com.google.gson.Gson;

import okhttp3.ResponseBody;
import retrofit2.HttpException;

public class ApiErrorHelper {

    @SuppressLint("DefaultLocale")
    public static String parseResponseMessage(Throwable throwable) {
        String errorMessage;

        try {
            ResponseBody body = ((HttpException) throwable).response().errorBody();
            Gson gson = new Gson();
            ErrorResponse errorResponse = gson.fromJson(body.charStream(), ErrorResponse.class);
            errorMessage = String.format("%d: %s", errorResponse.getCode(), errorResponse.getMessage());
        } catch (Exception e) {
            errorMessage = throwable.getMessage();
        }

        return errorMessage;
    }
}
