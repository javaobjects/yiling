package com.yiling.user.agreement.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/6/4
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveAgreementGoodsRequest extends BaseRequest {

    /**
     * 商品id
     */
    private Long   goodsId;
    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品标准库
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
     * 标准库Id
     */
    private Long standardId;

    /**
     * 销售规格Id
     */
    private Long sellSpecificationsId;

    /**
     * 专利类型 1-非专利 2-专利
     */
    private Integer isPatent;

}
