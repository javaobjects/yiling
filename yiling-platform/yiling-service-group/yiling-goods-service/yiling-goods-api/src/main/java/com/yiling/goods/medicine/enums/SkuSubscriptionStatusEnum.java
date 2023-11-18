package com.yiling.goods.medicine.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author shichen
 * @类名 SkuSubscriptionStatusEnum
 * @描述
 * @创建时间 2022/7/26
 * @修改人 shichen
 * @修改时间 2022/7/26
 **/
@Getter
@AllArgsConstructor
public enum SkuSubscriptionStatusEnum {
    /**
     * 关闭订阅
     */
    CLOSE(0, "关闭订阅"),
    /**
     * 开启订阅
     */
    OPEN(1, "开启订阅");
    private Integer type;
    private String  name;
}
