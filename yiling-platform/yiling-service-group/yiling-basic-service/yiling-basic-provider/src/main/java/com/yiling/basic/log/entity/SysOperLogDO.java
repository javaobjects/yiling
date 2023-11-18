package com.yiling.basic.log.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * <p>
 * 操作日志记录
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-06-11
 */
@Data
@TableName("sys_oper_log")
public class SysOperLogDO implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 系统标识
     */
    private String systemId;

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 请求标题
     */
    private String title;

    /**
     * 请求ID
     */
    private String requestId;

    /**
     * 请求URL
     */
    private String requestUrl;

    /**
     * 请求方法
     */
    private String requestMethod;

    /**
     * 请求数据
     */
    private String requestData;

    /**
     * 执行方法
     */
    private String classMethod;

    /**
     * 执行时间（毫秒）
     */
    private Long consumeTime;

    /**
     * 响应结果
     */
    private String responseData;

    /**
     * 操作状态：1-正常 2-异常
     */
    private Integer status;

    /**
     * 操作错误消息
     */
    private String errorMsg;

    /**
     * 操作人ID
     */
    private Long operId;

    /**
     * 操作人IP
     */
    private String operIp;

    /**
     * 操作时间
     */
    private Date opTime;


}
