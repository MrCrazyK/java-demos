package com.k.mongodb;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @Description TODO
 * @Author 王一鸣
 * @Date 2020/5/28 13:35
 **/
@Document(collection = "book")
@Data
public class Book {

    @Id
    private String id;
    private Integer price;
    private String name;
    private String info;
    private String publish;
    private Date createTime;
    private Date updateTime;
}
