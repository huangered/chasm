package com.yih.chasm.paxos;

import com.yih.chasm.io.IVersonSerializer;
import io.netty.buffer.ByteBuf;
import lombok.Data;

@Data
public class PrepareResponse {

    public static final PrepareResponseSerializer serializer = new PrepareResponseSerializer();
    private boolean promised;
    private int traceId;
    private Commit commit;

    public PrepareResponse() {
    }

    public PrepareResponse(int traceId, boolean promised, Commit commit) {
        this.traceId = traceId;
        this.promised = promised;
        this.commit = commit;
    }

    public static class PrepareResponseSerializer implements IVersonSerializer<PrepareResponse> {

        @Override
        public void serialize(PrepareResponse obj, ByteBuf buf) {
            buf.writeInt(obj.getTraceId());
            buf.writeBoolean(obj.promised);
            Commit.serializer.serialize(obj.commit, buf);
        }

        @Override
        public PrepareResponse deserialize(ByteBuf buf) {
            PrepareResponse pr = new PrepareResponse();
            pr.setTraceId(buf.readInt());
            pr.setPromised(buf.readBoolean());
            pr.setCommit(Commit.serializer.deserialize(buf));
            return pr;
        }
    }
}