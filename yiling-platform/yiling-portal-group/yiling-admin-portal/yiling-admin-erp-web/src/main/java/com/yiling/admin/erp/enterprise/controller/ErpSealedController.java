package com.yiling.admin.erp.enterprise.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.erp.enterprise.form.QueryErpSealedEnterprisePageForm;
import com.yiling.admin.erp.enterprise.form.QueryErpSealedPageForm;
import com.yiling.admin.erp.enterprise.form.QueryErpSealedSaveForm;
import com.yiling.admin.erp.enterprise.vo.ErpSealedEnterprisePageVO;
import com.yiling.admin.erp.enterprise.vo.ErpSealedPageVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.open.erp.api.ErpClientApi;
import com.yiling.open.erp.api.ErpFlowSealedApi;
import com.yiling.open.erp.dto.ErpClientDTO;
import com.yiling.open.erp.dto.ErpFlowSealedDTO;
import com.yiling.open.erp.dto.request.ErpFlowSealedLockOrUnlockRequest;
import com.yiling.open.erp.dto.request.QueryClientFlowEnterpriseRequest;
import com.yiling.open.erp.dto.request.QueryErpSealedPageRequest;
import com.yiling.open.erp.dto.request.QueryErpSealedSaveRequest;
import com.yiling.open.erp.enums.ErpFlowSealedStatusEnum;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentAdminInfo;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 流向封存
 *
 * @author: houjie.sun
 * @date: 2022/4/14
 */
@Api(tags = "ERP流向封存接口")
@RestController
@RequestMapping("/erpFlowSealed")
public class ErpSealedController {

    @DubboReference
    ErpFlowSealedApi erpFlowSealedApi;
    @DubboReference
    ErpClientApi erpClientApi;
    @DubboReference
    UserApi userApi;


    @ApiOperation(value = "流向封存信息列表分页", httpMethod = "POST")
    @PostMapping("/queryListPage")
    public Result<Page<ErpSealedPageVO>> queryParentListPage(@CurrentUser CurrentAdminInfo adminInfo,
                                                                       @RequestBody @Valid QueryErpSealedPageForm form) {
        QueryErpSealedPageRequest request = PojoUtils.map(form, QueryErpSealedPageRequest.class);
        Page<ErpSealedPageVO> page = PojoUtils.map(erpFlowSealedApi.page(request), ErpSealedPageVO.class);
        // 操作人、操作时间
        if(ObjectUtil.isNotNull(page) && CollUtil.isNotEmpty(page.getRecords())){
            Map<Long,Long> dataIdUserMap = new HashMap<>();
            List<Long> userIdList = this.getUserIdList(page.getRecords(), dataIdUserMap);
            Map<Long, UserDTO> userMap = new HashMap<>();
            if(CollUtil.isNotEmpty(userIdList)){
                userMap = this.getUserMapByIds(userIdList);
            }
            for (ErpSealedPageVO record : page.getRecords()) {
                Long operId = dataIdUserMap.get(record.getId());
                UserDTO userDTO = userMap.get(operId);
                if(ObjectUtil.isNotNull(userDTO)){
                    record.setOperName(userDTO.getName());
                }
                Date opTime = null;
                if (ObjectUtil.isNotNull(record.getUpdateUser())) {
                    opTime = record.getUpdateTime();
                } else if (ObjectUtil.isNotNull(record.getCreateUser())) {
                    opTime = record.getCreateTime();
                }
                record.setOpTime(opTime);
            }
        }
        return Result.success(page);
    }

    public List<Long> getUserIdList(List<ErpSealedPageVO> list, Map<Long,Long> dataIdUserMap) {
        if(CollUtil.isEmpty(list)){
            return ListUtil.empty();
        }
        Set<Long> userIdSet = new HashSet<>();
        list.forEach(p -> {
            Long operId = 0L;
            if (ObjectUtil.isNotNull(p.getUpdateUser())) {
                operId = p.getUpdateUser();
            } else if (ObjectUtil.isNotNull(p.getCreateUser())) {
                operId = p.getCreateUser();
            }
            userIdSet.add(operId);
            dataIdUserMap.put(p.getId(),operId);
        });
        return new ArrayList<>(userIdSet);
    }

    public Map<Long, UserDTO> getUserMapByIds(List<Long> userIds) {
        Map<Long, UserDTO> userMap = new HashMap<>();
        if (CollUtil.isEmpty(userIds)) {
            return userMap;
        }
        List<UserDTO> userList = userApi.listByIds(userIds);
        if (CollUtil.isEmpty(userList)) {
            return userMap;
        }
        return userList.stream().collect(Collectors.toMap(u -> u.getId(), u -> u, (v1, v2) -> v1));
    }

    @ApiOperation(value = "流向添加封存-月份列表, 仅支持前推6个整月", httpMethod = "GET")
    @GetMapping("/monthList")
    public Result<List<String>> monthList(@CurrentUser CurrentAdminInfo adminInfo) {
        return Result.success(erpFlowSealedApi.monthList(6));
    }

    @ApiOperation(value = "流向添加封存-商业公司列表分页", httpMethod = "POST")
    @PostMapping("/enterprisePage")
    public Result<Page<ErpSealedEnterprisePageVO>> enterprisePage(@CurrentUser CurrentAdminInfo adminInfo,
                                                                      @RequestBody @Valid QueryErpSealedEnterprisePageForm form) {
        QueryClientFlowEnterpriseRequest request = PojoUtils.map(form, QueryClientFlowEnterpriseRequest.class);
        Page<ErpClientDTO> erpClientPage = erpClientApi.flowEnterprisePage(request);
        Page<ErpSealedEnterprisePageVO> page = new Page();
        page.setSize(erpClientPage.getSize());
        page.setTotal(erpClientPage.getTotal());
        page.setCurrent(erpClientPage.getCurrent());

        if(ObjectUtil.isNull(erpClientPage) || CollUtil.isEmpty(erpClientPage.getRecords())){
            return Result.success(page);
        }

        List<ErpSealedEnterprisePageVO> list = new ArrayList<>();
        for (ErpClientDTO erpClient : erpClientPage.getRecords()) {
            ErpSealedEnterprisePageVO vo = new ErpSealedEnterprisePageVO();
            vo.setEid(erpClient.getRkSuId());
            vo.setEname(erpClient.getClientName());
            list.add(vo);
        }
        page.setRecords(list);
        return Result.success(page);
    }

    @Log(title = "流向封存-添加保存", businessType = BusinessTypeEnum.INSERT)
    @ApiOperation(value = "流向封存-添加保存", httpMethod = "POST")
    @PostMapping("/save")
    public Result<Boolean> save(@CurrentUser CurrentAdminInfo adminInfo,
                                                                  @RequestBody @Valid QueryErpSealedSaveForm form) {
        QueryErpSealedSaveRequest request = PojoUtils.map(form, QueryErpSealedSaveRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());
        // 商业公司名称
        Map<Long,String> enameMap = new HashMap<>();
        List<Long> eidList = form.getEidList();
        if(CollUtil.isNotEmpty(eidList)){
            List<ErpClientDTO> erpClientList = erpClientApi.selectByRkSuIdList(eidList);
            if(CollUtil.isNotEmpty(erpClientList)){
                for (ErpClientDTO erpClient : erpClientList) {
                    enameMap.put(erpClient.getRkSuId(), erpClient.getClientName());
                }
            }
        }
        request.setEnameMap(enameMap);
        return Result.success(erpFlowSealedApi.save(request));
    }

    @Log(title = "流向封存-解封", businessType = BusinessTypeEnum.INSERT)
    @ApiOperation(value = "流向封存-解封", httpMethod = "GET")
    @GetMapping("/unLock")
    public Result<Boolean> unLock(@CurrentUser CurrentAdminInfo adminInfo,
                                  @RequestParam("id") @ApiParam(value = "流向封存ID", required = true) Long id) {
        if(ObjectUtil.isNull(id)){
            throw new BusinessException(ResultCode.PARAM_MISS);
        }
        ErpFlowSealedDTO erpFlowSealed = erpFlowSealedApi.getErpFlowSealedByEidAndTypeAndMonth(id);
        if(ObjectUtil.isNull(erpFlowSealed)){
            throw new BusinessException(ResultCode.FAILED, "流向封存信息不存在");
        }
        if(ObjectUtil.equal(ErpFlowSealedStatusEnum.UN_LOCK.getCode(), erpFlowSealed.getStatus())){
            throw new BusinessException(ResultCode.FAILED, "已是解封状态，无需重复操作");
        }
        ErpFlowSealedLockOrUnlockRequest request = new ErpFlowSealedLockOrUnlockRequest();
        request.setId(id);
        request.setStatus(ErpFlowSealedStatusEnum.UN_LOCK.getCode());
        request.setOpUserId(adminInfo.getCurrentUserId());
        return Result.success(erpFlowSealedApi.lockOrUnLock(request));
    }

    @Log(title = "流向封存-封存", businessType = BusinessTypeEnum.INSERT)
    @ApiOperation(value = "流向封存-封存", httpMethod = "GET")
    @GetMapping("/lock")
    public Result<Boolean> lock(@CurrentUser CurrentAdminInfo adminInfo,
                                  @RequestParam("id") @ApiParam(value = "流向封存ID", required = true) Long id) {
        if(ObjectUtil.isNull(id)){
            throw new BusinessException(ResultCode.PARAM_MISS);
        }
        ErpFlowSealedDTO erpFlowSealed = erpFlowSealedApi.getErpFlowSealedByEidAndTypeAndMonth(id);
        if(ObjectUtil.isNull(erpFlowSealed)){
            throw new BusinessException(ResultCode.FAILED, "流向封存信息不存在");
        }
        if(ObjectUtil.equal(ErpFlowSealedStatusEnum.LOCK.getCode(), erpFlowSealed.getStatus())){
            throw new BusinessException(ResultCode.FAILED, "已是封存状态，无需重复操作");
        }
        ErpFlowSealedLockOrUnlockRequest request = new ErpFlowSealedLockOrUnlockRequest();
        request.setId(id);
        request.setStatus(ErpFlowSealedStatusEnum.LOCK.getCode());
        request.setOpUserId(adminInfo.getCurrentUserId());
        return Result.success(erpFlowSealedApi.lockOrUnLock(request));
    }

}
