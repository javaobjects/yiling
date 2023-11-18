package com.yiling.sjms.flee.dto.request;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2023/3/13 0013
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveAppendixRequest extends BaseRequest {

    /**
     * 文件url
     */
    private String url;

    /**
     * 文件url
     */
    private String key;

    /**
     * 文件名称
     */
    private String name;
}
