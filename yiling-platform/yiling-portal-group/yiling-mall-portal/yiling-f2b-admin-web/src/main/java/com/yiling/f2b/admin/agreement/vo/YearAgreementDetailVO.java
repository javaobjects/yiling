package com.yiling.f2b.admin.agreement.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 年度协议详情VO
 * @author dexi.yao
 * @date 2021-06-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel("年度协议详情VO")
public class YearAgreementDetailVO extends BaseVO {


	/**
	 * 协议主体(甲方)
	 */
	@ApiModelProperty("协议主体")
	private String ename;

	/**
	 * 协议主体ID（甲方）
	 */
	@ApiModelProperty(value = "协议主体ID(甲方)")
	private Long eid;

	/**
     * 协议客户ID
     */
	@ApiModelProperty("协议客户ID")
    private Long secondEid;

	/**
     * 当前企业名称
     */
	@ApiModelProperty("当前企业名称")
    private String secondName;

    /**
     * 渠道业务人员数量（乙方）
     */
	@ApiModelProperty("渠道业务人员数量")
    private Long secondSalesmanNum;

    /**
     * 渠道类型名称（乙方）
     */
	@ApiModelProperty("渠道类型名称")
    private String secondChannelName;

	/**
	 * 协议分类：1-年度协议 2-补充协议
	 */
	@ApiModelProperty("协议分类：1-年度协议 2-补充协议")
	private Integer category;

	/**
     * 协议名称
     */
	@ApiModelProperty("协议名称")
    private String name;

    /**
     * 协议描述
     */
	@ApiModelProperty("协议描述")
    private String content;

    /**
	 * 履约开始时间
     */
	@ApiModelProperty("履约开始时间")
    private Date startTime;

    /**
	 * 履约结束时间
     */
	@ApiModelProperty("履约结束时间")
    private Date endTime;

    /**
	 * 签署时间
     */
	@ApiModelProperty("签署时间")
    private Date createTime;

	/**
     * 补充协议数量
     */
	@ApiModelProperty("补充协议数量")
    private Integer supplementAgreementNum;

}
