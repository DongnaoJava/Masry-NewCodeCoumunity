package com.bin.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@Aspect
@Slf4j
public class LogAdvice {

    @Pointcut("execution(public * com.bin.service.impl.*.*(..))")
    public void pointcut() {
    }

    @Before("pointcut()")
    //用户[xxx.xxx.xx]在[yyyy-MM-dd HH:mm:ss]访问了[com.bin.service.Impl.xxx.xxx()].
    public void before(JoinPoint joinPoint) {
        //获取ip地址
        String ip = null;
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            ip = request.getRemoteHost();
        }
        //获取时间
        Format format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = format.format(new Date());
        //获取方法名
        Signature signature = joinPoint.getSignature();
        String target = signature.getDeclaringTypeName() + "." + signature.getName();
        //获取参数
        Object[] args = joinPoint.getArgs();
        String arg;
        if (args == null || args.length == 0)
            arg = "";
        else {
            StringBuilder stringBuilder = new StringBuilder();
            for (Object o : args) {
                if (o == null)
                    stringBuilder.append("Null null,");
                else
                    stringBuilder.append(o.getClass().getSimpleName()).append(" ").append(o).append(",");
            }
            arg = stringBuilder.substring(0, stringBuilder.length() - 1);
        }
        log.info(String.format("用户[%s]在[%S]访问了[%s(%s)].", ip, date, target, arg));
    }

 /*
   @After("pointcut()")
    public void after() {
        log.error("after");
    }

    @AfterReturning("pointcut()")
    public void afterReturning() {
        log.error("afterReturning");
    }

    @AfterThrowing("pointcut()")
    public void afterThrowing() {
        log.error("afterThrowing");
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object object;
        log.error("aroundBefore");
        object = proceedingJoinPoint.proceed();
        log.error("aroundAfter");
        return object;

    }
    */
}

