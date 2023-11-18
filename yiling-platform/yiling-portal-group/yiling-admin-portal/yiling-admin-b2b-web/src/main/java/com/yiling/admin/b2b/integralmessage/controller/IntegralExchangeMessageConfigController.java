package com.yiling.admin.b2b.integralmessage.controller;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.b2b.integralmessage.form.DeleteIntegralMessageForm;
import com.yiling.admin.b2b.integralmessage.form.QueryIntegralMessagePageForm;
import com.yiling.admin.b2b.integralmessage.form.SaveIntegralExchangeMessageConfigForm;
import com.yiling.admin.b2b.integralmessage.form.UpdateIntegralMessageOrderForm;
import com.yiling.admin.b2b.integralmessage.form.UpdateIntegralMessageStatusForm;
import com.yiling.admin.b2b.integralmessage.vo.IntegralExchangeMessageConfigItemVO;
import com.yiling.admin.b2b.integralmessage.vo.IntegralExchangeMessageConfigVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.marketing.integralmessage.api.IntegralExchangeMessageConfigApi;
import com.yiling.marketing.integralmessage.dto.IntegralExchangeMessageConfigDTO;
import com.yiling.marketing.integralmessage.dto.request.DeleteIntegralMessageRequest;
import com.yiling.marketing.integralmessage.dto.request.QueryIntegralMessagePageRequest;
import com.yiling.marketing.integralmessage.dto.request.SaveIntegralExchangeMessageConfigRequest;
import com.yiling.marketing.integralmessage.dto.request.UpdateIntegralMessageOrderRequest;
import com.yiling.marketing.integralmessage.dto.request.UpdateIntegralMessageStatusRequest;
import com.yiling.user.system.bo.CurrentAdminInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * 积分兑换消息配置 Controller
 *
 * @author: lun.yu
 * @date: 2023-01-16
 */
@Slf4j
@RestController
@RequestMapping("/integralExchangeMessage")
@Api(tags = "积分兑换消息配置接口")
public class IntegralExchangeMessageConfigController extends BaseController {

    @DubboReference
    IntegralExchangeMessageConfigApi integralExchangeMessageConfigApi;

    @Autowired
    FileService fileService;

    @ApiOperation(value = "积分兑换消息配置分页")
    @PostMapping("/queryListPage")
    public Result<Page<IntegralExchangeMessageConfigItemVO>> queryListPage(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid QueryIntegralMessagePageForm form) {
        QueryIntegralMessagePageRequest request = PojoUtils.map(form, QueryIntegralMessagePageRequest.class);
        Page<IntegralExchangeMessageConfigItemVO> page = PojoUtils.map(integralExchangeMessageConfigApi.queryListPage(request), IntegralExchangeMessageConfigItemVO.class);
        // 设置图标url
        page.getRecords().forEach(integralExchangeMessageConfigItemVO -> {
            String url = fileService.getUrl(integralExchangeMessageConfigItemVO.getIcon(), FileTypeEnum.INTEGRAL_MESSAGE_PICTURE);
            integralExchangeMessageConfigItemVO.setIconUrl(url);
        });
        return Result.success(page);
    }

    @ApiOperation(value = "更新状态")
    @PostMapping("/updateStatus")
    @Log(title = "更新状态", businessType = BusinessTypeEnum.UPDATE)
    public Result<Boolean> updateStatus(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid UpdateIntegralMessageStatusForm form) {
        UpdateIntegralMessageStatusRequest request = PojoUtils.map(form, UpdateIntegralMessageStatusRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());
        return Result.success(integralExchangeMessageConfigApi.updateStatus(request));
    }

    @ApiOperation(value = "更新排序")
    @PostMapping("/updateOrder")
    @Log(title = "更新排序", businessType = BusinessTypeEnum.UPDATE)
    public Result<Boolean> updateOrder(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid UpdateIntegralMessageOrderForm form) {
        UpdateIntegralMessageOrderRequest request = PojoUtils.map(form, UpdateIntegralMessageOrderRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());
        return Result.success(integralExchangeMessageConfigApi.updateOrder(request));
    }

    @ApiOperation(value = "保存积分兑换消息配置")
    @PostMapping("/saveConfig")
    @Log(title = "保存积分兑换消息配置", businessType = BusinessTypeEnum.UPDATE)
    public Result<Boolean> saveConfig(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid SaveIntegralExchangeMessageConfigForm form) {
        SaveIntegralExchangeMessageConfigRequest request = PojoUtils.map(form, SaveIntegralExchangeMessageConfigRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());
        return Result.success(integralExchangeMessageConfigApi.saveConfig(request));
    }

    @ApiOperation(value = "查看")
    @GetMapping("/get")
    public Result<IntegralExchangeMessageConfigVO> get(@CurrentUser CurrentAdminInfo adminInfo, @ApiParam("ID") @RequestParam("id") Long id) {
        IntegralExchangeMessageConfigDTO messageConfigDTO  = integralExchangeMessageConfigApi.get(id);
        IntegralExchangeMessageConfigVO exchangeMessageConfigVO = PojoUtils.map(messageConfigDTO, IntegralExchangeMessageConfigVO.class);
        String url = fileService.getUrl(messageConfigDTO.getIcon(), FileTypeEnum.INTEGRAL_MESSAGE_PICTURE);
        exchangeMessageConfigVO.setIconUrl(url);
        return Result.success(exchangeMessageConfigVO);
    }

    @ApiOperation(value = "删除")
    @PostMapping("/deleteConfig")
    @Log(title = "删除", businessType = BusinessTypeEnum.DELETE)
    public Result<Boolean> deleteConfig(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid DeleteIntegralMessageForm form) {
        DeleteIntegralMessageRequest request = PojoUtils.map(form, DeleteIntegralMessageRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());
        return Result.success(integralExchangeMessageConfigApi.deleteConfig(request));
    }

}
