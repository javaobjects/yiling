package com.yiling.mall.banner.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.yiling.framework.common.base.BaseMapper;
import com.yiling.mall.banner.dto.B2bAppBannerDTO;
import com.yiling.mall.banner.entity.B2bAppBannerDO;

/**
 * <p>
 * B2B的banner表 Dao 接口
 * </p>
 *
 * @author yong.zhang
 * @date 2021-10-22
 */
@Repository
public interface B2bAppBannerMapper extends BaseMapper<B2bAppBannerDO> {

    /**
     * app查询店铺banner信息
     *
     * @param usageScenario 使用位置：1-B2B移动端主Banner 2-B2B移动端副Banner 3-B2B移动端会员中心Banner 4-B2B移动端店铺Banner
     * @param bannerSource 来源1-POP 2-销售助手 3-B2B
     * @param eid 企业id
     * @param count 数量
     * @return 店铺banner信息
     */
    List<B2bAppBannerDTO> listByScenarioAndSourceAndEid(@Param("usageScenario") Integer usageScenario, @Param("bannerSource") Integer bannerSource, @Param("eid") Long eid, @Param("count") int count);
}
