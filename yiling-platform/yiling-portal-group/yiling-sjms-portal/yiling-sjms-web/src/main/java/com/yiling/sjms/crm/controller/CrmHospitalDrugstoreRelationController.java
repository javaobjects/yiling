package com.yiling.sjms.crm.controller;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.crm.api.CrmHospitalDrugstoreRelationApi;
import com.yiling.dataflow.crm.dto.request.QueryHospitalDrugstoreRelationPageRequest;
import com.yiling.dataflow.crm.dto.request.RemoveHospitalDrugstoreRelRequest;
import com.yiling.dataflow.crm.dto.request.SaveOrUpdateCrmHospitalDrugstoreRelRequest;
import com.yiling.dataflow.crm.enums.CrmDrugstoreRelStatusEnum;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.crm.form.QueryHospitalDrugstoreRelationPageForm;
import com.yiling.sjms.crm.vo.CrmHospitalDrugstoreRelationVO;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentSjmsUserInfo;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 院外药店关系绑定表 前端控制器
 * </p>
 *
 * @author fucheng.bai
 * @date 2023-05-30
 */
@Api(tags = "院外药店关系绑定管理")
@RestController
@RequestMapping("/crm/hospitalDrugstoreRelation")
public class CrmHospitalDrugstoreRelationController extends BaseController {

    @DubboReference
    private CrmHospitalDrugstoreRelationApi crmHospitalDrugstoreRelationApi;

    @DubboReference
    private UserApi userApi;

    @ApiOperation(value = "查询标签分页", httpMethod = "POST")
    @PostMapping("/listPage")
    public Result<Page<CrmHospitalDrugstoreRelationVO>> listPage(@RequestBody QueryHospitalDrugstoreRelationPageForm form) {
        QueryHospitalDrugstoreRelationPageRequest request = PojoUtils.map(form, QueryHospitalDrugstoreRelationPageRequest.class);
        Page<CrmHospitalDrugstoreRelationVO> pageResult = PojoUtils.map(crmHospitalDrugstoreRelationApi.listPage(request), CrmHospitalDrugstoreRelationVO.class);
        if (CollUtil.isEmpty(pageResult.getRecords())) {
            return Result.success(new Page<>());
        }

        List<UserDTO> userDTOList = new ArrayList<>();
        List<Long> userIds = pageResult.getRecords().stream()
                .map(CrmHospitalDrugstoreRelationVO::getLastOpUser)
                .filter(userId -> userId != null && userId > 0).distinct().collect(Collectors.toList());
        if (CollUtil.isNotEmpty(userIds)) {
            userDTOList = userApi.listByIds(userIds);
        }
        for (CrmHospitalDrugstoreRelationVO crmHospitalDrugstoreRelationVO : pageResult.getRecords()) {
            // 设置操作人姓名
            if (crmHospitalDrugstoreRelationVO.getLastOpUser() != null && crmHospitalDrugstoreRelationVO.getLastOpUser() > 0) {
                String name = userDTOList.stream().filter(u -> u.getId().equals(crmHospitalDrugstoreRelationVO.getLastOpUser())).map(UserDTO::getName).findAny().orElse(null);
                if (StringUtils.isNotEmpty(name) && crmHospitalDrugstoreRelationVO.getDataSource() == 2) {
                    name = name + "（申请人）";
                }
                crmHospitalDrugstoreRelationVO.setLastOpUserName(name);
            }

            // 设置状态
            if (crmHospitalDrugstoreRelationVO.getDisableFlag() == 1) {
                crmHospitalDrugstoreRelationVO.setStatus(CrmDrugstoreRelStatusEnum.DISABLE.getCode());
            } else {
                Date now = DateUtil.beginOfDay(new Date());
                if (now.before(crmHospitalDrugstoreRelationVO.getEffectStartTime())) {
                    crmHospitalDrugstoreRelationVO.setStatus(CrmDrugstoreRelStatusEnum.NOT_EFFECT.getCode());
                } else if (DateUtil.compare(now, crmHospitalDrugstoreRelationVO.getEffectStartTime()) >= 0 && DateUtil.compare(now, crmHospitalDrugstoreRelationVO.getEffectEndTime()) <= 0) {
                    crmHospitalDrugstoreRelationVO.setStatus(CrmDrugstoreRelStatusEnum.EFFECTING.getCode());
                } else if (now.after(crmHospitalDrugstoreRelationVO.getEffectEndTime())) {
                    crmHospitalDrugstoreRelationVO.setStatus(CrmDrugstoreRelStatusEnum.EXPIRED.getCode());
                }
            }
        }

        return Result.success(pageResult);
    }

    @ApiOperation(value = "删除", httpMethod = "GET")
    @GetMapping("/remove")
    public Result remove(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestParam(value = "id") Long id) {
        RemoveHospitalDrugstoreRelRequest request = new RemoveHospitalDrugstoreRelRequest();
        request.setOpUserId(userInfo.getCurrentUserId());
        request.setId(id);
        crmHospitalDrugstoreRelationApi.remove(request);
        return Result.success();
    }

    @ApiOperation(value = "停用", httpMethod = "GET")
    @GetMapping("/disable")
    public Result disable(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestParam(value = "id") Long id) {
        SaveOrUpdateCrmHospitalDrugstoreRelRequest request = new SaveOrUpdateCrmHospitalDrugstoreRelRequest();
        request.setOpUserId(userInfo.getCurrentUserId());
        request.setId(id);
        request.setOpUserId(userInfo.getCurrentUserId());
        request.setOpTime(DateUtil.date());
        request.setDisableFlag(1);
        crmHospitalDrugstoreRelationApi.disable(request);
        return Result.success();
    }
}
