package com.yiling.sales.assistant.app.banner.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sales.assistant.app.banner.vo.BannerVO;
import com.yiling.sales.assistant.app.banner.vo.SaAppBannerVO;
import com.yiling.sales.assistant.banner.api.BannerApi;
import com.yiling.sales.assistant.banner.dto.BannerDTO;
import com.yiling.sales.assistant.banner.dto.request.SaAppBannerAppListRequest;
import com.yiling.sales.assistant.banner.enums.BannerScenarioEnum;
import com.yiling.user.system.bo.CurrentStaffInfo;
import com.yiling.user.system.enums.UserTypeEnum;

import cn.hutool.core.collection.CollUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 销售助手banner表 前端控制器
 * </p>
 *
 * @author yong.zhang
 * @date 2021-09-14
 */
@RestController
@RequestMapping("/banner")
@Api(tags = "销售助手APP的banner模块")
@Slf4j
public class BannerController extends BaseController {
    @DubboReference
    BannerApi saBannerApi;

    @ApiOperation(value = "app查询展示banner")
    @ApiImplicitParams({ @ApiImplicitParam(name = "count", value = "需要banner的数量", required = true) })
    @GetMapping("/listByCondition")
    public Result<BannerVO> listByCondition(@CurrentUser CurrentStaffInfo staffInfo, @RequestParam(value = "count") Integer count) {
        // 注:以岭代表以岭员工，小三元代表商业公司员工，自然人代表非商业公司的非以岭人员
        UserTypeEnum userTypeEnum = staffInfo.getUserType();
        BannerVO vo = new BannerVO();
        SaAppBannerAppListRequest requestMain = new SaAppBannerAppListRequest().setInternalEmployeeFlag(userTypeEnum == UserTypeEnum.YILING)
            .setUsageScenario(BannerScenarioEnum.PRIMARY.getCode()).setCount(count);
        //使用位置：1-B2B移动端主Banner 2-B2B移动端副Banner
        // 主banner
        List<BannerDTO> listMain = saBannerApi.listByScenarioAndSource(requestMain);
        vo.setMainBannerVOList(addSellerId(listMain));

        // 副banner
        SaAppBannerAppListRequest requestSecond = new SaAppBannerAppListRequest().setInternalEmployeeFlag(userTypeEnum == UserTypeEnum.YILING)
            .setUsageScenario(BannerScenarioEnum.SECONDARY.getCode()).setCount(count);
        List<BannerDTO> listSecond = saBannerApi.listByScenarioAndSource(requestSecond);
        vo.setSecondBannerVOList(addSellerId(listSecond));

        return Result.success(vo);
    }

    /**
     * 数据转换
     * @param bannerList    banner集合
     * @return  banner移动端需要的是剧
     */
    private List<SaAppBannerVO> addSellerId(List<BannerDTO> bannerList) {
        if (CollUtil.isEmpty(bannerList)) {
            return new ArrayList<>();
        }
        return PojoUtils.map(bannerList, SaAppBannerVO.class);
    }
}
