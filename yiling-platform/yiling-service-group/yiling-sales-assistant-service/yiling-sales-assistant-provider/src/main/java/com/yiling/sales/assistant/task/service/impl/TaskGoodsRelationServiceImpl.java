package com.yiling.sales.assistant.task.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.util.SpringUtils;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.goods.medicine.api.B2bGoodsApi;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.bo.GoodsListItemBO;
import com.yiling.goods.medicine.constant.GoodsConstant;
import com.yiling.goods.medicine.dto.request.QueryGoodsPageListRequest;
import com.yiling.goods.medicine.enums.GoodsLineEnum;
import com.yiling.goods.ylprice.api.GoodsYilingPriceApi;
import com.yiling.goods.ylprice.dto.GoodsYilingPriceDTO;
import com.yiling.sales.assistant.task.dao.TaskGoodsRelationMapper;
import com.yiling.sales.assistant.task.dto.TaskSelectGoodsDTO;
import com.yiling.sales.assistant.task.dto.request.DeleteGoodsRequest;
import com.yiling.sales.assistant.task.dto.request.QueryGoodsPageRequest;
import com.yiling.sales.assistant.task.dto.request.QueryTaskGoodsMatchRequest;
import com.yiling.sales.assistant.task.dto.request.TaskGoodsMatchDTO;
import com.yiling.sales.assistant.task.dto.request.TaskGoodsMatchListDTO;
import com.yiling.sales.assistant.task.entity.MarketTaskDO;
import com.yiling.sales.assistant.task.entity.TaskDistributorDO;
import com.yiling.sales.assistant.task.entity.TaskGoodsRelationDO;
import com.yiling.sales.assistant.task.entity.UserTaskDO;
import com.yiling.sales.assistant.task.enums.FinishTypeEnum;
import com.yiling.sales.assistant.task.enums.TaskStatusEnum;
import com.yiling.sales.assistant.task.enums.TaskTypeEnum;
import com.yiling.sales.assistant.task.enums.UserTaskStatusEnum;
import com.yiling.sales.assistant.task.service.MarketTaskService;
import com.yiling.sales.assistant.task.service.TaskDistributorService;
import com.yiling.sales.assistant.task.service.TaskGoodsRelationService;
import com.yiling.sales.assistant.task.service.UserTaskService;
import com.yiling.user.enterprise.api.EnterpriseApi;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * 任务商品关联表  服务实现类
 * </p>
 *
 * @author gxl
 * @date 2021-09-10
 */
@Service
public class TaskGoodsRelationServiceImpl extends BaseServiceImpl<TaskGoodsRelationMapper, TaskGoodsRelationDO> implements TaskGoodsRelationService {
    @DubboReference
    private B2bGoodsApi b2bGoodsApi;
    @DubboReference
    private EnterpriseApi enterpriseApi;

    @DubboReference
    private GoodsApi goodsApi;

    @DubboReference
    private GoodsYilingPriceApi goodsYilingPriceApi;

    @Autowired
    private FileService fileService;

    @Autowired
    private TaskDistributorService taskDistributorService;

    @Override
    public Boolean deleteGoods(DeleteGoodsRequest request) {
        int delete = this.baseMapper.deleteById(request.getTaskGoodsId());
        if (delete > 0) {
            return true;
        } else {
            return false;
        }

    }

    @Override
    public Page<TaskSelectGoodsDTO> queryGoodsForAdd(QueryGoodsPageRequest queryGoodsPageRequest) {
        Page<TaskSelectGoodsDTO> taskSelectGoodsDTOPage = new Page<>(queryGoodsPageRequest.getCurrent(), queryGoodsPageRequest.getSize());
        Page<GoodsListItemBO> goodsListItemBOPage = null;
        //平台任务取tob的以岭品
        if (queryGoodsPageRequest.getTaskType().equals(TaskTypeEnum.PLATFORM.getCode())) {
            List<Long> eidList = enterpriseApi.listSubEids(Constants.YILING_EID);
            QueryGoodsPageListRequest request = new QueryGoodsPageListRequest();
            request.setSize(queryGoodsPageRequest.getSize()).setCurrent(queryGoodsPageRequest.getCurrent());
            request.setGoodsLine(GoodsLineEnum.B2B.getCode()).setEidList(eidList).setName(queryGoodsPageRequest.getGoodsName()).setManufacturer(queryGoodsPageRequest.getManufacturer()).setGoodsId(queryGoodsPageRequest.getGoodsId());
            goodsListItemBOPage = b2bGoodsApi.queryB2bGoodsPageList(request);
        }
        List<GoodsListItemBO> goodsListItemBOS = goodsListItemBOPage.getRecords();
        if (CollUtil.isEmpty(goodsListItemBOS)) {
            return queryGoodsPageRequest.getPage();
        }
        List<TaskSelectGoodsDTO> taskSelectGoodsDTOS = Lists.newArrayListWithExpectedSize(goodsListItemBOS.size());
        List<Long> goodsIds = goodsListItemBOS.stream().map(GoodsListItemBO::getId).collect(Collectors.toList());
        List<GoodsYilingPriceDTO> priceParamNameList = goodsYilingPriceApi.getPriceParamNameList(goodsIds, new Date());
        goodsListItemBOS.forEach(goodsListItemBO -> {
            TaskSelectGoodsDTO taskSelectGoodsDTO = new TaskSelectGoodsDTO();
            PojoUtils.map(goodsListItemBO, taskSelectGoodsDTO);
            taskSelectGoodsDTO.setGoodsId(goodsListItemBO.getId()).setGoodsName(goodsListItemBO.getName());
            taskSelectGoodsDTO.setPrice(goodsListItemBO.getPrice().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
            String pic = fileService.getUrl(GoodsConstant.GOODS_DEFAULT_PIC, FileTypeEnum.GOODS_PICTURE);
            if (StringUtils.isNotBlank(goodsListItemBO.getPic())) {
                pic = fileService.getUrl(goodsListItemBO.getPic(), FileTypeEnum.GOODS_PICTURE);
            } 
            taskSelectGoodsDTO.setGoodsPic(pic);
            //出货价格
            GoodsYilingPriceDTO goodsYilingPriceDTO = priceParamNameList.stream().filter(p -> p.getGoodsId().equals(goodsListItemBO.getId()) && p.getParamId().equals(1L)).findAny().orElse(null);
            if(Objects.nonNull(goodsYilingPriceDTO)){
                taskSelectGoodsDTO.setOutPrice(NumberUtil.round(goodsYilingPriceDTO.getPrice(),2, RoundingMode.HALF_UP));
            }else{
                taskSelectGoodsDTO.setOutPrice(BigDecimal.ZERO);
            }
            GoodsYilingPriceDTO goodsSellPriceDTO = priceParamNameList.stream().filter(p -> p.getGoodsId().equals(goodsListItemBO.getId()) && p.getParamId().equals(3L)).findAny().orElse(null);
            //商销售价格
            if(Objects.nonNull(goodsSellPriceDTO)){
                taskSelectGoodsDTO.setSellPrice(NumberUtil.round(goodsSellPriceDTO.getPrice(),2, RoundingMode.HALF_UP));
            }else{
                taskSelectGoodsDTO.setSellPrice(BigDecimal.ZERO);
            }
            taskSelectGoodsDTOS.add(taskSelectGoodsDTO);
        });
        taskSelectGoodsDTOPage.setRecords(taskSelectGoodsDTOS);
        taskSelectGoodsDTOPage.setTotal(goodsListItemBOPage.getTotal());
        return taskSelectGoodsDTOPage;
    }

    @Override
    public List<TaskGoodsMatchListDTO>  queryTaskGoodsList(List<QueryTaskGoodsMatchRequest> request) {
        //查询进行中的交易量任务
        LambdaQueryWrapper<MarketTaskDO> taskWrapper = Wrappers.lambdaQuery();
        taskWrapper.eq(MarketTaskDO::getFinishType, FinishTypeEnum.AMOUNT.getCode()).eq(MarketTaskDO::getTaskStatus, TaskStatusEnum.IN_PROGRESS.getStatus());
        List<MarketTaskDO> marketTaskDOS = SpringUtils.getBean(MarketTaskService.class).list(taskWrapper);
        if(CollUtil.isEmpty(marketTaskDOS)){
            return Lists.newArrayList();
        }
        List<Long> taskIds = marketTaskDOS.stream().map(MarketTaskDO::getId).collect(Collectors.toList());
        //查询用户承接的任务
        Long userId = request.get(0).getUserId();
        LambdaQueryWrapper<UserTaskDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(UserTaskDO::getUserId,userId).eq(UserTaskDO::getFinishType, FinishTypeEnum.AMOUNT.getCode()).in(UserTaskDO::getTaskStatus, UserTaskStatusEnum.IN_PROGRESS.getStatus(),UserTaskStatusEnum.FINISHED.getStatus()).select(UserTaskDO::getTaskId);
        wrapper.in(UserTaskDO::getTaskId,taskIds);
        List<Object> objects = SpringUtils.getBean(UserTaskService.class).listObjs(wrapper);
        if(CollUtil.isEmpty(objects)){
            return Lists.newArrayList();
        }
        objects = objects.stream().distinct().collect(Collectors.toList());
        List<Long> inTaskIds = PojoUtils.map(objects,Long.class);

        List<TaskGoodsMatchListDTO> taskGoodsMatchListDTOS = Lists.newArrayList();
        request.forEach(param->{
            TaskGoodsMatchListDTO taskGoodsMatchListDTO = new TaskGoodsMatchListDTO();
            taskGoodsMatchListDTO.setEid(param.getEid());
            List<TaskGoodsMatchDTO> goodsMatchList = Lists.newArrayList();
            taskGoodsMatchListDTO.setGoodsMatchList(goodsMatchList);
            //判断任务的配送商是否包含
            this.match(inTaskIds,taskGoodsMatchListDTO,param.getEid(),param.getGoodsName());
            if(CollUtil.isNotEmpty(taskGoodsMatchListDTO.getGoodsMatchList())){
                taskGoodsMatchListDTOS.add(taskGoodsMatchListDTO);
            }
        });
         return taskGoodsMatchListDTOS;
    }
    private void match(List<Long> inTaskIds,TaskGoodsMatchListDTO taskGoodsMatchListDTO,Long eid,String goodsName){
        inTaskIds.forEach(taskId->{
            LambdaQueryWrapper<TaskDistributorDO> wrapper = Wrappers.lambdaQuery();
            wrapper.eq(TaskDistributorDO::getTaskId,taskId);
            List<TaskDistributorDO> list = taskDistributorService.list(wrapper);
            boolean contain = false;
            if(CollUtil.isEmpty(list)){
                contain = true;
            }else {
                boolean contains = list.stream().map(TaskDistributorDO::getDistributorEid).collect(Collectors.toList()).contains(eid);
                if(contains){
                    contain = true;
                }
            }
            if(contain){
                LambdaQueryWrapper<TaskGoodsRelationDO> goodsRelationWrapper = Wrappers.lambdaQuery();
                goodsRelationWrapper.eq(TaskGoodsRelationDO::getTaskId,taskId).like(StrUtil.isNotEmpty(goodsName),TaskGoodsRelationDO::getGoodsName,goodsName);
                List<TaskGoodsRelationDO> goodsRelationDOS = this.list(goodsRelationWrapper);
                if(CollUtil.isEmpty(goodsRelationDOS)){
                    taskGoodsMatchListDTO.setGoodsMatchList(Lists.newArrayList());
                }
                List<TaskGoodsMatchDTO> map = PojoUtils.map(goodsRelationDOS, TaskGoodsMatchDTO.class);
                taskGoodsMatchListDTO.getGoodsMatchList().addAll(map);
            }
        });
        //去重
        taskGoodsMatchListDTO.getGoodsMatchList().stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparingLong(TaskGoodsMatchDTO::getGoodsId))), ArrayList::new));
    }
}
