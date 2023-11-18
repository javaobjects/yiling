package com.yiling.sjms.gb.api;

import java.util.List;

import com.yiling.sjms.gb.dto.AttachmentDTO;
import com.yiling.sjms.gb.dto.request.SaveGBFileRequest;

/**
 * 团购证据信息
 *
 * @author: wei.wang
 * @date: 2022/11/28
 */
public interface GbAttachmentApi {
    /**
     * 根据表单id获取团购证据
     * @param gbId
     * @return
     */
    List<AttachmentDTO> listByGbId(Long gbId);

    /**
     * 补充证据
     * @param request
     * @return
     */
    Boolean addSupport(SaveGBFileRequest request);

    /**
     * 图片MD5
     * @param md5
     * @param type
     * @return
     */
    AttachmentDTO getAttachmentByMD5(String md5,Integer type);
}
