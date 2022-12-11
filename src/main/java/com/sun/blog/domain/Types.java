package com.sun.blog.domain;

import com.sun.blog.utils.UuidUtil;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;

@Data
public class Types {
    private String id;
    private String name;
    private int count=1;

    public Types() {
        SimpleDateFormat format=new SimpleDateFormat("yyMMddHHmmss");
        this.id = format.format(new Date());
    }
}
