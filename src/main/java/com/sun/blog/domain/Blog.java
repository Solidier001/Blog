package com.sun.blog.domain;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Date;

@Data
public class Blog {


    private Integer id;
    private String blogId;
    private String filePath;
    private String content;
    private String[] lable;
    private String tittle;
    private String[] type;
    private String[] tag;
    private String abstractString;
    private String indexPicture;
    private boolean important;
    private boolean shareinfo;
    private Date publishDate;//一次错误：数据库publishDate字段设为了datatime，导致部分数据查询读出java.time.LocalDateTime,thymeleaf出错，因为thymeleaf datas只能操作java.util.Date对象，因此MySQL应当设为date,读出为java.sql.Date该类继承自java.util.Date；
    private final boolean appreciateOpen=false;
    private final boolean commentOpen=false;

    public void setImportant(String important) {
        if (important == null || important.equals("") || important.equals("null"))
            this.important = false;
        else this.important = true;
    }

    public void setShareinfo(String shareinfo) {
        if (shareinfo == null || shareinfo.equals("") || shareinfo.equals("null"))
            this.shareinfo = false;
        else this.shareinfo = true;
    }

    public String getTypeString() {
        if (type==null||type.length<1)return null;
        else return type[0];
    }
    public String getLableString() {
        if (lable==null||lable.length<1)return null;
        else return lable[0];
    }

    public String getTagString() {
        if (tag==null||tag.length<1)return null;
        else {
            StringBuffer buffer=new StringBuffer(tag[0]);
            for (int i=1;i<tag.length;i++){
                buffer.append(',');
                buffer.append(tag[i]);
            }
            return buffer.toString();
        }
    }
    public void setTypeString(String typeString) {
        if (typeString!=null&&!typeString.equals("")&&!typeString.equals("null"))
            type=typeString.split(",");
    }

    public void setTagString(String tagString) {
        if (tagString!=null&&!tagString.equals("")&&!tagString.equals("null"))
            tag=tagString.split(",");
    }
    public void setLableString(String lableString) {
        if (lableString!=null&&!lableString.equals("")&&!lableString.equals("null"))
            lable=lableString.split(",");
    }


}
