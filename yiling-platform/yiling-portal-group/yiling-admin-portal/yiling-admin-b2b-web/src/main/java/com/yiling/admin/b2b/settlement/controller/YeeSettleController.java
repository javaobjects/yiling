package com.yiling.admin.b2b.settlement.controller;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.b2b.settlement.form.QueryYeeSettlePageListForm;
import com.yiling.admin.b2b.settlement.vo.YeeSettlePageListItemVO;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.settlement.yee.api.YeeSettleSyncRecordApi;
import com.yiling.settlement.yee.dto.YeeSettleSyncRecordDTO;
import com.yiling.settlement.yee.dto.request.QueryYeeSettleListByPageRequest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: dexi.yao
 * @date: 2023-04-06
 */
@Api(tags = "会员回款记录")
@RestController
@RequestMapping("/yeeSettle")
@Slf4j
public class YeeSettleController extends BaseController {

    @DubboReference
    YeeSettleSyncRecordApi yeeSettleSyncRecordApi;

    /**
     * 分页查询结算记录
     *
     * @param form
     * @return
     */
    @ApiOperation("查询结算单列表")
    @PostMapping("/querySettlementPageList")
    Result<Page<YeeSettlePageListItemVO>> queryYeeSettlePageList(@RequestBody @Valid QueryYeeSettlePageListForm form) {
        Page<YeeSettleSyncRecordDTO> page = yeeSettleSyncRecordApi.queryListByPage(PojoUtils.map(form, QueryYeeSettleListByPageRequest.class));
        return Result.success(PojoUtils.map(page, YeeSettlePageListItemVO.class));
    }
}
