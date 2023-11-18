package com.yiling.sjms.gb.service;

import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.sjms.gb.dto.AttachmentDTO;
import com.yiling.sjms.gb.dto.request.SaveGBFileRequest;
import com.yiling.sjms.gb.entity.GbAttachmentDO;

/**
 * <p>
 * 团购附件 服务类
 * </p>
 *
 * @author wei.wang
 * @date 2022-11-28
 */
public interface GbAttachmentService extends BaseService<GbAttachmentDO> {

    /**
     * 根据表单id获取团购证据
     * @param gbId
     * @return
     */
    List<GbAttachmentDO> listByGbId(Long gbId);

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
    GbAttachmentDO getAttachmentByMD5(String md5,Integer type);
}
