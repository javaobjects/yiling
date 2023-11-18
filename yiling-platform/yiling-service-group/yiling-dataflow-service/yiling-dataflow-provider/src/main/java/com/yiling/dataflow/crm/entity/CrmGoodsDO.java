package com.yiling.dataflow.crm.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;
import com.baomidou.mybatisplus.annotation.Version;
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
@TableName("crm_goods")
public class CrmGoodsDO extends BaseDO {

    private static final long serialVersionUID = 1L;

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
