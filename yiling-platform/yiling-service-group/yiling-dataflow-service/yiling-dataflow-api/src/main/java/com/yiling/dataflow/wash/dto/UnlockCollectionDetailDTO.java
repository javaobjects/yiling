package com.yiling.dataflow.wash.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2023/5/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UnlockCollectionDetailDTO extends BaseDTO {

    private static final long serialVersionUID = -3573687449960971553L;

    /**
     * 标准产品id
     */
    private Long crmGoodsId;

    /**
     * 标准产品编码
     */
    private Long crmGoodsCode;

    /**
     * 标准产品名称
     */
    private String crmGoodsName;

    /**
     * 标准产品规格
     */
    private String crmGoodsSpec;

    /**
     * 采集价格
     */
    private BigDecimal collectionPrice;

    /**
     * 品种id
     */
    private Long categoryId;

    /**
     * 品种
     */
    private String varietyType;

    /**
     * 产品分类
     */
    private String goodsGroup;

    /**
     * 状态 0:有效 1无效
     */
    private Integer status;


    /**
     * 最后操作时间
     */
    private Date lastOpTime;

    /**
     * 操作人
     */
    private Long lastOpUser;

    /**
     * 是否删除：0-否 1-是
     */
    private Integer delFlag;

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
