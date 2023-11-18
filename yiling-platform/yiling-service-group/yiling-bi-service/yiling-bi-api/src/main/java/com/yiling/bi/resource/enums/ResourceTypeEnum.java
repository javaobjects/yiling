package com.yiling.bi.resource.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: shuang.zhang
 * @date: 2022/11/18
 */
@Getter
@AllArgsConstructor
public enum ResourceTypeEnum {

    //importCoverMaster2023
    //importCoverAssist2023
    //importCoverLhAssist2023
    RECORD("record", "2022备案表"),
    COVER("cover", "2022覆盖表"),
    COVER_MASTER_2023("importCoverMaster2023", "2023覆盖表主协议"),
    COVER_ASSIST_2023("importCoverAssist2023", "2023覆盖表辅助议"),
    COVER_LH_ASSIST_2023("importCoverLhAssist2023", "2023覆盖表连花辅助议"),
    ;

    private String code;
    private String name;

    public static ResourceTypeEnum getByCode(String code) {
        for (ResourceTypeEnum e : ResourceTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
