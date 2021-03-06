package com.yih.chasm.paxos;

import com.yih.chasm.io.IVersonSerializer;
import com.yih.chasm.io.StringSerializer;
import io.netty.buffer.ByteBuf;
import lombok.Data;

@Data
public class SuggestionID implements Comparable<SuggestionID> {
    public static final SuggestionID.SuggestionIdSerializer serializer = new SuggestionIdSerializer();
    private long propose_id;
    private String from_uid;

    public SuggestionID(long propose_id, String from_uid) {
        this.propose_id = propose_id;
        this.from_uid = from_uid;
    }

    @Override
    public String toString() {
        return String.format("Propose(%d - %s)", propose_id, from_uid);
    }

    @Override
    public int compareTo(SuggestionID o) {
        int cv = Long.compare(propose_id, o.propose_id);
        if (cv != 0) {
            return cv;
        }
        return from_uid.compareTo(o.from_uid);
    }

    public static class SuggestionIdSerializer implements IVersonSerializer<SuggestionID> {

        @Override
        public void serialize(SuggestionID obj, ByteBuf buf) {
            buf.writeLong(obj.propose_id);
            StringSerializer.serializer.serialize(obj.from_uid, buf);
        }

        @Override
        public SuggestionID deserialize(ByteBuf buf) {
            long pid = buf.readLong();
            String from_uid = StringSerializer.serializer.deserialize(buf);
            return new SuggestionID(pid, from_uid);
        }
    }
}