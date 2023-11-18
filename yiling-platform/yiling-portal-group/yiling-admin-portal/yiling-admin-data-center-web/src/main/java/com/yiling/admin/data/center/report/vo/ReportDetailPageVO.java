package com.yiling.admin.data.center.report.vo;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2021/9/14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel("总表详情VO")
public class ReportDetailPageVO<T> extends Page<T> {

    /**
     * 报表id
     */
    @ApiModelProperty("报表id")
    private Long reportId;

    /**
     * 报表类型：1-B2B返利 2-流向返利
     */
    @ApiModelProperty("报表类型：1-B2B返利 2-流向返利")
    private Integer type;

    /**
     * eid
     */
    @JsonIgnore
    @ApiModelProperty(value = "eid",hidden = true)
    private Long eid;

    /**
     * eid
     */
    @ApiModelProperty("企业名称")
    private String eName;

    /**
     * 报表状态：1-待运营确认 2-待财务确认 3-财务已确认 4-运营驳回 5-财务驳回 6-管理员驳回
     */
    @ApiModelProperty("报表状态：1-待运营确认 2-待财务确认 3-财务已确认 4-运营驳回 5-财务驳回 6-管理员驳回")
    private Integer status;

	/**
	 * 操作时间
	 */
	@ApiModelProperty("操作时间")
	private Date updateTime;

    /**
     * 结算单号
     */
    @ApiModelProperty(value = "订单数")
    private Long orderCount;

    /**
     * 商品数
     */
    @ApiModelProperty(value = "商品数")
    private Long goodsCount;

	/**
	 * 日志列表
	 */
	@ApiModelProperty("日志列表")
	private List<LogVO> logList;


    @Data
    public static class LogVO{

        /**
         * 日志类型：1-提交返利 2-运营确认 3-运营驳回 4-财务确认 5-财务驳回 6-调整金额 7-修改B2B订单标识 8-修改流向订单标识 9-管理员驳回 10-报表返利
         */
        @ApiModelProperty("日志类型：1-提交返利 2-运营确认 3-运营驳回 4-财务确认 5-财务驳回 6-调整金额 7-修改B2B订单标识 8-修改流向订单标识 9-管理员驳回 10-报表返利")
        private Integer type;

        /**
         * 操作值
         */
        @ApiModelProperty("操作值")
        private String opValue;

        /**
         * 操作备注
         */
        @ApiModelProperty("操作备注")
        private String opRemark;

        /**
         * 创建人
         */
        @JsonIgnore
        @ApiModelProperty(value = "创建人",hidden = true)
        private Long createUser;

        /**
         * 创建人
         */
        @ApiModelProperty("创建人")
        private String createUserName;

        /**
         * 创建时间
         */
        @ApiModelProperty("创建时间")
        private Date createTime;

    }

}
