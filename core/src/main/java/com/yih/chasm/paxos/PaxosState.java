package com.yih.chasm.paxos;

import com.google.common.util.concurrent.Striped;
import com.yih.chasm.storage.MetaService;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Lock;

@Slf4j
public class PaxosState {

    private static final Striped<Lock> LOCKS = Striped.lazyWeakLock(1024);

    private static Commit accepted = new Commit(0L, "");

    private static Commit promised = new Commit(0L, "");

    public static void print() {
        log.info("paxos status {} {}", accepted.getRnd(), promised.getRnd());
    }

    public static PrepareResponse prepare(Commit toPrepare) {

        MetaService.Value value = MetaService.instance().getByInstance(toPrepare.getRnd());
        long t = accepted.getRnd();
        PrepareResponse pr;
        if (toPrepare.getRnd() > accepted.getRnd()) {
            // write
            // ok
            accepted.setRnd(toPrepare.getRnd());
            pr = new PrepareResponse(t, promised.getRnd(), value.getValue());
        } else {
            // false
            pr = new PrepareResponse(t, promised.getRnd(), value.getValue());
        }
        return pr;
    }

    public static ProposeResponse propose(Commit toPropose) {
        ProposeResponse pr;
        if (toPropose.getRnd() != accepted.getRnd()) {
            pr = new ProposeResponse(toPropose.getRnd(), false);
        } else {
            MetaService.instance().getByInstance(toPropose.getRnd()).setValue(toPropose.getValue());
            promised.setRnd(toPropose.getRnd());
            pr = new ProposeResponse(toPropose.getRnd(), true);
        }
        return pr;
    }
}