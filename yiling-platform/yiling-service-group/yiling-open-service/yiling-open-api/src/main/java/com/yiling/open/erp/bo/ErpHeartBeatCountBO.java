package com.yiling.open.erp.bo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2022/1/21
 */
@Data
@Accessors(chain = true)
public class ErpHeartBeatCountBO implements java.io.Serializable{

    private static final long serialVersionUID = 8970745409583212668L;

    /**
     * 父类企业ID
     */
    private Integer  suId;

    /**
     * 统计数量
     */
    private Long heartCount;

}
