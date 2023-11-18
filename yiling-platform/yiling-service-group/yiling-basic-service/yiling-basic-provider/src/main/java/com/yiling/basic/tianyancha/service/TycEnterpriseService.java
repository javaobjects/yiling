package com.yiling.basic.tianyancha.service;

import com.yiling.basic.tianyancha.dto.TycEnterpriseInfoDTO;
import com.yiling.basic.tianyancha.dto.TycResultDTO;
import com.yiling.basic.tianyancha.dto.request.TycEnterpriseQueryRequest;

/**
 * @author shichen
 * @类名 TycEnterpriseService
 * @描述
 * @创建时间 2022/1/11
 * @修改人 shichen
 * @修改时间 2022/1/11
 **/
public interface TycEnterpriseService {

    /**
     * 天眼查查询普通企业信息（精准查询）
     * @param request
     * @return
     */
    TycResultDTO<TycEnterpriseInfoDTO> findEnterpriseByKeyword(TycEnterpriseQueryRequest request);

    /**
     * 名字和社会唯一信用代码查询天眼查对象
     * @param name
     * @param creditCode
     * @return
     */
    TycEnterpriseInfoDTO findTycEnterpriseByNameAndCreditCode(String name, String creditCode);
}
