package com.yiling.admin.hmc.gzh.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.yiling.admin.hmc.activity.form.BaseActivityForm;
import com.yiling.admin.hmc.gzh.form.GzhGreetingBaseForm;
import com.yiling.admin.hmc.gzh.form.QueryGzhGreetingForm;
import com.yiling.admin.hmc.gzh.form.SaveGzhGreetingForm;
import com.yiling.admin.hmc.gzh.vo.ButtonVO;
import com.yiling.admin.hmc.gzh.vo.HmcGzhGreetingVO;
import com.yiling.admin.hmc.gzh.vo.MenuVO;
import com.yiling.admin.hmc.gzh.vo.SubButtonVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.redis.RedisKey;
import com.yiling.framework.common.redis.service.RedisService;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.gzh.api.HmcGzhGreetingApi;
import com.yiling.hmc.gzh.dto.ButtonDTO;
import com.yiling.hmc.gzh.dto.GzhGreetingDTO;
import com.yiling.hmc.gzh.dto.Menu;
import com.yiling.hmc.gzh.dto.SubButtonDTO;
import com.yiling.hmc.gzh.dto.request.PublishGzhGreetingRequest;
import com.yiling.hmc.gzh.dto.request.QueryGzhGreetingRequest;
import com.yiling.hmc.gzh.dto.request.SaveGzhGreetingRequest;
import com.yiling.user.system.bo.CurrentAdminInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.menu.WxMenu;
import me.chanjar.weixin.common.bean.menu.WxMenuButton;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.menu.WxMpGetSelfMenuInfoResult;
import me.chanjar.weixin.mp.bean.menu.WxMpMenu;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 公众号菜单 Controller
 *
 * @author: fan.shen
 * @date: 2023-04-03
 */
@RestController
@RequestMapping("/gzhMenu")
@Api(tags = "公众号菜单")
@Slf4j
public class GzhMenuController extends BaseController {

    @Autowired
    WxMpService wxMpService;

    @Autowired
    RedisService redisService;

    @ApiOperation(value = "公众号菜单")
    @PostMapping("/menuListInfo")
    @Log(title = "公众号菜单", businessType = BusinessTypeEnum.OTHER)
    public Result<MenuVO> menListInfo() {
        String defaultMenu = "{\"button\":[{\"name\":\"健康福利\",\"sub_button\":[{\"type\":\"view\",\"name\":\"慢病关爱计划\",\"url\":\"https://www.wjx.cn/vm/QaB10oo.aspx#\",\"status\":\"1\"},{\"type\":\"view\",\"name\":\"\uD83D\uDD25转盘抽奖\",\"url\":\"https://h.59yi.com/#/active/luckyDraw/wechat?activeId=38\",\"status\":\"1\"}],\"status\":\"1\"},{\"name\":\"健康服务\",\"sub_button\":[{\"type\":\"view\",\"name\":\"\uD83D\uDD25健康自测\",\"url\":\"https://h.59yi.com/#/active/healthAssessment/\",\"status\":\"1\"},{\"type\":\"miniprogram\",\"name\":\"在线问诊\",\"url\":\"https://www.ylyy.org/\",\"appid\":\"wx19d5d0fb2b273808\",\"pagepath\":\"views/router\",\"status\":\"1\"},{\"type\":\"view\",\"name\":\"健康商城\",\"url\":\"https://j.youzan.com/g49Wn7\",\"status\":\"1\"},{\"type\":\"view\",\"name\":\"鉴别真伪\",\"url\":\"https://mp.weixin.qq.com/s?__biz=MzkwMjMyNjY4OQ==&mid=2247486615&idx=1&sn=b573e1c45334c88bfa4c0099630d6760&chksm=c0a67c59f7d1f54fcb4dfa1ff3bbb81b48af0668727c896572e7cea18fee879f02fcf14850f4#rd\",\"status\":\"1\"},{\"type\":\"miniprogram\",\"name\":\"用药提醒\",\"url\":\"https://hmc-api.59yi.com/\",\"appid\":\"wxc439962ebf26b00a\",\"pagepath\":\"pages/manager/index\",\"status\":\"1\"}],\"status\":\"1\"},{\"type\":\"miniprogram\",\"name\":\"个人中心\",\"url\":\"https://h.59yi.com/#/\",\"appid\":\"wxc439962ebf26b00a\",\"pagepath\":\"pages/my/index\",\"status\":\"1\"}]}";
        String key = RedisKey.generate("hmc", "gzh_menu");
        if (Objects.isNull(redisService.get(key))) {
            redisService.set(key, defaultMenu);
            MenuVO menuVO = JSONUtil.toBean(defaultMenu, MenuVO.class);
            return Result.success(menuVO);
        } else {
            String menu = redisService.get(key).toString();
            MenuVO menuVO = JSONUtil.toBean(menu, MenuVO.class);
            return Result.success(menuVO);
        }

    }

    @ApiOperation(value = "发布公众号菜单")
    @PostMapping("/publishMenu")
    @Log(title = "发布公众号菜单", businessType = BusinessTypeEnum.OTHER)
    public Result<Boolean> publishMenu(@RequestBody MenuVO menu) {
        List<ButtonVO> buttonVOList = menu.getButton().stream().filter(item -> item.getStatus().equals("1")).collect(Collectors.toList());
        WxMenu wxMenu = new WxMenu();
        List<WxMenuButton> buttons = new ArrayList<>();
        buttonVOList.forEach(buttonVO -> {
            if (buttonVO.getStatus().equals("1")) {
                WxMenuButton wxMenuButton = new WxMenuButton();
                wxMenuButton.setName(buttonVO.getName());
                if (CollUtil.isEmpty(buttonVO.getSubButton())) {
                    wxMenuButton.setType(buttonVO.getType());
                    wxMenuButton.setUrl(buttonVO.getUrl());
                    if (StrUtil.isNotBlank(buttonVO.getAppid())) {
                        wxMenuButton.setAppId(buttonVO.getAppid());
                    }
                    if (StrUtil.isNotBlank(buttonVO.getPagepath())) {
                        wxMenuButton.setPagePath(buttonVO.getPagepath());
                    }
                } else {
                    List<SubButtonVO> subButtonDTOS = buttonVO.getSubButton().stream().filter(subButtonDTO -> subButtonDTO.getStatus().equals("1")).collect(Collectors.toList());
                    List<WxMenuButton> wxSubButtons = Lists.newArrayList();
                    subButtonDTOS.forEach(subButtonDTO -> {
                        WxMenuButton wxMenuSubButton = new WxMenuButton();
                        wxMenuSubButton.setType(subButtonDTO.getType());
                        wxMenuSubButton.setName(subButtonDTO.getName());
                        wxMenuSubButton.setUrl(subButtonDTO.getUrl());
                        if (StrUtil.isNotBlank(subButtonDTO.getAppid())) {
                            wxMenuSubButton.setAppId(subButtonDTO.getAppid());
                        }
                        if (StrUtil.isNotBlank(subButtonDTO.getPagepath())) {
                            wxMenuSubButton.setPagePath(subButtonDTO.getPagepath());
                        }
                        wxSubButtons.add(wxMenuSubButton);
                    });
                    wxMenuButton.setSubButtons(wxSubButtons);
                }
                buttons.add(wxMenuButton);
            }
        });

        try {
            wxMenu.setButtons(buttons);
            log.info("ready to create menu：{}", JSONUtil.toJsonPrettyStr(wxMenu));
            wxMpService.getMenuService().menuCreate(wxMenu);

            // 放到redis
            String key = RedisKey.generate("hmc", "gzh_menu");
            log.info("key:{}", key);
            redisService.set(key, JSONUtil.toJsonStr(menu));

        } catch (WxErrorException e) {
            log.error("发布公众号菜单出错 error:{}", e.getMessage(), e);
            return Result.failed("发布公众号菜单出错,错误提示：" + e.getMessage());
        }
        return Result.success(Boolean.TRUE);
    }

}
