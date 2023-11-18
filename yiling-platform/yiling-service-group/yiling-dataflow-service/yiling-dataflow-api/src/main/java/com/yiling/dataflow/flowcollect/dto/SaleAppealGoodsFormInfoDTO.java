package com.yiling.dataflow.flowcollect.dto;

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
 * 销量申诉确认信息
 * @author: xinxuan.jia
 * @date: 2023/7/7
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaleAppealGoodsFormInfoDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 上传记录ID
     */
    private Long recordId;

    /**
     * 任务ID
     */
    private Long taskId;

    /**
     * 销售日期
     */
    private Date soTime;

    /**
     * 经销商名称
     */
    private String name;

    /**
     * 客户名称
     */
    private String enterpriseName;

    /**
     * 原始产品名称
     */
    private String goodsName;

    /**
     * 原始产品规格
     */
    private String soSpecifications;

    /**
     * 数量（盒）
     */
    private BigDecimal soQuantity;

    /**
     * 单位
     */
    private String soUnit;

    /**
     * 单价
     */
    private BigDecimal soPrice;

    /**
     * 金额
     */
    private BigDecimal soTotalAmount;

    /**
     * 业务代表工号
     */
    private String representativeCode;

    /**
     * 业务代表姓名
     */
    private String representativeName;

    /**
     * 业务部门
     */
    private String deptName;

    /**
     * 备注
     */
    private String notes;

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


    /**
     * 主流程表单id
     */
    private Long formId;

    /**
     * 窜货申报表单id
     */
    private Long saleAppealFormId;

    /**
     * 备注
     */
    private String businessRemark;

    /**
     * 是否删除：0-否 1-是
     */
    private Integer delFlag;

    /**
     * 客户供应链角色(终端类型)：1-商业公司 2-医疗机构 3-零售机构。数据字典：crm_supply_chain_role
     */
    private Integer customerSupplyChainRole;
    /**
     * 源流向key
     */
    private String originFlowKey;

    /**
     * 选择申述流向数据id
     */
    private Long selectFlowId;

    /**
     * 传输类型：1、上传excel; 2、选择流向
     */
    private Integer transferType;


}
