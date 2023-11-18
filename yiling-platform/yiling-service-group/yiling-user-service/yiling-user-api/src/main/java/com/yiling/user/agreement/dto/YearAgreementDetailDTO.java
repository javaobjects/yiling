package com.yiling.user.agreement.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 年度协议详情DTO
 * @author dexi.yao
 * @date 2021-06-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class YearAgreementDetailDTO extends BaseDTO {

	/**
     * 协议客户ID
     */
    private Long secondEid;

	/**
     * 当前企业名称,如果当前是主协议详情则该字段为当前渠道商信息的企业名称,如果为补充协议详情则该字段为顶部乙方相关信息
     */
    private String secondName;

    /**
     * 渠道业务人员数量（乙方）
	 * 业务人员数量,如果当前是主协议详情则该字段为当前渠道商信息的商务人个数,如果为补充协议详情则该字段为顶部乙方业务人员个数
     */
    private Integer secondSalesmanNum;

    /**
     * 渠道类型名称（乙方）
	 * 渠道类型名称,如果当前是主协议详情则该字段为当前渠道商信息的渠道类型名称,如果为补充协议详情则该字段为顶部乙方的渠道类型名称
     */
    private String secondChannelName;


	/**
	 * 协议主体(甲方)
	 * 协议主体名称,如果当前是主协议详情则该字段为协议基本信息的协议主体,如果为补充协议详情则该字段为顶部的甲方名称
	 */
	private String ename;

	/**
	 * 协议主体ID（甲方）
	 */
	private Long eid;

	/**
	 * 协议分类：1-年度协议 2-补充协议
	 */
	private Integer category;

	/**
     * 协议名称
     */
    private String name;

    /**
     * 协议描述
     */
    private String content;

    /**
     * 开始时间
	 * 履约开始时间
     */
    private Date startTime;

    /**
     * 结束时间
	 * 履约结束时间
     */
    private Date endTime;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 补充协议数量
	 */
	private Integer supplementAgreementNum;

}
