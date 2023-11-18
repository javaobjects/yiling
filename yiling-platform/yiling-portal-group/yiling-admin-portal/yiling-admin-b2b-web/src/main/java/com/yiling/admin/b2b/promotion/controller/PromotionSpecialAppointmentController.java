package com.yiling.admin.b2b.promotion.controller;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.b2b.promotion.form.PromotionSpecialActivityAppointmentPageForm;
import com.yiling.admin.b2b.promotion.vo.PromotionSpecialActivityAppointmentPageVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.promotion.api.PromotionActivityApi;
import com.yiling.marketing.promotion.dto.PromotionSpecialActivityAppointmentPageDTO;
import com.yiling.marketing.promotion.dto.request.SpecialActivityPageRequest;
import com.yiling.user.system.bo.CurrentAdminInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 专场活动预约表 前端控制器
 * </p>
 *
 * @author shixing.sun
 * @date 2022-05-20
 */
@RestController
@Slf4j
@Api(tags = "专场活动预约管理-运营后台")
@RequestMapping("/promotion/special/appointment")
public class PromotionSpecialAppointmentController extends BaseController {

    @DubboReference
    PromotionActivityApi promotionActivityApi;

    @ApiOperation(value = "分页查询预约管理-运营后台")
    @PostMapping("/pageList")
    public Result<Page<PromotionSpecialActivityAppointmentPageVO>> pageList(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody PromotionSpecialActivityAppointmentPageForm form) {
        SpecialActivityPageRequest request = PojoUtils.map(form, SpecialActivityPageRequest.class);
        Page<PromotionSpecialActivityAppointmentPageDTO> activityAppointment = promotionActivityApi.querySpecialActivityAppointment(request);
        Page<PromotionSpecialActivityAppointmentPageVO> pageVO = PojoUtils.map(activityAppointment, PromotionSpecialActivityAppointmentPageVO.class);
        return Result.success(pageVO);
    }

}
