package com.yiling.dataflow.report.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * @author zhigang.guo
 * @date: 2023/3/6
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CreateFlowWashReportRequest extends BaseRequest {

    /**
     * 经销商客户Id
     */
    private Long crmId;

    /**
     * 年份
     */
    private Integer year;

    /**
     * 月份
     */
    private Integer month;

    /**
     * 报表统计类型
     */
    private ReportTypeEnum reportType;



    @Getter
    @AllArgsConstructor
    public enum ReportTypeEnum {


        SUPPLY(1, "供应商进销存报表"), HOSPITAL(2, "医疗进销存报表"),PHARMACY(3, "零售购进报表");

        private Integer code;

        private String name;

        public static CreateFlowWashReportRequest.ReportTypeEnum getByCode(Integer code) {

            for (CreateFlowWashReportRequest.ReportTypeEnum e : CreateFlowWashReportRequest.ReportTypeEnum.values()) {
                if (e.getCode().equals(code)) {
                    return e;
                }
            }
            return null;
        }
    }
}
