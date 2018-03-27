package com.yih.chasm.paxos;

import com.yih.chasm.net.IVerbHandler;
import com.yih.chasm.net.MessageIn;

public class PrepareVerbHandler implements IVerbHandler<Commit> {
    @Override
    public void doVerb(MessageIn<Commit> in) {

    }
}
