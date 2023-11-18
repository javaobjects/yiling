package com.yiling.user.agreement.service;

import java.util.List;
import java.util.Map;

import com.yiling.framework.common.base.BaseService;
import com.yiling.user.agreement.dto.AgreementRebateDictionariesDTO;
import com.yiling.user.agreement.entity.AgreementRebateDictionariesDO;
import com.yiling.user.agreement.enums.AgreementRebateDictionariesStatusEnum;

/**
 * <p>
 * 协议返利字典表 服务类
 * </p>
 *
 * @author dexi.yao
 * @date 2021-07-26
 */
public interface AgreementRebateDictionariesService extends BaseService<AgreementRebateDictionariesDO> {

	/**
	 * 根据ids 和code
	 * @param ids
	 * @param code
	 * @return
	 */
	List<AgreementRebateDictionariesDO> listByIds(List<Long> ids, AgreementRebateDictionariesStatusEnum code);

	/**
	 * 根据父id和code查询
	 * @param parentIds
	 * @param code
	 * @return
	 */
	Map<Long,List<AgreementRebateDictionariesDTO>> listByParentIds(List<Long> parentIds, AgreementRebateDictionariesStatusEnum code);


	/**
	 * 根据nameList和code查询
	 * @param nameList
	 * @param code
	 * @return
	 */
	List<AgreementRebateDictionariesDO> listByNameList(List<String> nameList, AgreementRebateDictionariesStatusEnum code);
}
