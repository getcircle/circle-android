package com.rhlabs.circle.services;

import com.rhlabs.protobufs.services.registry.requests.Ext_requests;
import com.rhlabs.protobufs.services.registry.responses.Ext_responses;
import com.rhlabs.protobufs.services.search.actions.search.SearchRequestV1;
import com.rhlabs.protobufs.services.search.actions.search.SearchResponseV1;
import com.rhlabs.protobufs.services.search.containers.SearchResultV1;

import java.util.List;
import java.util.Map;

import retrofit.RetrofitError;

/**
 * Created by anju on 6/26/15.
 */
public class SearchService {

    public final static String LOG_TAG = SearchService.class.getSimpleName();

    public interface SearchResultsCallback {
        void success(List<SearchResultV1> results, Map<String, String> errors);
        void failure(RetrofitError error);
    }

    public static void search(String query, final SearchResultsCallback cb) {
        SearchRequestV1 request = new SearchRequestV1.Builder()
                .query(query)
                .build();

        ServiceClient serviceClient = new ServiceClient("search");
        serviceClient.callAction(
                Ext_requests.search,
                Ext_responses.search,
                request,
                null,
                new Callback<WrappedResponse>() {
                    @Override
                    public void success(WrappedResponse wrappedResponse) {
                        SearchResponseV1 response = (SearchResponseV1) wrappedResponse.getResponse();
                        List<SearchResultV1> results = response != null ? response.results : null;
                        cb.success(results, wrappedResponse.getErrors());
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        cb.failure(error);
                    }
                }
        );
    }
}
