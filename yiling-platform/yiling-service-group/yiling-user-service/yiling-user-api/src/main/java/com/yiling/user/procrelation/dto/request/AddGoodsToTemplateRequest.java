package com.yiling.user.procrelation.dto.request;

import java.math.BigDecimal;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2023/6/19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddGoodsToTemplateRequest extends BaseRequest {

    /**
     * 模板id
     */
    private Long templateId;

    /**
     * 工业id
     */
    private Long factoryEid;

    /**
     * 商品id
     */
    private Long goodsId;

    /**
     * 标准库ID
     */
    private Long standardId;

    /**
     * 售卖规格ID
     */
    private Long sellSpecificationsId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 标准库商品名称
     */
    private String standardGoodsName;

    /**
     * 标准库批准文号
     */
    private String standardLicenseNo;

    /**
     * 售卖规格
     */
    private String sellSpecifications;

    /**
     * 批准文号
     */
    private String licenseNo;

    /**
     * 专利类型 0-全部 1-非专利 2-专利
     */
    private Integer isPatent;

    /**
     * 使用要求金额
     */
    private BigDecimal requirements;
}
