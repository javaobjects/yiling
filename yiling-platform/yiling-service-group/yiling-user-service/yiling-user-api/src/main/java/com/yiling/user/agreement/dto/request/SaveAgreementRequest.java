package com.yiling.user.agreement.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/6/4
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveAgreementRequest extends BaseRequest {

    /**
     * 年度协议ID
     */
    private Long parentId;

    /**
     * 协议主体ID（甲方）
     */
    private Long eid;

    /**
     * 主体名称
     */
    private String ename;

    /**
     * 协议客户ID（乙方）
     */
    private Long secondEid;

    /**
     * 乙方企业名称
     */
    private String secondName;

    /**
     * 乙方渠道类型名字
     */
    private String secondChannelName;

    /**
     * 第三方客户ID（丙方）
     */
    private Long thirdEid;

    /**
     * 丙方名称
     */
    private String thirdName;

    /**
     * 丙方渠道类型名称
     */
    private String thirdChannelName;

    /**
     * 协议分类：1-年度协议 2-补充协议
     */
    private Integer category;

    /**
     * 协议方式：1-双方协议 2-三方协议
     */
    private Integer mode;

    /**
     * 协议类型 1-采购类 2-其他
     */
    private Integer type;

    /**
     * 子类型 1-购进额 2-购进量 3-回款额 4-数据直连 5-破损返利
     */
    private Integer childType;

    /**
     * 协议编号
     */
    private String agreementNo;

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
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 返利周期 1-订单立返 2-进入返利池
     */
    private Integer rebateCycle;

    /**
     * 专利类型 0-全部 1-非专利 2-专利
     */
    private Integer isPatent;

    /**
     * 返利形式0全部1指定（返利形式集合 1- 票折 2- 现金 3-冲红 4-健康城卡）
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
     * 条件规则 1-购进总额 2-月度拆解 3-季度拆解 4-梯度 5-固定时间
     */
    private Integer conditionRule;

    /**
     * 状态：1-启用 2-停用
     */
    private Integer status;

    /**
     * 停用时间
     */
    private Date stopTime;

    /**
     * 协议商品
     */
    private List<SaveAgreementGoodsRequest> agreementGoodsList;

    /**
     * 返利条件
     */
    private List<SaveAgreementConditionRequest> agreementConditionList;
}
