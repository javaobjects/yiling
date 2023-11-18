package com.yiling.admin.hmc.gzh.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * SubButtonDTO
 */
@NoArgsConstructor
@Data
public class SubButtonVO {
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