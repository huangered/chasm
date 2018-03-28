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

        final int op;
        final Direction direction;
        final Codec<?> codec;
        Type(int op, Direction direction, Codec<?> codec) {
            this.op = op;
            this.direction = direction;
            this.codec = codec;
        }
    }



    public static class RequestMessage extends Message {

        static final  RequestCodec codec = new RequestCodec();
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

        static final  ResponseCodec codec = new ResponseCodec();
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
