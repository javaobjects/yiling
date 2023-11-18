package com.yiling.dataflow.report.dto.request;

import java.math.BigDecimal;
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
public class FlowWashInventoryReportPageRequest extends QueryPageListRequest {

    /**
     * 年月
     */
    private String soMonth;

    /**
     *
     * 经销商名称
     */
    private String name;

    /**
     * 经销商编码
     */
    private Long crmId;

    /**
     * 机构省份
     */
    private String provinceName;

    /**
     * 城市代码
     */
    private String cityCode;

    /**
     * 省份代码
     */
    private String provinceCode;

    /**
     * 机构城市
     */
    private String cityName;

    /**
     * 区县代码
     */
    private String regionCode;

    /**
     * 机构区县
     */
    private String regionName;

    /**
     * 代表工号
     */
    private String representativeCode;


    /**
     * 岗位名称
     */
    private String postName;

    /**
     * 产品(sku)编码
     */
    private Long goodsCode;

    /**
     * 商品集合信息
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
     * 匹配状态
     */
    private Integer mappingStatus;

    /**
     * 数据权限过滤字段
     */
    private List<Long> crmIdList;

    /**
     * 数据权限过滤字段
     */
    private List<String> provinceNameList;

}
