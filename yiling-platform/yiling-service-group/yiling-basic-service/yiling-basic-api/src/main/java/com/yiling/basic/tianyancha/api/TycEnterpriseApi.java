package com.yiling.basic.tianyancha.api;

import com.yiling.basic.tianyancha.dto.TycEnterpriseInfoDTO;
import com.yiling.basic.tianyancha.dto.TycResultDTO;
import com.yiling.basic.tianyancha.dto.request.TycEnterpriseQueryRequest;

/**
 * @author shichen
 * @类名 TycEnterpriseApi
 * @描述
 * @创建时间 2022/1/12
 * @修改人 shichen
 * @修改时间 2022/1/12
 **/
public interface TycEnterpriseApi {

    TycResultDTO<TycEnterpriseInfoDTO> findEnterpriseByKeyword(TycEnterpriseQueryRequest request);
}
