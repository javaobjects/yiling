package com.yiling.marketing.lotteryactivity.dto.request;

import java.util.List;

import com.yiling.framework.common.base.form.BaseForm;
import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 保存抽奖活动赠送范围（B端使用） Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-09-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveLotteryActivityGiveScopeRequest extends BaseRequest {

    /**
     * ID
     */
    private Long id;

    /**
     * 抽奖活动ID
     */
    private Long lotteryActivityId;

    /**
     * 每人赠送次数
     */
    private Integer giveTimes;

    /**
     * 重复执行：1-关闭 2-每天重复执行
     */
    private Integer loopGive;

    /**
     * 赠送范围：1-全部客户 2-指定客户 3-指定范围客户
     */
    private Integer giveScope;

    /**
     * 指定范围客户企业类型：1-全部类型 2-指定类型
     */
    private Integer giveEnterpriseType;

    /**
     * 指定范围企业类型集合
     */
    private List<Integer> enterpriseTypeList;

    /**
     * 指定范围客户用户类型：1-全部用户 2-普通用户 3-全部会员 4-指定方案会员 5-指定推广方会员
     */
    private Integer giveUserType;

}
