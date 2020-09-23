package com.k.CA;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description TODO
 * @Author 王一鸣
 * @Date 2020/7/20 16:20
 **/
@Controller
@RequestMapping("request")
public class RequestController {

    @RequestMapping("SAMLResponse")
    @ResponseBody
    public String getSAMLResponse() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String SAMLResponse = (String) request.getAttribute("SAMLResponse");
        System.out.println(SAMLResponse);
        return "SAMLResponse:" + SAMLResponse;
    }

    @RequestMapping("login")
    public String login() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String SAMLResponse = (String) request.getAttribute("SAMLResponse");
        System.out.println("SAMLResponse:" + SAMLResponse);
        return "login";
    }

    @Bean
    public InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/jsp/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }


}
