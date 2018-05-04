package com.yih.chasm.storage;

import com.yih.chasm.paxos.SuggestionID;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class MetaService {

    final static String name = "storage";
    public static Map<Long, PaxosInstance> valueMap = new HashMap<>();
    private static MetaService service = new MetaService(0);
    private long instance_id;

    public MetaService(long initIid) {
        this.instance_id = initIid;
    }

    public static MetaService instance() {
        return service;
    }

    public static void read() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(name));
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

    public static void write() {
        try {
            BufferedWriter reader = new BufferedWriter(new FileWriter(name));

            for (Map.Entry<Long, PaxosInstance> entry : MetaService.valueMap.entrySet()) {
                String line = entry.getKey() + "," + entry.getValue().getValue();
                reader.write(line);
                reader.write("\r\n");
            }
            reader.flush();
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized PaxosInstance createInstance() {
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
}