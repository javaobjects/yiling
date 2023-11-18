package com.yiling.dataflow.report.dto.request;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotEmpty;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author zhigang.guo
 * @date: 2023/5/5
 */
@Data
@Accessors(chain = true)
public class UpdateFlowWashSaleReportRequest  implements java.io.Serializable {

    private static final long serialVersionUID = 1l;

    /**
     *更新明细信息
     */
    @NotEmpty
    private List<UpdateFlowWashSaleReportDetailRequest> detailRequests;

    @Data
    @Accessors(chain = true)
    public static class UpdateFlowWashSaleReportDetailRequest extends BaseRequest {

        private static final long serialVersionUID = 1L;

        /**
         * 主键Id
         */
        private Long id;

        /**
         * 机构部门
         */
        private String department;

        /**
         * 机构业务部门
         */
        private String businessDepartment;

        /**
         * 机构省区
         */
        private String provincialArea;

        /**
         * 机构业务省区
         */
        private String businessProvince;

        /**
         * 机构区办代码
         */
        private String districtCountyCode;

        /**
         * 机构区办
         */
        private String districtCounty;

        /**
         * 主管工号
         */
        private String superiorSupervisorCode;

        /**
         * 主管名称
         */
        private String superiorSupervisorName;

        /**
         * 代表工号
         */
        private String representativeCode;

        /**
         * 代表姓名
         */
        private String representativeName;

        /**
         * 岗位代码
         */
        private Long postCode;

        /**
         * 岗位名称
         */
        private String postName;

        /**
         * 产品组
         */
        private String productGroup;

        /**
         * 省份代码
         */
        private String provinceCode;

        /**
         * 机构省份
         */
        private String provinceName;


        /**
         * 原始产品价格
         */
        private BigDecimal soPrice;

        /**
         * 产品单价
         */
        private BigDecimal salesPrice;

        /**
         * 金额
         */
        private BigDecimal soTotalAmount;

    }
}
