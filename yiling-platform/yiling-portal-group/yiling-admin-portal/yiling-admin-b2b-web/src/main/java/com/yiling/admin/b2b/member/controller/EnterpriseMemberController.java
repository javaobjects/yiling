package com.yiling.admin.b2b.member.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.b2b.member.form.QueryEnterpriseMemberPageForm;
import com.yiling.admin.b2b.member.vo.EnterpriseMemberDetailVO;
import com.yiling.admin.b2b.member.vo.EnterpriseMemberVO;
import com.yiling.admin.b2b.member.vo.MemberDetailVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.base.BaseDTO;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.user.member.api.EnterpriseMemberApi;
import com.yiling.user.member.bo.EnterpriseMemberBO;
import com.yiling.user.member.dto.request.QueryEnterpriseMemberRequest;
import com.yiling.user.member.enums.MemberStatusEnum;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentAdminInfo;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.core.collection.CollUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * 企业会员 Controller
 *
 * @author: lun.yu
 * @date: 2022-09-30
 */
@RestController
@RequestMapping("/enterpriseMember")
@Api(tags = "企业会员接口")
public class EnterpriseMemberController extends BaseController {

    @DubboReference
    EnterpriseMemberApi enterpriseMemberApi;
    @DubboReference
    UserApi userApi;

    @Autowired
    FileService fileService;

    @ApiOperation(value = "企业会员列表")
    @PostMapping("/queryEnterpriseMemberPage")
    public Result<Page<EnterpriseMemberVO>> queryEnterpriseMemberPage(@CurrentUser CurrentAdminInfo adminInfo , @RequestBody @Valid QueryEnterpriseMemberPageForm form) {
        QueryEnterpriseMemberRequest request = PojoUtils.map(form,QueryEnterpriseMemberRequest.class);

        Page<EnterpriseMemberBO> enterpriseMemberBOPage = enterpriseMemberApi.queryEnterpriseMemberPage(request);
        if (CollUtil.isEmpty(enterpriseMemberBOPage.getRecords())) {
            return Result.success(PojoUtils.map(enterpriseMemberBOPage, EnterpriseMemberVO.class));
        }

        // 查询用户
        List<Long> userIdList = enterpriseMemberBOPage.getRecords().stream().map(EnterpriseMemberBO::getUpdateUser).collect(Collectors.toList());
        Map<Long, String> userNameMap = userApi.listByIds(userIdList).stream().collect(Collectors.toMap(UserDTO::getId, UserDTO::getName));
        // 完整地址
        Map<Long, String> addressMap = enterpriseMemberBOPage.getRecords().stream().collect(Collectors.toMap(BaseDTO::getId, this::getCompleteAddress));

        Page<EnterpriseMemberVO> page = PojoUtils.map(enterpriseMemberBOPage, EnterpriseMemberVO.class);
        page.getRecords().forEach(enterpriseMemberVO -> {
            // 完整地址
            enterpriseMemberVO.setCompleteAddress(addressMap.get(enterpriseMemberVO.getId()));
            //更新人
            enterpriseMemberVO.setUpdateUserName(userNameMap.get(enterpriseMemberVO.getUpdateUser()));
            // 会员状态
            Date now = new Date();
            if (enterpriseMemberVO.getEndTime().compareTo(now) > 0) {
                enterpriseMemberVO.setStatus(MemberStatusEnum.NORMAL.getCode());
            } else {
                enterpriseMemberVO.setStatus(MemberStatusEnum.EXPIRED.getCode());
            }
        });

        return Result.success(page);
    }

    @ApiOperation(value = "获取企业会员详情")
    @GetMapping("/getDetail")
    public Result<EnterpriseMemberDetailVO> getDetail(@CurrentUser CurrentAdminInfo adminInfo , @ApiParam(value = "主键ID", required = true) @RequestParam("id") Long id) {
        EnterpriseMemberBO enterpriseMemberBO = enterpriseMemberApi.getDetail(id);
        EnterpriseMemberDetailVO enterpriseMemberDetailVO = PojoUtils.map(enterpriseMemberBO, EnterpriseMemberDetailVO.class);
        enterpriseMemberDetailVO.setEnterpriseAddress(enterpriseMemberBO.getProvinceName() + " " + enterpriseMemberBO.getCityName() + " " + enterpriseMemberBO.getRegionName());
        enterpriseMemberDetailVO.setLightPicture(fileService.getUrl(enterpriseMemberBO.getLightPicture(), FileTypeEnum.MEMBER_LIGHT_PICTURE));
        enterpriseMemberDetailVO.setExtinguishPicture(fileService.getUrl(enterpriseMemberBO.getExtinguishPicture(), FileTypeEnum.MEMBER_LIGHT_PICTURE));
        // 会员状态
        if (enterpriseMemberDetailVO.getEndTime().compareTo(new Date()) > 0) {
            enterpriseMemberDetailVO.setStatus(MemberStatusEnum.NORMAL.getCode());
        } else {
            enterpriseMemberDetailVO.setStatus(MemberStatusEnum.EXPIRED.getCode());
        }
        return Result.success(enterpriseMemberDetailVO);
    }

    private String getCompleteAddress(EnterpriseMemberBO enterpriseMemberBO) {
        return enterpriseMemberBO.getProvinceName() + " " + enterpriseMemberBO.getCityName() + " " + enterpriseMemberBO.getRegionName() + " " + enterpriseMemberBO.getAddress();
    }

}
