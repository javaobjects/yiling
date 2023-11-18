package com.yiling.basic.dict.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.dict.bo.request.QueryDictTypeRequest;
import com.yiling.basic.dict.bo.request.SaveDictTypeRequest;
import com.yiling.basic.dict.bo.request.UpdateDictTypeRequest;
import com.yiling.basic.dict.entity.DictDataDO;
import com.yiling.basic.dict.entity.DictTypeDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 字典类型表 服务类
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-06-03
 */
public interface DictTypeService extends BaseService<DictTypeDO> {

    /**
     * 获取type 所有数据
     * @return
     */
    List<DictTypeDO> getEnabledList();

    /**
     * 获取type分页数据
     * @param request
     * @return
     */
    Page<DictTypeDO> getEnabledDicTypePage(QueryDictTypeRequest request);

    /**
     * 修改字典类型数据
     * @param request
     * @return
     */
    Boolean updateDictTypeById(UpdateDictTypeRequest request);

    /**
     * 新增字典类型
     * @param request
     * @return
     */
    Boolean saveDictType(SaveDictTypeRequest request);

    /**
     *
     * @param id
     * @return
     */
    Boolean updateStopDictType(Long id, Long opUserId);

    /**
     * 启用字典类型
     * @param id
     * @return
     */
    Boolean enabledDictType(Long id, Long opUserId);

    /**
     * 删除字典类型
     * @param idList
     * @return
     */
    Boolean deleteType(List<Long> idList, Long opUserId);

    /**
     * 根据类型名称获取字段类型
     * @param name
     * @return
     */
    List<DictDataDO> mapByName(String name);
}
