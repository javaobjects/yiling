package com.yiling.admin.hmc.common.controller;


import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.hmc.common.form.AdvertisementDeleteForm;
import com.yiling.admin.hmc.common.form.AdvertisementPageForm;
import com.yiling.admin.hmc.common.form.AdvertisementSaveForm;
import com.yiling.admin.hmc.common.vo.AdvertisementVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.hmc.common.api.AdvertisementApi;
import com.yiling.hmc.common.dto.AdvertisementDTO;
import com.yiling.hmc.common.dto.request.AdvertisementDeleteRequest;
import com.yiling.hmc.common.dto.request.AdvertisementPageRequest;
import com.yiling.hmc.common.dto.request.AdvertisementSaveRequest;
import com.yiling.user.system.bo.CurrentAdminInfo;

import cn.hutool.core.collection.CollUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 * 广告
 * </p>
 *
 * @author yong.zhang
 * @date 2022-03-23
 */
@Api(tags = "广告")
@RestController
@RequestMapping("/advertisement/")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdvertisementController extends BaseController {
    @DubboReference
    AdvertisementApi advertisementApi;

    private final FileService fileService;

    @ApiOperation(value = "广告信息分页查询")
    @PostMapping("/pageList")
    public Result<Page<AdvertisementVO>> pageList(@CurrentUser CurrentAdminInfo currentAdminInfo, @RequestBody AdvertisementPageForm form) {
        AdvertisementPageRequest request = PojoUtils.map(form, AdvertisementPageRequest.class);
        Page<AdvertisementDTO> dtoPage = advertisementApi.pageList(request);
        Page<AdvertisementVO> voPage = PojoUtils.map(dtoPage, AdvertisementVO.class);
        List<AdvertisementVO> voList = new ArrayList<>();
        for (AdvertisementDTO record : dtoPage.getRecords()) {
            AdvertisementVO advertisementVO = PojoUtils.map(record, AdvertisementVO.class);
            advertisementVO.setPosition(operatePosition(record.getPosition()));
            advertisementVO.setPicUrl(fileService.getUrl(advertisementVO.getPic(), FileTypeEnum.ADVERTISEMENT_PIC));
            voList.add(advertisementVO);
        }
        voPage.setRecords(voList);
        return Result.success(voPage);
    }

    @ApiOperation(value = "通过id查询广告信息")
    @GetMapping("/queryById")
    public Result<AdvertisementVO> queryById(@RequestParam("id") @ApiParam("保险公司id") Long id) {
        AdvertisementDTO dto = advertisementApi.queryById(id);
        AdvertisementVO vo = PojoUtils.map(dto, AdvertisementVO.class);
        vo.setPicUrl(fileService.getUrl(dto.getPic(), FileTypeEnum.ADVERTISEMENT_PIC));
        vo.setPosition(operatePosition(dto.getPosition()));
        return Result.success(vo);
    }

    /**
     * position字符串转List<Integer>格式
     *
     * @param position string格式字符串
     * @return List<Integer>返回
     */
    private List<Integer> operatePosition(String position) {
        List<Integer> result = new ArrayList<>();
        if (StringUtils.isBlank(position)) {
            return result;
        }
        if (!position.contains(",")) {
            result.add(Integer.parseInt(position));
            return result;
        }
        String[] split = position.split(",");
        for (String string : split) {
            result.add(Integer.parseInt(string));
        }
        return result;
    }

    @ApiOperation(value = "广告信息新增和修改")
    @PostMapping("/save")
    public Result saveInsuranceCompany(@CurrentUser CurrentAdminInfo currentAdminInfo, @RequestBody @Valid AdvertisementSaveForm form) {
        AdvertisementSaveRequest request = PojoUtils.map(form, AdvertisementSaveRequest.class);
        if (CollUtil.isNotEmpty(form.getPosition())) {
            request.setPosition(StringUtils.join(form.getPosition(), ","));
        } else {
            request.setPosition("");
        }
        request.setOpUserId(currentAdminInfo.getCurrentUserId());
        boolean isSuccess = advertisementApi.saveAdvertisement(request);
        if (isSuccess) {
            return Result.success();
        } else {
            return Result.failed("保存出现问题");
        }
    }

    @ApiOperation(value = "广告删除")
    @PostMapping("/delete")
    public Result deleteInsuranceCompany(@CurrentUser CurrentAdminInfo currentAdminInfo, @RequestBody @Valid AdvertisementDeleteForm form) {
        AdvertisementDeleteRequest request = PojoUtils.map(form, AdvertisementDeleteRequest.class);
        request.setOpUserId(currentAdminInfo.getCurrentUserId());
        boolean isSuccess = advertisementApi.deleteAdvertisement(request);
        if (isSuccess) {
            return Result.success();
        } else {
            return Result.failed("删除出现问题");
        }
    }
}
