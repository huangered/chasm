package com.yih.chasm.storage;

import com.yih.chasm.io.ISerializer;
import lombok.Data;
import lombok.ToString;

import java.nio.ByteBuffer;

@Data
@ToString
public class StorageData {
    public static ISerializer<StorageData> serializer = new StorageDataSerializer();
    private long id;
    private long timestamp;
    private String value;

    public StorageData() {
    }

    public StorageData(long id, String value) {
        this.id = id;
        this.value = value;
    }

    public static void main(String[] argc) {
        StorageData dat = new StorageData(1, "test");
        dat.timestamp = 2;

        byte[] b = StorageData.serializer.serialize(dat);
        StorageData dat2 = StorageData.serializer.deserialize(b);
        System.out.println(dat2);
    }

    public static class StorageDataSerializer implements ISerializer<StorageData> {


        @Override
        public byte[] serialize(StorageData obj) {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            buffer.putLong(obj.id);
            buffer.putLong(obj.timestamp);
            buffer.putInt(obj.value.length());
            buffer.put(obj.value.getBytes());
            return buffer.array();
        }

        @Override
        public StorageData deserialize(byte[] data) {
            ByteBuffer buffer = ByteBuffer.wrap(data);
            StorageData dat = new StorageData();
            dat.id = buffer.getLong();
            dat.timestamp = buffer.getLong();
            int len = buffer.getInt();
            byte[] dst = new byte[len];
            buffer.get(dst, 0, len);
            dat.value = new String(dst);
            return dat;
        }
    }
}