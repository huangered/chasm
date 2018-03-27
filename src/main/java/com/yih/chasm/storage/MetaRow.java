package com.yih.chasm.storage;

import lombok.Data;

import java.util.List;

@Data
public class MetaRow {
    private String key;
    private List<MetaColumn> columns;
}
