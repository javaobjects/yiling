package com.yiling.user.agreement.bo;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/7/8
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class StatisticsKeyValuesBO implements Serializable {
    private static final long serialVersionUID = -9044217574008714L;

    private Long statisticsKey;

    private BigDecimal statisticsValues;

}
