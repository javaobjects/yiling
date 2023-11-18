package com.yiling.framework.common.log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;

/**
 * @author shuan
 */
@Component
@Aspect
public class ErpLogAspect {

    private static final Logger log= LoggerFactory.getLogger("RequestLogger");

    /**
     * 自定义切点
     */
    @Pointcut("@annotation(com.yiling.framework.common.annotations.ErpLogAnnotation)")
    private void pointcut() {
    }

    @Around("pointcut()")
    public Object traceAround(ProceedingJoinPoint joinPoint) {
        ErpLog erpLog = new ErpLog();
        Object[] objects = joinPoint.getArgs();
        long startTime = System.currentTimeMillis();
        if (objects != null && objects.length > 0) {
            for (Object object : objects) {
                if (object instanceof HttpServletRequest) {
                    HttpServletRequest request = (HttpServletRequest) object;
                    erpLog.setTime(DateUtil.formatDateTime(new Date()));
                    erpLog.setIp(request.getRemoteAddr());
                    erpLog.setKey(request.getHeader("app_key"));
                    erpLog.setMethod(request.getHeader("method"));
                    erpLog.setRequesStr(getDataRequest(request));
                }
            }
        }
        Object result = null;
        try {
            result = joinPoint.proceed(objects);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        erpLog.setResponseStr(JSONUtil.toJsonStr(result));
        erpLog.setDisposeTime(System.currentTimeMillis() - startTime);
        log.debug(JSONUtil.toJsonStr(erpLog));
        return result;
    }

    /**
     * 获取所有的请求参数并存放在到Map集合中
     *
     * @param request
     * @return
     */
    public String getDataRequest(HttpServletRequest request) {
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = null;
        try {
            request.setCharacterEncoding("UTF-8");
            reader = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
            String line = null;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            return URLDecoder.decode(buffer.toString(), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != reader) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


    class ErpLog {
        private String key;
        private String ip;
        private String method;
        private String time;
        private String requesStr;
        private String responseStr;
        private long disposeTime;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getRequesStr() {
            return requesStr;
        }

        public void setRequesStr(String requesStr) {
            this.requesStr = requesStr;
        }

        public String getResponseStr() {
            return responseStr;
        }

        public void setResponseStr(String responseStr) {
            this.responseStr = responseStr;
        }

        public long getDisposeTime() {
            return disposeTime;
        }

        public void setDisposeTime(long disposeTime) {
            this.disposeTime = disposeTime;
        }
    }
}
