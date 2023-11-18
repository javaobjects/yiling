package com.yiling.user.agreement.dto;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 补充协议商品详情
 * @author dexi.yao
 * @date 2021-06-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SupplementAgreementDetailDTO extends BaseDTO {

    /**
     * 协议版本号
     */
    private Integer version;

	/**
	 * 年度协议ID
	 */
	private Long parentId;

    /**
     * 甲方
     */
    private Long eid;

	/**
	 * 乙方协议客户ID
	 */
	private Long secondEid;

	/**
	 * 第三方客户ID（丙方）
	 */
	private Long thirdEid;

	/**
	 * 协议方式：1-双方协议 2-三方协议
	 */
	private Integer mode;

	/**
	 * 当前企业名称
	 */
	private String secondName;

	/**
	 * 乙方业务人员数量
	 */
	private Long secondSalesmanNum;

	/**
	 * 乙方渠道类型名称
	 */
	private String secondChannelName;

	/**
	 * 丙方企业名称,只有在mode=2时会有该字段
	 */
	private String thirdName;

	/**
	 * 丙方渠道类型名称,只有在mode=2时会有该字段
	 */
	private String thirdChannelName;

	/**
	 * 丙方业务人员数量,只有在mode=2时会有该字段
	 */
	private Long thirdSalesmanNum;

	/**
	 * 协议主体(甲方)
	 */
	private String ename;

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
	 * 协议类型 1-采购类 2-其他
	 */
	private Integer type;

	/**
	 * 协议子类型 1-购进额 2-购进量 3-回款额 4-数据直连 5-破损返利
	 */
	private Integer childType;

	/**
	 * 返利周期 1-订单立返 2-进入返利池
	 */
	private Integer rebateCycle;

	/**
	 * 专利类型 0-全部 1-非专利 2-专利
	 */
	private Integer isPatent;

	/**
	 * 返利形式：1- 票折 2- 现金 3-冲红 4-健康城卡
	 */
	private Integer restitutionType;

	/**
	 * 返利类型：1-年度返利 2-临时政策返利
	 */
	private Integer rebateType;

    /**
     * 返利形式0全部1指定（返利形式集合 1- 票折 2- 现金 3-冲红 4-健康城卡）
     */
    private List<Integer> restitutionTypeValues;

    /**
     * 条件规则 1-购进总额 2-月度拆解 3-季度拆解 4-梯度
     */
    private Integer conditionRule;

	/**
	 * 协议条件（type=2时该字段为空，，为空则说明没有条件）
	 */
	private List<AgreementConditionDTO> agreementsConditionList;

	/**
	 * 协议政策列表，因为阶梯可能会有多个政策
	 */
	private List<AgreementPolicyDTO> policys;

	private Integer status;

	private Date stopTime;

}
