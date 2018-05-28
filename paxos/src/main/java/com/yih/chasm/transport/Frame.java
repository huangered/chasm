package com.yih.chasm.transport;

import com.yih.chasm.io.IVersonSerializer;
import com.yih.chasm.service.PaxosPhase;
import io.netty.buffer.ByteBuf;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
public class Frame {
    public static final int MinLen = 4 + 4 + 8 + 4;
    public static final FrameSerializer serializer = new FrameSerializer();
    private int version;
    private PaxosPhase phase;
    private long traceId;
    private int length;
    private ByteBuf payload;

    public Frame(int version, int verbCode, int length, ByteBuf payload, long traceId) {
        this.version = version;
        this.length = length;
        this.payload = payload;
        this.phase = PaxosPhase.get(verbCode);
        this.traceId = traceId;
    }

    @Slf4j
    public static class FrameSerializer implements IVersonSerializer<Frame> {

        @Override
        public void serialize(Frame obj, ByteBuf buf) {
            int total = MinLen + obj.length;

            log.debug("Serialize phase: {}, len: {}, total: {}", obj.phase, obj.length, total);

            buf.writeByte(total);
            buf.writeInt(obj.version);
            buf.writeInt(obj.phase.id);
            buf.writeLong(obj.traceId);
            buf.writeInt(obj.length);
            buf.writeBytes(obj.payload);
        }

        @Override
        public Frame deserialize(ByteBuf buf) {
            return null;
        }
    }
}