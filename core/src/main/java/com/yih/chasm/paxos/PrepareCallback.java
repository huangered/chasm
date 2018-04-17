package com.yih.chasm.paxos;

import com.yih.chasm.net.MessageIn;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class PrepareCallback extends AbstractPaxosCallback<PrepareResponse> {
    public Boolean promised;
    private long rnd;
    private String value;
    private long v_rnd;

    public PrepareCallback(int count, long rnd) {
        super(count);
        this.rnd = rnd;
    }

    @Override
    public void response(MessageIn<PrepareResponse> in) {
        log.info("handle response {}", in);
        PrepareResponse pr = in.payload;
        if (pr.getLast_rnd() > rnd) {
            promised = false;
            while (this.latch.getCount() > 0) {
                this.latch.countDown();
            }
        } else {
            if (pr.getVrnd() > v_rnd) {
                value = pr.getValue();
            }
            this.latch.countDown();
        }
    }
}