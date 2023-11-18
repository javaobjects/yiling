package com.yiling.marketing.promotion.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseMapper;
import com.yiling.marketing.promotion.dto.SpecialActivityEnterpriseDTO;
import com.yiling.marketing.promotion.dto.request.SpecialActivityPageRequest;
import com.yiling.marketing.promotion.entity.PromotionActivityDO;
import com.yiling.marketing.promotion.entity.PromotionSpecialActivityEnterpriseDO;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 专场活动企业表 Dao 接口
 * </p>
 *
 * @author shixing.sun
 * @date 2022-05-18
 */
@Repository
public interface PromotionSpecialActivityEnterpriseMapper extends BaseMapper<PromotionSpecialActivityEnterpriseDO> {

}
