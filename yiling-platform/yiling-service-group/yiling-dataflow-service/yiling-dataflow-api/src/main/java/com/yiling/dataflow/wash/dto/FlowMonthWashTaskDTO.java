package com.yiling.dataflow.wash.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2023/3/1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class FlowMonthWashTaskDTO extends BaseDTO {

    private static final long serialVersionUID = -5872915463815908081L;

    /**
     * 对应日程表ID
     */
    private Long fmwcId;

    /**
     * 年
     */
    private Integer year;

    /**
     * 月
     */
    private Integer month;

    /**
     * 经销商编码
     */
    private Long crmEnterpriseId;

    /**
     * 经销商eid
     */
    private Long eid;

    /**
     * 经销商名称
     */
    private String name;

    /**
     * 收集方式 1-工具 2-ftp 3-第三方接口 4-以岭平台接口 5-excel导入
     */
    private Integer collectionMethod;

    /**
     * 流向分类 1-正常 2-销量申诉 3-窜货申报
     */
    private Integer flowClassify;

    /**
     * 申诉类型 1-补传月流向、2-调整月流向、3-代表终端对应错误、4-终端类型申诉、5-其他
     */
    private Integer appealType;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 数量
     */
    private Integer count;

    /**
     * 清洗状态 1-未清洗 2-清洗中 3-已完成
     */
    private Integer washStatus;

    /**
     * 未匹配商品数量
     */
    private Integer unGoodsMappingCount;

    /**
     * 未匹配客户数量
     */
    private Integer unCustomerMappingCount;

    /**
     * 未匹配供应商数量
     */
    private Integer unSupplierMappingCount;

    /**
     * 区间外数量
     */
    private Integer flowOutCount;

    /**
     * 疑似重复数量
     */
    private Integer repeatCount;

    /**
     * 流向确认状态 0-未确认 1-已确认
     */
    private Integer confirmStatus;

    /**
     * 流向收集时间
     */
    private Date collectTime;

    /**
     * 清洗完成时间
     */
    private Date completeTime;

    /**
     * 报表生成MQ是否已消费 0-否 1-是
     */
    private Integer reportConsumeStatus;

    /**
     * 流向类型 1-采购 2-销售 3-库存
     */
    private Integer flowType;

    /**
     * 成功数量
     */
    private Integer successNumber;

    /**
     * 失败数量
     */
    private Integer failNumber;

    /**
     * 所属省份编码
     */
    private String provinceCode;

    /**
     * 所属省份名称
     */
    private String provinceName;

    /**
     * 所属城市编码
     */
    private String cityCode;

    /**
     * 所属城市名称
     */
    private String cityName;

    /**
     * 所属区域编码
     */
    private String regionCode;

    /**
     * 所属区域名称
     */
    private String regionName;

    /**
     * 是否删除：0-否 1-是
     */
    private Integer delFlag;

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
