package com.yiling.dataflow.wash.entity;

import java.math.BigDecimal;
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
 * 采集明细
 * </p>
 *
 * @author fucheng.bai
 * @date 2023-05-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("unlock_collection_detail")
public class UnlockCollectionDetailDO extends BaseDO {

    private static final long serialVersionUID = 1L;

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
