package com.rhlabs.circle.services;

import retrofit.RetrofitError;

/**
 * Created by mhahn on 4/27/15.
 */
public interface Callback<T> {

    /** Successful service request **/
    void success(T t);

    /**
     * Unsuccessful HTTP response due to network failure, non-2XX status code, or unexpected
     * exception.
     */
    void failure(RetrofitError error);
}
