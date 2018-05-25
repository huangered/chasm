package com.yih.chasm.sql;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ContentService {

    private static ContentService service = new ContentService();

    public static ContentService instance() {
        return service;
    }

    private Map<String, String> data = new HashMap<>();

    public void put(String key, String value) {
        data.put(key, value);
    }

    public void debug() {
//        if (log.isDebugEnabled()) {
            log.info("content: {}", data);
//        }
    }
}