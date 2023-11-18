package com.yiling.b2b.app.common.controller;


import java.util.List;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yiling.b2b.app.common.vo.B2bAppHotWordsVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.mall.banner.api.HotWordsApi;
import com.yiling.mall.banner.dto.B2bAppHotWordsDTO;
import com.yiling.sales.assistant.message.enums.SourceEnum;
import com.yiling.user.system.bo.CurrentStaffInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 热词 前端控制器
 * </p>
 *
 * @author yong.zhang
 * @date 2021-10-26
 */
@Slf4j
@Api(tags = "热词管理接口")
@RestController
@RequestMapping("/hotWords")
public class HotWordsController extends BaseController {

    @DubboReference
    HotWordsApi hotWordsApi;

    @ApiOperation(value = "app搜索查询热词展示")
    @PostMapping("/listAll")
    public Result<List<B2bAppHotWordsVO>> listAll(@CurrentUser CurrentStaffInfo staffInfo) {
        List<B2bAppHotWordsDTO> listDTO = hotWordsApi.listAll(SourceEnum.B2B.getCode());
        List<B2bAppHotWordsVO> listVO = PojoUtils.map(listDTO, B2bAppHotWordsVO.class);
        return Result.success(listVO);
    }

}
