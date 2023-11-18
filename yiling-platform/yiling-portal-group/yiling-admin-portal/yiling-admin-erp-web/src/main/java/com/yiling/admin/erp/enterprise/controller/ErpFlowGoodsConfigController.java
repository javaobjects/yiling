package com.yiling.admin.erp.enterprise.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.yiling.admin.erp.enterprise.form.QueryErpFlowGoodsConfigPageForm;
import com.yiling.admin.erp.enterprise.form.SaveErpFlowGoodsConfigForm;
import com.yiling.admin.erp.enterprise.vo.ErpFlowGoodsConfigEnterpriseVO;
import com.yiling.admin.erp.enterprise.vo.ErpFlowGoodsConfigPageVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.open.erp.api.ErpClientApi;
import com.yiling.open.erp.api.ErpFlowGoodsConfigApi;
import com.yiling.open.erp.dto.ErpClientDTO;
import com.yiling.open.erp.dto.ErpFlowGoodsConfigDTO;
import com.yiling.open.erp.dto.request.DeleteErpFlowGoodsConfigRequest;
import com.yiling.open.erp.dto.request.QueryErpFlowGoodsConfigPageRequest;
import com.yiling.open.erp.dto.request.SaveErpFlowGoodsConfigRequest;
import com.yiling.open.erp.enums.ClientStatusEnum;
import com.yiling.open.erp.enums.ErpFlowLevelEnum;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentAdminInfo;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * B2B运营后台 ERP流向非以岭商品配置信息
 *
 * @author: houjie.sun
 * @date: 2022/4/26
 */
@Api(tags = "ERP流向非以岭商品配置信息接口")
@RestController
@RequestMapping("/erpFlowGoodsConfig")
public class ErpFlowGoodsConfigController {

    @DubboReference
    ErpFlowGoodsConfigApi erpFlowGoodsConfigApi;
    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    ErpClientApi erpClientApi;
    @DubboReference
    UserApi userApi;

    @ApiOperation(value = "商品配置信息列表分页", httpMethod = "POST")
    @PostMapping("/queryListPage")
    public Result<Page<ErpFlowGoodsConfigPageVO>> queryParentListPage(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid QueryErpFlowGoodsConfigPageForm form) {
        QueryErpFlowGoodsConfigPageRequest request = PojoUtils.map(form, QueryErpFlowGoodsConfigPageRequest.class);
        Page<ErpFlowGoodsConfigPageVO> page = PojoUtils.map(erpFlowGoodsConfigApi.page(request), ErpFlowGoodsConfigPageVO.class);
        // 操作人、操作时间
        if(ObjectUtil.isNotNull(page) && CollUtil.isNotEmpty(page.getRecords())){
            List<Long> userIdList = this.getUserIdList(page.getRecords());
            Map<Long, UserDTO> userMap = new HashMap<>();
            if(CollUtil.isNotEmpty(userIdList)){
                userMap = this.getUserMapByIds(userIdList);
            }
            for (ErpFlowGoodsConfigPageVO record : page.getRecords()) {
                record.setOpTime(record.getCreateTime());
                UserDTO userDTO = userMap.get(record.getCreateUser());
                if(ObjectUtil.isNotNull(userDTO)){
                    record.setOperName(userDTO.getName());
                }
            }
        }
        return Result.success(page);
    }

    public List<Long> getUserIdList(List<ErpFlowGoodsConfigPageVO> list) {
        if(CollUtil.isEmpty(list)){
            return ListUtil.empty();
        }
        return list.stream().map(ErpFlowGoodsConfigPageVO::getCreateUser).distinct().collect(Collectors.toList());
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

    @ApiOperation(value = "查询商业公司ID", httpMethod = "GET")
    @GetMapping("/getEnterpriseId")
    public Result<List<ErpFlowGoodsConfigEnterpriseVO>> enterprisePage(@CurrentUser CurrentAdminInfo adminInfo,
                                                                       @RequestParam("ename") @ApiParam(value = "商业名称", required = true) String ename) {
        List<ErpClientDTO> erpClientList = erpClientApi.getFlowEnterpriseListByName(ename);
        if(CollUtil.isEmpty(erpClientList)){
            return Result.success(ListUtil.empty());
        }

        List<ErpFlowGoodsConfigEnterpriseVO> list = new ArrayList<>();
        for (ErpClientDTO erpClientDTO : erpClientList) {
            ErpFlowGoodsConfigEnterpriseVO enterpriseVO = new ErpFlowGoodsConfigEnterpriseVO();
            enterpriseVO.setEid(erpClientDTO.getRkSuId());
            enterpriseVO.setEname(erpClientDTO.getClientName());
            list.add(enterpriseVO);
        }
        return Result.success(list);
    }

    @Log(title = "企业流向商品配置-新增", businessType = BusinessTypeEnum.INSERT)
    @ApiOperation(value = "新增", httpMethod = "POST")
    @PostMapping("/save")
    public Result<Boolean> save(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid SaveErpFlowGoodsConfigForm form) {
        // 规格字段长度校验
        if(form.getSpecifications().length() > 32){
            throw new BusinessException(ResultCode.FAILED, "商品规格名称不规范，请确认商品规格");
        }
        // 商业公司
        Long eid = form.getEid();
        ErpClientDTO erpClient = erpClientApi.selectByRkSuId(eid);
        if (ObjectUtil.isNull(erpClient)) {
            throw new BusinessException(ResultCode.FAILED, "此商业公司对接信息不存在");
        }
        if(!ObjectUtil.equal(ClientStatusEnum.IN.getCode(),erpClient.getClientStatus())){
            throw new BusinessException(ResultCode.FAILED, "此商业公司对接信息的终端状态未激活");
        }
        if(!ObjectUtil.equal(ClientStatusEnum.IN.getCode(), erpClient.getSyncStatus())){
            throw new BusinessException(ResultCode.FAILED, "此商业公司对接信息的同步状态未开启");
        }
        if(ObjectUtil.equal(ErpFlowLevelEnum.NO.getCode(), erpClient.getFlowLevel())){
            throw new BusinessException(ResultCode.FAILED, "此商业公司对接信息的流向级别未对接");
        }
        String manufacturer = form.getManufacturer();
        if(StrUtil.isNotBlank(manufacturer) && manufacturer.contains("以岭")){
            throw new BusinessException(ResultCode.FAILED, "流向商品配置的是非以岭品，生产厂家不能包含以岭");
        }
        ErpFlowGoodsConfigDTO old = erpFlowGoodsConfigApi.getByEidAndGoodsInSn(eid, form.getGoodsInSn());
        if (ObjectUtil.isNotNull(old)) {
            throw new BusinessException(ResultCode.FAILED, "此商业公司的商品内码已存在，无需重复添加");
        }

        SaveErpFlowGoodsConfigRequest request = PojoUtils.map(form, SaveErpFlowGoodsConfigRequest.class);
        request.setEid(eid);
        request.setEname(erpClient.getClientName());
        request.setOpUserId(adminInfo.getCurrentUserId());
        return Result.success(erpFlowGoodsConfigApi.save(request));
    }

    @Log(title = "企业流向商品配置-删除", businessType = BusinessTypeEnum.DELETE)
    @ApiOperation(value = "删除", httpMethod = "GET")
    @GetMapping("/delete")
    public Result<Boolean> delete(@CurrentUser CurrentAdminInfo adminInfo, @RequestParam("id") @ApiParam(value = "流向商品配置ID", required = true) Long id) {
        ErpFlowGoodsConfigDTO erpFlowGoodsConfigDTO = erpFlowGoodsConfigApi.getById(id);
        if(ObjectUtil.isNull(erpFlowGoodsConfigDTO)){
            throw new BusinessException(ResultCode.FAILED, "此流向商品配置信息不存在，无需删除");
        }
        DeleteErpFlowGoodsConfigRequest request = new DeleteErpFlowGoodsConfigRequest();
        request.setId(id);
        request.setEid(erpFlowGoodsConfigDTO.getEid());
        request.setOpUserId(adminInfo.getCurrentUserId());
        return Result.success(erpFlowGoodsConfigApi.deleteById(request));
    }

}
