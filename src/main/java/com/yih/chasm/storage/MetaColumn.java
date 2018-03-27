package com.yih.chasm.storage;

import lombok.Data;

@Data
public class MetaColumn {
    private String name;
    private MetaType type;
    private MetaData data;
}
