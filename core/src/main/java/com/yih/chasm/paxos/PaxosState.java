package com.yih.chasm.paxos;

import com.yih.chasm.storage.MetaService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PaxosState {

    public static synchronized PrepareResponse prepare(Commit toPrepare) {

        PaxosInstance curInst = MetaService.instance().currentInstance();
        SuggestionID promised = curInst.getPromised();
        PrepareResponse pr;
        if (toPrepare.getRnd().compareTo(promised) > 0) {
            // write
            // ok
            curInst.setPromised(toPrepare.getRnd());
            if (curInst.getAccepted() != null) {
                pr = new PrepareResponse(curInst.getPromised(), curInst.getAccepted(), curInst.getValue());
            } else {
                pr = new PrepareResponse(curInst.getPromised());
            }
        } else {
            // false
            pr = new PrepareResponse(promised);
        }
        return pr;
    }

    public static synchronized ProposeResponse propose(Commit toPropose) {
        PaxosInstance curInst = MetaService.instance().currentInstance();
        ProposeResponse pr;
        if (toPropose.getRnd().compareTo(curInst.getPromised()) >= 0) {
            curInst.setAccepted(toPropose.getRnd());
            curInst.setValue(toPropose.getValue());
            pr = new ProposeResponse(true);
        } else {
            pr = new ProposeResponse(false);
        }
        return pr;
    }

    public static synchronized void learn(Commit toLearn) {
        PaxosInstance curInst = MetaService.instance().currentInstance();
        curInst.setValue(toLearn.getValue());
        log.debug("Cur instance {}", curInst.getValue());
        MetaService.instance().createInstance();
    }
}