package com.yih.chasm.paxos;

import com.yih.chasm.net.MessageIn;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class PrepareCallback extends AbstractPaxosCallback<PrepareResponse> {
    private boolean promised = true;
    private Commit request;
    private Commit response;

    public PrepareCallback(int count, Commit commit) {
        super(count);
        this.request = commit;
        this.response = commit;
    }

    @Override
    public synchronized void response(MessageIn<PrepareResponse> in) {
        log.info("handle response {}", in);
        PrepareResponse pr = in.payload;
        if (pr.getLast_rnd() > request.getRnd()) {
            promised = false;
            while (this.latch.getCount() > 0) {
                this.latch.countDown();
            }
            return;
        }
        if (pr.getVrnd() > response.getRnd()) {
            response.setValue(pr.getValue());
        }
        this.latch.countDown();

    }
}