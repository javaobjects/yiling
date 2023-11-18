package com.yiling.sjms.flee.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: xinxuan.jia
 * @date: 2023/6/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaleAppealSelectFlowFormDTO extends BaseDTO {
    /**
     * 主流程表单id
     */
    private Long formId;
    /**
     * 申诉类型 4终端类型申诉、5其他 6、漏做客户关系对照 7、未备案商业销售到锁定终端 8、医院分院以总院名头进货
     * 9、医院的院内外药店进货 10、医联体、医共体共用进货名头 11、互联网医院无法体现医院名字 12、药店子公司以总部名头进货 13、产品对照错误
     */
    private Integer appealType;
    /**
     * 销售日期字符串
     */
    private Date saleTime;
    /**
     * 销售日期
     */
    private String soTime;
    /**
     * 机构编码
     */
    private Long customerCrmId;

    /**
     * 机构名称
     */
    private String enterpriseName;

    /**
     * 经销商名称编码
     */
    private Long crmId;

    /**
     * 经销商名称（商家名称）
     */
    private String ename;

    /**
     * 客户供应链角色：1-经销商 2-终端医院 3-终端药店。数据字典：erp_crm_supply_chain_role
     */
    private Integer customerSupplyChainRole;

    /**
     * 产品品名编码
     */
    private Long goodsCode;

    /**
     * 产品品名
     */
    private String goodsName;

    /**
     * 产品品规
     */
    private String goodsSpec;

    /**
     * 数量（最终数量）
     */
    private BigDecimal finalQuantity;
    /**
     * 单位（原始单位）
     */
    private String soUnit;

    /**
     * 金额
     */
    private BigDecimal soTotalAmount;

    /**
     * 申诉数量
     */
    private BigDecimal appealFinalQuantity;
    /**
     * 根据变化类型变化的机构名称编码、产品名称编码、机构属性编码
     */
    private Integer changeCode;
    /**
     * 根据申诉类型变化的机构名称、产品名称、机构属性字段
     */
    private String changeName;
    /**
     * 根据申诉类型变化的变化类型：1、标准机构名称 2、标准产品名称 3、机构属性
     */
    private Integer changeType;

    /**
     * 操作人名称
     */
    private String createUser;
    /**
     * 选择申诉流向数据id
     */
    private Long selectFlowId;

    /**
     * 流向Key
     */
    private String flowKey;

}
