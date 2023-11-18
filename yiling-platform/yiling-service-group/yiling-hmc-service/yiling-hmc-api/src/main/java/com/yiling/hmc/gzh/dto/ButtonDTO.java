package com.yiling.hmc.gzh.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * ButtonDTO
 */
@NoArgsConstructor
@Data
public class ButtonDTO {
    /**
     * type
     */
    private String type;
    /**
     * name
     */
    private String name;
    /**
     * url
     */
    private String url;
    /**
     * subButton
     */
    private List<SubButtonDTO> subButton = new ArrayList<>();
    /**
     * appid
     */
    private String appid;
    /**
     * pagepath
     */
    private String pagepath;

    /**
     * 1-启用，2-禁用
     */
    private String status;
}
