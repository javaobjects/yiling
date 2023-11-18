package com.yiling.user.agreement.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2021/7/12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddAgreementGoodsRequest extends BaseRequest {


    /**
     * 乙方eid
     */
    private Long   eid;
    /**
     * 协议id
     */
    private Long   agreementId;
	/**
	 * 协议分类：1-年度协议 2-补充协议
	 */
	private Integer category;
    /**
     * 商品id
     */
    private Long   goodsId;
    /**
     * 商品名称
     */
    private String goodsName;
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
