package com.yih.chasm.paxos;

import com.yih.chasm.io.IVersonSerializer;
import io.netty.buffer.ByteBuf;
import lombok.Data;

@Data
public class Commit {

    private Integer traceId;

    private Integer proposalNumber;

    public Commit(int traceId, Integer proposalNumber) {
        this.traceId = traceId;
        this.proposalNumber = proposalNumber;
    }

    public static final CommitSerializer serializer = new CommitSerializer();

    public static Commit newPrepare(int id, int n) {
        return new Commit(id, n);
    }

    public static Commit newPropose(int id, int n) {
        return new Commit(id, n);
    }

    public static class CommitSerializer implements IVersonSerializer<Commit> {

        @Override
        public void serialize(Commit obj, ByteBuf buf) {
            buf.writeInt(obj.traceId);
            buf.writeInt(obj.proposalNumber);
        }

        @Override
        public Commit deserialize(ByteBuf buf) {
            return new Commit(buf.readInt(), buf.readInt());
        }
    }
}