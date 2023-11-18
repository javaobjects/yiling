package com.yiling.dataflow.flow.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 FlowEnterpriseGoodsMappingDO
 * @描述 商品对照关系
 * @创建时间 2023/2/27
 * @修改人 shichen
 * @修改时间 2023/2/27
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("flow_enterprise_goods_mapping")
public class FlowEnterpriseGoodsMappingDO extends BaseDO {

    /**
     * 流向原始名称
     */
    private String flowGoodsName;

    /**
     * 流向原始规格
     */
    private String flowSpecification;

    /**
     * 流向原始商品内码
     */
    private String flowGoodsInSn;

    /**
     * 流向原始商品厂家
     */
    private String flowManufacturer;

    /**
     * 流向原始商品单位
     */
    private String flowUnit;
    /**
     * crm商品编码
     */
    private Long crmGoodsCode;

    /**
     * 标准商品名称
     */
    private String goodsName;

    /**
     * 标准商品规格
     */
    private String goodsSpecification;

    /**
     * 经销商名称
     */
    private String enterpriseName;

    /**
     * 经销商编码
     */
    private Long crmEnterpriseId;

    /**
     * 省份代码
     */
    private String provinceCode;

    /**
     * 省份
     */
    private String province;

    /**
     * 转换单位：1-乘 2-除
     */
    private Integer convertUnit;

    /**
     * 转换系数
     */
    private BigDecimal convertNumber;

    /**
     * 最后上传时间
     */
    private Date lastUploadTime;

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
