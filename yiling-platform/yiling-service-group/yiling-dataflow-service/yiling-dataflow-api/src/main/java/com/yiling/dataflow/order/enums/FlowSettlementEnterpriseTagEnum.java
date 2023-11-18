package com.yiling.dataflow.order.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 流向报表商业标签枚举类
 *
 * @author: houjie.sun
 * @date: 2022/6/6
 */
@Getter
@AllArgsConstructor
public enum FlowSettlementEnterpriseTagEnum {

    YUN_CANG_SHANG_YE("云仓商业", 4),
    XIAO_YAO_YAO("小药药", 6),
    YAO_SHI_BANG("药师帮", 7),
    YI_HAO("壹号", 8),
    ;

    private String tagName;
    private Integer tageId;

    public static FlowSettlementEnterpriseTagEnum getByTagName(Integer tagName) {
        for(FlowSettlementEnterpriseTagEnum e: FlowSettlementEnterpriseTagEnum.values()) {
            if(e.getTagName().equals(tagName)) {
                return e;
            }
        }
        return null;
    }

    public static List<String> getTagNameList(){
        return Arrays.stream(FlowSettlementEnterpriseTagEnum.values()).map(FlowSettlementEnterpriseTagEnum::getTagName).collect(Collectors.toList());
    }

}
