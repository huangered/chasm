package com.yih.chasm.storage;

import com.yih.chasm.io.IVersonSerializer;
import com.yih.chasm.io.StringSerializer;
import io.netty.buffer.ByteBuf;
import lombok.Data;

@Data
public class StorageData {
    private long id;
    private String value;

    public static IVersonSerializer<StorageData> serializer = new StorageDataSerializer();

    public StorageData(){}

    public StorageData(long id, String value) {
        this.id = id;
        this.value = value;
    }

    public static class StorageDataSerializer implements IVersonSerializer<StorageData> {

        @Override
        public void serialize(StorageData obj, ByteBuf buf) {
            buf.writeLong(obj.id);
            StringSerializer.serializer.serialize(obj.value, buf);
        }

        @Override
        public StorageData deserialize(ByteBuf buf) {
            StorageData data = new StorageData();
            data.id = buf.readLong();
            data.value = StringSerializer.serializer.deserialize(buf);
            return data;
        }
    }
}
