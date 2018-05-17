package com.yih.chasm.storage;

import com.yih.chasm.paxos.PaxosInstance;
import com.yih.chasm.paxos.SuggestionID;
import com.yih.chasm.util.BufUtil;
import io.netty.buffer.ByteBuf;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.Map;
import java.util.TreeMap;

@Slf4j
public class MetaService {

    private final static String NAME = "storage";
    private static Map<Long, PaxosInstance> valueMap = new TreeMap<>();
    private static MetaService service = new MetaService(0);
    private long instance_id;

    PaxosInstanceSerializer serializer = new PaxosInstanceSerializer();

    public MetaService(long initIid) {
        this.instance_id = initIid;
    }

    public static MetaService instance() {
        return service;
    }

    public void read() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(NAME));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] array = line.split(",", 2);
                Long id = Long.parseLong(array[0]);
                MetaService.valueMap.put(id, new PaxosInstance(id, array[1]));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(PaxosInstance instance) {
        FileOutputStream reader = null;
        ByteBuf buf = BufUtil.createBuf();
        try {
            reader = new FileOutputStream(NAME);

            serializer.serialize(instance, buf);
            byte[] bytes = buf.array();
            reader.write(bytes);
            reader.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
            buf.release();
        }
    }

    public synchronized PaxosInstance createInstance() {
        log.info("Create instance");
        instance_id++;
        PaxosInstance instance = new PaxosInstance(instance_id);
        instance.setPromised(new SuggestionID(-1L, ""));
        valueMap.put(instance_id, instance);
        return instance;
    }

    public PaxosInstance getByInstance(Long iid) {
        return valueMap.get(iid);
    }

    public PaxosInstance currentInstance() {
        PaxosInstance ii = valueMap.get(instance_id);
        if (ii == null) {
            createInstance();
        }
        return valueMap.get(instance_id);
    }

    public void print() {
        log.info("=====");
        for (Map.Entry<Long, PaxosInstance> entry : valueMap.entrySet()) {
            log.info("iid {} value {} accept {}", entry.getKey(), entry.getValue().getValue(), entry.getValue().getAccepted());
        }
    }
}