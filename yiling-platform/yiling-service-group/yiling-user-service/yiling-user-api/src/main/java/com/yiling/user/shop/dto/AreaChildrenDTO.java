package com.yiling.user.shop.dto;

import java.util.List;

import lombok.Data;

/**
 * <p>
 * B2B-店铺区域json解析DTO
 * </p>
 *
 * @author lun.yu
 * @date 2021/11/2
 */
@Data
public class AreaChildrenDTO {

    /**
     * 区域编码
     */
    private String code;

    /**
     * 区域名称
     */
    private String name;

    /**
     * 下级
     */
    private List<AreaChildrenDTO> children;

}