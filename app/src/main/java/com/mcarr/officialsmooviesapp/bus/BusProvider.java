package com.mcarr.officialsmooviesapp.bus;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**********************************
 * Created by Mikel on 23-May-16.
 *********************************/
public final class BusProvider {

    private static final Bus BUS = new Bus(ThreadEnforcer.ANY);

    public static Bus getInstance() {
        return BUS;
    }

    private BusProvider() {

    }

}
