package com.k.aspect.controller;

import com.k.aspect.annotation.NotDuplicate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description TODO
 * @Author 王一鸣
 * @Date 2020/5/27 14:05
 **/
@Controller
@RequestMapping("test-anno")
public class TestController {
    @ResponseBody
    @RequestMapping("test")
    @NotDuplicate
    public String test() {
        return "123";
    }
}
