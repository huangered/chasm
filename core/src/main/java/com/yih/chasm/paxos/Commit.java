package com.yih.chasm.paxos;

import com.yih.chasm.io.IVersonSerializer;
import com.yih.chasm.io.StringSerializer;
import io.netty.buffer.ByteBuf;
import lombok.Data;

@Data
public class Commit {

    public static final CommitSerializer serializer = new CommitSerializer();
    private SuggestionID rnd;
    private String value;

    public Commit(SuggestionID propose) {
        this.rnd = propose;
        this.value = "";
    }

    public Commit(SuggestionID rnd, String value) {
        this.rnd = rnd;
        this.value = value;
    }

    public static Commit newPrepare(SuggestionID rnd) {
        return new Commit(rnd);
    }

    public static Commit newPropose(SuggestionID rnd, String value) {
        return new Commit(rnd, value);
    }

    public static class CommitSerializer implements IVersonSerializer<Commit> {

        @Override
        public void serialize(Commit obj, ByteBuf buf) {
            buf.writeLong(obj.rnd.getPropose_id());
            new StringSerializer().serialize(obj.rnd.getFrom_uid(), buf);
            new StringSerializer().serialize(obj.value, buf);
        }

        @Override
        public Commit deserialize(ByteBuf buf) {
            return new Commit(SuggestionID.serializer.deserialize(buf), new StringSerializer().deserialize(buf));
        }
    }
}