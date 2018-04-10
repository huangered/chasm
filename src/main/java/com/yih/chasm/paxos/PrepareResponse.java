package com.yih.chasm.paxos;

import com.yih.chasm.io.IVersonSerializer;
import io.netty.buffer.ByteBuf;
import lombok.Data;

@Data
public class PrepareResponse {

    private int traceId;

    private boolean promised;

    public PrepareResponse(){}

    public PrepareResponse(int traceId, boolean promised) {
        this.traceId = traceId;
        this.promised = promised;
    }

    public static final PrepareResponseSerializer serializer = new PrepareResponseSerializer();

    public static class PrepareResponseSerializer implements IVersonSerializer<PrepareResponse> {

        @Override
        public void serialize(PrepareResponse obj, ByteBuf buf) {
            buf.writeInt(obj.getTraceId());
            buf.writeBoolean(obj.promised);
        }

        @Override
        public PrepareResponse deserialize(ByteBuf buf) {
            PrepareResponse pr = new PrepareResponse();
            pr.setTraceId(buf.readInt());
            pr.setPromised(buf.readBoolean());
            return pr;
        }
    }
}