package com.yiling.admin.b2b.lotteryactivity.form;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import com.yiling.framework.common.base.form.QueryPageListForm;
import com.yiling.framework.common.base.request.QueryPageListRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 查询抽奖活动分页列表 Form
 * </p>
 *
 * @author lun.yu
 * @date 2022-08-29
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryLotteryActivityPageForm extends QueryPageListForm {

    /**
     * 平台类型：1-B端 2-C端
     */
    @NotNull
    @ApiModelProperty(value = "平台类型：1-B端 2-C端", required = true)
    private Integer platformType;

    /**
     * 抽奖活动ID
     */
    @ApiModelProperty("抽奖活动ID")
    private Long lotteryActivityId;

    /**
     * 活动名称
     */
    @Length(max = 20)
    @ApiModelProperty("活动名称")
    private String activityName;

    /**
     * 活动状态：1-启用 2-停用
     */
    @ApiModelProperty("活动状态：1-启用 2-停用")
    private Integer status;

    /**
     * 活动进度：1-未开始 2-进行中 3-已结束 （动态状态，字典：lottery_activity_progress ，枚举：LotteryActivityProgressEnums）
     */
    @ApiModelProperty("活动进度：1-未开始 2-进行中 3-已结束（见字典：lottery_activity_progress）")
    private Integer progress;


    /**
     * 不等于该活动进度：1-未开始 2-进行中 3-已结束
     */
    @ApiModelProperty("不等于该活动进度：1-未开始 2-进行中 3-已结束")
    private Integer neProgress;

    /**
     * 企业名称
     */
    @ApiModelProperty("企业名称")
    private String ename;

    /**
     * 创建人名称
     */
    @ApiModelProperty("创建人名称")
    private String createUserName;

    /**
     * 创建人手机号
     */
    @ApiModelProperty("创建人手机号")
    private String mobile;

    /**
     * 开始创建时间
     */
    @ApiModelProperty("开始创建时间")
    private Date startCreateTime;

    /**
     * 结束创建时间
     */
    @ApiModelProperty("结束创建时间")
    private Date endCreateTime;

    /**
     * 活动分类：1-平台活动 2-商家活动
     */
    @ApiModelProperty("活动分类：1-平台活动 2-商家活动")
    private Integer category;

    /**
     * 运营备注
     */
    @ApiModelProperty("运营备注")
    private String opRemark;

}
