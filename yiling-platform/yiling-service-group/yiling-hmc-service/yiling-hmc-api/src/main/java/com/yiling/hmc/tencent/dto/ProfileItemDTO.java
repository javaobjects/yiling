package com.yiling.hmc.tencent.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * ProfileItemDTO
 */
@NoArgsConstructor
@Data
public class ProfileItemDTO implements Serializable {
    /**
     * tag
     */
    private String Tag;
    /**
     * value
     */
    private String Value;
}
