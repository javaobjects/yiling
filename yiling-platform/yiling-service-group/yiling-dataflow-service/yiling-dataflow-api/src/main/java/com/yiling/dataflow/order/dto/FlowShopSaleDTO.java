package com.yiling.dataflow.order.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2023/4/10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class FlowShopSaleDTO extends BaseDTO {

    /**
     * 总店商业代码（商家eid）
     */
    private Long eid;

    /**
     * 总店商业名称（商家名称）
     */
    private String ename;

    /**
     * crm机构id
     */
    private Long crmEnterpriseId;

    /**
     * 商业公司crm编码
     */
    private String crmCode;

    /**
     * 门店商业代码（商家eid）
     */
    private Long shopEid;

    /**
     * 门店商业名称（商家名称）
     */
    private String shopEname;

    /**
     * 门店编码
     */
    private String shopNo;

    /**
     * op库主键
     */
    private String soId;

    /**
     * Erp销售订单号
     */
    private String soNo;

    /**
     * 统一信用代码
     */
    private String licenseNumber;

    /**
     * 销售日期
     */
    private Date soTime;

    /**
     * 销售月份
     */
    private String soMonth;

    /**
     * 下单日期
     */
    private Date orderTime;

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
     * 商品+规格id
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
     * crm商品编码
     */
    private Integer crmGoodsCode;

    /**
     * 可能错误0正常1可能错误
     */
    private Integer goodsPossibleError;

    /**
     * 同步返利状态，0-未同步 1-已同步
     */
    private Integer reportSyncStatus;

    /**
     * 客户类型名称
     */
    private String customerTypeName;

    /**
     * 流向数据归属部门
     */
    private String belongDepartment;

    /**
     * 锁定类型：0 未处理， 1 锁定，2未锁定
     */
    private Integer lockType;

    /**
     * 数据标签 0-正常 1-(异常)新增 2-(异常)修改 3-已删除
     */
    private Integer dataTag;

    /**
     * 是否删除：0-否 1-是
     */
    private Integer delFlag;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;
}
