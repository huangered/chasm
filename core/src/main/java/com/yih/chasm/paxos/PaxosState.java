package com.yih.chasm.paxos;

import com.google.common.util.concurrent.Striped;
import com.yih.chasm.storage.MetaService;

import java.util.concurrent.locks.Lock;

public class PaxosState {

    private static final Striped<Lock> LOCKS = Striped.lazyWeakLock(1024);

    private static long last_rnd;
    private static long vrnd;

    private final Commit accepted;

    private final Commit promised;

    public PaxosState(Commit accepted, Commit promised) {
        this.accepted = accepted;
        this.promised = promised;
    }

    public static PrepareResponse prepare(Commit toPrepare) {

        MetaService.Value value = MetaService.instance().getByInstance(toPrepare.getRnd());
        long t = last_rnd;
        PrepareResponse pr;
        if (toPrepare.getRnd() > last_rnd) {
            // write
            // ok
            last_rnd = toPrepare.getRnd();
            pr = new PrepareResponse(t, vrnd, value.getValue());
        } else {
            // false
            pr = new PrepareResponse(t, vrnd, value.getValue());
        }
        return pr;
    }

    public static ProposeResponse propose(Commit toPropose) {
        ProposeResponse pr;
        if (toPropose.getRnd() != last_rnd) {
            pr = new ProposeResponse(toPropose.getRnd(), false);
        } else {
            MetaService.instance().getByInstance(toPropose.getRnd()).setValue(toPropose.getValue());
            pr = new ProposeResponse(toPropose.getRnd(), true);
        }
        return pr;
    }
}