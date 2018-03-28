package com.yih.chasm.transport;

import io.netty.buffer.ByteBuf;

public abstract class Message {
    public enum Direction {
        REQUEST,
        RESPONSE;
    }

    public enum Type {
        PEQ(0, Direction.REQUEST, RequestMessage.codec),
        REP(1, Direction.RESPONSE, ResponseMessage.codec);

        private static final Type[] opcodeIdx;

        static {
            int maxOpcode = -1;
            for (Type type : Type.values())
                maxOpcode = Math.max(maxOpcode, type.opcode);
            opcodeIdx = new Type[maxOpcode + 1];
            for (Type type : Type.values()) {
                if (opcodeIdx[type.opcode] != null)
                    throw new IllegalStateException("Duplicate opcode");
                opcodeIdx[type.opcode] = type;
            }
        }

        public final int opcode;
        public final Direction direction;
        public final Codec<?> codec;

        Type(int opcode, Direction direction, Codec<?> codec) {
            this.opcode = opcode;
            this.direction = direction;
            this.codec = codec;
        }

        public static Type fromOpcode(int opcode, Direction direction) {
            if (opcode >= opcodeIdx.length) {
            }
//                throw new ProtocolException(String.format("Unknown opcode %d", opcode));
            Type t = opcodeIdx[opcode];
            if (t == null) {
            }
//                throw new ProtocolException(String.format("Unknown opcode %d", opcode));
//            if (t.direction != direction)
//                throw new ProtocolException(String.format("Wrong protocol direction (expected %s, got %s) for opcode %d (%s)",
//                        t.direction,
//                        direction,
//                        opcode,
//                        t));
            return t;
        }
    }


    public static class RequestMessage extends Message {

        static final RequestCodec codec = new RequestCodec();

        static class RequestCodec implements Codec<RequestMessage> {

            @Override
            public RequestMessage decode(ByteBuf body, int version) {
                return null;
            }

            @Override
            public void encode(RequestMessage t, ByteBuf dest, int version) {

            }
        }
    }

    public static class ResponseMessage extends Message {

        static final ResponseCodec codec = new ResponseCodec();

        static class ResponseCodec implements Codec<ResponseMessage> {

            @Override
            public ResponseMessage decode(ByteBuf body, int version) {
                return null;
            }

            @Override
            public void encode(ResponseMessage t, ByteBuf dest, int version) {

            }
        }
    }
}
