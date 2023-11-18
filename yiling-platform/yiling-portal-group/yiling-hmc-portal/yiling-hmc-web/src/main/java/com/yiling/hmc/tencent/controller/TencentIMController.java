package com.yiling.hmc.tencent.controller;

import cn.hutool.core.util.StrUtil;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.hmc.tencent.api.TencentIMApi;
import com.yiling.ih.patient.api.HmcDiagnosisApi;
import com.yiling.user.system.bo.CurrentUserInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @DubboReference
    HmcDiagnosisApi hmcDiagnosisApi;

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

        if(StrUtil.isBlank(param)) {
            return StringUtils.EMPTY;
        }

        log.info("msgCallBack:\n{}", param);
        Boolean isSuccess = hmcDiagnosisApi.messageNotify(param);
        if (isSuccess) {
            return "{\n" +
                    "    \"ActionStatus\": \"OK\",\n" +
                    "    \"ErrorInfo\": \"\",\n" +
                    "    \"ErrorCode\": 0 \n" +
                    "}";
        }
        return "{\n" +
                "    \"ActionStatus\": \"FAIL\",\n" +
                "    \"ErrorInfo\": \"\",\n" +
                "    \"ErrorCode\": 1 \n" +
                "}";
    }

    /**
     * 事件回调
     *
     * @return
     */
    @ApiOperation(value = "事件回调")
    @Log(title = "事件回调", businessType = BusinessTypeEnum.OTHER)
    @PostMapping("/eventCallBack")
    public String eventCallBack(@RequestBody String param) {

        log.info("eventCallBack:\n{}", param);
        if(StrUtil.isBlank(param)) {
            return StringUtils.EMPTY;
        }
        Boolean isSuccess = hmcDiagnosisApi.eventCallBack(param);
        if (isSuccess) {
            return "{\n" +
                    "    \"ActionStatus\": \"OK\",\n" +
                    "    \"ErrorInfo\": \"\",\n" +
                    "    \"ErrorCode\": 0 \n" +
                    "}";
        }
        return "{\n" +
                "    \"ActionStatus\": \"FAIL\",\n" +
                "    \"ErrorInfo\": \"\",\n" +
                "    \"ErrorCode\": 1 \n" +
                "}";
    }

    /**
     * 云端录制回调
     *
     * @return
     */
    @ApiOperation(value = "云端录制回调")
    @Log(title = "云端录制回调", businessType = BusinessTypeEnum.OTHER)
    @PostMapping("/cloudRecordCallBack")
    public String cloudRecordCallBack(@RequestBody String param) {

        if(StrUtil.isBlank(param)) {
            return StringUtils.EMPTY;
        }

        log.info("cloudRecordCallBack:\n{}", param);

        return "{\n" +
                "    \"ActionStatus\": \"OK\",\n" +
                "    \"ErrorInfo\": \"\",\n" +
                "    \"ErrorCode\": 0 \n" +
                "}";
    }

}
