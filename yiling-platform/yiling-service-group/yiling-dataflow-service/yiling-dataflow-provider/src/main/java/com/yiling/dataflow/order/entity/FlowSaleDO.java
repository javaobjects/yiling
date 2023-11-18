package com.yiling.dataflow.order.entity;

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
 * <p>
 * 流向销售明细信息表
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-02-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("flow_sale")
public class FlowSaleDO extends BaseDO {

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

    /**
     * op库主键
     */
    private String soId;

    /**
     * Erp销售订单号
     */
    private String soNo;

    /**
     * 客户编码（客户内码）
     */
    private String enterpriseInnerCode;

    /**
     * 客户名称
     */
    private String enterpriseName;

    /**
     * 执业许可证号/社会信用统一代码
     */
    private String licenseNumber;

    /**
     * 销售日期
     */
    private Date soTime;

    /**
     * 下单日期
     */
    private Date orderTime;

    /**
     * 销售月份
     */
    private String soMonth;

    /**
     * 商品内码
     */
    private String goodsInSn;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品规格
     */
    private String soSpecifications;

    /**
     * 商品规格id
     */
    private Long specificationId;

    /**
     * 批号
     */
    private String soBatchNo;

    /**
     * 生产日期
     */
    private Date soProductTime;

    /**
     * 有效期
     */
    private Date soEffectiveTime;

    /**
     * 销售数量
     */
    private BigDecimal soQuantity;

    /**
     * 商品单位
     */
    private String soUnit;

    /**
     * 价格
     */
    private BigDecimal soPrice;

    /**
     * 金额
     */
    private BigDecimal soTotalAmount;

    /**
     * 商品生产厂家
     */
    private String soManufacturer;

    /**
     * 批准文号
     */
    private String soLicense;

    /**
     * 订单来源，1-大运河销售 2-自建平台销售 3-其它渠道销售
     */
    private String soSource;


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
     * 数据标签 0-正常 1-(异常)新增 2-(异常)修改 3-已删除
     */
    private Integer dataTag;

    /**
     * 同步返利状态，0-未同步 1-已同步
     */
    private Integer reportSyncStatus;

    private String crmCode;

    private String enterpriseCrmCode;

    private String  customerTypeName;
    private String   belongDepartment;

    private Integer lockType;
    private Long crmGoodsCode;
    /**
     * 可能错误信息
     */
    private Integer goodsPossibleError;
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
