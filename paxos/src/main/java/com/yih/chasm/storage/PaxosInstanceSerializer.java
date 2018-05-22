package com.yih.chasm.storage;

import com.yih.chasm.io.IVersonSerializer;
import com.yih.chasm.io.StringSerializer;
import com.yih.chasm.paxos.PaxosInstance;
import com.yih.chasm.paxos.SuggestionID;
import io.netty.buffer.ByteBuf;

public class PaxosInstanceSerializer implements IVersonSerializer<PaxosInstance> {
    @Override
    public void serialize(PaxosInstance obj, ByteBuf buf) {
        buf.writeLong(obj.getId());
        StringSerializer.serializer.serialize(obj.getValue(), buf);
        SuggestionID.serializer.serialize(obj.getPromised(), buf);
        SuggestionID.serializer.serialize(obj.getAccepted(), buf);
    }

    @Override
    public PaxosInstance deserialize(ByteBuf buf) {
        Long id = buf.readLong();
        String value = StringSerializer.serializer.deserialize(buf);
        SuggestionID promised = SuggestionID.serializer.deserialize(buf);
        SuggestionID accepted = SuggestionID.serializer.deserialize(buf);
        PaxosInstance instance = new PaxosInstance(id, value, promised, accepted);
        return instance;

    }
}