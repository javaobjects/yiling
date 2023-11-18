package com.yiling.user.agreementv2.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 协议供销商品组表
 * </p>
 *
 * @author lun.yu
 * @date 2022-03-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("agreement_supply_sales_goods_group")
public class AgreementSupplySalesGoodsGroupDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 协议ID
     */
    private Long agreementId;

    /**
     * 控销类型：1-无 2-黑名单 3-白名单
     */
    private Integer controlSaleType;

    /**
     * 出库价含税是否维价：0-否 1-是
     */
    private Integer exitWarehouseTaxPriceFlag;

    /**
     * 零售价含税是否维价：0-否 1-是
     */
    private Integer retailTaxPriceFlag;

    /**
     * 是否删除：0-否 1-是
     */
    @TableLogic
    private Integer delFlag;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    /**
     * 备注
     */
    private String remark;


}
