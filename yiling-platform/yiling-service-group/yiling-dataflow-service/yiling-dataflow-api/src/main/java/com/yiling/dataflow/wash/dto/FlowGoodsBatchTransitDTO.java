package com.yiling.dataflow.wash.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author houjie.sun
 * @date 2023-03-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class FlowGoodsBatchTransitDTO extends BaseDTO {

    private static final long serialVersionUID = -3942944583499924024L;

    /**
     * 所属月份
     */
    private String gbDetailMonth;

    /**
     * 行业库经销商编码
     */
    private Long crmEnterpriseId;

    /**
     * 经销商名称
     */
    private String name;

    /**
     * 经销商所属省份名称
     */
    private String crmProvinceName;

    /**
     * 经销商所属省份编码
     */
    private String crmProvinceCode;

    /**
     * 行业库经采购渠道机构编码
     */
    private Long supplyCrmEnterpriseId;

    /**
     * 采购渠道机构名称
     */
    private String supplyName;

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
     * 采购时间
     */
    private Date gbPurchaseTime;

    /**
     * 库存类型：1-在途订单库存 2-终端库存
     */
    private Integer goodsBatchType;

    /**
     * 经销商三者关系ID
     */
    private Long enterpriseCersId;

    /**
     * 采购渠道机构三者关系ID
     */
    private Long supplyCersId;

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
