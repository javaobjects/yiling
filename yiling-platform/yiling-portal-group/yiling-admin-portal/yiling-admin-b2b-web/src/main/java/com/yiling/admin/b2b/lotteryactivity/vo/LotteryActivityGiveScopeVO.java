package com.yiling.admin.b2b.lotteryactivity.vo;

import java.util.List;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 保存抽奖活动赠送范围（B端使用） Form
 * </p>
 *
 * @author lun.yu
 * @date 2022-09-13
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class LotteryActivityGiveScopeVO extends BaseForm {

    /**
     * 每人赠送次数
     */
    @ApiModelProperty("每人赠送次数")
    private Integer giveTimes;

    /**
     * 重复执行：1-关闭 2-每天重复执行
     */
    @ApiModelProperty("重复执行：1-关闭 2-每天重复执行")
    private Integer loopGive;

    /**
     * 赠送范围：1-全部客户 2-指定客户 3-指定范围客户
     */
    @ApiModelProperty("赠送范围：1-全部客户 2-指定客户 3-指定范围客户（见字典：lottery_activity_give_scope）")
    private Integer giveScope;

    /**
     * 指定客户数量
     */
    @ApiModelProperty("指定客户数量")
    private Integer eidListCount;

    /**
     * 指定范围客户企业类型：1-全部类型 2-指定类型
     */
    @ApiModelProperty("指定范围客户企业类型：1-全部类型 2-指定类型（见字典：lottery_activity_give_enterprise_type）")
    private Integer giveEnterpriseType;

    /**
     * 指定范围企业类型集合
     */
    @ApiModelProperty("指定范围企业类型集合（企业类型：3-连锁总店 4-连锁直营 5-连锁加盟 6-单体药房 7-医院 8-诊所，参考企业类型的字典即可）")
    private List<Integer> enterpriseTypeList;

    /**
     * 指定范围客户用户类型：1-全部用户 2-普通用户 3-全部会员 4-指定方案会员 5-指定推广方会员
     */
    @ApiModelProperty("指定范围客户用户类型：1-全部用户 2-普通用户 3-全部会员 4-指定方案会员 5-指定推广方会员（见字典：lottery_activity_give_user_type）")
    private Integer giveUserType;

    /**
     * 指定方案会员数量
     */
    @ApiModelProperty("指定方案会员数量")
    private Integer memberIdListCount;

    /**
     * 指定推广方会员数量
     */
    @ApiModelProperty("指定推广方会员数量")
    private Integer promoterIdListCount;

}
