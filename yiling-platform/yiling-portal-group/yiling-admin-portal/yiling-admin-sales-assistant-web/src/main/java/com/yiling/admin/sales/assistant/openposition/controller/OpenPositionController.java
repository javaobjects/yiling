package com.yiling.admin.sales.assistant.openposition.controller;


import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import com.yiling.admin.sales.assistant.openposition.form.SaveOpenPositionForm;
import com.yiling.admin.sales.assistant.openposition.form.UpdateOpenPositionStatusForm;
import com.yiling.admin.sales.assistant.openposition.vo.OpenPositionItemVO;
import com.yiling.admin.sales.assistant.openposition.vo.OpenPositionVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.base.BaseDTO;
import com.yiling.framework.common.base.form.QueryPageListForm;
import com.yiling.framework.common.base.request.QueryPageListRequest;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.mall.openposition.api.OpenPositionApi;
import com.yiling.mall.openposition.dto.OpenPositionDTO;
import com.yiling.mall.openposition.dto.request.QueryOpenPositionPageRequest;
import com.yiling.mall.openposition.dto.request.SaveOpenPositionRequest;
import com.yiling.mall.openposition.dto.request.UpdateOpenPositionStatusRequest;
import com.yiling.mall.openposition.enums.OpenPositionPlatformEnum;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentAdminInfo;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.core.collection.CollUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 销售助手-开屏位 前端控制器
 * </p>
 *
 * @author lun.yu
 * @date 2023-06-26
 */
@Slf4j
@Api(tags = "开屏位接口")
@RestController
@RequestMapping("/openPosition")
public class OpenPositionController extends BaseController {

    @DubboReference
    OpenPositionApi openPositionApi;
    @DubboReference
    UserApi userApi;

    @Autowired
    FileService fileService;

    @ApiOperation(value = "分页查询开屏位")
    @PostMapping("/queryListPage")
    public Result<Page<OpenPositionItemVO>> queryListPage(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid QueryPageListForm form) {
        QueryOpenPositionPageRequest request = PojoUtils.map(form, QueryOpenPositionPageRequest.class);
        request.setPlatform(OpenPositionPlatformEnum.SA.getCode());
        Page<OpenPositionDTO> dtoPage = openPositionApi.queryListPage(request);
        if (CollUtil.isEmpty(dtoPage.getRecords())) {
            return Result.success(PojoUtils.map(dtoPage, OpenPositionItemVO.class));
        }

        Page<OpenPositionItemVO> voPage = PojoUtils.map(dtoPage, OpenPositionItemVO.class);
        List<Long> userIdList = voPage.getRecords().stream().map(OpenPositionItemVO::getUpdateUser).distinct().collect(Collectors.toList());
        Map<Long, String> nameMap = userApi.listByIds(userIdList).stream().collect(Collectors.toMap(BaseDTO::getId, UserDTO::getName));

        voPage.getRecords().forEach(openPositionItemVO -> {
            openPositionItemVO.setUpdateUserName(nameMap.get(openPositionItemVO.getUpdateUser()));
            openPositionItemVO.setPictureUrl(fileService.getUrl(openPositionItemVO.getPicture(), FileTypeEnum.B2B_OPEN_POSITION));
        });

        return Result.success(voPage);
    }

    @ApiOperation(value = "保存开屏位")
    @PostMapping("/saveOpenPosition")
    @Log(title = "保存开屏位", businessType = BusinessTypeEnum.UPDATE)
    public Result<Boolean> saveOpenPosition(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid SaveOpenPositionForm form) {
        SaveOpenPositionRequest request = PojoUtils.map(form, SaveOpenPositionRequest.class);
        request.setPlatform(OpenPositionPlatformEnum.SA.getCode());
        request.setOpUserId(adminInfo.getCurrentUserId());
        boolean result = openPositionApi.saveOpenPosition(request);
        return Result.success(result);
    }

    @ApiOperation(value = "删除开屏位")
    @GetMapping("/deleteOpenPosition")
    @Log(title = "删除开屏位", businessType = BusinessTypeEnum.DELETE)
    public Result<Boolean> deleteOpenPosition(@CurrentUser CurrentAdminInfo adminInfo, @ApiParam("ID") @RequestParam("id") Long id) {
        boolean result = openPositionApi.deleteOpenPosition(id, adminInfo.getCurrentUserId());
        return Result.success(result);
    }


    @ApiOperation(value = "更新状态")
    @PostMapping("/updateStatus")
    @Log(title = "更新状态", businessType = BusinessTypeEnum.UPDATE)
    public Result<Boolean> updateStatus(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid UpdateOpenPositionStatusForm form) {
        UpdateOpenPositionStatusRequest request = PojoUtils.map(form, UpdateOpenPositionStatusRequest.class);
        request.setPlatform(OpenPositionPlatformEnum.SA.getCode());
        request.setOpUserId(adminInfo.getCurrentUserId());
        boolean result = openPositionApi.updateStatus(request);
        return Result.success(result);
    }

    @ApiOperation(value = "详情")
    @GetMapping("/getById")
    public Result<OpenPositionVO> getById(@CurrentUser CurrentAdminInfo adminInfo, @ApiParam("ID") @RequestParam("id") Long id) {
        OpenPositionDTO openPositionDTO = openPositionApi.getById(id);
        OpenPositionVO openPositionVO = PojoUtils.map(openPositionDTO, OpenPositionVO.class);
        openPositionVO.setPictureUrl(fileService.getUrl(openPositionDTO.getPicture(), FileTypeEnum.B2B_OPEN_POSITION));
        return Result.success(openPositionVO);
    }

}
