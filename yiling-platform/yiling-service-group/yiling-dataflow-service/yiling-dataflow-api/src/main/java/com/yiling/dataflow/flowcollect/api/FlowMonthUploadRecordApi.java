package com.yiling.dataflow.flowcollect.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.flowcollect.bo.FlowMonthUploadRecordBO;
import com.yiling.dataflow.flowcollect.dto.FlowMonthUploadRecordDTO;
import com.yiling.dataflow.flowcollect.dto.request.QueryFlowMonthUploadPageRequest;
import com.yiling.dataflow.flowcollect.dto.request.SaveFlowMonthUploadRecordRequest;
import com.yiling.dataflow.flowcollect.dto.request.UpdateFlowMonthUploadRecordRequest;
import com.yiling.user.system.bo.CurrentSjmsUserInfo;

/**
 * 月流向上传记录 API
 *
 * @author: lun.yu
 * @date: 2023-03-04
 */
public interface FlowMonthUploadRecordApi {

    /**
     * 月流向上传记录列表分页
     *
     * @param request
     * @return
     */
    Page<FlowMonthUploadRecordBO> queryFlowMonthPage(QueryFlowMonthUploadPageRequest request);

    /**
     * 检查月流向文件名格式
     *
     * @param fileName 文件名称
     * @return 错误信息
     */
    String checkFlowFileName(String fileName);

    /**
     * 保存月流向上传记录
     *
     * @param request
     * @return
     */
    Long saveFlowMonthRecord(SaveFlowMonthUploadRecordRequest request);

    /**
     * 更新流向上传记录
     *
     * @param request
     * @return
     */
    boolean updateFlowMonthRecord(UpdateFlowMonthUploadRecordRequest request);

    /**
     * 根据文件名获取文件类型
     *
     * @param fileName
     * @return
     */
    Integer getDataType(String fileName);

    /**
     * 根据文件名获取excelCode
     *
     * @param fileName
     * @return
     */
    String getExcelCodeByFileName(String fileName);

    /**
     * 删除月流向记录
     *
     * @param id
     * @param currentUserId
     * @return
     */
    Boolean deleteRecord(Long id, Long currentUserId);

    /**
     * 根据名称查询月流向上传记录
     *
     * @param fileName
     * @return
     */
    FlowMonthUploadRecordDTO getByFileName(String fileName);

    /**
     * 根据名称查询月流向上传记录
     *
     * @param recordId
     * @return
     */
    FlowMonthUploadRecordDTO getByRecordId(Long recordId);

    /**
     * 根据ID获取上传记录
     *
     * @param id
     * @return
     */
    FlowMonthUploadRecordDTO getById(Long id);

    /**
     * 月流向上传数据检查接口
     * @param fileName
     * @param userInfo
     * @param isFixUpload
     * @return
     */
    String checkFlowFileNameNew(String fileName, CurrentSjmsUserInfo userInfo, boolean isFixUpload);
}
