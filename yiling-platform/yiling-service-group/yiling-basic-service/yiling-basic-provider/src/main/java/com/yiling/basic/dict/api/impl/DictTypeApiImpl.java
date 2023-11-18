package com.yiling.basic.dict.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.dict.api.DictTypeApi;
import com.yiling.basic.dict.bo.DictDataBO;
import com.yiling.basic.dict.bo.DictTypeBO;
import com.yiling.basic.dict.bo.request.QueryDictTypeRequest;
import com.yiling.basic.dict.bo.request.SaveDictTypeRequest;
import com.yiling.basic.dict.bo.request.UpdateDictTypeRequest;
import com.yiling.basic.dict.entity.DictTypeDO;
import com.yiling.basic.dict.service.DictTypeService;
import com.yiling.framework.common.util.PojoUtils;

/**
 * @author:wei.wang
 * @date:2021/6/9
 */
@DubboService
public class DictTypeApiImpl implements DictTypeApi {
    @Autowired
    DictTypeService dictTypeService;


    /**
     * 分页获取所有字典类型
     *
     * @param request
     * @return
     */
    @Override
    public Page<DictTypeBO> getDictTypePage(QueryDictTypeRequest request) {
        Page<DictTypeDO> page = dictTypeService.getEnabledDicTypePage(request);
        Page<DictTypeBO> result = PojoUtils.map(page, DictTypeBO.class);
        return result;
    }

    /**
     * 修改字典类型数据
     *
     * @param request
     * @return
     */
    @Override
    public Boolean updateDictTypeById(UpdateDictTypeRequest request) {
        return dictTypeService.updateDictTypeById(request);
    }

    /**
     * 保存字典表类型
     *
     * @param request
     * @return
     */
    @Override
    public Boolean saveDictType(SaveDictTypeRequest request) {
        return dictTypeService.saveDictType(request);
    }

    /**
     * 停用字典类型
     *
     * @param id
     * @return
     */
    @Override
    public Boolean updateStopDictType(Long id, Long opUserId) {

        return dictTypeService.updateStopDictType(id, opUserId);
    }

    /**
     * 启用字典类型
     *
     * @param id
     * @return
     */
    @Override
    public Boolean enabledDictType(Long id, Long opUserId) {
        return dictTypeService.enabledDictType(id, opUserId);
    }

    /**
     * 删除字典类型
     * @param idList
     * @return
     */
    @Override
    public Boolean deleteType(List<Long> idList, Long opUserId) {
        return dictTypeService.deleteType(idList, opUserId);

    }

    /**
     * 根据类型名称获取字段类型
     *
     * @param name
     * @return
     */
    @Override
    public List<DictDataBO> mapByName(String name) {
        return PojoUtils.map(dictTypeService.mapByName(name),DictDataBO.class);
    }

}
