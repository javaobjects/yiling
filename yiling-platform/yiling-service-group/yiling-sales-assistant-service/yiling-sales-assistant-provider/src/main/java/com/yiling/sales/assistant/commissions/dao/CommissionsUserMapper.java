package com.yiling.sales.assistant.commissions.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseMapper;
import com.yiling.sales.assistant.commissions.dto.CommissionsUserDTO;
import com.yiling.sales.assistant.commissions.dto.StatisticsCommissionsUserDTO;
import com.yiling.sales.assistant.commissions.dto.request.QueryCommissionsUserPageListRequest;
import com.yiling.sales.assistant.commissions.entity.CommissionsUserDO;

/**
 * <p>
 * 销售助手用户佣金表 Dao 接口
 * </p>
 *
 * @author dexi.yao
 * @date 2021-09-17
 */
@Repository
public interface CommissionsUserMapper extends BaseMapper<CommissionsUserDO> {

	/**
	 * 统计平台佣金相关
	 * @return
	 */
	StatisticsCommissionsUserDTO statisticsCommissionsUser();
}
