package com.yiling.dataflow.wash.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2023/3/3
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class FlowGoodsBatchDetailWashDTO  extends BaseDTO {

    private static final long serialVersionUID = 8705471915602828265L;
    /**
     * 行业库经销商编码
     */
    private Long crmEnterpriseId;

    /**
     * 月流向清洗任务id
     */
    private Long fmwtId;

    /**
     * 年份
     */
    private Integer year;

    /**
     * 月份
     */
    private Integer month;

    /**
     * 业务主键
     */
    private String flowKey;

    /**
     * 商业代码（商家eid）
     */
    private Long eid;

    /**
     * 商业名称（商家名称）
     */
    private String name;

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
     * 商品批次号
     */
    private String gbBatchNo;

    /**
     * 库存数量
     */
    private BigDecimal gbNumber;

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
     * crm商品编码
     */
    private Long crmGoodsCode;

    /**
     * crm商品名称
     */
    private String crmGoodsName;

    /**
     * crm商品规格
     */
    private String crmGoodsSpecifications;

    /**
     * 换算数量
     */
    private BigDecimal conversionQuantity;

    /**
     * 搜集时间
     */
    private Date collectTime;

    /**
     * 清洗结果：1-未清洗 2-正常 3-疑似重复 4-区间外删除
     */
    private Integer washStatus;

    /**
     * 产品是否对照 0-否 1-是
     */
    private Integer goodsMappingStatus;

    /**
     * 经销商三者关系ID
     */
    private Long enterpriseCersId;

    /**
     * 是否异常 0-否 1-是
     */
    private Integer errorFlag;

    /**
     * 异常信息
     */
    private String errorMsg;

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
