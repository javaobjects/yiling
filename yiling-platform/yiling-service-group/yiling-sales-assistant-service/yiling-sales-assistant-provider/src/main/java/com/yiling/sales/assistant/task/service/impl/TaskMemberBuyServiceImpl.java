package com.yiling.sales.assistant.task.service.impl;

import java.awt.*;
import java.util.Objects;

import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.aliyun.oss.model.ObjectMetadata;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.bo.FileInfo;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.sales.assistant.task.dao.TaskMemberBuyMapper;
import com.yiling.sales.assistant.task.dto.TaskMemberDTO;
import com.yiling.sales.assistant.task.dto.request.app.InviteTaskMemberRequest;
import com.yiling.sales.assistant.task.entity.TaskMemberBuyDO;
import com.yiling.sales.assistant.task.entity.UserTaskShareDO;
import com.yiling.sales.assistant.task.service.TaskMemberBuyService;
import com.yiling.sales.assistant.task.service.UserTaskShareService;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.member.api.MemberApi;
import com.yiling.user.member.dto.MemberBuyStageDTO;
import com.yiling.user.member.dto.MemberDetailDTO;

import cn.hutool.core.codec.Base64;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 会员推广-单独购买任务 服务实现类
 * </p>
 *
 * @author gxl
 * @date 2021-12-16
 */
@Service
@Slf4j
public class TaskMemberBuyServiceImpl extends BaseServiceImpl<TaskMemberBuyMapper, TaskMemberBuyDO> implements TaskMemberBuyService {
    @DubboReference
    private MemberApi memberApi;

    @DubboReference
    private EnterpriseApi enterpriseApi;

    @Autowired
    private FileService fileService;
    @Autowired
    private UserTaskShareService userTaskShareService;

    @Value("${task.memberShareUrl}")
    private String taskMemberShareUrl;


    @Override
    public TaskMemberDTO getMemberById(Long taskId) {
        LambdaQueryWrapper<TaskMemberBuyDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(TaskMemberBuyDO::getTaskId, taskId);
        TaskMemberBuyDO memberBuyDO = this.getOne(wrapper);
        TaskMemberDTO taskMemberDTO = new TaskMemberDTO();
        PojoUtils.map(memberBuyDO, taskMemberDTO);
        MemberDetailDTO member = memberApi.getMember(memberBuyDO.getMemberId());
        taskMemberDTO.setName(member.getName());
        MemberBuyStageDTO memberBuyStage = member.getMemberBuyStageList().stream().filter(memberBuyStageDTO -> memberBuyStageDTO.getId().equals(memberBuyDO.getMemberStageId())).findAny().orElse(null);
        if (Objects.isNull(memberBuyStage)) {
            return taskMemberDTO;
        }
        taskMemberDTO.setPrice(memberBuyStage.getPrice()).setValidTime(memberBuyStage.getValidTime()).setPlaybill(fileService.getUrl(memberBuyDO.getPlaybill(), FileTypeEnum.TASK_MEMBER_SHARE)).setMemberId(member.getId()).setMemberStageId(memberBuyDO.getMemberStageId());
        return taskMemberDTO;
    }

    @Override
    public String getPromotionPic(InviteTaskMemberRequest inviteTaskMemberRequest) {
        LambdaQueryWrapper<UserTaskShareDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(UserTaskShareDO::getUserTaskId, inviteTaskMemberRequest.getUserTaskId());
        if(Objects.nonNull(inviteTaskMemberRequest.getEid())){
            wrapper.eq(UserTaskShareDO::getEid,0);
        }
        wrapper.last("limit 1");

        UserTaskShareDO taskShareDO = userTaskShareService.getOne(wrapper);
        String userQrcode = null;
        Long eid = 0L;
        if (Objects.isNull(taskShareDO)) {
            String eidStr = "id";

            if(Objects.nonNull(inviteTaskMemberRequest.getEid())){
                eidStr = inviteTaskMemberRequest.getEid().toString();
                eid = inviteTaskMemberRequest.getEid();
            }
            String shareUrl = taskMemberShareUrl+eidStr+"/"+inviteTaskMemberRequest.getOpUserId();
            log.info("会员任务分享shareUrl={}",shareUrl);
            userQrcode = this.generateQrCode(shareUrl, "二维码.jpg").getKey();
            UserTaskShareDO userTaskShareDO = new UserTaskShareDO();
            userTaskShareDO.setQrcodeKey(userQrcode);
            userTaskShareDO.setTaskId(inviteTaskMemberRequest.getTaskId()).setUserTaskId(inviteTaskMemberRequest.getUserTaskId()).setCreateUser(inviteTaskMemberRequest.getOpUserId()).setEid(eid);
            userTaskShareService.save(userTaskShareDO);
        } else {
            userQrcode = taskShareDO.getQrcodeKey();
        }
        String textStr = "";
        if(eid>0){
            EnterpriseDTO enterpriseDTO = enterpriseApi.getById(eid);
            if(enterpriseDTO.getName().length()>17){
                enterpriseDTO.setName(enterpriseDTO.getName().substring(0,13)+"...");
            }
            textStr = "/watermark,text_"+Base64.encodeUrlSafe("邀请方："+enterpriseDTO.getName())+",color_dddbdc,size_30,g_sw,x_30,y_426";
        }
        LambdaQueryWrapper<TaskMemberBuyDO> memberBuyDOLambdaQueryWrapper = Wrappers.lambdaQuery();
        memberBuyDOLambdaQueryWrapper.eq(TaskMemberBuyDO::getTaskId, inviteTaskMemberRequest.getTaskId());
        TaskMemberBuyDO memberBuyDO = this.getOne(memberBuyDOLambdaQueryWrapper);
        String url = fileService.getUrl(memberBuyDO.getPlaybill(), FileTypeEnum.TASK_MEMBER_SHARE) + "?x-oss-process=image/watermark,image_" + Base64.encodeUrlSafe(userQrcode)+",g_sw,x_95,y_183";
        if(StringUtils.isNotEmpty(textStr)){
            url = url+textStr;
        }
        return url;
    }

    @Override
    public FileInfo generateQrCode(String content, String originalFileName) {
        QrConfig config = new QrConfig(182, 182);
        // 设置边距，既二维码和背景之间的边距
        config.setMargin(1);
        // 设置前景色，既二维码颜色
        config.setForeColor(Color.BLACK);
        // 设置背景色
        config.setBackColor(Color.WHITE);
        //不设报错
        config.setQrVersion(null);

        // 生成二维码到文件，也可以到流
        byte[] generatePng = QrCodeUtil.generatePng(content, config);
        FileInfo fileInfo = null;
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("image/jpg");
        try {
            fileInfo = fileService.upload(generatePng, originalFileName, FileTypeEnum.TASK_MEMBER_SHARE, metadata);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileInfo;
    }
}
