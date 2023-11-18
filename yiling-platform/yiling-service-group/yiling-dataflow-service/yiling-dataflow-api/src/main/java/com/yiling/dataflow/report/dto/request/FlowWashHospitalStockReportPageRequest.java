package com.yiling.dataflow.report.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author zhigang.guo
 * @date: 2023/3/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class FlowWashHospitalStockReportPageRequest extends QueryPageListRequest {
    /**
     * 年月
     */
    private String soMonth;

    /**
     *
     * 机构名称
     */
    private String name;

    /**
     * 机构编码
     */
    private Long crmId;

    /**
     * 机构省份
     */
    private String provinceName;

    /**
     * 机构城市
     */
    private String cityName;

    /**
     * 机构区县
     */
    private String regionName;

    /**
     * 机构业务省区
     */
    private String businessProvince;

    /**
     * 机构业务部门
     */
    private String businessDepartment;

    /**
     * 产品(sku)编码
     */
    private Long goodsCode;

    /**
     * 产品编码集合
     */
    private List<Long> goodsCodeList;

    /**
     * 标准商品名称
     */
    private String goodsName;

    /**
     * 国家等级 1三级甲等、2三级乙等、3三级丙等、4二级甲等、5二级乙等、6二级丙等、7一级甲等、8三级特等、9一级乙等、10一级丙等、11其他
     */
    private Integer nationalGrade;

    /**
     * 以岭级别
     */
    private Integer ylLevel;

    /**
     * 页面是否隐藏
     */
    private Integer hideFlag;

    /**
     * 数据权限过滤字段
     */
    private List<Long> crmIdList;
    /**
     * 数据权限过滤字段
     */
    private List<String> provinceNameList;
}
