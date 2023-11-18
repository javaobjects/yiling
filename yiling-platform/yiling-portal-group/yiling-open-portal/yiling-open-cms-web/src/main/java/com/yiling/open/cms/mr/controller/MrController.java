package com.yiling.open.cms.mr.controller;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.CollectionObject;
import com.yiling.goods.medicine.api.PopGoodsApi;
import com.yiling.goods.medicine.dto.GoodsInfoDTO;
import com.yiling.open.cms.mr.form.QueryMrListForm;
import com.yiling.open.cms.mr.form.QueryMrPageListForm;
import com.yiling.open.cms.mr.vo.MrFullInfoVO;
import com.yiling.open.cms.mr.vo.MrPageListItemVO;
import com.yiling.user.enterprise.api.EmployeeApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.system.api.MrApi;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.MrBO;
import com.yiling.user.system.dto.request.QueryMrPageListRequest;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 医药代表模块
 *
 * @author: xuan.zhou
 * @date: 2022/6/7
 */
@RestController
@RequestMapping("/mr")
@Api(tags = "医药代表模块接口")
@Slf4j
public class MrController extends BaseController {

    @DubboReference
    MrApi mrApi;
    @DubboReference
    EmployeeApi employeeApi;
    @DubboReference
    UserApi userApi;
    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    PopGoodsApi popGoodsApi;

    @ApiOperation(value = "医药代表分页列表")
    @PostMapping("/pageList")
    public Result<Page<MrPageListItemVO>> pageList(@RequestBody @Valid QueryMrPageListForm form) {
        QueryMrPageListRequest request = PojoUtils.map(form, QueryMrPageListRequest.class);

        Page<MrBO> page = mrApi.pageList(request);
        List<MrBO> list = page.getRecords();
        if (CollUtil.isEmpty(list)) {
            return Result.success(request.getPage());
        }

        Page<MrPageListItemVO> pageVO = PojoUtils.map(page, MrPageListItemVO.class);
        return Result.success(pageVO);
    }

    @ApiOperation(value = "批量获取医药代表信息")
    @PostMapping("/listByIds")
    public Result<CollectionObject<MrFullInfoVO>> listByIds(@RequestBody @Valid QueryMrListForm form) {
        List<MrBO> mrBOList = mrApi.listByIds(form.getIds());
        if (CollUtil.isEmpty(mrBOList)) {
            return Result.success(new CollectionObject<>(ListUtil.empty()));
        }

        List<Long> eids = mrBOList.stream().map(MrBO::getEid).distinct().collect(Collectors.toList());
        List<EnterpriseDTO> enterpriseDTOList = enterpriseApi.listByIds(eids);
        Map<Long, EnterpriseDTO> enterpriseDTOMap = enterpriseDTOList.stream().collect(Collectors.toMap(EnterpriseDTO::getId, Function.identity()));

        Map<Long, List<Long>> employeeSalesGoodsIdsMap = mrApi.listGoodsIdsByEmployeeIds(form.getIds());
        List<Long> goodsIds = employeeSalesGoodsIdsMap.values().stream().flatMap(Collection::stream).distinct().collect(Collectors.toList());
        List<GoodsInfoDTO> goodsInfoDTOList = popGoodsApi.batchQueryInfo(goodsIds);
        Map<Long, GoodsInfoDTO> goodsInfoDTOMap = goodsInfoDTOList.stream().collect(Collectors.toMap(GoodsInfoDTO::getId, Function.identity()));

        List<MrFullInfoVO> mrInfoVOList = CollUtil.newArrayList();
        mrBOList.forEach(e -> {
            EnterpriseDTO enterpriseDTO = enterpriseDTOMap.get(e.getEid());

            MrFullInfoVO mrInfoVO = new MrFullInfoVO();
            mrInfoVO.setId(e.getId());
            mrInfoVO.setName(e.getName());
            mrInfoVO.setMobile(e.getMobile());
            mrInfoVO.setStatus(e.getStatus());
            mrInfoVO.setEname(enterpriseDTO.getName());
            mrInfoVO.setSalesGoodsInfoList(PojoUtils.map(this.get(goodsInfoDTOMap, employeeSalesGoodsIdsMap.get(e.getUserId())), MrFullInfoVO.SalesGoodsInfoVO.class));

            mrInfoVOList.add(mrInfoVO);
        });

        return Result.success(new CollectionObject<>(mrInfoVOList));
    }

    private List<GoodsInfoDTO> get(Map<Long, GoodsInfoDTO> map, List<Long> keys) {
        if (CollUtil.isEmpty(map) || CollUtil.isEmpty(keys)) {
            return ListUtil.empty();
        }

        List<GoodsInfoDTO> list = CollUtil.newArrayList();
        keys.forEach(e -> {
            list.add(map.get(e));
        });

        return list;
    }
}
