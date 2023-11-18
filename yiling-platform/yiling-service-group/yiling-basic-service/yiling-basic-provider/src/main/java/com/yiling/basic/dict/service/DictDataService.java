package com.yiling.basic.dict.service;

import java.util.List;

import com.yiling.basic.dict.bo.request.SaveDictDataRequest;
import com.yiling.basic.dict.bo.request.UpdateDictDataRequest;
import com.yiling.basic.dict.entity.DictDataDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 字典内容表 服务类
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-06-03
 */
public interface DictDataService extends BaseService<DictDataDO> {

    List<DictDataDO> getEnabledList();

    /**
     * 根据typeId获取字典信息
     * @param typeId
     * @return
     */
    List<DictDataDO> getEnabledByTypeIdList(Long typeId);

    /**
     * 根据id修改字典内容信息
     * @param request
     * @return
     */
    Boolean updateDictDataById(UpdateDictDataRequest request);

    /**
     * 保存字典内容
     * @param request
     * @return
     */
    Boolean saveDictData(SaveDictDataRequest request);

    /**
     * 停用字典内容根据id
     * @param id
     * @return
     */
    Boolean updateStopDictData(Long id, Long opUserId);

    /**
     * 停用字典内容根据类型id
     * @param type
     * @return
     */
    Boolean updateStopDictDataByTypeId(Long type, Long opUserId);

    /**
     * 启用字典内容根据id
     * @param id
     * @return
     */
    Boolean  enabledDictData(Long id, Long opUserId);

    /**
     * 删除字典数据
     * @param idList
     * @return
     */
    Boolean deleteData(List<Long> idList, Long opUserId);
}
