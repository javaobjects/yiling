package com.yiling.dataflow.wash.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author shuang.zhang
 * @date 2023-04-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("unlock_sale_rule")
public class UnlockSaleRuleDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 规则编码
     */
    private String code;
    /**
     * 规则名称
     */
    private String name;

    /**
     * 顺序
     */
    private Integer sort;

    /**
     * 来源：1-手动设置2-小三批备案3-区域备案4-集采明细
     */
    private Integer source;

    /**
     * 判断：1-不计入2-公司发货不计入3-漏做客户关系对照商业客户4-不参与计算品种5-当月非锁销量6-商业客户数据计入营销中心7-政府机构
     */
    private Integer judgment;

    /**
     * 商业公司范围：0-全部1-指定商业公司
     */
    private Integer businessRange;

    /**
     * 客户公司范围：0-全部1-指定商业公司2-指定范围
     */
    private Integer customerRange;

    /**
     * 商品范围：0-全部1-指定商品2-指定标签3-指定品规
     */
    private Integer goodsRange;

    /**
     * 销量计入规则：0-指定部门1-指定部门+省区2-商业公司三者关系3-商业公司负责人
     */
    private Integer saleRange;

    /**
     * 非锁分配备注
     */
    private String notes;

    /**
     * 状态：0-开启1-关闭
     */
    private Integer status;

    /**
     * 是否系统内置：0-不是1-系统内置
     */
    private Integer isSystem;

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
