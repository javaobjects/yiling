package com.yiling.admin.system.system.form;

import java.util.Date;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 系统操作日志 Form
 *
 * @author: lun.yu
 * @date: 2021/11/27
 */
@Data
@ApiModel
@Accessors(chain = true)
public class QuerySysOperLogPageListForm extends QueryPageListForm {

    /**
     * 系统标识
     */
    @ApiModelProperty("系统标识")
    private String systemId;

    /**
     * traceId
     */
    @ApiModelProperty("traceId")
    private String requestId;

    /**
     * 业务类型
     */
    @ApiModelProperty("业务类型")
    private String businessType;

    /**
     * 请求标题
     */
    @ApiModelProperty("请求标题")
    private String title;

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
     * 请求url
     */
    @ApiModelProperty("请求url")
    private String requestUrl;

    /**
     * 响应结果
     */
    @ApiModelProperty("响应结果")
    private String responseData;

    /**
     * 操作错误消息
     */
    @ApiModelProperty("操作错误消息")
    private String errorMsg;

    /**
     * 操作状态：1-正常 2-异常
     */
    @ApiModelProperty("操作状态：1-正常 2-异常")
    private Integer status;

    /**
     * 操作人ID
     */
    @ApiModelProperty("操作人ID")
    private Long operId;

    /**
     * 开始操作时间
     */
    @ApiModelProperty("开始操作时间（精确到秒）")
    private Date startOpTime;

    /**
     * 结束操作时间
     */
    @ApiModelProperty("结束操作时间（精确到秒）")
    private Date endOpTime;

}
