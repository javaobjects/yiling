package com.yiling.dataflow.wash.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 销量申诉机构名称、产品名称、机构属性改变枚举
 * @author: xinxuan.jia
 * @date: 2023/6/27
 */
@Getter
@AllArgsConstructor
public enum SaleAppealChangeTypeEnum {
    // 申诉类型 4终端类型申诉、5其他 6、漏做客户关系对照 7、未备案商业销售到锁定终端 8、医院分院以总院名头进货
    // 9、医院的院内外药店进货 10、医联体、医共体共用进货名头 11、互联网医院无法体现医院名字 12、药店子公司以总部名头进货 13、产品对照错误
    // 前7+其他---机构名称
    // 产品对照错误---产品品名
    // 终端类型申诉---终端类型（商业公司、零售机构、医疗机构）
    //根据申诉类型变化的变化类型：1、标准机构名称 2、标准产品名称 3、机构属性
    INSTITUTION_NAME(1, "标准机构名称"),
    PRODUCT_NAME(2, "标准产品名称"),
    INSTITUTIONAL_ATTRIBUTES(3, "终端类型"),
    ;
    private Integer code;
    private String  desc;

    public static FlowClassifyEnum getByCode(Integer code) {
        for(FlowClassifyEnum e: FlowClassifyEnum.values()) {
            if(e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
