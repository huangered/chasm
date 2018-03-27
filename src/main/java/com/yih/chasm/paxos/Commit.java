package com.yih.chasm.paxos;

import com.yih.chasm.io.IVersonSerializer;
import io.netty.buffer.ByteBuf;

public class Commit {

    public static final CommitSerializer serializer = new CommitSerializer();

    public static Commit newPrepare() {
        return new Commit();
    }

    public static Commit newPropose() {
        return new Commit();
    }

    public static class CommitSerializer implements IVersonSerializer<Commit> {

        @Override
        public void serialize(Commit obj, ByteBuf buf) {

        }

        @Override
        public Commit deserialize(ByteBuf buf) {
            return null;
        }
    }
}
