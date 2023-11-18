package com.yiling.dataflow.wash.dto.request;

import java.math.BigDecimal;
import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2023/5/17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SaveOrUpdateUnlockCollectionDetailRequest extends BaseRequest {

    private static final long serialVersionUID = -3236235219105266877L;


    private Long id;

    /**
     * 标准产品编码
     */
    private Long crmGoodsCode;

    /**
     * 采集价格
     */
    private BigDecimal collectionPrice;

    /**
     * 备注
     */
    private String remark;

    /**
     * 设置区域列表
     */
    private List<String> regionCodeList;
}
