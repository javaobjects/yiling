package com.yiling.basic.dict.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.dict.bo.DictDataBO;
import com.yiling.basic.dict.bo.DictTypeBO;
import com.yiling.basic.dict.bo.request.QueryDictTypeRequest;
import com.yiling.basic.dict.bo.request.SaveDictTypeRequest;
import com.yiling.basic.dict.bo.request.UpdateDictTypeRequest;

/**
 * @author:wei.wang
 * @date:2021/6/9
 */
public interface DictTypeApi {
    /**
     * 分页获取所有字典类型
     * @param request
     * @return
     */
    Page<DictTypeBO> getDictTypePage(QueryDictTypeRequest request);

    /**
     * 修改字典类型数据
     * @param request
     * @return
     */
    Boolean updateDictTypeById(UpdateDictTypeRequest request);

    /**
     * 保存字典表类型
     * @param request
     * @return
     */
    Boolean saveDictType(SaveDictTypeRequest request);

    /**
     * 停用字典类型
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
    List<DictDataBO>  mapByName(String name);

}
