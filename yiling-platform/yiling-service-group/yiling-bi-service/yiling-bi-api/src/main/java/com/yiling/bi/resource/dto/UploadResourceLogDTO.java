package com.yiling.bi.resource.dto;

import java.util.Date;

import lombok.Data;

/**
 * @author: shuang.zhang
 * @date: 2022/12/30
 */
@Data
public class UploadResourceLogDTO implements java.io.Serializable{

    private static final long serialVersionUID = -3337103042833235608L;

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

    private String createRole;  // 角色 0 省区业务人员 1 零售运营提报

}
