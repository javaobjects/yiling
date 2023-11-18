package com.yiling.dataflow.report.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;
import com.yiling.order.order.dto.request.CreateOrderExtendRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * @author zhigang.guo
 * @date: 2023/3/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class FlowWashSaleReportPageRequest extends QueryPageListRequest {

    /**
     * 计入年份
     */
    private String year;

    /**
     * 计入月份
     */
    private String month;

    /**
     * 商业名称（商家名称）
     */
    private String ename;

    /**
     * 经销商编码
     */
    private Long crmId;

    /**
     * 机构编码
     */
    private Long customerCrmId;

    /**
     * 原始商品规格
     */
    private String soSpecifications;

    /**
     * 原始客户名称
     */
    private String originalEnterpriseName;

    /**
     * 原始商品名称
     */
    private String soGoodsName;

    /**
     * 通过商品编码搜索
     */
    private Long goodsCode;

    /**
     * 商品编码集合
     */
    private List<Long> goodsCodeList;

    /**
     * 流向分类 {@link com.yiling.dataflow.wash.enums.FlowClassifyEnum}
     */
    private List<Integer> flowClassifyList;

    /**
     * 销售时间
     */
    private Date soTime;

    /**
     * 匹配状态
     */
    private Integer mappingStatus;

    /**
     * 部门
     */
    private String department;

    /**
     * 业务部门
     */
    private String businessDepartment;

    /**
     * 省区
     */
    private String provincialArea;

    /**
     * 业务省区
     */
    private String businessProvince;

    /**
     * 区办
     */
    private String regionName;

    /**
     * 业务代表
     */
    private String representativeCode;

    /**
     * 机构是否锁定  1是 2否
     */
    private Integer isChainFlag;

    /**
     * 流向是否锁定 1:是 2否
     */
    private Integer isLockFlag;

    /**
     * 查询数据范围(正数,负数)
     */
    private DataScopeEnum dataScope;

    /**
     * 数据权限过滤字段
     */
    private List<Long> crmIdList;

    /**
     * 数据权限过滤字段
     */
    private List<String> supplierProvinceNameList;

    @Getter
    @AllArgsConstructor
    public enum DataScopeEnum {


        GT(1, "正数"),GE(2, "正数 + 0"), LT(3, "负数"), LE(4, "负数 + 0");

        private Integer code;

        private String name;

        public static FlowWashSaleReportPageRequest.DataScopeEnum getByCode(Integer code) {

            for (FlowWashSaleReportPageRequest.DataScopeEnum e : FlowWashSaleReportPageRequest.DataScopeEnum.values()) {

                if (e.getCode().equals(code)) {
                    return e;
                }
            }

            return null;
        }

    }

}
