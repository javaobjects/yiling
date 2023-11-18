package com.yiling.hmc.tencent.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 自定义数据
 */
@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
public class AppDefinedDTO implements java.io.Serializable{

    /**
     * key
     */
    private String Key;

    /**
     *
     */
    private String Value;
}
