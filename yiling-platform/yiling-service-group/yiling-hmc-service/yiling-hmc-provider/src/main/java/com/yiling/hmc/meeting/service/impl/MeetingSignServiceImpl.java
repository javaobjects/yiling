package com.yiling.hmc.meeting.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.json.JSONUtil;
import com.aliyun.oss.model.ObjectMetadata;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.bo.FileInfo;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.hmc.config.H5DomainProperties;
import com.yiling.hmc.meeting.dto.MeetingSignDTO;
import com.yiling.hmc.meeting.dto.SubmitMeetingCheckCodeDTO;
import com.yiling.hmc.meeting.dto.SubmitMeetingSignDTO;
import com.yiling.hmc.meeting.dto.request.GetMeetingSignRequest;
import com.yiling.hmc.meeting.dto.request.SubmitCheckCodeRequest;
import com.yiling.hmc.meeting.dto.request.SubmitMeetingSignRequest;
import com.yiling.hmc.meeting.entity.MeetingSignDO;
import com.yiling.hmc.meeting.dao.MeetingSignMapper;
import com.yiling.hmc.meeting.service.MeetingSignService;
import com.yiling.framework.common.base.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 会议签到 服务实现类
 * </p>
 *
 * @author fan.shen
 * @date 2023-04-12
 */
@Slf4j
@Service
public class MeetingSignServiceImpl extends BaseServiceImpl<MeetingSignMapper, MeetingSignDO> implements MeetingSignService {


    @Autowired
    H5DomainProperties domainProperties;

    @Autowired
    FileService fileService;

    @Override
    public List<MeetingSignDTO> getMeetingSignInfo(GetMeetingSignRequest request) {
        QueryWrapper<MeetingSignDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StrUtil.isNotBlank(request.getCustomerName()), MeetingSignDO::getCustomerName, request.getCustomerName());
        List<MeetingSignDO> list = this.list(wrapper);
        return PojoUtils.map(list, MeetingSignDTO.class);
    }

    @Override
    public void saveMeeting(List<MeetingSignDTO> meetingSignDTOList) {
        List<MeetingSignDO> signDOList = PojoUtils.map(meetingSignDTOList, MeetingSignDO.class);
        this.saveBatch(signDOList);
    }

    @Override
    public SubmitMeetingSignDTO submitMeetingSign(SubmitMeetingSignRequest request) {
        SubmitMeetingSignDTO signDTO = new SubmitMeetingSignDTO();
        signDTO.setCustomerName(request.getCustomerName());
        if (Objects.nonNull(request.getId())) {
            QueryWrapper<MeetingSignDO> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(MeetingSignDO::getId, request.getId());
            MeetingSignDO one = this.getOne(wrapper);
            if (Objects.isNull(one)) {
                log.warn("根据id未获取到签到数据,跳过处理,入参：{}", JSONUtil.toJsonStr(request));
                signDTO.setSignResult(0);
                return signDTO;
            }
            // 如果已经签到->返回2
            if (Objects.nonNull(one.getSignFlag()) && Objects.nonNull(one.getSignTime())) {
                log.warn("当前用户已签到,入参：{}", JSONUtil.toJsonStr(one));
                signDTO.setSignResult(2);
                signDTO.setCheckCode(one.getCheckCode());
                signDTO.setFirstSignDate(one.getSignTime());
                return signDTO;
            }
            // 签到 -> 返回1
            one.setHospitalName(request.getHospitalName());
            one.setDepartmentName(request.getDepartmentName());
            one.setJobTitle(request.getJobTitle());
            one.setMobile(request.getMobile());
            one.setSysCode(request.getSysCode());
            one.setSignTime(DateUtil.date());
            one.setSignFlag(1);
            one.setMeetingSourceId(request.getMeetingSourceId());


            // 生成核销码
            // todo
            String content = domainProperties.getDomainUrl() + "scanSign/verify?id=" + one.getId() + "&customerName=" + request.getCustomerName() + "&hospitalName=" + request.getHospitalName();
            byte[] bytes = QrCodeUtil.generatePng(content, 300, 300);
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("image/png");
            try {
                FileInfo fileInfo = fileService.upload(bytes, RandomUtil.randomString(20) + Constants.FILE_SUFFIX, FileTypeEnum.HMC_ACTIVITY_PIC, metadata);
                one.setCheckCode(fileInfo.getUrl());
                one.setCheckFlag(2); // 未核销

                signDTO.setSignResult(1);
                signDTO.setCheckCode(fileInfo.getUrl());
            } catch (Exception e) {
                log.error("上传核销码异常，错误信息:{}", e.getMessage(), e);
                signDTO.setSignResult(-1);
                return signDTO;
            }
            this.updateById(one);
            return signDTO;
        } else {
            MeetingSignDO meetingSignDO = PojoUtils.map(request, MeetingSignDO.class);
            meetingSignDO.setSignFlag(1);
            meetingSignDO.setSignTime(DateUtil.date());
            this.save(meetingSignDO);

            // todo
            String content = domainProperties.getDomainUrl() + "scanSign/verify?id=" + meetingSignDO.getId() + "&customerName=" + request.getCustomerName() + "&hospitalName=" + request.getHospitalName();
            byte[] bytes = QrCodeUtil.generatePng(content, 300, 300);
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("image/png");
            try {
                FileInfo fileInfo = fileService.upload(bytes, RandomUtil.randomString(20) + Constants.FILE_SUFFIX, FileTypeEnum.HMC_ACTIVITY_PIC, metadata);
                meetingSignDO.setCheckCode(fileInfo.getUrl());
                meetingSignDO.setCheckFlag(2); // 未核销
                this.updateById(meetingSignDO);

                signDTO.setCheckCode(fileInfo.getUrl());
                signDTO.setSignResult(1);
                return signDTO;
            } catch (Exception e) {
                log.error("上传核销码异常，错误信息:{}", e.getMessage(), e);
                signDTO.setSignResult(-1);
                return signDTO;
            }
        }
    }

    @Override
    public SubmitMeetingCheckCodeDTO submitCheckCode(SubmitCheckCodeRequest request) {
        QueryWrapper<MeetingSignDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(MeetingSignDO::getId, request.getId());
        MeetingSignDO meetingSignDO = this.getOne(wrapper);
        SubmitMeetingCheckCodeDTO checkCodeDTO = new SubmitMeetingCheckCodeDTO();
        checkCodeDTO.setSignDate(meetingSignDO.getSignTime());
        // 判断如果已核销 -> 返回0
        if (meetingSignDO.getCheckFlag() == 1) {
            log.info("当前记录已核销，入参id:{},meetingSignDO：{}", request.getId(), JSONUtil.toJsonStr(meetingSignDO));
            checkCodeDTO.setCheckResult(0);
            checkCodeDTO.setCheckDate(meetingSignDO.getCheckTime());
            return checkCodeDTO;

        }
        meetingSignDO.setCheckFlag(1);
        meetingSignDO.setCheckTime(DateUtil.date());
        this.updateById(meetingSignDO);
        checkCodeDTO.setCheckResult(1);// 核销完成
        return checkCodeDTO;
    }
}
