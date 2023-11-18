package com.yiling.dataflow.wash.dto;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;
import com.yiling.framework.common.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author shuang.zhang
 * @date 2023-05-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UnlockFlowWashTaskDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 对应日程表ID
     */
    private Long fmwcId;

    /**
     * 年
     */
    private Integer year;

    /**
     * 月
     */
    private Integer month;

    /**
     * 经销商编码
     */
    private Long crmEnterpriseId;

    /**
     * 经销商名称
     */
    private String name;

    /**
     * 非锁分类是否已经发送：0未发送1已接收
     */
    private Integer classificationStatus;

    /**
     * 非锁规则是否已经发送：0未发送1已接收
     */
    private Integer ruleStatus;

}
