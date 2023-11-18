package com.yiling.dataflow.crm.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.crm.dto.CrmManorDTO;
import com.yiling.dataflow.crm.dto.request.QueryCrmManorPageRequest;
import com.yiling.dataflow.crm.dto.request.QueryCrmManorParamRequest;
import com.yiling.dataflow.crm.dto.request.RemoveManorRequest;
import com.yiling.dataflow.crm.dto.request.SaveCrmManorInfoRequest;
import com.yiling.dataflow.crm.dto.request.UpdateManorNumRequest;

import java.util.List;

/**
 * 辖区API
 */
public interface CrmManorApi {
    Page<CrmManorDTO> pageList(QueryCrmManorPageRequest request);

    CrmManorDTO getManorById(Long id);

    Long saveOrUpdate(SaveCrmManorInfoRequest request);

    /**
     * 根据名称获取当前辖区信息
     *
     * @param name
     * @return
     */
    List<CrmManorDTO> getByName(String name);

    List<CrmManorDTO> listByParam(QueryCrmManorParamRequest crmManorParamRequest);

    int removeById(RemoveManorRequest request);

    void updateNum(UpdateManorNumRequest request);
}
