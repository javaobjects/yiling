package com.yiling.marketing.presale.dao;

import java.util.List;

import com.yiling.marketing.presale.dto.PresaleActivityDTO;
import com.yiling.marketing.presale.dto.request.QueryPresaleInfoRequest;
import com.yiling.marketing.presale.entity.MarketingPresaleActivityDO;
import com.yiling.framework.common.base.BaseMapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 预售活动主表 Dao 接口
 * </p>
 *
 * @author shixing.sun
 * @date 2022-10-10
 */
@Repository
public interface MarketingPresaleActivityMapper extends BaseMapper<MarketingPresaleActivityDO> {

    /**
     * 获取可用的预售活动
     * @param request
     * @return 获取可用的预售活动
     */
    List<PresaleActivityDTO> getAvailablePresaleInfo(@Param("request") QueryPresaleInfoRequest request);
}
