package com.yiling.dataflow.crm.bo;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: gxl
 * @date: 2023/5/12
 */
@Data
@Accessors(chain = true)
public class CrmRelationManorBO extends BaseDTO {
    private static final long serialVersionUID = -4546633829414443979L;
    /**
     * 品类ID
     */
    private Long categoryId;

    private String manorNo;
    /**
     * 辖区名称
     */
    private String manorName;

    /**
     * 辖区表Id
     */
    private Long crmManorId;



}