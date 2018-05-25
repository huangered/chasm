package com.yih.chasm.sql;

public class SimpleSql implements SqlHandler {
    @Override
    public void handle(String command) {
        String[] dd = command.split(" ");
        if (dd[0].equalsIgnoreCase("set")) {
            String key = dd[1];
            String value = dd[2];

            ContentService.instance().put(key, value);
        }
    }
}