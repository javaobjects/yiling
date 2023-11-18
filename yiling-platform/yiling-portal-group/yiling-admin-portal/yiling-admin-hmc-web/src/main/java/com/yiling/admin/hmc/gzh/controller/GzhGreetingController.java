package com.yiling.admin.hmc.gzh.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.hmc.activity.form.*;
import com.yiling.admin.hmc.activity.vo.*;
import com.yiling.admin.hmc.gzh.form.GzhGreetingBaseForm;
import com.yiling.admin.hmc.gzh.form.QueryGzhGreetingForm;
import com.yiling.admin.hmc.gzh.form.SaveGzhGreetingForm;
import com.yiling.admin.hmc.gzh.vo.HmcGzhGreetingVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.hmc.activity.api.HMCActivityApi;
import com.yiling.hmc.activity.api.HMCActivityPatientEducateApi;
import com.yiling.hmc.activity.dto.ActivityDTO;
import com.yiling.hmc.activity.dto.ActivityDocToPatientDTO;
import com.yiling.hmc.activity.dto.request.BaseActivityRequest;
import com.yiling.hmc.activity.dto.request.QueryActivityRequest;
import com.yiling.hmc.activity.dto.request.SaveActivityDocPatientRequest;
import com.yiling.hmc.activity.enums.ActivityProgressEnum;
import com.yiling.hmc.activity.enums.ActivityTypeEnum;
import com.yiling.hmc.gzh.api.HmcGzhGreetingApi;
import com.yiling.hmc.gzh.dto.GzhGreetingDTO;
import com.yiling.hmc.gzh.dto.request.PublishGzhGreetingRequest;
import com.yiling.hmc.gzh.dto.request.QueryGzhGreetingRequest;
import com.yiling.hmc.gzh.dto.request.SaveGzhGreetingRequest;
import com.yiling.ih.user.api.DoctorApi;
import com.yiling.ih.user.api.IHActivityPatientEducateApi;
import com.yiling.ih.user.dto.ActivityDocPatientCountDTO;
import com.yiling.ih.user.dto.HmcActivityDocDTO;
import com.yiling.ih.user.dto.HmcActivityDocPatientDTO;
import com.yiling.ih.user.dto.HmcActivityDocPatientDetailDTO;
import com.yiling.ih.user.dto.request.*;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentAdminInfo;
import com.yiling.user.system.dto.UserDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 公众号欢迎语 Controller
 *
 * @author: fan.shen
 * @date: 2023-03-28
 */
@RestController
@RequestMapping("/gzhGreeting")
@Api(tags = "公众号欢迎语")
@Slf4j
public class GzhGreetingController extends BaseController {

    @DubboReference
    HmcGzhGreetingApi gzhGreetingApi;

    @ApiOperation(value = "公众号欢迎语列表")
    @PostMapping("/pageList")
    @Log(title = "公众号欢迎语列表", businessType = BusinessTypeEnum.OTHER)
    public Result<Page<HmcGzhGreetingVO>> pageList(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody QueryGzhGreetingForm form) {
        QueryGzhGreetingRequest request = PojoUtils.map(form, QueryGzhGreetingRequest.class);
        Page<GzhGreetingDTO> page = gzhGreetingApi.pageList(request);
        return Result.success(PojoUtils.map(page, HmcGzhGreetingVO.class));
    }

    @ApiOperation(value = "公众号欢迎语详情")
    @PostMapping("/getDetailById")
    @Log(title = "公众号欢迎语详情", businessType = BusinessTypeEnum.OTHER)
    public Result<HmcGzhGreetingVO> getDetailById(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid BaseActivityForm form) {
        GzhGreetingDTO gzhGreetingDTO = gzhGreetingApi.getDetailById(form.getId());
        return Result.success(PojoUtils.map(gzhGreetingDTO, HmcGzhGreetingVO.class));
    }

    @ApiOperation(value = "保存公众号欢迎语")
    @PostMapping("/saveGreetings")
    @Log(title = "保存公众号欢迎语", businessType = BusinessTypeEnum.INSERT)
    public Result<Long> saveGreetings(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid SaveGzhGreetingForm form) {

        GzhGreetingDTO checkIsExists = gzhGreetingApi.checkIsExists(form.getSceneId());
        if(Objects.isNull(form.getId()) && Objects.nonNull(checkIsExists)) {
            return Result.failed("此场景已经存在！");
        }

        SaveGzhGreetingRequest request = PojoUtils.map(form, SaveGzhGreetingRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());
        return Result.success(gzhGreetingApi.saveGreetings(request));
    }

    @ApiOperation(value = "发布公众号欢迎语")
    @PostMapping("/publishGreetings")
    @Log(title = "发布公众号欢迎语", businessType = BusinessTypeEnum.INSERT)
    public Result<Boolean> publishGreetings(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid GzhGreetingBaseForm form) {
        PublishGzhGreetingRequest request = PojoUtils.map(form, PublishGzhGreetingRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());
        return Result.success(gzhGreetingApi.publishGreetings(request));
    }

}
