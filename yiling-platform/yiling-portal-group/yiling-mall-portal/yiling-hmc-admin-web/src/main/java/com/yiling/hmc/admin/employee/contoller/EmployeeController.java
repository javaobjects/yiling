package com.yiling.hmc.admin.employee.contoller;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.yiling.goods.standard.api.StandardGoodsSpecificationApi;
import com.yiling.goods.standard.dto.StandardGoodsSpecificationDTO;
import com.yiling.hmc.admin.employee.vo.DrugWelfareQrCodeVO;
import com.yiling.hmc.welfare.api.DrugWelfareApi;
import com.yiling.hmc.welfare.api.DrugWelfareEnterpriseApi;
import com.yiling.hmc.welfare.dto.DrugWelfareDTO;
import com.yiling.hmc.welfare.dto.DrugWelfareEnterpriseDTO;
import com.yiling.hmc.welfare.enums.WxQrTypeEnum;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.admin.employee.form.QueryEmployeePageListForm;
import com.yiling.hmc.admin.employee.vo.EmployeePageListItemVO;
import com.yiling.user.enterprise.api.EmployeeApi;
import com.yiling.user.enterprise.dto.EnterpriseEmployeeDTO;
import com.yiling.user.enterprise.dto.request.QueryEmployeePageListRequest;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentStaffInfo;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.core.collection.CollUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;

/**
 * 员工模块 Controller
 *
 * @author: gxl
 * @date: 2022/3/20
 */
@RestController
@RequestMapping("/employee")
@Api(tags = "销售员二维码")
@Slf4j
public class EmployeeController extends BaseController {

    @DubboReference
    UserApi userApi;
    @DubboReference
    EmployeeApi employeeApi;

    @DubboReference
    DrugWelfareApi welfareApi;

    @DubboReference
    DrugWelfareEnterpriseApi welfareEnterpriseApi;

    @Autowired
    private WxMpService mpService;

    @DubboReference
    StandardGoodsSpecificationApi specificationApi;

    @ApiOperation(value = "员工分页列表")
    @GetMapping("/pageList")
    public Result<Page<EmployeePageListItemVO>> pageList(@CurrentUser CurrentStaffInfo staffInfo, QueryEmployeePageListForm form) {
        QueryEmployeePageListRequest request = PojoUtils.map(form, QueryEmployeePageListRequest.class);
        request.setEid(staffInfo.getCurrentEid());
        Page<EnterpriseEmployeeDTO> page = employeeApi.pageList(request);
        List<EnterpriseEmployeeDTO> list = page.getRecords();
        if (CollUtil.isEmpty(list)) {
            return Result.success(request.getPage());
        }

        List<Long> userIds = list.stream().map(EnterpriseEmployeeDTO::getUserId).collect(Collectors.toList());
        List<UserDTO> userDTOList = userApi.listByIds(userIds);
        Map<Long, UserDTO> userDTOMap = userDTOList.stream().collect(Collectors.toMap(UserDTO::getId, Function.identity()));

        // 获取当前企业参与的药品福利计划
        List<DrugWelfareEnterpriseDTO> welfareEnterprise = welfareEnterpriseApi.getByEid(staffInfo.getCurrentEid());

        List<EmployeePageListItemVO> voList = CollUtil.newArrayList();
        for (EnterpriseEmployeeDTO item : list) {
            EmployeePageListItemVO vo = PojoUtils.map(item, EmployeePageListItemVO.class);
            vo.setId(item.getUserId());

            UserDTO userDTO = userDTOMap.get(item.getUserId());
            if (userDTO != null) {
                vo.setName(userDTO.getName());
                vo.setMobile(userDTO.getMobile());
            }

            if(CollUtil.isNotEmpty(welfareEnterprise)) {
                vo.setShowDrugWelfareButtonFlag(1);
            }

            voList.add(vo);
        }

        Page<EmployeePageListItemVO> pageVO = new Page<>(page.getCurrent(), page.getSize());
        pageVO.setRecords(voList);
        return Result.success(pageVO);
    }

    @ApiOperation(value = "查看二维码")
    @GetMapping("/getQrcode")
    public Result getQrcode(@CurrentUser CurrentStaffInfo staffInfo, @RequestParam Long id) throws WxErrorException {
//        String sceneStr = "2_"+staffInfo.getCurrentEid()+"_"+id;
        String sceneStr = "qt:10_so:2_eId:" + staffInfo.getCurrentEid() + "_uId:" + id;
        WxMpQrCodeTicket ticket = mpService.getQrcodeService().qrCodeCreateLastTicket(sceneStr);
        String url = this.mpService.getQrcodeService().qrCodePictureUrl(ticket.getTicket());
        Map map = Maps.newHashMap();
        map.put("url", url);
        return Result.success(map);
    }

    @ApiOperation(value = "药品福利计划二维码")
    @GetMapping("/getDrugWelfareQrcode")
    public Result<List<DrugWelfareQrCodeVO>> getDrugWelfareQrcode(@CurrentUser CurrentStaffInfo staffInfo, @RequestParam Long id) throws WxErrorException {
        // 获取当前企业参与的药品福利计划
        List<DrugWelfareEnterpriseDTO> welfareEnterprise = welfareEnterpriseApi.getByEid(staffInfo.getCurrentEid());
        if (CollUtil.isEmpty(welfareEnterprise)) {
            return Result.success(Lists.newArrayList());
        }
        List<Long> welfareIdList = welfareEnterprise.stream().map(DrugWelfareEnterpriseDTO::getDrugWelfareId).collect(Collectors.toList());
        List<DrugWelfareDTO> welfareDTOList = welfareApi.getByIdList(welfareIdList);
        if (CollUtil.isEmpty(welfareDTOList)) {
            return Result.success(Lists.newArrayList());
        }

        List result = Lists.newArrayList();
        welfareDTOList.forEach(item -> {
            DrugWelfareQrCodeVO qrCodeVO = new DrugWelfareQrCodeVO();
            WxQrTypeEnum type = WxQrTypeEnum.getByCode(item.getDrugWelfareType());
            if (Objects.isNull(type)) {
                log.info("根据药品福利类型未匹配到枚举");
                return;
            }
            String sceneStr = "qt:" + type.getCode() + "_eId:" + staffInfo.getCurrentEid() + "_uId:" + id + "_wId:" + item.getId();
            try {
                WxMpQrCodeTicket ticket = mpService.getQrcodeService().qrCodeCreateLastTicket(sceneStr);
                String url = this.mpService.getQrcodeService().qrCodePictureUrl(ticket.getTicket());
                qrCodeVO.setUrl(url);
            } catch (WxErrorException e) {
                log.error("生成微信二维码报错:{}", ExceptionUtils.getStackTrace(e), e);
            }

            StandardGoodsSpecificationDTO specification = specificationApi.getStandardGoodsSpecification(item.getSellSpecificationsId());
            if (Objects.isNull(specification)) {
                log.info("根据规格id未获取到规格对象");
                return;
            }

            qrCodeVO.setSellSpecifications(specification.getName() + "（" + (specification.getLicenseNo() + "," + specification.getSellSpecifications()) + "）");
            qrCodeVO.setName(item.getName());
            qrCodeVO.setSellSpecificationsId(item.getSellSpecificationsId());

            result.add(qrCodeVO);
        });

        return Result.success(result);
    }

}
