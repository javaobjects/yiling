package com.yiling.sjms.manor.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 根据id更新归档状态
 *
 * @author: dexi.yao
 * @date: 2023/2/27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateArchiveRequest extends BaseRequest {


    private static final long serialVersionUID = 6627947363367607822L;
    private Long id;

    /**
     * 数据归档：1-开启 2-关闭
     */
    private Integer archiveStatus;
}