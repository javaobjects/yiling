package com.yiling.export.excel.entity;

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
 * 
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-11-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("excel_task_config")
public class ExcelTaskConfigDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    private String excelCode;

    /**
     * 实现BaseExcelImportService接口的类名
     */
    private String className;

    /**
     * 数据验证处理器beanName
     */
    private String verifyHandlerBeanName;

    /**
     * 数据导入处理器beanName
     */
    private String importDataHandlerBeanName;

    /**
     * 数据导入模型class路径
     */
    private String modelClass;

    /**
     * 所属组名
     */
    private String title;

    /**
     * 菜单地址
     */
    private String menuName;

    /**
     * 模板地址
     */
    private String templateUrl;

    /**
     * 限制类型 以分号隔开
     */
    private String limitType;

    /**
     * 大小限制
     */
    private Integer limitSize;

    /**
     * 行数限制
     */
    private Integer limitNumber;

    /**
     * 描述
     */
    private String excelDescribe;

    @TableField("`source`")
    private Long source;

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
