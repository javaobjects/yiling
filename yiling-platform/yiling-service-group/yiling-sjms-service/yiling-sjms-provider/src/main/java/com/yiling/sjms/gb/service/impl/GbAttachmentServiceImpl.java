package com.yiling.sjms.gb.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.gb.dao.GbAttachmentMapper;
import com.yiling.sjms.gb.dto.AttachmentDTO;
import com.yiling.sjms.gb.dto.request.FileInfoRequest;
import com.yiling.sjms.gb.dto.request.SaveGBFileRequest;
import com.yiling.sjms.gb.entity.GbAttachmentDO;
import com.yiling.sjms.gb.entity.GbFormDO;
import com.yiling.sjms.gb.service.GbAttachmentService;
import com.yiling.sjms.gb.service.GbFormService;

/**
 * <p>
 * 团购附件 服务实现类
 * </p>
 *
 * @author wei.wang
 * @date 2022-11-28
 */
@Service
public class GbAttachmentServiceImpl extends BaseServiceImpl<GbAttachmentMapper, GbAttachmentDO> implements GbAttachmentService {

    @Autowired
    private GbFormService gbFormService;

    @Override
    public List<GbAttachmentDO> listByGbId(Long gbId) {
        QueryWrapper<GbAttachmentDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(GbAttachmentDO:: getGbId,gbId);
        return list(wrapper);
    }

    @Override
    public Boolean addSupport(SaveGBFileRequest request) {
        //GbFormDO gbFormDO = gbFormService.getOneByFormId(request.getGbId());
        GbAttachmentDO gbAttachmentDO = new GbAttachmentDO();
        gbAttachmentDO.setOpTime(request.getOpTime());
        gbAttachmentDO.setOpUserId(request.getOpUserId());
        LambdaQueryWrapper<GbAttachmentDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GbAttachmentDO::getGbId, request.getGbId());
        batchDeleteWithFill(gbAttachmentDO,queryWrapper);

        List<GbAttachmentDO> list = new ArrayList<>();
        for(FileInfoRequest fileKey : request.getFileKeyList()){
            GbAttachmentDO one = new GbAttachmentDO();
            one.setGbId(request.getGbId());
            one.setFileType(1);
            one.setFileMd5(fileKey.getFileMd5());
            one.setFileKey(fileKey.getFileUrl());
            one.setFileName(fileKey.getFileName());
            one.setOpTime(request.getOpTime());
            one.setOpUserId(request.getOpUserId());
            list.add(one);
        }
        return  saveBatch(list);
    }

    @Override
    public GbAttachmentDO getAttachmentByMD5(String md5,Integer type) {
        QueryWrapper<GbAttachmentDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(GbAttachmentDO:: getFileMd5,md5)
                .eq(GbAttachmentDO :: getFileType,type)
                .last(" limit 1 ");
        return getOne(wrapper);
    }
}
