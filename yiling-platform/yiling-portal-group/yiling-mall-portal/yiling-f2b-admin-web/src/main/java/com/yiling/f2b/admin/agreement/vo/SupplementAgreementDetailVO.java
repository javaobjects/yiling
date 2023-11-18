package com.yiling.f2b.admin.agreement.vo;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseVO;
import com.yiling.user.agreement.enums.AgreementModeEnum;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 补充协议详情
 * @author dexi.yao
 * @date 2021-06-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SupplementAgreementDetailVO extends BaseVO {


	/**
	 * 年度协议名称
	 */
	@ApiModelProperty(value = "年度协议名称")
	private String parentName;

	/**
	 * 年度协议ID
	 */
	@ApiModelProperty(value = "年度协议ID")
	private Long parentId;

	/**
	 * 协议客户ID
	 */
	@ApiModelProperty(value = "协议客户ID")
	private Long secondEid;

	/**
	 * 协议方式：1-双方协议 2-三方协议
	 */
	@ApiModelProperty(value = "协议方式：1-双方协议 2-三方协议")
	private Integer mode;

	/**
	 * 当前企业名称
	 */
	@ApiModelProperty(value = "当前企业名称")
	private String secondName;

	/**
	 * 协议主体(甲方)
	 */
	@ApiModelProperty(value = "协议主体(甲方)")
	private String ename;

	/**
	 * 协议主体ID（甲方）
	 */
	@ApiModelProperty(value = "协议主体ID(甲方)")
	private Long eid;

	/**
	 * 乙方业务人员数量
	 */
	@ApiModelProperty(value = "乙方业务人员数量")
	private Long secondSalesmanNum;

	/**
	 * 乙方渠道类型名称
	 */
	@ApiModelProperty(value = "乙方渠道类型名称")
	private String secondChannelName;

	/**
	 * 第三方客户ID（丙方）
	 */
	private Long thirdEid;

	/**
	 * 丙方企业名称
	 */
	@ApiModelProperty(value = "丙方企业名称")
	private String thirdName;

	/**
	 * 丙方渠道类型名称
	 */
	@ApiModelProperty(value = "丙方渠道类型名称")
	private String thirdChannelName;

	/**
	 * 丙方业务人员数量
	 */
	@ApiModelProperty(value = "丙方业务人员数量")
	private Long thirdSalesmanNum;


	/**
	 * 协议分类：1-年度协议 2-补充协议
	 */
	@ApiModelProperty(value = "协议分类：1-年度协议 2-补充协议")
	private Integer category;

	/**
	 * 协议名称
	 */
	@ApiModelProperty(value = "协议名称")
	private String name;

	/**
	 * 协议描述
	 */
	@ApiModelProperty(value = "协议描述")
	private String content;

	/**
	 * 开始时间
	 * 履约开始时间
	 */
	@ApiModelProperty(value = "履约开始时间")
	private Date startTime;

	/**
	 * 结束时间
	 * 履约结束时间
	 */
	@ApiModelProperty(value = "履约结束时间")
	private Date endTime;

	/**
	 * 协议类型 1-采购类 2-其他
	 */
	@ApiModelProperty(value = "协议类型 1-采购类 2-其他")
	private Integer type;

	/**
	 * 协议子类型 1-购进额 2-购进量 3-回款额 4-数据直连 5-破损返利
	 */
	@ApiModelProperty(value = "协议子类型 1-购进额 2-购进量 3-回款额 4-数据直连 5-破损返利")
	private Integer childType;

	/**
	 * 返利周期 1-订单立返 2-进入返利池
	 */
	@ApiModelProperty(value = "返利周期 1-订单立返 2-进入返利池")
	private Integer rebateCycle;

	/**
	 * 返利形式：1- 票折 2- 现金 3-冲红 4-健康城卡
	 */
	@ApiModelProperty(value = "返利形式：1- 票折 2- 现金 3-冲红 4-健康城卡")
	private Integer restitutionType;

	/**
	 * 返利类型：1-年度返利 2-临时政策返利
	 */
	@ApiModelProperty(value = "返利类型：1-年度返利 2-临时政策返利")
	private Integer rebateType;

    /**
     * 返利形式：1- 票折 2- 现金 3-冲红 4-健康城卡
     */
    @ApiModelProperty(value = "返利形式：1- 票折 2- 现金 3-冲红 4-健康城卡")
    private List<Integer> restitutionTypeValues;

	/**
	 * 协议条件（type=2时该字段为空，，为空则说明没有条件）
	 */
	@ApiModelProperty(value = "协议条件（type=2时该字段为空，，为空则说明没有条件）")
	private AgreementConditionVO agreementsCondition;

	/**
	 * 协议政策列表，因为阶梯可能会有多个政策
	 */
	@ApiModelProperty(value = "协议政策列表，除阶梯可能会有多个政策其他规则类型该集合只有一条记录")
	private List<AgreementPolicyVO> policys;

	public Long getSecondEid() {
		if(AgreementModeEnum.SECOND_AGREEMENTS.getCode().equals(mode)){
			return thirdEid;
		}else {
			return secondEid;
		}
	}

	public String getSecondName() {
		if(AgreementModeEnum.SECOND_AGREEMENTS.getCode().equals(mode)){
			return thirdName;
		}else {
			return secondName;
		}
	}

	public Long getSecondSalesmanNum() {
		if(AgreementModeEnum.SECOND_AGREEMENTS.getCode().equals(mode)){
			return thirdSalesmanNum;
		}else {
			return secondSalesmanNum;
		}
	}

	public String getSecondChannelName() {
		if(AgreementModeEnum.SECOND_AGREEMENTS.getCode().equals(mode)){
			return thirdChannelName;
		}else {
			return secondChannelName;
		}
	}
}
