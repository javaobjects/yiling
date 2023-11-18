package com.yiling.export.export.dto.request;

import java.util.List;

import com.yiling.export.export.dto.ExportSearchConditionDTO;
import com.yiling.framework.common.base.request.BaseRequest;

import io.swagger.annotations.ApiModelProperty;
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
public class SaveExportTaskRequest extends BaseRequest {

    private static final long serialVersionUID = -3337103042833235608L;
    /**
     * ID
     */
    private Long id;

    /**
     * 操作企业Eid
     */
    private Long eid;

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
     * 下载文件的名称
     */
    private String fileName;

    /**
     * 操作平台：1-运营后台 2-商家后台 3-帆软系统前台 4-数据洞察系统。枚举类：ExcelSourceEnum
     */
    private Integer source;



    /**
     * 查询条件对象列表
     */
    private List<ExportSearchConditionDTO> searchConditionList;
}
