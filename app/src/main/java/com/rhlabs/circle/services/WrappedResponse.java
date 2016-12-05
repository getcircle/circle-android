package com.rhlabs.circle.services;

import android.util.Log;

import com.rhlabs.protobufs.soa.ActionControlV1;
import com.rhlabs.protobufs.soa.ActionRequestV1;
import com.rhlabs.protobufs.soa.ActionResponseV1;
import com.rhlabs.protobufs.soa.ActionResultV1;
import com.rhlabs.protobufs.soa.PaginatorV1;
import com.rhlabs.protobufs.soa.ServiceRequestV1;
import com.rhlabs.protobufs.soa.ServiceResponseV1;

import java.util.HashMap;
import java.util.Map;

import retrofit.client.Response;

/**
 * Created by mhahn on 4/27/15.
 */
public class WrappedResponse {

    private static final String LOGTAG = WrappedResponse.class.getSimpleName();

    ServiceRequestV1 mServiceRequest;
    ServiceResponseV1 mServiceResponse;
    ActionResponseV1 mActionResponse;
    Object mResponse;

    Map<String, String> mErrors;

    public WrappedResponse(ServiceRequestV1 mServiceRequest, ServiceResponseV1 mServiceResponse, ActionResponseV1 mActionResponse, Object mResponse, Response httpResponse) {
        this.mServiceRequest = mServiceRequest;
        this.mServiceResponse = mServiceResponse;
        this.mActionResponse = mActionResponse;
        this.mResponse = mResponse;

        if (mActionResponse != null &&
                mActionResponse.result != null &&
                mActionResponse.result.errors != null &&
                mActionResponse.result.errors.size() > 0 &&
                httpResponse != null) {

            Log.d(LOGTAG, mActionResponse.result.errors.toString());
            Log.d(LOGTAG, mActionResponse.result.error_details.toString());
            Log.d(LOGTAG, String.valueOf(httpResponse.getStatus()));

            mErrors = new HashMap<>();

            // TODO: Post global notification for non-200 and SERVER_ERRORS
            // Only return application level errors
            if (httpResponse.getStatus() == 200) {
                for (ActionResultV1.ErrorDetailV1 errorDetail : mActionResponse.result.error_details) {
                    if (!errorDetail.error.equals("SERVER_ERROR")) {
                        mErrors.put(errorDetail.key, errorDetail.detail);
                    }
                }
            }
        }
    }

    public Object getResponse() {
        return mResponse;
    }

    public ActionResponseV1 getActionResponse() {
        return mActionResponse;
    }

    public Map<String, String> getErrors() {
        return mErrors;
    }

    public PaginatorV1 getPaginator() {
        if (mActionResponse != null) {
            return mActionResponse.control.paginator;
        }

        return null;
    }

    public ServiceRequestV1 getNextRequest() {
        PaginatorV1 paginatorV1 = getPaginator();
        if (paginatorV1 != null && paginatorV1.next_page != null) {
            ServiceRequestV1.Builder nextServiceRequestBuilder = new ServiceRequestV1.Builder(mServiceRequest);
            nextServiceRequestBuilder.actions.clear();
            for (ActionRequestV1 action: mServiceRequest.actions) {
                ActionRequestV1.Builder actionBuilder = new ActionRequestV1.Builder(action);
                ActionControlV1.Builder actionControlBuilder = new ActionControlV1.Builder(action.control);
                PaginatorV1.Builder paginatorBuilder = new PaginatorV1.Builder(paginatorV1);
                paginatorBuilder.page = paginatorV1.next_page;
                actionControlBuilder.paginator = paginatorBuilder.build();
                actionBuilder.control = actionControlBuilder.build();
                nextServiceRequestBuilder.actions.add(actionBuilder.build());
            }

            return nextServiceRequestBuilder.build();
        }

        return null;
    }
}
