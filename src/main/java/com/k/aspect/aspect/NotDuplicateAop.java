package com.k.aspect.aspect;

import org.apache.activemq.transport.LogWriter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * @Description TODO
 * @Author 王一鸣
 * @Date 2020/5/27 13:58
 **/
@Aspect
@Component
public class NotDuplicateAop {
    private static final Set<String> KEY =  new ConcurrentSkipListSet<>();


    @Pointcut("@annotation(com.k.aspect.annotation.NotDuplicate)")
    public void duplicate() {
        System.out.println("duplicate");
    }

    /**
     * 对方法拦截后进行参数验证
     * @param pjp
     * @return
     * @throws Throwable
     */
    @Around("duplicate()")
    public Object duplicate(ProceedingJoinPoint pjp) throws Throwable {
        System.out.println("Around");
        MethodSignature msig = (MethodSignature) pjp.getSignature();
        Method currentMethod = pjp.getTarget().getClass().getMethod(msig.getName(), msig.getParameterTypes());
        //拼接签名
        StringBuilder sb = new StringBuilder(currentMethod.toString());
        Object[] args = pjp.getArgs();
        for (Object object : args) {
            if(object != null){
                sb.append(object.getClass().toString());
                sb.append(object.toString());
            }
        }
        String sign = sb.toString();
        boolean success = KEY.add(sign);
        if(!success){
            System.out.println("该方法正在执行,不能重复请求");
        }else{
            System.out.println("本次请求的sign："+sign);
        }
        try {
            return pjp.proceed();
        } finally {
            KEY.remove(sign);
        }

    }
}
