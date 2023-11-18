package com.yiling.admin.hmc.user.form;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.form.QueryPageListForm;

import com.yiling.hmc.wechat.enums.SourceEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询HMC用户分页列表 Request
 *
 * @author xuan.zhou
 * @date 2022/4/22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryHmcUserPageListForm extends QueryPageListForm {


    /**
     * 注册开始时间
     */
    @ApiModelProperty(value = "注册开始时间")
    private Date registBeginTime;

    /**
     * 注册结束时间
     */
    @ApiModelProperty(value = "注册结束时间")
    private Date registEndTime;

    /**
     * 活动id
     */
    @ApiModelProperty(value = "活动id")
    private Long activityId;

    /**
     * 活动名称
     */
    @ApiModelProperty(value = "活动名称")
    private String activityName;

    /**
     * @see SourceEnum
     */
    @ApiModelProperty(value = "注册来源：1-自然流量 2-店员或销售 3-扫药盒二维码 4-医生推荐 5-用户推荐 6- 以岭互联网医院 ")
    private Integer registerSource;

    @ApiModelProperty(value = "医生姓名")
    private String doctorName;

    @ApiModelProperty(value = "医生id")
    private Long doctorId;

}

