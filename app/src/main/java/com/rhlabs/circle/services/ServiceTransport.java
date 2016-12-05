package com.rhlabs.circle.services;

import com.rhlabs.protobufs.soa.ServiceRequestV1;
import com.rhlabs.protobufs.soa.ServiceResponseV1;

import rx.Observable;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by anju on 4/26/15.
 */
public interface ServiceTransport {
    @POST("/")
    void sendRequest(@Body ServiceRequestV1 serviceRequest, Callback<ServiceResponseV1> cb);

    @POST("/")
    Observable<ServiceResponseV1> sendRequest(@Body ServiceRequestV1 serviceRequest);
}