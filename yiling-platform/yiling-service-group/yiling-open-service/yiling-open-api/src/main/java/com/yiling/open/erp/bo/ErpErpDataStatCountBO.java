package com.yiling.open.erp.bo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2022/1/21
 */
@Data
@Accessors(chain = true)
public class ErpErpDataStatCountBO implements java.io.Serializable {

    private static final long serialVersionUID = -3779246557375289637L;

    /**
     * 父类企业ID
     */
    private Integer  suId;

    /**
     * 新增数量
     */
    private Long addCount;

    /**
     * 修改数量
     */
    private Long updateCount;

    /**
     * 删除数量
     */
    private Long deleteCount;

}
