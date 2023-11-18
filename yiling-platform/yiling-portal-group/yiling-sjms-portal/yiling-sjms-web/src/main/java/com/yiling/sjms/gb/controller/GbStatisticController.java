package com.yiling.sjms.gb.controller;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.gb.api.GbStatisticApi;
import com.yiling.sjms.gb.dto.StatisticDTO;
import com.yiling.sjms.gb.dto.request.GbFormStatisticPageRequest;
import com.yiling.sjms.gb.form.GbFormStatisticPageForm;
import com.yiling.sjms.gb.vo.GbStatisticVO;
import com.yiling.user.system.bo.CurrentSjmsUserInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 团购
 * @author: wei.wang
 * @date: 2022/11/28
 */
@Slf4j
@RestController
@RequestMapping("/gb/manage")
@Api(tags = "团购管理")
public class GbStatisticController extends BaseController {

    @DubboReference
    GbStatisticApi gbStatisticApi;

    @ApiOperation(value = "团购日统计")
    @PostMapping("/statistic/list")
    public Result<Page<GbStatisticVO>> getGBSubmitFormListPage(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid GbFormStatisticPageForm form){
        GbFormStatisticPageRequest request = PojoUtils.map(form, GbFormStatisticPageRequest.class);
        if(form.getGoodsType() == 1 ){
            //只勾选产品
            request.setType(1);
            if(form.getMonthType() == 1){
                //选择产品加团购月份
                request.setType(5);
            }
            if(form.getProvinceType() == 1 ){
                //选择产品加省区
                request.setType(4);
            }
            if(form.getProvinceType() == 1 && form.getMonthType() == 1 ){
                //全选
                request.setType(6);
            }
        }else{
            if(form.getProvinceType() == 0 && form.getMonthType() == 0 ){
                //默认不选
                request.setType(8);
            }
            if(form.getProvinceType() == 1  ){
                //只选省区
                request.setType(2);
            }
            if(form.getMonthType() == 1){
                //只选月份
                request.setType(3);
            }
            if(form.getProvinceType() == 1 && form.getMonthType() == 1 ){
                //省区和月份
                request.setType(7);
            }
        }
        Page<StatisticDTO> statistic = gbStatisticApi.getStatistic(request);
        return Result.success(PojoUtils.map(statistic,GbStatisticVO.class));
    }
}
