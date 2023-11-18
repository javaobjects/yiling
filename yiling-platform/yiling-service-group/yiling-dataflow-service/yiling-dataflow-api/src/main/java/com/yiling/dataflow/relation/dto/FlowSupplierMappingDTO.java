package com.yiling.dataflow.relation.dto;

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
 * @date 2022-08-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class FlowSupplierMappingDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 流向数据供应商名称
     */
    private String flowEnterpriseName;

    /**
     * 供应商id
     */
    private Long supplierId;


}
