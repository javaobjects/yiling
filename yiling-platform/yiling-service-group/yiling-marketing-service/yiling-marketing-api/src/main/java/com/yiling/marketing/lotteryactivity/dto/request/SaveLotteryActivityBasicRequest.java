package com.yiling.marketing.lotteryactivity.dto.request;

import java.util.Date;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 保存抽奖活动基本信息 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-09-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveLotteryActivityBasicRequest extends BaseRequest {

    /**
     * ID
     */
    private Long id;

    /**
     * 活动名称
     */
    @NotEmpty
    @Length(max = 20)
    private String activityName;

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
     * 活动分类：1-平台活动 2-商家活动
     */
    @NotNull
    @Range(min = 1, max = 2)
    private Integer category;

    /**
     * 运营备注
     */
    @Length(max = 20)
    private String opRemark;

    /**
     * 活动平台：1-B2B 2-健康管理中心公众号 3-健康管理中心患者端 4-以岭互联网医院患者端 5-以岭互联网医院医生端 6-医药代表端 7-店员端
     */
    @NotNull
    private Integer platform;

}
