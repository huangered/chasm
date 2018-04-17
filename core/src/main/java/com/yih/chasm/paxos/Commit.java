package com.yih.chasm.paxos;

import com.yih.chasm.io.IVersonSerializer;
import com.yih.chasm.io.StringSerializer;
import io.netty.buffer.ByteBuf;
import lombok.Data;

@Data
public class Commit {

    public static final CommitSerializer serializer = new CommitSerializer();
    private Long rnd;
    private String value;

    public Commit(Long rnd, String value) {
        this.rnd = rnd;
        this.value = value;
    }

    public static Commit newPrepare(long rnd) {
        return new Commit(rnd, "");
    }

    public static Commit newPropose(long rnd, String value) {
        return new Commit(rnd, value);
    }

    public static class CommitSerializer implements IVersonSerializer<Commit> {

        @Override
        public void serialize(Commit obj, ByteBuf buf) {
            buf.writeLong(obj.rnd);
            new StringSerializer().serialize(obj.value, buf);
        }

        @Override
        public Commit deserialize(ByteBuf buf) {
            return new Commit(buf.readLong(), new StringSerializer().deserialize(buf));
        }
    }
}