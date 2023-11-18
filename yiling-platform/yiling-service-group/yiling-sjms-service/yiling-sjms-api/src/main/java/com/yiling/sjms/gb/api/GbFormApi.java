package com.yiling.sjms.gb.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.sjms.gb.dto.GbFormFlowDTO;
import com.yiling.sjms.gb.dto.GbFormInfoListDTO;
import com.yiling.sjms.gb.dto.GbFormDTO;
import com.yiling.sjms.gb.dto.GbFormExportListDTO;
import com.yiling.sjms.gb.dto.GbFormInfoDTO;
import com.yiling.sjms.gb.dto.request.QueryGBFormListPageRequest;
import com.yiling.sjms.gb.dto.request.SaveGBCancelInfoRequest;
import com.yiling.sjms.gb.dto.request.SaveGBInfoRequest;
import com.yiling.sjms.gb.dto.request.UpdateGBFormInfoRequest;

/**
 * 团购表单信息
 *
 * @author: wei.wang
 * @date: 2023/02/14
 */
public interface GbFormApi {

    /**
     * 获取团购表单数据
     * @param id
     * @return
     */
    GbFormInfoDTO getOneById(Long id);

    /**
     * 根据表单id查询团购信息
     * @param formId
     * @return
     */
    GbFormDTO getOneByFormId(Long formId);

    /**
     * 团购取消保存
     * @param request
     * @return
     */
    Long saveGbCancel(SaveGBCancelInfoRequest request);

    /**
     * 团购取消驳回保存
     * @param request
     * @return
     */
    Long saveRejectGbCancel(SaveGBCancelInfoRequest request);

    /**
     * 保存团购信息
     * @param request
     * @return
     */
    Long saveGBInfo(SaveGBInfoRequest request);

    /**
     * 获取团购表单数据列表
     * @param request
     * @return
     */
    Page<GbFormInfoListDTO> getGBFormListPage(QueryGBFormListPageRequest request);


    /**
     * 表单修改状态
     * @param request
     * @return
     */
    Boolean updateStatusById(UpdateGBFormInfoRequest request );

    /**
     * 修改添加复核意见和状态
     * @param gbFormDTO
     * @return
     */
    Boolean updateByFormId(GbFormDTO gbFormDTO);

    /**
     * 获取导出信息
     * @param request
     * @return
     */
    Page<GbFormExportListDTO> getGBFormExportListPage(QueryGBFormListPageRequest request);

    /**
     * 删除团购
     * @param formId
     * @param userId
     */
    void delete(Long formId , Long userId);


    /**
     * 团购费用申请取消保存
     * @param request
     * @return
     */
    Long saveGbFeeApplication(SaveGBCancelInfoRequest request);

    /**
     * 团购费用申请取消重新保存
     * @param request
     * @return
     */
    Long saveRejectGbFeeApplication(SaveGBCancelInfoRequest request);

    /**
     * 获取团购表单数据列表，过滤已经关联过费用申请的
     * @param request
     * @return
     */
    Page<GbFormInfoListDTO> getGBFeeApplicationFormListPage(QueryGBFormListPageRequest request);

    /**
     * 主键获取gbform
     * @param id
     * @return
     */
    GbFormDTO getByPramyKey(Long id);

    /**
     * 获取团购表单数据列表
     * @param request
     * @return
     */
    Page<GbFormInfoListDTO> getGBFeeFormListPage(QueryGBFormListPageRequest request);

    /**
     * 提供团购数据给流向
     * @param formId
     * @return
     */
    GbFormFlowDTO getGbFormFlowList(Long formId);
}
