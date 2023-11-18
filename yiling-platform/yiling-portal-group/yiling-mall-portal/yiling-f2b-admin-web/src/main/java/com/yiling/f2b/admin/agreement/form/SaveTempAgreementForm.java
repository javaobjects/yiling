package com.yiling.f2b.admin.agreement.form;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.yiling.framework.common.base.form.BaseForm;

import cn.hutool.core.date.DateUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/6/3
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveTempAgreementForm extends BaseForm {

	//===========================不带条件================================
	/**
	 * 第三方客户ID（丙方）
	 */
	@ApiModelProperty(value = "第三方客户ID（丙方）")
	private Long thirdEid;

	/**
	 * 第三方客户名称（丙方）
	 */
	@ApiModelProperty(value = "第三方客户名称（丙方）")
	private String thirdName;

	/**
	 * 第三方客户渠道名称（丙方）
	 */
	@ApiModelProperty(value = "第三方客户渠道名称（丙方）")
	private String thirdChannelName;

	/**
	 * 协议编号
	 */
	@ApiModelProperty(value = "协议编号")
	private String agreementNo;

	/**
	 * 协议名称
	 */
	@NotNull
	@ApiModelProperty(value = "协议名称")
	@Length(max = 50, message = "协议名称长度0-50")
	private String name;

	/**
	 * 协议描述
	 */
	@NotNull
	@ApiModelProperty(value = "协议描述")
	@Length(max = 200, message = "协议描述长度0-200")
	private String content;

	/**
	 * 协议类型 1-采购类 2-其他
	 */
	@NotNull
	@ApiModelProperty(value = "协议类型 1-采购类 2-其他")
	private Integer type;

	/**
	 * 协议方式：1-双方协议 2-三方协议
	 */
	@NotNull
	@ApiModelProperty(value = "协议类型 1-双方协议 2-三方协议")
	private Integer mode;

	/**
	 * 主协议ID
	 */
	@NotNull
	@ApiModelProperty(value = "主协议ID,如果是主协议parentId就是0")
	private Long parentId;

	/**
	 * 开始时间
	 */
	@NotNull
	@ApiModelProperty(value = "开始时间")
	private Date startTime;

	/**
	 * 结束时间
	 */
	@NotNull
	@ApiModelProperty(value = "结束时间")
	private Date endTime;

	/**
	 * 子类型 1-购进额 2-购进量 3-回款额 4-数据直连 5-破损返利
	 */
	@NotNull
	@ApiModelProperty(value = "子类型 1-购进额 2-购进量 3-回款额 4-数据直连 5-破损返利")
	private Integer childType;

	/**
	 * 返利周期 1-订单立返 2-进入返利池
	 */
	@NotNull
	@ApiModelProperty(value = "返利周期 1-订单立返 2-进入返利池")
	private Integer rebateCycle;

	/**
	 * 返还形式：1- 票折 2- 现金
	 */
	@NotNull
	@ApiModelProperty(value = "返利形式集合 1- 票折 2- 现金 3-冲红 4-健康城卡")
	private Integer restitutionType;

	/**
	 * 返利类型：1-年度返利 2-临时政策返利
	 */
//	@NotNull
	@ApiModelProperty(value = "返利类型：1-年度返利 2-临时政策返利")
	private Integer rebateType;

	/**
	 * 返还形式：1- 票折 2- 现金
	 */
	@NotNull
	@ApiModelProperty(value = "返利形式集合 1- 票折 2- 现金 3-冲红 4-健康城卡")
	private List<Integer> restitutionTypeValues;


	//===========================带返利条件================================

	/**
	 * 专利类型 0-全部 1-非专利 2-专利
	 */
	@ApiModelProperty(value = "专利类型 0-全部 1-非专利 2-专利")
	private Integer isPatent;

	/**
	 * 条件规则 1-购进总额 2-月度拆解 3-季度拆解 4-梯度 5-时间点
	 */
	@ApiModelProperty(value = "条件规则 1-购进总额 2-月度拆解 3-季度拆解 4-梯度 5-时间点")
	private Integer conditionRule;

	/**
	 * 协议返利条件
	 */
	@ApiModelProperty(value = "协议返利条件")
	private List<SaveAgreementConditionForm> agreementConditionList;

	//===========================协议商品列表================================

	/**
	 * 协议商品列表
	 */
	@ApiModelProperty(value = "协议商品列表")
	private List<SaveAgreementGoodsForm> agreementGoodsList;

	public Date getStartTime() {
		if (startTime != null) {
			return DateUtil.beginOfDay(startTime);
		}
		return null;
	}

	public Date getEndTime() {
		if (endTime != null) {
			return DateUtil.endOfDay(endTime);
		}
		return null;
	}
}
