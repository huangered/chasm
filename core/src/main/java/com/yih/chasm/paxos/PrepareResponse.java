package com.yih.chasm.paxos;

import com.yih.chasm.io.IVersonSerializer;
import com.yih.chasm.io.StringSerializer;
import io.netty.buffer.ByteBuf;
import lombok.Data;

@Data
public class PrepareResponse {

    public static final PrepareResponseSerializer serializer = new PrepareResponseSerializer();
    private SuggestionID promised;
    private SuggestionID accepted;
    private String value;

    public PrepareResponse() {
    }


    public PrepareResponse(SuggestionID promised) {
        this.promised = promised;
        this.accepted = null;
        this.value = null;
    }

    public PrepareResponse(SuggestionID promised, SuggestionID accepted, String value) {
        this.promised = promised;
        this.accepted = accepted;
        this.value = value;
    }

    public static class PrepareResponseSerializer implements IVersonSerializer<PrepareResponse> {

        @Override
        public void serialize(PrepareResponse obj, ByteBuf buf) {
            SuggestionID.serializer.serialize(obj.promised, buf);
            if (obj.accepted != null) {
                buf.writeBoolean(true);
                SuggestionID.serializer.serialize(obj.accepted, buf);
                StringSerializer.serializer.serialize(obj.value, buf);
            } else {
                buf.writeBoolean(false);
            }
        }

        @Override
        public PrepareResponse deserialize(ByteBuf buf) {
            PrepareResponse pr = new PrepareResponse();
            pr.setPromised(SuggestionID.serializer.deserialize(buf));
            boolean hasAccept = buf.readBoolean();
            if (hasAccept) {
                pr.setAccepted(SuggestionID.serializer.deserialize(buf));
                pr.setValue(StringSerializer.serializer.deserialize(buf));
            }
            return pr;
        }
    }
}