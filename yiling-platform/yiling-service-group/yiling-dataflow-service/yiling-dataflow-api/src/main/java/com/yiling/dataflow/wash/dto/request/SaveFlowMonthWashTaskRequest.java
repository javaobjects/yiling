package com.yiling.dataflow.wash.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2023/3/6
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SaveFlowMonthWashTaskRequest extends BaseRequest {

    /**
     * 经销商eid
     */
    private Long eid;

    /**
     * 经销商编码
     */
    private Long crmEnterpriseId;

    /**
     * 对应日程表ID
     */
    private Long fmwcId;

    /**
     * 流向分类 1-正常 2-销量申诉 3-窜货申报
     */
    private Integer flowClassify;

    /**
     * 申诉类型 1-补传月流向、2-调整月流向、3-代表终端对应错误、4-终端类型申诉、5-其他
     */
    private Integer appealType;

    /**
     * 流向类型 1-采购 2-销售 3-库存
     */
    private Integer flowType;

    /**
     * 收集方式 1-工具 2-ftp 3-第三方接口 4-以岭平台接口 5-excel导入 6- 系统导入
     */
    private Integer collectionMethod;

    /**
     * 文件名称
     */
    private String fileName;
    /**
     * 数据条目
     */
    private Integer count;
}
