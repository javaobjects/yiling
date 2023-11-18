package com.yiling.goods.standard.dto;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author:wei.wang
 * @date:2021/5/21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class StandardGoodsDosageDTO extends BaseDTO {

    private static final long serialVersionUID = -333712121331608L;

    /**
     * 剂型名称
     */
    private String gdfName;

}
