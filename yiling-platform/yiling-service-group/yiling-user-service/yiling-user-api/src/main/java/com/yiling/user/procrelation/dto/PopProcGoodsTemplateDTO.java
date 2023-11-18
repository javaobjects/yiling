package com.yiling.user.procrelation.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: dexi.yao
 * @date: 2023/6/19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PopProcGoodsTemplateDTO extends BaseDTO {
    private static final long serialVersionUID = -1430253163753171231L;

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

    /**
     * 商品优化折扣，单位为百分比
     */
    private BigDecimal rebate;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 备注
     */
    private String remark;
}
