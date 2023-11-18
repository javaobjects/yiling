package com.yiling.sjms.flow.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 补传月流向
 * </p>
 *
 * @author gxl
 * @date 2023-03-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class MonthFlowExtFormDTO extends BaseDTO {


    private static final long serialVersionUID = -5831998762896873549L;
    /**
     * 主流程表单id
     */
    private Long formId;

    /**
     * 申诉类型 1 补传月流向
     */
    private Integer appealType;

    /**
     * 申诉金额
     */
    private BigDecimal appealAmount;



    /**
     * 附件
     */
    private String appendix;

    /**
     * 申诉描述
     */
    private String appealDescribe;



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
