package com.yiling.basic.shortlink.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 短链接 DTO
 * </p>
 *
 * @author lun.yu
 * @date 2022-01-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ShortLinkDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 短地址key
     */
    private String urlKey;

    /**
     * 短地址
     */
    private String shortUrl;

    /**
     * 长地址
     */
    private String longUrl;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 修改时间
     */
    private Date updateTime;

}
