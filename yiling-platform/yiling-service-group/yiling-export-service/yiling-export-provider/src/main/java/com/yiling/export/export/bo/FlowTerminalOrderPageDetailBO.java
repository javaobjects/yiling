package com.yiling.export.export.bo;

import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: houjie.sun
 * @date: 2023/3/9
 */
@Data
public class FlowTerminalOrderPageDetailBO {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 所属月份
     */
    private String gbDetailMonth;

    /**
     * 终端机构编码
     */
    private Long crmEnterpriseId;

    /**
     * 终端机构
     */
    private String name;

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
     * 操作人
     */
    private String opUser;

    /**
     * 操作时间
     */
    private Date opTime;
}
