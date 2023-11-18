package com.yiling.open.cms.diagnosis.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.config.TencentServiceConfig;
import com.yiling.hmc.tencent.api.TencentIMApi;
import com.yiling.hmc.tencent.dto.PortraitGetDTO;
import com.yiling.hmc.tencent.dto.TencentImSigDTO;
import com.yiling.hmc.tencent.dto.UserProfileItemDTO;
import com.yiling.hmc.tencent.dto.request.SendGroupMsgRequest;
import com.yiling.open.cms.diagnosis.utils.GenerateUserSignature;
import com.yiling.open.cms.diagnosis.vo.TencentIMSigVO;
import com.yiling.user.system.bo.CurrentUserInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * 腾讯IM控制器
 *
 * @Description
 * @Author fan.shen
 * @Date 2023/4/27
 */
@Slf4j
@RestController
@RequestMapping("/tencent/im")
@Api(tags = "腾讯IM控制器")
public class TencentIMController {

    @DubboReference
    TencentIMApi tencentIMApi;

    @Autowired
    TencentServiceConfig tencentServiceConfig;

    @ApiOperation(value = "获取腾讯IM通讯密码")
    @GetMapping("/getTencentIMSig")
    @Log(title = "获取腾讯IM通讯密码", businessType = BusinessTypeEnum.OTHER)
    public Result<TencentIMSigVO> getTencentIMSig(@CurrentUser CurrentUserInfo currentUser) {

        // 生成腾讯IM密码
        TencentImSigDTO tencentIMSig = tencentIMApi.getTencentIMSig(currentUser.getCurrentUserId(), 1);

        // 获取资料
        UserProfileItemDTO profileItemDTO = tencentIMApi.portraitGet(currentUser.getCurrentUserId(), 1);

        log.info("获取资料结果:{}", JSONUtil.toJsonStr(profileItemDTO));
        // 如果获取结果为空 -> 设置头像、昵称属性
        if (Objects.isNull(profileItemDTO) || CollUtil.isEmpty(profileItemDTO.getProfileItem())) {
            tencentIMApi.portraitSet(currentUser.getCurrentUserId(), 1);
        }
        return Result.success(PojoUtils.map(tencentIMSig, TencentIMSigVO.class));
    }

    /**
     * 查询用户未读消息数量
     *
     * @param currentUser
     * @return
     */
    @ApiOperation(value = "查询用户未读消息数量")
    @Log(title = "查询用户未读消息数量", businessType = BusinessTypeEnum.OTHER)
    @PostMapping("/unreadMsgNum")
    public Result<Integer> unreadMsgNum(@CurrentUser CurrentUserInfo currentUser) {
        return Result.success(tencentIMApi.unreadMsgNum(currentUser.getCurrentUserId()));
    }


    /**
     * 消息回调
     *
     * @return
     */
    @ApiOperation(value = "消息回调")
    @Log(title = "消息回调", businessType = BusinessTypeEnum.OTHER)
    @PostMapping("/msgCallBack")
    public String msgCallBack(@RequestBody String param) {

        log.info("msgCallBack:{}", param);
        return "{\n" +
                "    \"ActionStatus\": \"OK\",\n" +
                "    \"ErrorInfo\": \"\",\n" +
                "    \"ErrorCode\": 0 \n" +
                "}";
    }

    /**
     * 消息回调
     *
     * @return
     */
    @ApiOperation(value = "发送群消息")
    @Log(title = "发送群消息", businessType = BusinessTypeEnum.OTHER)
    @PostMapping("/sendGroupMsg")
    public String sendGroupMsg(@RequestBody String param) {

        SendGroupMsgRequest request = new SendGroupMsgRequest();
        tencentIMApi.sendGroupMsg(request);

        log.info("msgCallBack:{}", param);
        return "{\n" +
                "    \"ActionStatus\": \"OK\",\n" +
                "    \"ErrorInfo\": \"\",\n" +
                "    \"ErrorCode\": 0 \n" +
                "}";
    }

}
