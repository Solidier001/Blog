package com.sun.blog.domain;

import com.sun.blog.utils.UuidUtil;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;

@Data
public class Tags {
    private String tagId;
    private String name;
    private int count=1;

    public Tags(String name) {
        this.tagId = UuidUtil.getShortUuid();
        this.name=name;
    }
}
