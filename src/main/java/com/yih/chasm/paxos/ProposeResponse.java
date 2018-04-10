package com.yih.chasm.paxos;

import com.yih.chasm.io.IVersonSerializer;
import io.netty.buffer.ByteBuf;
import lombok.Data;


@Data
public class ProposeResponse {
    public static final ProposeResponseSerializer serializer = new ProposeResponseSerializer();
    private int traceId;

    public ProposeResponse(int traceId) {
        this.traceId = traceId;
    }

    public static class ProposeResponseSerializer implements IVersonSerializer<ProposeResponse> {

        @Override
        public void serialize(ProposeResponse obj, ByteBuf buf) {
            buf.writeInt(obj.getTraceId());
        }

        @Override
        public ProposeResponse deserialize(ByteBuf buf) {
            ProposeResponse pr = new ProposeResponse(buf.readInt());
            return pr;
        }
    }
}