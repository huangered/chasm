package com.yih.chasm.paxos;

import com.yih.chasm.io.IVersonSerializer;
import com.yih.chasm.io.StringSerializer;
import io.netty.buffer.ByteBuf;
import lombok.Data;

@Data
public class PrepareResponse {

    public static final PrepareResponseSerializer serializer = new PrepareResponseSerializer();
    private long vrnd;
    private long last_rnd;
    private String value;

    public PrepareResponse() {
    }

    public PrepareResponse(long last_rnd, long vrnd, String value) {
        this.last_rnd = last_rnd;
        this.vrnd = vrnd;
        this.value = value;
    }

    public static class PrepareResponseSerializer implements IVersonSerializer<PrepareResponse> {

        @Override
        public void serialize(PrepareResponse obj, ByteBuf buf) {
            buf.writeLong(obj.getLast_rnd());
            buf.writeLong(obj.vrnd);
            new StringSerializer().serialize(obj.value, buf);
        }

        @Override
        public PrepareResponse deserialize(ByteBuf buf) {
            PrepareResponse pr = new PrepareResponse();
            pr.setLast_rnd(buf.readLong());
            pr.setVrnd(buf.readLong());
            pr.setValue(new StringSerializer().deserialize(buf));
            return pr;
        }
    }
}