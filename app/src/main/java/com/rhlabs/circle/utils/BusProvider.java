package com.rhlabs.circle.utils;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * Created by anju on 6/7/15.
 */
public final class BusProvider {
    private static final Bus MAIN_BUS = new Bus(ThreadEnforcer.MAIN);

    private BusProvider() {
    }

    public static Bus getMainBus() {
        return MAIN_BUS;
    }
}
