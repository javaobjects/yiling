package com.yiling.admin.b2b.integral.controller;

import java.util.List;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.b2b.integral.form.QueryIntegralRecordForm;
import com.yiling.admin.b2b.integral.vo.IntegralGiveUseRecordVO;
import com.yiling.admin.b2b.integral.vo.IntegralOrderRecordDetailVO;
import com.yiling.admin.b2b.integral.vo.UserSignDetailVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.CollectionObject;
import com.yiling.user.integral.api.IntegralRecordApi;
import com.yiling.user.integral.bo.UserSignDetailBO;
import com.yiling.user.integral.dto.IntegralOrderRecordDetailDTO;
import com.yiling.user.integral.dto.request.QueryIntegralRecordRequest;
import com.yiling.user.system.bo.CurrentAdminInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * 积分发放扣减记录 Controller
 *
 * @author: lun.yu
 * @date: 2023-01-10
 */
@Slf4j
@RestController
@RequestMapping("/integralRecord")
@Api(tags = "积分发放扣减记录接口")
public class IntegralRecordController extends BaseController {

    @DubboReference
    IntegralRecordApi integralRecordApi;

    @ApiOperation(value = "积分发放/扣减记录分页列表")
    @PostMapping("/queryListPage")
    public Result<Page<IntegralGiveUseRecordVO>> queryListPage(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid QueryIntegralRecordForm form) {
        QueryIntegralRecordRequest request = PojoUtils.map(form, QueryIntegralRecordRequest.class);
        Page<IntegralGiveUseRecordVO> page = PojoUtils.map(integralRecordApi.queryListPage(request), IntegralGiveUseRecordVO.class);
        return Result.success(page);
    }

    @ApiOperation(value = "查询积分订单明细（订单送积分）")
    @GetMapping("/getOrderDetailByRecordId")
    public Result<CollectionObject<IntegralOrderRecordDetailVO>> getOrderDetailByRecordId(@CurrentUser CurrentAdminInfo adminInfo, @ApiParam("ID") @RequestParam("id") Long id) {
        List<IntegralOrderRecordDetailDTO> orderRecordDetailDTOS = integralRecordApi.getOrderDetailByRecordId(id);
        List<IntegralOrderRecordDetailVO> recordDetailVOList = PojoUtils.map(orderRecordDetailDTOS, IntegralOrderRecordDetailVO.class);
        return Result.success(new CollectionObject<>(recordDetailVOList));
    }

    @ApiOperation(value = "查询签到明细（签到送积分）")
    @GetMapping("/getSignDetailByRecordId")
    public Result<CollectionObject<UserSignDetailVO>> getSignDetailByRecordId(@CurrentUser CurrentAdminInfo adminInfo, @ApiParam("ID") @RequestParam("id") Long id) {
        List<UserSignDetailBO> userSignDetailBOList = integralRecordApi.getSignDetailByRecordId(id);
        List<UserSignDetailVO> userSignDetailVOList = PojoUtils.map(userSignDetailBOList, UserSignDetailVO.class);
        return Result.success(new CollectionObject<>(userSignDetailVOList));
    }

}
