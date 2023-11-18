package com.yiling.admin.pop.banner.controller;


import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.pop.banner.form.CheckBannerForm;
import com.yiling.admin.pop.banner.form.QueryBannerPageListForm;
import com.yiling.admin.pop.banner.form.SaveBannerForm;
import com.yiling.admin.pop.banner.form.UpdateBannerForm;
import com.yiling.admin.pop.banner.vo.BannerVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.pojo.vo.FileInfoVO;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.BoolObject;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.goods.medicine.constant.GoodsConstant;
import com.yiling.mall.banner.api.BannerApi;
import com.yiling.mall.banner.dto.BannerDTO;
import com.yiling.mall.banner.dto.request.CheckBannerRequest;
import com.yiling.mall.banner.dto.request.QueryBannerPageListRequest;
import com.yiling.mall.banner.dto.request.SaveBannerRequest;
import com.yiling.mall.banner.dto.request.UpdateBannerRequest;
import com.yiling.user.system.bo.CurrentAdminInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * banner表 前端控制器
 * </p>
 *
 * @author yuecheng.chen
 * @date 2021-06-15
 */
@RestController
@RequestMapping("/banner")
@Api(tags = "banner模块接口")
public class BannerController extends BaseController {

    @DubboReference
    BannerApi bannerApi;

    @Autowired
    FileService fileService;

    @ApiOperation(value = "运营后台获取banner列表")
    @PostMapping("/pageList")
    public Result<Page<BannerVO>> pageList(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody QueryBannerPageListForm form) {
        QueryBannerPageListRequest request = PojoUtils.map(form, QueryBannerPageListRequest.class);
        request.setEid(Constants.YILING_EID);
        Page<BannerDTO> bannerDTOPage = bannerApi.pageList(request);
        Page<BannerVO> pageVO = PojoUtils.map(bannerDTOPage, BannerVO.class);
        return Result.success(pageVO);
    }

    @ApiOperation(value = "运营后台获取banner详细信息")
    @GetMapping("/get")
    public Result<BannerVO> getBannerDetail(@RequestParam("id") Long id) {
        BannerDTO dto = bannerApi.get(id);
        BannerVO result = PojoUtils.map(dto, BannerVO.class);
        FileInfoVO fileInfo = new FileInfoVO();
        fileInfo.setFileKey(dto.getPic());
        fileInfo.setFileUrl(getBannerUrl(dto.getPic()));
        result.setFileInfo(fileInfo);
        return Result.success(result);

    }

    private String getBannerUrl(String pic){
        if (StringUtils.isNotBlank(pic)) {
            return fileService.getUrl(pic, FileTypeEnum.BANNER_PICTURE);
        } else {
            return fileService.getUrl(GoodsConstant.GOODS_DEFAULT_PIC, FileTypeEnum.BANNER_PICTURE);
        }
    }


    @ApiOperation(value = "运营后台保存banner信息")
    @PostMapping("/save")
    public Result<BoolObject> saveBanner(@CurrentUser CurrentAdminInfo staffInfo, @Valid @RequestBody SaveBannerForm form) {
        SaveBannerRequest request = PojoUtils.map(form, SaveBannerRequest.class);
        request.setEid(Constants.YILING_EID);
        Boolean result = bannerApi.createBanner(request);
        return Result.success(new BoolObject(result));
    }

    @ApiOperation(value = "运营后台编辑banner信息")
    @PostMapping("/update")
    public Result<BoolObject> updateBanner(@CurrentUser CurrentAdminInfo staffInfo,@Valid @RequestBody UpdateBannerForm form) {
        UpdateBannerRequest request = PojoUtils.map(form, UpdateBannerRequest.class);
        request.setEid(Constants.YILING_EID);
        Boolean result = bannerApi.updateBanner(request);
        return Result.success(new BoolObject(result));
    }

    @ApiOperation(value = "运营后台检查banner内容是否合法")
    @PostMapping("/check")
    public Result<BoolObject> checkRightful(@Valid @RequestBody CheckBannerForm form) {
        CheckBannerRequest request = PojoUtils.map(form, CheckBannerRequest.class);
        return Result.success(new BoolObject(bannerApi.checkRightful(request)));
    }

    @ApiOperation(value = "启用停用banner")
    @GetMapping("/update/status")
    public Result<BoolObject> checkRightful(@CurrentUser CurrentAdminInfo staffInfo,
                                            @RequestParam("id") Long id ,
                                            @RequestParam("status") Integer status) {
        return Result.success(new BoolObject(bannerApi.updateStatusById(id,status,staffInfo.getCurrentUserId())));
    }

}
