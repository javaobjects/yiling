package com.yiling.search.word.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author shichen
 * @类名 EsWordTypeEnum
 * @描述
 * @创建时间 2022/4/19
 * @修改人 shichen
 * @修改时间 2022/4/19
 **/
@Getter
@AllArgsConstructor
public enum EsWordTypeEnum {
    EXPAND(1,"扩展词","ik","custom.dic"),
    STOP(2,"停止词","ik","stop.dic"),
    ONE_WAY_SYNONYM(3,"单向同义词","ik","synonym.dic"),
    TWO_WAY_SYNONYM(4,"双向同义词","ik","synonym.dic"),
    ;
    private Integer type;
    private String  name;
    private String filePath;
    private String fileName;

    public static EsWordTypeEnum getByType(Integer type) {
        for (EsWordTypeEnum e : EsWordTypeEnum.values()) {
            if (e.getType().equals(type)) {
                return e;
            }
        }
        return null;
    }
}
