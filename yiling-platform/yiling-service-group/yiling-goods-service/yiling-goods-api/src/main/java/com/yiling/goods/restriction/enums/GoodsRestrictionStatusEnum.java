package com.yiling.goods.restriction.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author shichen
 * @类名 GoodsRestrictionStatusEnum
 * @描述
 * @创建时间 2022/12/6
 * @修改人 shichen
 * @修改时间 2022/12/6
 **/
@Getter
@AllArgsConstructor
public enum GoodsRestrictionStatusEnum {
    //正常
    NORMAL(0, "正常"),
    //关闭
    CLOSE(1, "关闭");

    private Integer code;
    private String  name;
}
