package com.yiling.bi.resource.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2022/9/9
 */
@Data
@Accessors(chain = true)
@TableName("upload_resource_log")
public class UploadResourceLogDO{

    private static final long serialVersionUID = 1L;

    private String flowId;

    private String  dyear;//年份

    private String  status;// '数据状态:0 默认状态 1进入审批流  2 退回调整  3解析中 4写入成功',

    private String  lastName;//'最后修改人',

    private Date lastTime;//'最后修改时间',

    private String fileName;//文件名称',

    private String createName;//创建人',

    private Date createTime;//'创建时间',

    private String  id;//'主键id',

    private String province;//'省区',

    private String msg;

    private String dataSource;

    private String createId;

    private String xyType;

    private String qdType;

    private Long excelTaskRecordId;

    private String createRole;
}
