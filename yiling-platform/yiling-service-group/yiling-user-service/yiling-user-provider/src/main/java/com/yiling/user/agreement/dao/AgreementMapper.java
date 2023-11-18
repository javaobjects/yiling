package com.yiling.user.agreement.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseMapper;
import com.yiling.user.agreement.dto.EntPageListItemDTO;
import com.yiling.user.agreement.dto.request.QueryEntPageListRequest;
import com.yiling.user.agreement.entity.AgreementDO;

/**
 * <p>
 * 协议表 Dao 接口
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-06-03
 */
@Repository
public interface AgreementMapper extends BaseMapper<AgreementDO> {

	/**
	 * 分页查询三方协议有关的企业idList
	 * @param page     分页信息
	 * @param queryEid 当前查询者eid
	 * @return
	 */
	Page<AgreementDO> queryPageListThirdEntIdList(Page page, @Param("queryEid") Long queryEid);
	/**
	 * 查询的企业列表按年度协议数量倒叙排序
	 *
	 * @param request
	 * @param page
	 * @return
	 */
	Page<EntPageListItemDTO> queryEntPageList(Page page, @Param("request")QueryEntPageListRequest request);

    /**
     * 查询所有生效的补充协议
     * @return
     */
    List<Long> queryEidByMigrate();
}
