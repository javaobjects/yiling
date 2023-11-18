package com.yiling.export.export.entity;

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
 * @author: shuang.zhang
 * @date: 2021/5/17
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("export_task_record")
public class ExportTaskRecordDO extends BaseDO {

    private static final long serialVersionUID = 6151776504405188746L;

    /**
     * 所属组名
     */
    private String groupName;

    /**
     * 实现BaseExportQueryDataService接口的类名
     */
    private String className;

    /**
     * 调用菜单路径
     */
    private String menuName;

    /**
     * 请求地址
     */
    private String requestUrl;

    /**
     * 导出状态状态：0-正在导出；1-导出成功;-1导出失败
     */
    private Integer status;

    /**
     * 下载文件的名称
     */
    private String fileName;

    /**
     * 查询条件json串
     */
    private String searchCondition;

    /**
     * 操作平台:f2b商城;b2b商城;admin后台;
     */
    private Integer source;

    /**
     * 操作ip
     */
    private String exportIp;

    /**
     * 完成时间
     */
    private Date finishTime;

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
