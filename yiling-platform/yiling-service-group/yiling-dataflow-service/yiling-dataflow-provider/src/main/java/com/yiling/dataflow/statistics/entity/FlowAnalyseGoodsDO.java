package com.yiling.dataflow.statistics.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 商品信息配置表
 * </p>
 *
 * @author handy
 * @date 2023-01-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("flow_analyse_goods")
public class FlowAnalyseGoodsDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 商品crm编码
     */
    private String goodsCode;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品规格ID
     */
    private Long specificationId;


}
