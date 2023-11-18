package com.yiling.basic.dict.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.basic.dict.api.DictDataApi;
import com.yiling.basic.dict.bo.DictDataBO;
import com.yiling.basic.dict.bo.request.SaveDictDataRequest;
import com.yiling.basic.dict.bo.request.UpdateDictDataRequest;
import com.yiling.basic.dict.entity.DictDataDO;
import com.yiling.basic.dict.service.DictDataService;
import com.yiling.framework.common.util.PojoUtils;

/**
 * @author:wei.wang
 * @date:2021/6/9
 */
@DubboService
public class DictDataApiImpl implements DictDataApi {
    @Autowired
    DictDataService dictDataService;


    /**
     * 根据typeId获取字典信息
     *
     * @param typeId
     * @return
     */
    @Override
    public List<DictDataBO> getEnabledByTypeIdList(Long typeId) {
        List<DictDataDO> result = dictDataService.getEnabledByTypeIdList(typeId);
        return PojoUtils.map(result, DictDataBO.class);
    }

    /**
     * 根据id修改字典内容信息
     *
     * @param request
     * @return
     */
    @Override
    public Boolean updateDictDataById(UpdateDictDataRequest request) {
        return dictDataService.updateDictDataById(request);
    }

    /**
     * 保存字典内容
     *
     * @param request
     * @return
     */
    @Override
    public Boolean saveDictData(SaveDictDataRequest request) {
        return dictDataService.saveDictData(request);
    }

    /**
     * 根据id停用字典内容
     *
     * @param id
     * @return
     */
    @Override
    public Boolean updateStopDictData(Long id, Long opUserId) {
        return dictDataService.updateStopDictData(id, opUserId);
    }

    /**
     * 根据id启用字典内容
     *
     * @param id
     * @return
     */
    @Override
    public Boolean enabledDictData(Long id, Long opUserId) {
        return dictDataService.enabledDictData(id, opUserId);

    }

    /**
     * 删除字典数据
     * @param idList
     * @return
     */
    @Override
    public Boolean deleteData(List<Long> idList, Long opUserId) {
        return dictDataService.deleteData(idList, opUserId);
    }


}
