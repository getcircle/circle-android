package com.rhlabs.circle.utils;

import com.rhlabs.protobufs.soa.ActionRequestV1;
import com.rhlabs.protobufs.soa.PaginatorV1;
import com.rhlabs.protobufs.soa.ServiceRequestV1;

/**
 * Created by anju on 6/24/15.
 */
public abstract class ServiceRequestUtils {

    public static PaginatorV1 getPaginator(ServiceRequestV1 request) {
        ActionRequestV1 action = request.actions.get(0);
        if (action != null) {
            return action.control.paginator;
        }

        return null;
    }

    public static int getTotalItemsCount(ServiceRequestV1 request) {
        if (request != null) {
            PaginatorV1 paginator = getPaginator(request);
            if (paginator != null) {
                return paginator.count;
            }
        }

        return 0;
    }
}
