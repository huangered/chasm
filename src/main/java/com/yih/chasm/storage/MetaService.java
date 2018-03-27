package com.yih.chasm.storage;

public class MetaService {

    public MetaService instance() {
        return new MetaService();
    }

    public void set(String key, String metaRow) {

    }

    public MetaRow get(String key) {
        return new MetaRow();
    }

    private void persist(String key, String metaRow) {

    }
}
