package com.yiling.sjms.gb.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.gb.api.GbAttachmentApi;
import com.yiling.sjms.gb.dto.AttachmentDTO;
import com.yiling.sjms.gb.dto.request.SaveGBFileRequest;
import com.yiling.sjms.gb.entity.GbAttachmentDO;
import com.yiling.sjms.gb.service.GbAttachmentService;

/**
 * 团购证据信息
 *
 * @author: wei.wang
 * @date: 2022/11/28
 */
@DubboService
public class GbAttachmentApiImpl implements GbAttachmentApi {
    @Autowired
    private GbAttachmentService gbAttachmentService;
    /**
     * 根据表单id获取团购证据
     * @param gbId
     * @return
     */
    @Override
    public List<AttachmentDTO> listByGbId(Long gbId) {
        return PojoUtils.map(gbAttachmentService.listByGbId(gbId),AttachmentDTO.class);
    }

    @Override
    public Boolean addSupport(SaveGBFileRequest request) {
        return gbAttachmentService.addSupport(request);
    }

    @Override
    public AttachmentDTO getAttachmentByMD5(String md5,Integer type) {
        return PojoUtils.map(gbAttachmentService.getAttachmentByMD5(md5,type),AttachmentDTO.class);
    }


}
