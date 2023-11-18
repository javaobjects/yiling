package com.yiling.dataflow.order.entity;

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
 * ERP库存汇总同步数据
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-06-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("flow_goods_batch_detail")
public class FlowGoodsBatchDetailDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 商业代码（商家eid）
     */
    private Long eid;

    /**
     * 商业名称（商家名称）
     */
    private String ename;

    /**
     * crm企业ID
     */
    private Long crmEnterpriseId;

    /**
     * 经销商级别
     */
    private Integer supplierLevel;

    private String crmCode;

    private Long crmGoodsCode;

    /**
     * op库主键
     */
    private String gbIdNo;

    /**
     * 药品内码（供应商的ERP的商品主键）
     */
    private String inSn;

    /**
     * 当前库存时间
     */
    private Date gbDetailTime;

    /**
     * 当前库存月份
     */
    private String gbDetailMonth;

    /**
     * 入库时间
     */
    private Date gbTime;

    /**
     * 商品名称
     */
    private String gbName;

    /**
     * 商品规格
     */
    private String gbSpecifications;

    /**
     * 商品+规格id
     */
    private Long specificationId;

    /**
     * 商品批次号
     */
    private String gbBatchNo;

    /**
     * 库存数量
     */
    private BigDecimal gbNumber;

    /**
     * 商品库存
     */
    private BigDecimal totalNumber;

    /**
     * 商品单位
     */
    private String gbUnit;

    /**
     * 生产时间
     */
    private String gbProduceTime;

    /**
     * 有效期
     */
    private String gbEndTime;

    /**
     * 商品生产厂家
     */
    private String gbManufacturer;

    /**
     * 商品批准文号
     */
    private String gbLicense;

    /**
     * 订单来源，字典：erp_goods_batch_flow_source，1-大运河库存 2-非大运河库存
     */
    private String gbSource;

    /**
     * 所属省份编码
     */
    private String provinceCode;

    /**
     * 所属省份名称
     */
    private String provinceName;

    /**
     * 所属城市编码
     */
    private String cityCode;

    /**
     * 所属城市名称
     */
    private String cityName;

    /**
     * 所属区域编码
     */
    private String regionCode;

    /**
     * 所属区域名称
     */
    private String regionName;

    /**
     * 可能错误信息
     */
    private Integer goodsPossibleError;

    private Date collectTime;

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
