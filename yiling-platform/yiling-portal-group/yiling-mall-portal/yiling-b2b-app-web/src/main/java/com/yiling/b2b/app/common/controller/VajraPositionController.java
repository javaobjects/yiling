package com.yiling.b2b.app.common.controller;


import java.util.List;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yiling.b2b.app.common.vo.B2bAppVajraPositionVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.CollectionObject;
import com.yiling.mall.banner.api.VajraPositionApi;
import com.yiling.mall.banner.dto.B2bAppVajraPositionDTO;
import com.yiling.sales.assistant.message.enums.SourceEnum;
import com.yiling.user.system.bo.CurrentStaffInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 金刚位表 前端控制器
 * </p>
 *
 * @author yong.zhang
 * @date 2021-10-22
 */
@Slf4j
@Api(tags = "金刚位管理接口")
@RestController
@RequestMapping("/vajra")
public class VajraPositionController extends BaseController {

    @DubboReference
    VajraPositionApi vajraPositionApi;

    @ApiOperation(value = "app首页查询金刚位")
    @PostMapping("/listAll")
    public Result<CollectionObject<List<B2bAppVajraPositionVO>>> listAll(@CurrentUser CurrentStaffInfo staffInfo) {
        List<B2bAppVajraPositionDTO> listDTO = vajraPositionApi.listAll(SourceEnum.B2B.getCode());
        List<B2bAppVajraPositionVO> listVO = PojoUtils.map(listDTO, B2bAppVajraPositionVO.class);
        return Result.success(new CollectionObject(listVO));
    }

}
