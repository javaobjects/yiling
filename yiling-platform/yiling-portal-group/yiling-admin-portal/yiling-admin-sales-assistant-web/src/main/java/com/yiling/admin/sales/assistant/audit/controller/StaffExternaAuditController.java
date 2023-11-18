package com.yiling.admin.sales.assistant.audit.controller;

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
import com.yiling.admin.sales.assistant.audit.form.AuditStaffExternaInfoForm;
import com.yiling.admin.sales.assistant.audit.form.QueryStaffExternaAuditPageListForm;
import com.yiling.admin.sales.assistant.audit.vo.StaffExternaAuditInfoVO;
import com.yiling.admin.sales.assistant.audit.vo.StaffExternaAuditListItemVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.user.system.api.StaffExternaAuditApi;
import com.yiling.user.system.bo.CurrentAdminInfo;
import com.yiling.user.system.dto.StaffExternaAuditDTO;
import com.yiling.user.system.dto.request.AuditStaffExternaInfoRequest;
import com.yiling.user.system.dto.request.QueryStaffExternaAuditPageListRequest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 外部员工个人信息审核 Controller
 *
 * @author: xuan.zhou
 * @date: 2022/1/21
 */
@Slf4j
@RestController
@RequestMapping("/staffExternaAudit")
@Api(tags = "证件审核模块")
public class StaffExternaAuditController extends BaseController {

    @Autowired
    FileService fileService;
    @DubboReference
    StaffExternaAuditApi staffExternaAuditApi;

    @ApiOperation(value = "审核记录分页列表")
    @PostMapping("/pageList")
    public Result<Page<StaffExternaAuditListItemVO>> pageList(@RequestBody QueryStaffExternaAuditPageListForm form) {
        QueryStaffExternaAuditPageListRequest request = PojoUtils.map(form, QueryStaffExternaAuditPageListRequest.class);
        Page<StaffExternaAuditDTO> page = staffExternaAuditApi.pageList(request);
        return Result.success(PojoUtils.map(page, StaffExternaAuditListItemVO.class));
    }

    @ApiOperation(value = "获取审核信息详情")
    @GetMapping("/get")
    public Result<StaffExternaAuditInfoVO> get(@RequestParam("id") Long id) {
        StaffExternaAuditDTO staffExternaAuditDTO = staffExternaAuditApi.getById(id);
        if (staffExternaAuditDTO == null) {
            return Result.failed("审核信息不存在");
        }

        StaffExternaAuditInfoVO staffExternaAuditInfoVO = PojoUtils.map(staffExternaAuditDTO, StaffExternaAuditInfoVO.class);
        staffExternaAuditInfoVO.setIdCardFrontPhotoUrl(fileService.getUrl(staffExternaAuditDTO.getIdCardFrontPhotoKey(), FileTypeEnum.ID_CARD_FRONT_PHOTO));
        staffExternaAuditInfoVO.setIdCardBackPhotoUrl(fileService.getUrl(staffExternaAuditDTO.getIdCardBackPhotoKey(), FileTypeEnum.ID_CARD_BACK_PHOTO));
        return Result.success(staffExternaAuditInfoVO);
    }

    @ApiOperation(value = "审核通过/驳回")
    @PostMapping("/audit")
    @Log(title = "审核通过/驳回",businessType = BusinessTypeEnum.UPDATE)
    public Result<Page<StaffExternaAuditListItemVO>> audit(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid AuditStaffExternaInfoForm form) {
        AuditStaffExternaInfoRequest request = PojoUtils.map(form, AuditStaffExternaInfoRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());
        boolean result = staffExternaAuditApi.audit(request);
        return result ? Result.success() : Result.failed(ResultCode.FAILED);
    }

}
