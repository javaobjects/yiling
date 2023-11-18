package com.yiling.marketing.goodsgift.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>
 * 赠品信息表
 * </p>
 *
 * @author houjie.sun
 * @date 2021-10-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("marketing_goods_gift")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GoodsGiftDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 企业ID
     */
    private Long eid;

    /**
     * 企业名称
     */
    private String ename;

    /**
     * 类型（1-平台；2-商家）
     */
    private Integer sponsorType;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品类别（1-真实物品；2-虚拟物品；3-优惠券；4-会员）
     */
    private Integer goodsType;

    /**
     * 所属业务（1-全部；2-2b；3-2c
     */
    private Integer businessType;

    /**
     * 商品数量
     */
    private Long quantity;

    /**
     * 商品安全库存数量
     */
    private Long safeQuantity;

    /**
     * 使用商品数量
     */
    private Long useQuantity;

    /**
     * 可用商品数量
     */
    private Long availableQuantity;

    /**
     * 商品规格
     */
    private String specifications;

    /**
     * 单位
     */
    private String unit;

    /**
     * 商品品牌
     */
    private String brand;

    /**
     * 商品单价
     */
    private BigDecimal price;

    /**
     * 商品图片
     */
    private String pictureUrl;

    /**
     * 卡号
     */
    private String cardNo;

    /**
     * 密码
     */
    private String password;

    /**
     * 说明内容
     */
    private String introduction;

    /**
     * 是否删除：0-否 1-是
     */
    @TableLogic
    private Integer delFlag;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;


}
