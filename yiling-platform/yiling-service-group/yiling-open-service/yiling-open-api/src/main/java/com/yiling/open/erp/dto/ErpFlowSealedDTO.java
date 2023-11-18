package com.yiling.open.erp.dto;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 流向封存DTO
 *
 * @author: houjie.sun
 * @date: 2022/4/14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ErpFlowSealedDTO extends BaseDTO {

    /**
     * 商业ID
     */
    private Long eid;

    /**
     * 商业名称
     */
    private String ename;

    /**
     * 流向类型，字典(erp_flow_type)：1-采购流向 2-销售流向 0-全部
     */
    private Integer type;

    /**
     * 封存状态，字典(erp_flow_sealed_status)：1-已解封 2-已封存 0-全部
     */
    private Integer status;

    /**
     * 封存月份
     */
    private String month;

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

}
