package com.yih.chasm.paxos;

import com.yih.chasm.io.IVersonSerializer;
import io.netty.buffer.ByteBuf;
import lombok.Data;


@Data
public class ProposeResponse {
    public static final ProposeResponseSerializer serializer = new ProposeResponseSerializer();
    private Long rnd;
    private boolean promised;

    public ProposeResponse(Long rnd, boolean promised) {
        this.rnd = rnd;
        this.promised = promised;
    }

    public static class ProposeResponseSerializer implements IVersonSerializer<ProposeResponse> {

        @Override
        public void serialize(ProposeResponse obj, ByteBuf buf) {
            buf.writeLong(obj.getRnd());
            buf.writeBoolean(obj.isPromised());
        }

        @Override
        public ProposeResponse deserialize(ByteBuf buf) {
            ProposeResponse pr = new ProposeResponse(buf.readLong(), buf.readBoolean());

            return pr;
        }
    }
}