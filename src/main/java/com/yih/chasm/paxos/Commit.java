package com.yih.chasm.paxos;

import com.yih.chasm.io.IVersonSerializer;
import io.netty.buffer.ByteBuf;
import lombok.Data;

@Data
public class Commit {

    private int id;

    public Commit(int id){
        this.id = id;
    }

    public static final CommitSerializer serializer = new CommitSerializer();

    public static Commit newPrepare(int id) {
        return new Commit(id);
    }

    public static Commit newPropose(int id) {
        return new Commit(id);
    }

    public static class CommitSerializer implements IVersonSerializer<Commit> {

        @Override
        public void serialize(Commit obj, ByteBuf buf) {
buf.writeInt(obj.getId());
        }

        @Override
        public Commit deserialize(ByteBuf buf) {
            return new Commit(buf.readInt());
        }
    }
}
