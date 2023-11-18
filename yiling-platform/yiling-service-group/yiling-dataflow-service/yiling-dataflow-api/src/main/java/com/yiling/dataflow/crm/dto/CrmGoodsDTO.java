package com.yiling.dataflow.crm.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;
import com.yiling.framework.common.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * crm商品信息
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-10-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CrmGoodsDTO extends BaseDTO {

    /**
     * crm商品ID
     */
    private Long goodsCode;

    /**
     * crm商品名称
     */
    private String goodsName;

    /**
     * 平台标准库规格ID
     */
    private Long specificationId;


}
