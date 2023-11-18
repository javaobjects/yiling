package com.yiling.sjms.wash.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.wash.api.UnlockFlowWashSaleApi;
import com.yiling.dataflow.wash.dto.request.QueryUnlockFlowWashSalePageRequest;
import com.yiling.dataflow.wash.dto.request.SaveUnlockSaleDepartmentRequest;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.wash.api.SjmsUnlockSaleRuleApi;
import com.yiling.sjms.wash.dto.request.UpdateUnlockFlowWashSaleDistributionRequest;
import com.yiling.sjms.wash.form.QueryUnlockFlowWashSalePageForm;
import com.yiling.sjms.wash.form.UpdateUnlockFlowWashSaleDistributionForm;
import com.yiling.sjms.wash.vo.UnlockFlowWashSalePageVO;
import com.yiling.sjms.wash.vo.UnlockFlowWashSaleVO;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentSjmsUserInfo;
import com.yiling.user.system.dto.UserDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: shuang.zhang
 * @date: 2023/5/19
 */
@RestController
@Api(tags = "非锁销量分配明细")
@RequestMapping("/unlockSale")
public class UnlockFlowWashSaleController extends BaseController {

    @DubboReference
    private UnlockFlowWashSaleApi unlockFlowWashSaleApi;
    @DubboReference
    private SjmsUnlockSaleRuleApi sjmsUnlockSaleRuleApi;
    @DubboReference
    private UserApi               userApi;

    @ApiOperation(value = "非锁销量分配规则列表")
    @PostMapping("/listPage")
    public Result<UnlockFlowWashSalePageVO<UnlockFlowWashSaleVO>> listPage(@RequestBody QueryUnlockFlowWashSalePageForm form) {
        QueryUnlockFlowWashSalePageRequest request = PojoUtils.map(form, QueryUnlockFlowWashSalePageRequest.class);
        Date date = DateUtil.parse(form.getYear()+"-"+form.getMonth(), "yyyy-MM");
        request.setYear(StrUtil.toString(DateUtil.year(date)));
        request.setMonth(StrUtil.toString(DateUtil.month(date) + 1));
        Page<UnlockFlowWashSaleVO> page = PojoUtils.map(unlockFlowWashSaleApi.pageList(request),UnlockFlowWashSaleVO.class);
        List<Long> updateUserIds = unlockFlowWashSaleApi.pageList(request).getRecords().stream().map(e -> e.getUpdateUser()).collect(Collectors.toList());
        List<UserDTO> userDTOList= ListUtil.empty();
        if(CollUtil.isNotEmpty(updateUserIds)){
            userDTOList = userApi.listByIds(updateUserIds);
        }
        Map<Long, String> userMap = userDTOList.stream().collect(Collectors.toMap(UserDTO::getId, UserDTO::getName));

        UnlockFlowWashSalePageVO<UnlockFlowWashSaleVO> newPage = new UnlockFlowWashSalePageVO<>();
        newPage.setRecords(page.getRecords());
        newPage.setTotal(page.getTotal());
        newPage.setCurrent(page.getCurrent());
        newPage.setSize(page.getSize());
        if(request.getDistributionStatus()==null||request.getDistributionStatus()==0) {
            request.setDistributionStatus(2);
            newPage.setHasDistributionCount(unlockFlowWashSaleApi.countDistributionStatus(request));
            request.setDistributionStatus(1);
            newPage.setNotDistributionCount(unlockFlowWashSaleApi.countDistributionStatus(request));
        }else if(request.getDistributionStatus()==1){
            newPage.setHasDistributionCount(0);
            newPage.setNotDistributionCount(Integer.parseInt(String.valueOf(page.getTotal())));
        }else if(request.getDistributionStatus()==2){
            newPage.setHasDistributionCount(Integer.parseInt(String.valueOf(page.getTotal())));
            newPage.setNotDistributionCount(0);
        }
        newPage.getRecords().stream().forEach(e->{
            if(e.getUpdateUser()==0){
                e.setUpdateUserName("系统");
            }else{
                e.setUpdateUserName(userMap.get(e.getUpdateUser()));
            }

        });
        return Result.success(newPage);
    }

    @ApiOperation(value = "批量非锁销量分配")
    @PostMapping("/batchDistribution")
    public Result<Boolean> batchDistribution(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody UpdateUnlockFlowWashSaleDistributionForm form) {
        UpdateUnlockFlowWashSaleDistributionRequest request = PojoUtils.map(form, UpdateUnlockFlowWashSaleDistributionRequest.class);
        request.setOpUserId(userInfo.getCurrentUserId());
        request.setSaveUnlockSaleDepartmentRequest(PojoUtils.map(form.getUnlockSaleDepartment(), SaveUnlockSaleDepartmentRequest.class));
        return Result.success(sjmsUnlockSaleRuleApi.updateUnlockSaleRuleDistribution(request));
    }
}
