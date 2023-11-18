package com.yiling.basic.tianyancha.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;

/**
 * @author shichen
 * @类名 TycEnterpriseQueryRequest
 * @描述
 * @创建时间 2022/1/11
 * @修改人 shichen
 * @修改时间 2022/1/11
 **/
@Data
public class TycEnterpriseQueryRequest extends BaseRequest {

    /**
     * 天眼查搜索关键字（公司名称、公司id、注册号或社会统一信用代码）
     */
    private String keyword;

}
