package com.yiling.dataflow.wash.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2023/5/29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UnlockCustomerMatchingInfoDTO extends BaseDTO {


    private static final long serialVersionUID = -4301586715108073230L;
    /**
     * 任务id
     */
    private Long taskId;

    /**
     * 客户原始名称
     */
    private String customerName;

    /**
     * 推荐客户crm_id
     */
    private Long recommendCrmId;

    /**
     * 推荐客户名称
     */
    private String recommendName;

    /**
     * 推荐分数
     */
    private BigDecimal recommendScore;

    /**
     * 是否删除：0-否 1-是
     */
    private Integer delFlag;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;
}
