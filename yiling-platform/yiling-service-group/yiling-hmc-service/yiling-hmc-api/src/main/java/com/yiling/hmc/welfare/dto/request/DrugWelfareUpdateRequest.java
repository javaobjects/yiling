package com.yiling.hmc.welfare.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: benben.jia
 * @date:  2022-09-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DrugWelfareUpdateRequest extends BaseRequest {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 活动名称
     */
    private String name;

    /**
     * 药品规格id
     */
    private Long sellSpecificationsId;


    /**
     * 福利券包
     */
    private List<DrugWelfareCouponUpdateRequest> drugWelfareCouponList;

    /**
     * 开始时间
     */
    private Date beginTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 活动状态：1-启用 2-停用
     */
    private Integer status;

    /**
     * 药品福利类型 1-通心络
     */
    private Integer drugWelfareType;

    /**
     * 备注
     */
    private String remark;
}
