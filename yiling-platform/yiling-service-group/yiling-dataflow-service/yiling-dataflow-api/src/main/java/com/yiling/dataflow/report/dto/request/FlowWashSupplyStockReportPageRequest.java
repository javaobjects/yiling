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
public class FlowWashSupplyStockReportPageRequest extends QueryPageListRequest {
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
     * 商品集合list
     */
    private List<Long> goodsCodeList;

    /**
     * 标准商品名称
     */
    private String goodsName;

    /**
     * 客户商业级别 1一级经销商、2二级经销商、3准一级经销商、4连锁商业、5云仓商业、6未分级经销商
     */
    private Integer supplierLevel;

    /**
     * 商业属性 1-城市商业、2-县级商业
     */
    private Integer supplierAttribute;

    /**
     * 是否连锁总部 1是 2否
     */
    private Integer headChainFlag;

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
