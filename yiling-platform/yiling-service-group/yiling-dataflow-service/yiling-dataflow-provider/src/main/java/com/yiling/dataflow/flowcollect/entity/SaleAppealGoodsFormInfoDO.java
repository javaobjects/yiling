package com.yiling.dataflow.flowcollect.entity;

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
 * 申诉确认以后保存excel文件里面数据
 * </p>
 *
 * @author shixing.sun
 * @date 2023-03-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sale_appeal_goods_form_info")
public class SaleAppealGoodsFormInfoDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 主流程表单id
     */
    private Long formId;

    /**
     * 导入任务id
     */
    private Long recordId;

    /**
     * 导入任务id
     */
    private Long taskId;

    /**
     * 窜货申报表单id
     */
    private Long saleAppealFormId;

    /**
     * 销售时间
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
    private String soQuantity;

    /**
     * 单位
     */
    private String soUnit;

    /**
     * 单价
     */
    private String soPrice;

    /**
     * 金额
     */
    private String soTotalAmount;

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
    private String businessRemark;

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
