package com.k.es.entity;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Description TODO
 * @Author 王一鸣
 * @Date 2020/3/17 16:52
 **/
@Data
@AllArgsConstructor
public class ResponseBean {

        //状态码
        private Integer code;
        //返回信息
        private String message;
        //返回的数据
        private Object data;
}
