package com.yiling.sjms.flee.entity;

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
 * 销量申诉选择流向表单
 * @author: xinxuan.jia
 * @date: 2023/6/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sale_appeal_select_flow_form")
public class SaleAppealSelectFlowFormDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 主流程表单id
     */
    private Long formId;

    /**
     * 清洗任务id
     */
    private Long taskId;
    /**
     * 选择申述流向数据id
     */
    private Long selectFlowId;

    /**
     * 流向Key
     */
    private String flowKey;

    /**
     * 表单类型 1销量申诉表，2销量申诉确认表
     */
    private Integer type;
    /**
     * 销售日期
     */
    private Date saleTime;
    /**
     * 商业编码
     */
    private Long crmId;
    /**
     * 商业名称
     */
    private String ename;
    /**
     * 机构名称编码
     */
    private Long customerCrmId;
    /**
     * 机构名称
     */
    private String orgName;
    /**
     * 终端类型：1-经销商 2-终端医院 3-终端药店。
     */
    private Integer customerSupplyChainRole;
    /**
     * 产品编码
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
     * 数量
     */
    private BigDecimal finalQuantity;
    /**
     * 金额/元
     */
    private BigDecimal soTotalAmount;
    /**
     * 单位
     */
    private String soUnit;
    /**
     * 申诉数量
     */
    private BigDecimal appealFinalQuantity;
    /**
     * 申诉类型：4终端类型申诉、5其他 6、漏做客户关系对照 7、未备案商业销售到锁定终端 8、医院分院以总院名头进货
     * 9、医院的院内外药店进货 10、医联体、医共体共用进货名头 11、互联网医院无法体现医院名字 12、药店子公司以总部名头进货 13、产品对照错误
     */
    private Integer appealType;
    /**
     * 根据变化类型变化的机构名称编码、产品名称编码、机构属性编码
     */
    private Long changeCode;
    /**
     * 根据申诉类型变化的机构名称、产品名称、机构属性字段
     */
    private String changeName;
    /**
     * 根据申诉类型变化的变化类型：1、标准机构名称 2、标准产品名称 3、终端类型
     */
    private Integer changeType;

    /**
     * 保存状态：0-选择确认  1-待提交 3、提交审核
     */
    private Integer saveStatus;

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

    /**
     * 产品单价
     */
    private BigDecimal salesPrice;
    /**
     * 传输方式 2、选择流向
     */
    private Integer transferType;

    /**
     * 申诉后产品品规
     */
    private String appealGoodsSpec;

}