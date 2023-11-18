package com.yiling.export.export.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * @author: shuang.zhang
 * @date: 2021/5/17
 */
@Data
public class ExportTaskRecordDTO implements Serializable {

    private static final long serialVersionUID = 6151776504405188746L;

    /**
     * id
     */
    private Long id;

    /**
     * 所属组名
     */
    private String groupName;

    /**
     * 调用菜单路径
     */
    private String menuName;

    /**
     * 实现BaseExportQueryDataService接口的类名
     */
    private String className;

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
     * 生成文件时间
     */
    private Date finishTime;

    /**
     * 查询条件对象列表
     */
    private List<ExportSearchConditionDTO> searchConditionList;

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
}
