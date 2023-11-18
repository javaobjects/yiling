package com.yiling.admin.system.system.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 系统操作日志 VO
 *
 * @author: lun.yu
 * @date: 2021/11/27
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SysOperLogVO extends BaseVO {

    /**
     * 系统标识
     */
    @ApiModelProperty("系统标识（查询下拉框该字段取值字典项：log_system_type）")
    private String systemId;

    /**
     * 业务类型
     */
    @ApiModelProperty("业务类型（查询下拉框该字段取值字典项：log_business_type）")
    private String businessType;

    /**
     * 请求标题
     */
    @ApiModelProperty("请求标题")
    private String title;

    /**
     * 请求ID
     */
    @ApiModelProperty("请求ID")
    private String requestId;

    /**
     * 请求URL
     */
    @ApiModelProperty("请求URL")
    private String requestUrl;

    /**
     * 请求方法
     */
    @ApiModelProperty("请求方法")
    private String requestMethod;

    /**
     * 请求数据
     */
    @ApiModelProperty("请求数据")
    private String requestData;

    /**
     * 执行方法
     */
    @ApiModelProperty("执行方法")
    private String classMethod;

    /**
     * 执行时间（毫秒）
     */
    @ApiModelProperty("执行时间（毫秒）")
    private Long consumeTime;

    /**
     * 响应结果
     */
    @ApiModelProperty("响应结果")
    private String responseData;

    /**
     * 操作状态：1-正常 2-异常
     */
    @ApiModelProperty("操作状态：1-正常 2-异常")
    private Integer status;

    /**
     * 操作错误消息
     */
    @ApiModelProperty("操作错误消息")
    private String errorMsg;

    /**
     * 操作人
     */
    @ApiModelProperty("操作人")
    private Long operId;

    /**
     * 操作人名称
     */
    @ApiModelProperty("操作人名称")
    private String operName;

    /**
     * 操作人IP
     */
    @ApiModelProperty("操作人IP")
    private String operIp;

    /**
     * 操作时间
     */
    @ApiModelProperty("操作时间")
    private Date opTime;
}
