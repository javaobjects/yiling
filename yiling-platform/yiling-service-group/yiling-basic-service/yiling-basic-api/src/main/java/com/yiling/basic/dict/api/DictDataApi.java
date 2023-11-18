package com.yiling.basic.dict.api;

import java.util.List;

import com.yiling.basic.dict.bo.DictDataBO;
import com.yiling.basic.dict.bo.request.SaveDictDataRequest;
import com.yiling.basic.dict.bo.request.UpdateDictDataRequest;

/**
 * @author:wei.wang
 * @date:2021/6/9
 */
public interface DictDataApi {
    /**
     * 根据typeId获取字典信息
     * @param typeId
     * @return
     */
    List<DictDataBO> getEnabledByTypeIdList(Long typeId);

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
     * 根据id停用字典内容
     * @param id
     * @return
     */
    Boolean updateStopDictData(Long id, Long opUserId);

    /**
     * 根据id启用字典内容
     * @param id
     * @return
     */
    Boolean enabledDictData(Long id, Long opUserId);

    /**
     * 删除字典数据
     * @param idList
     * @return
     */
    Boolean deleteData(List<Long> idList, Long opUserId);

}
