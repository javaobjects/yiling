package com.yiling.hmc.activity.api.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import com.aliyun.oss.model.ObjectMetadata;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.oss.bo.FileInfo;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.hmc.activity.api.HMCActivityApi;
import com.yiling.hmc.activity.dto.ActivityDTO;
import com.yiling.hmc.activity.dto.ActivityDocToPatientDTO;
import com.yiling.hmc.activity.dto.request.BaseActivityRequest;
import com.yiling.hmc.activity.dto.request.QueryActivityRequest;
import com.yiling.hmc.activity.dto.request.SaveActivityDocPatientRequest;
import com.yiling.hmc.activity.dto.request.SaveActivityGoodsPromoteRequest;
import com.yiling.hmc.activity.service.ActivityService;
import com.yiling.hmc.config.H5DomainProperties;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;

/**
 * 患教活动API
 *
 * @author: fan.shen
 * @date: 2022/09/01
 */
@Slf4j
@DubboService
public class HMCActivityApiImpl implements HMCActivityApi {

    @Autowired
    private ActivityService activityService;

    @Autowired
    private WxMpService mpService;

    @Autowired
    H5DomainProperties domainProperties;

    @Autowired
    FileService fileService;

    @Override
    public Page<ActivityDTO> pageList(QueryActivityRequest request) {
        return activityService.pageList(request);
    }

    @Override
    public Long saveOrUpdateDocToPatient(SaveActivityDocPatientRequest request) {
        return activityService.saveOrUpdateDocToPatient(request);
    }

    @Override
    public ActivityDocToPatientDTO queryActivityById(Long id) {
        return activityService.queryActivityById(id);
    }

    @Override
    public List<ActivityDocToPatientDTO> queryActivityByIdList(List<Long> idList) {
        return activityService.queryActivityByIdList(idList);
    }

    @Override
    public Boolean stopActivity(BaseActivityRequest request) {
        return activityService.stopActivity(request);
    }

    @Override
    public Boolean delActivity(BaseActivityRequest request) {
        return activityService.delActivity(request);
    }

    @Override
    public List<ActivityDTO> queryActivity(QueryActivityRequest request) {
        return activityService.queryActivity(request);
    }

    @Override
    public String getQrCodeUrl(Long activityId, Integer doctorId) {
        try {
            String sceneStr = "qt:40_docId:" + doctorId;
            if (Objects.nonNull(activityId) && activityId.compareTo(0L) > 0) {
                sceneStr += "_actId:" + activityId;
            }
            WxMpQrCodeTicket ticket = mpService.getQrcodeService().qrCodeCreateLastTicket(sceneStr);
            return this.mpService.getQrcodeService().qrCodePictureUrl(ticket.getTicket());
        } catch (Exception e) {
            log.error("生成HMC活动二维码出错，错误信息:{}", e.getMessage(), e);
        }
        return null;
    }

    @Override
    public String getGoodsPromoteQrCodeUrl(Integer activityId, Integer doctorId) throws Exception {
        // todo
        String content = domainProperties.getDomainUrl() + "active/salesPromotion/product/details/" + "?activityId=" + activityId + "&doctorId=" + doctorId +"&activitySource=3";
        byte[] bytes = QrCodeUtil.generatePng(content, 300, 300);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("image/png");
        FileInfo fileInfo = fileService.upload(bytes, RandomUtil.randomString(20) + Constants.FILE_SUFFIX, FileTypeEnum.HMC_ACTIVITY_PIC, metadata);
        return fileInfo.getUrl();
    }

    @Override
    public ActivityDTO queryActivityGoodsPromoteById(Long id) {
        return activityService.queryActivity(id);
    }

    @Override
    public Long saveOrUpdateGoodsPromote(SaveActivityGoodsPromoteRequest request) {
        return activityService.saveOrUpdateGoodsPromote(request);
    }

    //
    // @Override
    // public Page<ActivityPatientEducateDTO> pageList(QueryActivityPatientEducateRequest request) {
    //     return activityPatientEducateService.pageList(request);
    // }
    //
    // @Override
    // public ActivityPatientEducateDTO saveActivityPatientEducate(SaveActivityPatientEducationRequest request) {
    //     return activityPatientEducateService.saveActivityPatientEducate(request);
    // }
    //
    // @Override
    // public ActivityPatientEducateDTO queryActivityById(Long id) {
    //     return activityPatientEducateService.queryById(id);
    // }
    //
    // @Override
    // public List<ActivityPatientEducateDTO> queryActivity(QueryActivityRequest request) {
    //     return activityPatientEducateService.queryActivity(request);
    // }
    //
    // @Override
    // public boolean delActivityById(Long id) {
    //     return activityPatientEducateService.delActivityById(id);
    // }
    //
    // @Override
    // public List<ActivityPatientEducateDTO> getActivityPatientEducate(List<Long> idList) {
    //     return activityPatientEducateService.getActivityPatientEducate(idList);
    // }
}
