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
 * 区域备案
 * </p>
 *
 * @author fucheng.bai
 * @date 2023-05-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("unlock_area_record")
public class UnlockAreaRecordDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 非锁客户分类：1-零售机构 2-商业公司 3-医疗机构 4-政府机构
     */
    private Integer customerClassification;

    /**
     * 品种id
     */
    private Long categoryId;

    /**
     * 品种名字
     */
    private String categoryName;

    /**
     * 销量计入类型：1-销量计入主管 2-销量计入代表
     */
    private Integer type;

    /**
     * 代表岗位代码
     */
    private String representativePostCode;

    /**
     * 代表岗位名称
     */
    private String representativePostName;

    /**
     * 代表工号
     */
    private String representativeCode;

    /**
     * 代表姓名
     */
    private String representativeName;

    /**
     * 主管岗位代码
     */
    private String executivePostCode;

    /**
     * 主管岗位名称
     */
    private String executivePostName;

    /**
     * 主管工号
     */
    private String executiveCode;

    /**
     * 主管姓名
     */
    private String executiveName;

    /**
     * 业务部门
     */
    private String department;

    /**
     * 业务省区
     */
    private String province;

    /**
     * 业务区域
     */
    private String area;

    /**
     * 最后操作时间
     */
    private Date lastOpTime;

    /**
     * 操作人
     */
    private Long lastOpUser;

    /**
     * 是否删除：0-否 1-是
     */
    @TableLogic
    private Integer delFlag;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    /**
     * 备注
     */
    private String remark;


}
