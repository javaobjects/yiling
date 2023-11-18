package com.yiling.dataflow.statistics.entity;

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
 * 商业公司每天平衡表
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-07-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("flow_balance_statistics")
public class FlowBalanceStatisticsDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 企业ID
     */
    private Long eid;

    private Long crmEnterpriseId;

    private String provinceCode;

    /**
     * 企业名称
     */
    private String ename;

    /**
     * 实施负责人
     */
    private String installEmployee;

    /**
     * 统计时间
     */
    private Date dateTime;

    /**
     * 采购数量
     */
    private Long poQuantity;

    /**
     * 销售数量
     */
    private Long soQuantity;

    /**
     * 库存数量
     */
    private Long gbQuantity;

    private Long poRowNumber;

    private Long soRowNumber;

    private Long gbRowNumber;

    private Date collectTime;

    /**
     * 上一次库存数量
     */
    private Long lastGbQuantity;

    /**
     * 相差数量=(上一天库存数量+采购数量-当天库存数量-销售数量)
     */
    private Long differQuantity;

    /**
     * 对接方式：1-工具 2-ftp 3-第三方接口 4-以岭平台接口。字典：erp_client_flow_mode
     */
    private Integer flowMode;

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
