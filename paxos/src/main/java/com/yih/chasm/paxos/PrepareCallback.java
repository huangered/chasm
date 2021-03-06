package com.yih.chasm.paxos;

import com.yih.chasm.net.MessageIn;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@EqualsAndHashCode(callSuper = false)
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
        log.debug("handle response {}", in);
        PrepareResponse pr = in.payload;
        if (pr.getPromised().compareTo(request.getRnd()) > 0) {
            promised = false;
            while (this.latch.getCount() > 0) {
                this.latch.countDown();
            }
            return;
        }
        if (pr.getAccepted() != null && pr.getAccepted().compareTo(response.getRnd()) > 0) {
            response.setValue(pr.getValue());
        }
        this.latch.countDown();

    }
}