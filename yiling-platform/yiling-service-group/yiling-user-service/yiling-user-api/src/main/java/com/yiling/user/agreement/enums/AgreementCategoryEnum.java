package com.yiling.user.agreement.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: shuang.zhang
 * @date: 2021/5/24
 */
@Getter
@AllArgsConstructor
public enum AgreementCategoryEnum {
    //年度协议
    YEAR_AGREEMENT(1, "年度协议"),
    //临时协议
    TEMP_AGREEMENT(2, "补充协议");

    private Integer code;
    private String  name;

	public static AgreementCategoryEnum getByCode(Integer code) {
		for (AgreementCategoryEnum e : AgreementCategoryEnum.values()) {
			if (e.getCode().equals(code)) {
				return e;
			}
		}
		return null;
	}

}
