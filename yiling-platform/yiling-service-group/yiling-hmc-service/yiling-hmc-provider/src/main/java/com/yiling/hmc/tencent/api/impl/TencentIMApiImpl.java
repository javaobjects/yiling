package com.yiling.hmc.tencent.api.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.beust.jcommander.internal.Lists;
import com.google.common.collect.Maps;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.exception.ServiceException;
import com.yiling.hmc.config.TencentServiceConfig;
import com.yiling.hmc.tencent.api.TencentIMApi;
import com.yiling.hmc.tencent.dto.AccountOnlineStatusDTO;
import com.yiling.hmc.tencent.dto.PortraitGetDTO;
import com.yiling.hmc.tencent.dto.TencentImSigDTO;
import com.yiling.hmc.tencent.dto.UserProfileItemDTO;
import com.yiling.hmc.tencent.dto.request.*;
import com.yiling.hmc.tencent.enums.OnlineStatusEnum;
import com.yiling.hmc.tencent.feign.TencentIMClient;
import com.yiling.hmc.tencent.feign.response.IMUnReadMsgNumResponse;
import com.yiling.hmc.tencent.feign.response.PortraitSetResponse;
import com.yiling.hmc.tencent.utils.GenerateUserSignature;
import com.yiling.ih.user.api.DoctorApi;
import com.yiling.ih.user.dto.DoctorAppInfoDTO;
import com.yiling.user.common.util.Constants;
import com.yiling.user.system.api.HmcUserApi;
import com.yiling.user.system.bo.HmcUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 腾讯IM API
 *
 * @author: fan.shen
 * @date: 2022/5/31
 */
@Slf4j
@DubboService
public class TencentIMApiImpl implements TencentIMApi {

    @Autowired
    TencentIMClient tencentIMClient;

    @DubboReference
    DoctorApi doctorApi;

    @Autowired
    TencentServiceConfig tencentServiceConfig;

    @DubboReference
    HmcUserApi hmcUserApi;

    @Value("${env.name}")
    private String envName;

    @Override
    public Integer unreadMsgNum(Long userId) {
        Map<String, String> param = new HashMap<>();
        param.put("To_Account", String.valueOf(userId));
        String result = tencentIMClient.unreadMsgNum(JSONUtil.toJsonStr(param));
        if (StrUtil.isBlank(result)) {
            log.error("API调用失败");
            throw new ServiceException("腾讯API调用失败：");
        }
        IMUnReadMsgNumResponse imUnReadMsgNumResponse = JSONUtil.toBean(result, IMUnReadMsgNumResponse.class);
        log.info(JSONUtil.toJsonStr(result));
        if (imUnReadMsgNumResponse.getErrorCode() != 0) {
            log.error("API调用失败：data={}", JSONUtil.toJsonStr(result));
            throw new ServiceException("腾讯API调用失败：" + imUnReadMsgNumResponse.getErrorInfo());
        }
        return imUnReadMsgNumResponse.getAllC2CUnreadMsgNum();
    }

    @Override
    public UserProfileItemDTO portraitGet(Long userId, Integer type) {
        Map<String, Object> param = new HashMap<>();
        String To_Account = null;
        // IH 医生
        if (type == 1) {
            To_Account = envName + "_" + "IH_DOC_" + userId;
        }
        // 患者
        if (type == 2) {
            To_Account = envName + "_" + "HMC_" + userId;
        }
        param.put("To_Account", Lists.newArrayList(To_Account));
        param.put("TagList", Lists.newArrayList("Tag_Profile_IM_Nick", "Tag_Profile_IM_Image"));
        log.info("");
        String result = tencentIMClient.portraitGet(JSONUtil.toJsonStr(param));
        if (StrUtil.isBlank(result)) {
            log.error("API调用失败");
            throw new ServiceException("腾讯API调用失败：");
        }
        JSONObject jsonObject = JSONUtil.parseObj(result);
        if (!jsonObject.containsKey("ErrorCode")) {
            throw new ServiceException("腾讯API调用失败：" + result);
        }
        if (jsonObject.getInt("ErrorCode") != 0) {
            throw new ServiceException("腾讯API调用失败：" + result);
        }
        if (!jsonObject.containsKey("UserProfileItem")) {
            throw new ServiceException("腾讯API调用失败：" + result);
        }
        if (jsonObject.getJSONArray("UserProfileItem").getJSONObject(0).getInt("ResultCode") != 0) {
            return null;
        }
        return JSONUtil.toBean(jsonObject.getJSONArray("UserProfileItem").getJSONObject(0), UserProfileItemDTO.class);
    }

    @Override
    public Boolean portraitSet(Long userId, Integer type) {
        // 医生
        Map<String, Object> param = new HashMap<>();
        HashMap<Object, Object> Tag_Profile_IM_Nick = Maps.newHashMap();
        HashMap<Object, Object> Tag_Profile_IM_Image = Maps.newHashMap();
        List<Object> ProfileItem = Lists.newArrayList();
        ProfileItem.add(Tag_Profile_IM_Nick);
        ProfileItem.add(Tag_Profile_IM_Image);
        // 医生
        if (type == 1) {
            param.put("From_Account", envName + "_" + "IH_DOC_" + userId);
            DoctorAppInfoDTO doctorAppInfoDTO = doctorApi.getDoctorInfoByDoctorId(userId.intValue());

            Tag_Profile_IM_Nick.put("Tag", "Tag_Profile_IM_Nick");
            Tag_Profile_IM_Nick.put("Value", doctorAppInfoDTO.getDoctorName());

            Tag_Profile_IM_Image.put("Tag", "Tag_Profile_IM_Image");
            Tag_Profile_IM_Image.put("Value", doctorAppInfoDTO.getPicture());


            param.put("ProfileItem", ProfileItem);
            String result = tencentIMClient.portraitSet(JSONUtil.toJsonStr(param));
            if (StrUtil.isBlank(result)) {
                log.error("API调用失败");
                throw new ServiceException("腾讯API调用失败：");
            }
            PortraitSetResponse portraitSetResponse = JSONUtil.toBean(result, PortraitSetResponse.class);
            log.info(JSONUtil.toJsonStr(portraitSetResponse));
            if (portraitSetResponse.getErrorCode() != 0) {
                log.error("API调用失败：data={}", JSONUtil.toJsonStr(result));
                throw new ServiceException("腾讯API调用失败：" + portraitSetResponse.getErrorDisplay());
            }
            return true;
        }
        // C端用户
        if (type == 2) {
            HmcUser hmcUser = hmcUserApi.getById(userId);
            param.put("From_Account", envName + "_" + "HMC_" + userId);

            Tag_Profile_IM_Nick.put("Tag", "Tag_Profile_IM_Nick");
            Tag_Profile_IM_Nick.put("Value", hmcUser.getNickName());

            Tag_Profile_IM_Image.put("Tag", "Tag_Profile_IM_Image");
            Tag_Profile_IM_Image.put("Value", StrUtil.isBlank(hmcUser.getAvatarUrl()) ? Constants.HZ_DEFAULT_AVATAR : hmcUser.getAvatarUrl());

        }
        return false;
    }

    @Override
    public String patientSendMsgToDoctor(PatientSendMsgToDoctorRequest request) {
        String result = tencentIMClient.patientSendMsgToDoctor(JSONUtil.toJsonStr(request));
        log.info("腾讯im，患者给医生发送消息入参：{},返回值：{}", JSONUtil.toJsonStr(request), result);
        return result;
    }

    @Override
    public TencentImSigDTO getTencentIMSig(Long currentUserId, Integer type) {
        // 1、生成腾讯IM密码
        String userId = null;
        // IH 医生
        if (type == 1) {
            userId = envName + "_" + "IH_DOC_" + currentUserId;
        }
        // 患者
        if (type == 2) {
            userId = envName + "_" + "HMC_" + currentUserId;
        }
        log.info("userId:{}", userId);
        String signature = GenerateUserSignature.GenTLSSignature(tencentServiceConfig.getTencentIMAppId(), userId, GenerateUserSignature.EXPIRETIME, null, tencentServiceConfig.getTencentIMSecretKey());
        TencentImSigDTO sigDTO = new TencentImSigDTO();
        sigDTO.setTencentIMUserSig(signature);
        sigDTO.setTencentIMAppId(tencentServiceConfig.getTencentIMAppId());
        sigDTO.setUserId(userId);

        // 2、导入用户到腾讯IM
        accountImport(currentUserId, type);
        return sigDTO;
    }

    @Override
    public Boolean accountImport(Long userId, Integer type) {

        // 校验用户是否存在 -> 不存在则导入
        Boolean checkResult = accountCheck(userId, type);
        if (checkResult) {
            log.info("账户已导入");
            return Boolean.TRUE;
        }

        Map<String, String> map = Maps.newHashMap();

        // IH 医生
        if (type == 1) {
            DoctorAppInfoDTO doctorAppInfoDTO = doctorApi.getDoctorInfoByDoctorId(userId.intValue());
            map.put("UserID", "IH_DOC_" + userId);
            map.put("Nick", doctorAppInfoDTO.getDoctorName());
            map.put("FaceUrl", doctorAppInfoDTO.getPicture());
        }

        // 患者
        if (type == 2) {
            HmcUser hmcUser = hmcUserApi.getById(userId);
            map.put("UserID", "HMC_" + userId);
            map.put("Nick", hmcUser.getNickName());
            map.put("FaceUrl", StrUtil.isBlank(hmcUser.getAvatarUrl()) ? Constants.HZ_DEFAULT_AVATAR : hmcUser.getAvatarUrl());
        }

        String result = tencentIMClient.accountImport(JSONUtil.toJsonStr(map));
        if (StrUtil.isBlank(result)) {
            log.error("API调用失败");
            throw new ServiceException("腾讯API调用失败：");
        }
        if (JSONUtil.parseObj(result).getInt("ErrorCode") != 0) {
            log.error("API调用失败：data={}", JSONUtil.toJsonStr(result));
            throw new ServiceException("腾讯API调用失败：" + result);
        }

        return true;
    }

    @Override
    public Boolean accountCheck(Long userId, Integer type) {
        Map map = Maps.newHashMap();
        Map userIdMap = Maps.newHashMap();
        List<Object> list = Lists.newArrayList();
        list.add(userIdMap);
        map.put("CheckItem", list);
        // IH 医生
        if (type == 1) {
            userIdMap.put("UserID", "IH_DOC_" + userId);
        }
        // 患者
        if (type == 2) {
            userIdMap.put("UserID", "HMC_" + userId);
        }
        String result = tencentIMClient.accountCheck(JSONUtil.toJsonStr(map));
        if (StrUtil.isBlank(result)) {
            log.error("API调用失败");
            throw new ServiceException("腾讯API调用失败：");
        }
        if (JSONUtil.parseObj(result).getInt("ErrorCode") != 0) {
            log.error("API调用失败：data={}", JSONUtil.toJsonStr(result));
            throw new ServiceException("腾讯API调用失败：" + result);
        }
        JSONArray resultItem = JSONUtil.parseObj(result).getJSONArray("ResultItem");
        if (JSONUtil.parseObj(resultItem.get(0).toString()).getStr("AccountStatus").equals("Imported")) {
            return true;
        }
        return false;
    }

    @Override
    public Map<String, Integer> queryOnlineStatus(List<String> accountList) {
        Map<String, Integer> resultMap = new HashMap<>();
        Map<String, Object> param = new HashMap<>();
        param.put("To_Account", accountList);
        String result = tencentIMClient.queryOnlineStatus(JSONUtil.toJsonStr(param));
        if (StrUtil.isBlank(result)) {
            log.error("API调用失败");
            throw new ServiceException("腾讯API调用失败：");
        }
        if (JSONUtil.parseObj(result).getInt("ErrorCode") != 0) {
            log.error("API调用失败：data={}", JSONUtil.toJsonStr(result));
            throw new ServiceException("腾讯API调用失败：" + result);
        }
        JSONArray resultItem = JSONUtil.parseObj(result).getJSONArray("QueryResult");
        List<AccountOnlineStatusDTO> dtoList = resultItem.toList(AccountOnlineStatusDTO.class);
        for (AccountOnlineStatusDTO accountOnlineStatusDTO : dtoList) {
            resultMap.put(accountOnlineStatusDTO.getTo_Account(), OnlineStatusEnum.getByName(accountOnlineStatusDTO.getStatus()).getType());
        }
        return resultMap;
    }

    @Override
    public String createGroup(CreateGroupRequest request) {
        log.info("创建群组参数:{}", JSONUtil.toJsonStr(request));
        String result = tencentIMClient.createGroup(JSONUtil.toJsonStr(request));
        log.info("创建群组返回:{}", result);
        if (StrUtil.isBlank(result)) {
            log.error("API调用失败");
            throw new ServiceException("腾讯API调用失败：");
        }
        if (JSONUtil.parseObj(result).getInt("ErrorCode") != 0) {
            log.error("API调用失败：data={}", JSONUtil.toJsonStr(result));
            throw new ServiceException("腾讯API调用失败：" + result);
        }
        return JSONUtil.parseObj(result).getStr("GroupId");
    }

    @Override
    public String sendGroupMsg(SendGroupMsgRequest request) {
        log.info("发送群组消息参数:{}", JSONUtil.toJsonStr(request));
        String result = tencentIMClient.sendGroupMsg(JSONUtil.toJsonStr(request));
        log.info("发送群组消息返回:{}", result);
        if (StrUtil.isBlank(result)) {
            log.error("API调用失败");
            throw new ServiceException("腾讯API调用失败：");
        }
        if (JSONUtil.parseObj(result).getInt("ErrorCode") != 0) {
            log.error("API调用失败：data={}", JSONUtil.toJsonStr(result));
            throw new ServiceException("腾讯API调用失败：" + result);
        }
        return result;
    }

    @Override
    public String getGroupInfo(GetGroupInfoRequest request) {
        log.info("获取群组信息参数:{}", JSONUtil.toJsonStr(request));
        String result = tencentIMClient.getGroupInfo(JSONUtil.toJsonStr(request));
        log.info("获取群组信息返回:{}", result);
        if (StrUtil.isBlank(result)) {
            log.error("API调用失败");
            throw new ServiceException("腾讯API调用失败：");
        }
        if (JSONUtil.parseObj(result).getInt("ErrorCode") != 0) {
            log.error("API调用失败：data={}", JSONUtil.toJsonStr(result));
            throw new ServiceException("腾讯API调用失败：" + result);
        }
        return result;
    }
}