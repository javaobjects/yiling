package com.yiling.user.agreementv2.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 协议时段 VO
 * </p>
 *
 * @author lun.yu
 * @date 2022-03-10
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AgreementRebateTimeSegmentBO extends BaseVO implements Serializable {

    /**
     * 时段类型：1-全时段 2-子时段
     */
    @ApiModelProperty(value = "时段类型：1-全时段 2-子时段")
    private Integer type;

    /**
     * 时段开始时间
     */
    @ApiModelProperty("时段开始时间（时段类型为全时段时不需传入）")
    private Date startTime;

    /**
     * 时段结束时间
     */
    @ApiModelProperty("时段结束时间（时段类型为全时段时不需传入）")
    private Date endTime;

    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Integer sort;

    /**
     * 是否规模返利（ka协议时才存在）
     */
    private Boolean scaleRebateFlag;

    /**
     * 是否基础服务奖励（ka协议时才存在）
     */
    private Boolean basicServiceRewardFlag;

    /**
     * 是否项目服务奖励（ka协议时才存在）
     */
    private Boolean projectServiceRewardFlag;

    /**
     * 全品表单
     */
    private List<AgreementAllProductFormBO> allProductFormList;

    /**
     * 分类表单
     */
    private List<List<AgreementCategoryProductFormBO>> categoryProductFormList;

}
