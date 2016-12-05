package com.rhlabs.circle.services;

import com.rhlabs.circle.common.AppPreferences;
import com.rhlabs.protobufs.services.registry.responses.Ext_responses;
import com.rhlabs.protobufs.soa.ActionControlV1;
import com.rhlabs.protobufs.soa.ActionRequestParamsV1;
import com.rhlabs.protobufs.soa.ActionRequestV1;
import com.rhlabs.protobufs.soa.ActionResponseV1;
import com.rhlabs.protobufs.soa.ActionResultV1;
import com.rhlabs.protobufs.soa.ControlV1;
import com.rhlabs.protobufs.soa.PaginatorV1;
import com.rhlabs.protobufs.soa.ServiceRequestV1;
import com.rhlabs.protobufs.soa.ServiceResponseV1;
import com.squareup.wire.Extension;
import com.squareup.wire.Message;
import com.squareup.wire.Wire;

import java.util.ArrayList;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.WireConverter;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by anju on 4/26/15.
 */
public class ServiceClient {

    String mServiceName;
    String mToken;
    ServiceTransport mServiceTransport;

    public ServiceClient(String serviceName) {
        mServiceName = serviceName;
        mToken = AppPreferences.getAuthToken();
        Wire wire = new Wire(Ext_responses.class);

        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
//                request.addHeader("Content-Agent", "application/x-protobuf");
//                request.addHeader("Accept-Encoding", "gzip");
                if (mToken != null) {
                    request.addHeader("Authorization", "Token " + mToken);
                }
            }
        };

        RestAdapter restAdapter = new RestAdapter.Builder()
                // LOCAL HOST
//                .setEndpoint("http://10.0.2.2:8000/")
                .setEndpoint("https://api.circlehq.co/")
                .setConverter(new WireConverter(wire))
                .setRequestInterceptor(requestInterceptor)
                .build();


        mServiceTransport = restAdapter.create(ServiceTransport.class);
    }

    public ServiceRequestV1 buildRequest(
            final Object genericRequestExtension,
            final Object genericResponseExtension,
            Message request,
            PaginatorV1 paginator
    ) {

        final Extension<ActionRequestParamsV1, Object> requestExtension = (Extension<ActionRequestParamsV1, Object>) genericRequestExtension;
        final Extension<ActionResultV1, Object> responseExtension = (Extension<ActionResultV1, Object>) genericResponseExtension;

        ActionRequestParamsV1 params = new ActionRequestParamsV1.Builder()
                .setExtension(requestExtension, request)
                .build();

        String extensionName = requestExtension.getName();
        String actionName = extensionName.substring(extensionName.lastIndexOf(".") + 1);

        if (paginator == null) {
            paginator = new PaginatorV1.Builder().build();
        }

        ControlV1 serviceControl = new ControlV1.Builder()
                .service(mServiceName)
                .token(mToken)
                .build();

        ActionControlV1 actionControl = new ActionControlV1.Builder()
                .service(mServiceName)
                .action(actionName)
                .paginator(paginator)
                .build();

        ActionRequestV1 actionRequest = new ActionRequestV1.Builder()
                .control(actionControl)
                .params(params)
                .build();

        ServiceRequestV1.Builder serviceRequest = new ServiceRequestV1.Builder();
        serviceRequest.control = serviceControl;
        ArrayList<ActionRequestV1> actions = new ArrayList<>();
        if (serviceRequest.actions != null) {
            actions.addAll(serviceRequest.actions);
        }
        actions.add(actionRequest);
        serviceRequest.actions =  actions;
        return serviceRequest.build();
    }

    public void callAction(
            final Object genericRequestExtension,
            final Object genericResponseExtension,
            Message request,
            PaginatorV1 paginator,
            final Callback<WrappedResponse> cb
    ) {
        final Extension<ActionResultV1, Object> responseExtension = (Extension<ActionResultV1, Object>) genericResponseExtension;
        final ServiceRequestV1 serviceRequest = buildRequest(genericRequestExtension, genericResponseExtension, request, paginator);
        mServiceTransport.sendRequest(serviceRequest, new retrofit.Callback<ServiceResponseV1>() {
            @Override
            public void success(ServiceResponseV1 serviceResponse, Response response) {
                ActionResponseV1 actionResponse = serviceResponse.actions.get(0);
                Object result = actionResponse.result.getExtension(responseExtension);
                WrappedResponse wrappedResponse = new WrappedResponse(serviceRequest, serviceResponse, actionResponse, result, response);
                cb.success(wrappedResponse);
            }

            @Override
            public void failure(RetrofitError error) {
                System.out.println("ERROR!");
                System.out.println(error);
                cb.failure(error);
            }
        });
    }

    public void callAction(
            final Object genericRequestExtension,
            final Object genericResponseExtension,
            Message request,
            final Callback<WrappedResponse> cb
    ) {
        callAction(genericRequestExtension, genericResponseExtension, request, null, cb);
    }

    public Observable<WrappedResponse> callAction(
            final Object genericRequestExtension,
            final Object genericResponseExtension,
            Message request,
            PaginatorV1 paginator
    ) {
        final Extension<ActionResultV1, Object> responseExtension = (Extension<ActionResultV1, Object>) genericResponseExtension;
        final ServiceRequestV1 serviceRequest = buildRequest(genericRequestExtension, genericResponseExtension, request, paginator);

        return mServiceTransport.sendRequest(serviceRequest).map(new Func1<ServiceResponseV1, WrappedResponse>() {
            @Override
            public WrappedResponse call(ServiceResponseV1 serviceResponseV1) {
                ActionResponseV1 actionResponse = serviceResponseV1.actions.get(0);
                Object result = actionResponse.result.getExtension(responseExtension);
                return new WrappedResponse(serviceRequest, serviceResponseV1, actionResponse, result, null);
            }
        });
    }

    public ServiceTransport getServiceTransport() {
        return mServiceTransport;
    }

    public static void sendRequest(final ServiceRequestV1 serviceRequest, final Object genericResponseExtension, final Callback<WrappedResponse> cb) {
        ServiceClient serviceClient = new ServiceClient("");
        final Extension<ActionResultV1, Object> responseExtension = (Extension<ActionResultV1, Object>) genericResponseExtension;
        serviceClient.getServiceTransport().sendRequest(serviceRequest, new retrofit.Callback<ServiceResponseV1>() {

            @Override
            public void success(ServiceResponseV1 serviceResponse, Response response) {
                ActionResponseV1 actionResponse = serviceResponse.actions.get(0);
                Object result = actionResponse.result.getExtension(responseExtension);
                WrappedResponse wrappedResponse = new WrappedResponse(serviceRequest, serviceResponse, actionResponse, result, response);
                cb.success(wrappedResponse);
            }

            @Override
            public void failure(RetrofitError error) {
                cb.failure(error);
            }
        });
    }
}