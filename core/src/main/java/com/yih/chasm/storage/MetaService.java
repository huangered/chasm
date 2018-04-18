package com.yih.chasm.storage;

import lombok.Data;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class MetaService {

    final static String name = "storage";

    public static Map<Long, Value> valueMap = new HashMap<>();

    private static MetaService service = new MetaService();

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
                MetaService.valueMap.put(id, new Value(array[1]));
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

            for (Map.Entry<Long, Value> entry : MetaService.valueMap.entrySet()) {
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

    public boolean hasInstance(Long rnd) {
        return valueMap.containsKey(rnd);
    }

    public void createInstance(Long rnd) {
        Value v = new Value("");
        valueMap.put(rnd, v);
    }

    public Value getByInstance(Long rnd) {
        if (!hasInstance(rnd)) {
            createInstance(rnd);
        }
        return valueMap.get(rnd);
    }

    @Data
    public static class Value {
        private String value = "";

        public Value(String value) {
            this.value = value;
        }
    }
}