package com.yiling.mall.agreement.dto.request;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import cn.hutool.core.date.DateUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2021/6/3
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ImportTempAgreementRequest extends BaseForm {

	//===========================不带条件================================
	/**
	 * 第三方客户ID（丙方）
	 */
	private Long thirdEid;

	/**
	 * 第三方客户名称（丙方）
	 */
	private String thirdName;

	/**
	 * 第三方客户渠道名称（丙方）
	 */
	private String thirdChannelName;

	/**
	 * 协议编号
	 */
	private String agreementNo;

	/**
	 * 协议名称
	 */
	@NotNull
	private String name;

	/**
	 * 协议描述
	 */
	@NotNull
	private String content;

	/**
	 * 协议类型 1-采购类 2-其他
	 */
	@NotNull
	private Integer type;

	/**
	 * 协议方式：1-双方协议 2-三方协议
	 */
	@NotNull
	private Integer mode;

	/**
	 * 主协议ID
	 */
	@NotNull
	private Long parentId;

	/**
	 * 开始时间
	 */
	@NotNull
	private Date startTime;

	/**
	 * 结束时间
	 */
	@NotNull
	private Date endTime;

	/**
	 * 子类型 1-购进额 2-购进量 3-回款额 4-数据直连 5-破损返利
	 */
	@NotNull
	private Integer childType;

	/**
	 * 返利周期 1-订单立返 2-进入返利池
	 */
	@NotNull
	private Integer rebateCycle;

	/**
	 * 返还形式：1- 票折 2- 现金
	 */
	@NotNull
	private Integer restitutionType;

	/**
	 * 返利类型：1-年度返利 2-临时政策返利
	 */
//	@NotNull
	private Integer rebateType;

	/**
	 * 返还形式：1- 票折 2- 现金
	 */
	@NotNull
	private List<Integer> restitutionTypeValues;


	//===========================带返利条件================================

	/**
	 * 专利类型 0-全部 1-非专利 2-专利
	 */
	private Integer isPatent;

	/**
	 * 条件规则 1-购进总额 2-月度拆解 3-季度拆解 4-梯度 5-时间点
	 */
	private Integer conditionRule;

	/**
	 * 协议返利条件
	 */
	private List<ImportAgreementConditionRequest> agreementConditionList;

	//===========================协议商品列表================================

	/**
	 * 协议商品列表
	 */
	private List<ImportAgreementGoodsRequest> agreementGoodsList;

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
