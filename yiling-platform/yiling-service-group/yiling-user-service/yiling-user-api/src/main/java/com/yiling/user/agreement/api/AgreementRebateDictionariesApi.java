package com.yiling.user.agreement.api;

import java.util.List;
import java.util.Map;

import com.yiling.user.agreement.dto.AgreementRebateDictionariesDTO;
import com.yiling.user.agreement.enums.AgreementRebateDictionariesStatusEnum;

/**
 * @author: dexi.yao
 * @date: 2021/7/29
 */
public interface AgreementRebateDictionariesApi {

    /**
     * 根据id 和code
     *
     * @param ids
     * @param code
     * @return
     */
    List<AgreementRebateDictionariesDTO> listByIds(List<Long> ids, AgreementRebateDictionariesStatusEnum code);

    /**
     * 根据parentIds 和code
     * @param parentIds
     * @param code
     * @return
     */
    Map<Long, List<AgreementRebateDictionariesDTO>> listByParentIds(List<Long> parentIds, AgreementRebateDictionariesStatusEnum code);

    /**
     * nameList 和code查询
     * @param nameList
     * @param code
     * @return
     */
    List<AgreementRebateDictionariesDTO> listByNameList(List<String> nameList, AgreementRebateDictionariesStatusEnum code);

}