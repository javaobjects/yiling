package com.yiling.dataflow.report.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.order.order.dto.request.CreateOrderExtendRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * @author zhigang.guo
 * @date: 2023/3/3
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class FlowWashChangeRequest extends BaseRequest {
    /**
     * 流动变动Id
     */
    private List<Long> flowWashIds;

    /**
     * 年份
     */
    private Integer year;

    /**
     * 月份
     */
    private Integer month;

    /**
     * 变更类型
     */
    private ChangeTypeEnum changeTypeEnum;


    @Getter
    @AllArgsConstructor
    public enum ChangeTypeEnum {


        SALE(1, "流向销售报表变动"), INVENTORY(2, "流向库存报表变动");

        private Integer code;

        private String name;

        public static FlowWashChangeRequest.ChangeTypeEnum getByCode(Integer code) {

            for (FlowWashChangeRequest.ChangeTypeEnum e : FlowWashChangeRequest.ChangeTypeEnum.values()) {
                if (e.getCode().equals(code)) {
                    return e;
                }
            }
            return null;
        }
    }
}
