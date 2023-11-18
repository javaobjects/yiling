package com.yiling.sjms.wash.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author fucheng.bai
 * @date 2023/3/1
 */
@Data
public class FlowMonthWashTaskListVO extends BaseVO {

    /**
     * 年
     */
    @ApiModelProperty(value = "年")
    private Integer year;

    /**
     * 月
     */
    @ApiModelProperty(value = "月")
    private Integer month;

    /**
     * 经销商编码
     */
    @ApiModelProperty(value = "经销商编码")
    private Long crmEnterpriseId;

    /**
     * 经销商名称
     */
    @ApiModelProperty(value = "经销商名称")
    private String name;

    /**
     * 收集方式 1-工具 2-ftp 3-第三方接口 4-以岭接口 5-excel导入
     *
     */
    @ApiModelProperty(value = "收集方式 字典：flow_month_wash_task_collection_method  1-工具 2-ftp 3-第三方接口 4-以岭接口 5-excel导入")
    private Integer collectionMethod;

    /**
     * 流向分类 1-正常 2-销量申诉 3-窜货申报
     */
    @ApiModelProperty(value = "流向分类 字典：flow_month_wash_task_classify 1-正常 2-销量申诉 3-窜货申报")
    private Integer flowClassify;

    /**
     * 文件名称
     */
    @ApiModelProperty(value = "文件名称")
    private String fileName;

    /**
     * 数量
     */
    @ApiModelProperty(value = "数量")
    private Long count;

    /**
     * 清洗状态 0-未清洗 1-清洗中 2-已完成
     */
    @ApiModelProperty(value = "清洗状态 字典：flow_month_wash_task_status 1-未清洗 2-清洗中 3-已完成 4-清洗失败")
    private Integer washStatus;

    /**
     * 未匹配商品数量
     */
    @ApiModelProperty(value = "未匹配商品数量")
    private Long unGoodsMappingCount;

    /**
     * 未匹配客户数量
     */
    @ApiModelProperty(value = "未匹配客户数量")
    private Long unCustomerMappingCount;

    /**
     * 未匹配供应商数量
     */
    @ApiModelProperty(value = "未匹配供应商数量")
    private Long unSupplierMappingCount;

    /**
     * 区间外数量
     */
    @ApiModelProperty(value = "区间外数量")
    private Long flowOutCount;

    /**
     * 疑似重复数量
     */
    @ApiModelProperty(value = "疑似重复数量")
    private Long repeatCount;

    /**
     * 流向确认状态 0-未确认 1-已确认
     */
    @ApiModelProperty(value = "流向确认状态 0-未确认 1-已确认")
    private Integer confirmStatus;

    /**
     * 流向收集时间
     */
    @ApiModelProperty(value = "流向收集时间")
    private Date collectTime;

}
