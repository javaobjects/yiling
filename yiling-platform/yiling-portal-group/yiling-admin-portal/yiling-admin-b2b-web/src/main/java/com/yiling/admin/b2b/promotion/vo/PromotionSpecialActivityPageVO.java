package com.yiling.admin.b2b.promotion.vo;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shixing.sun
 * @date: 2021/05/18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PromotionSpecialActivityPageVO extends BaseVO {

    @ApiModelProperty(value = "专场活动名称")
    private String specialActivityName;

    @ApiModelProperty(value = "专场活动开始时间")
    private Date startTime;

    @ApiModelProperty(value = "专场活动开始时间")
    private Date endTime;

    @ApiModelProperty(value = "专场活动创建时间")
    private Date createTime;

    @ApiModelProperty(value = "创建人")
    private Long createUser;

    @ApiModelProperty(value = "创建人姓名")
    private String createrName;

    @ApiModelProperty(value = "创建人手机号")
    private String mobile;

    @ApiModelProperty(value = "活动类型（1-满赠,2-特价,3-秒杀,4-组合包）")
    private Integer type;

    @ApiModelProperty(value = "1启用，2停用，3未开始，4进行中，5已结束")
    private Integer status;

    @ApiModelProperty(value = "活动进度1待开始，2进行中，3已结束")
    private Integer progress;

    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 专场活动关联的企业营销活动信息vo
     */
    private List<PromotionSpecialEnterpriseVO> specialActivityEnterpriseDTOS;
}
