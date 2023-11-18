// 创建，修改，查看 任务
<template>
  <div class="app-container">
    <!-- 内容区 -->
    <div class="app-container-content has-bottom-bar">
      <el-form 
        :model="rwInfoData" 
        :rules="rules" 
        ref="dataForm" 
        inline-message 
        label-width="112px" 
        class="demo-ruleForm">
        <!-- 任务信息 -->
        <div class="top-box">
          <div class="flex-row-left item">
            <div class="line-view"></div>
            <span class="font-size-lg bold">任务信息</span>
          </div>
          <el-form-item label="任务主体：" class="from-item-bottom">
            平台任务
          </el-form-item>
          <el-form-item label="任务类型：" prop="finishType" class="from-item-bottom">
            <el-radio-group v-model="rwInfoData.finishType" :disabled="buttomType == 0 ? false : true">
              <el-radio :label="2">交易量</el-radio>
              <el-radio :label="3">新户推广</el-radio>
              <el-radio :label="7">新人推广</el-radio>
              <el-radio :label="8">购买会员</el-radio>
              <el-radio :label="10">上传资料</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="任务名称：" prop="taskName">
            <el-input 
              :disabled="disabled" 
              v-model.trim="rwInfoData.taskName" 
              maxlength="20" 
              show-word-limit 
              placeholder="请输入任务名称"/>
            <span style="color:#666;padding-left:10px">最多20个汉字，包含标点符号</span>
          </el-form-item>
          <el-form-item label="任务说明：" prop="taskDesc">
            <div class="inputTextarea">
              <el-input 
                show-word-limit
                :disabled="disabled" 
                type="textarea" 
                placeholder="请输入内容" 
                v-model="rwInfoData.taskDesc" 
                maxlength="500" 
                :autosize="{minRows: 4, maxRows: 6 }">
              </el-input>
            </div>
          </el-form-item>
        </div>
        <!-- 参与条件 -->
        <div class="top-box">
          <div class="flex-row-left item">
            <div class="line-view"></div>
            <span class="font-size-lg bold">参与条件</span>
          </div>
          <el-form-item 
            label="任务发布部门：" 
            v-if="rwInfoData.finishType == 10" 
            class="from-item-bottom" 
            prop="departmentData" 
            :rules="{ required: true, message: '任务发布部门不能为空', trigger: 'change'}"
            >
            <el-select
              v-model="rwInfoData.departmentDataName"
              multiple
              @remove-tag="removeTag"
              placeholder="请选择部门"
              class="form-select">
              <el-option value="" style="height: auto; padding: 0">
                <department-tree
                  node-key="id"
                  :props="{ children: 'children', label: 'name' }"
                  @node-click="handleNodeFormClick">
                </department-tree>
              </el-option>
            </el-select>
            <!-- <el-input style="display:none" v-model="rwInfoData.departmentData.toString()"/>
            <span v-show="rwInfoData.departmentData && rwInfoData.departmentData.length > 0">已选择 {{ rwInfoData.departmentData.length }} 个部门</span>
            <span class="tfqy-span" @click="addDepartment"> 
              {{ rwInfoData.departmentData && rwInfoData.departmentData.length > 0 ? '修改与查看' : '请选择任务发布部门' }}
            </span> -->
          </el-form-item>
          <el-form-item label="任务承接人群：" class="from-item-bottom" prop="cyStatus">
            <el-checkbox-group v-model="rwInfoData.cyStatus" :disabled="disabled" style="display: inline-block">
              <el-checkbox label="1">小三元 (非以岭且有企业主体的人群)</el-checkbox>
              <el-checkbox label="2" v-if="rwInfoData.finishType != 10">医药自然人 (非以岭且没有主体的人群)</el-checkbox>
            </el-checkbox-group>
          </el-form-item>
          <el-form-item label="参与配送商：" v-if="rwInfoData.finishType == 2 || rwInfoData.finishType == 10" class="from-item-bottom">
            <span>{{ rwInfoData.dialogDataNum == '' ? '' : rwInfoData.dialogDataNum + '家' }}</span>
            <span class="tfqy-span" @click="distributorClick"> 
              {{ rwInfoData.dialogDataNum ? '修改与查看' : '请选择配送商' }}
            </span>
          </el-form-item>
        </div>
        <!-- 投放条件 -->
        <div class="top-box topBox2">
          <div class="flex-row-left item">
            <div class="line-view"></div>
            <span class="font-size-lg bold">投放条件</span>
          </div>
          <el-form-item label="投放时间：" class="from-item-bottom" prop="launchTime" v-if="rwInfoData.finishType == 10">
            <el-date-picker 
              :disabled="disabled"
              v-model="rwInfoData.launchTime"
              type="daterange"
              format="yyyy/MM/dd"
              value-format="yyyy-MM-dd"
              range-separator="-"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              :picker-options="pickerOption2"
              >
            </el-date-picker>
          </el-form-item>
          <el-form-item label="投放时间：" class="from-item-bottom" prop="launchTime" v-else>
            <el-date-picker 
              :disabled="disabled"
              v-model="rwInfoData.launchTime"
              type="daterange"
              format="yyyy/MM/dd"
              value-format="yyyy-MM-dd"
              range-separator="-"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              :picker-options="pickerOptions"
              >
            </el-date-picker>
          </el-form-item>
          <el-form-item
            v-if="rwInfoData.finishType != 10"
            label="投放区域：" 
            class="from-item-bottom" 
            prop="desc" 
            :rules="{ required: true, message: '投放区域不能为空', trigger: 'change'}">
              <el-input style="display:none" v-model="rwInfoData.desc"/>
              <span>{{ rwInfoData.desc }}</span>
              <span class="tfqy-span" @click="selectAreaClick"> 
                {{ rwInfoData.desc == '' ? '请选择区域' : '修改与查看' }}
              </span>
          </el-form-item>
        </div>
        <!-- 任务条件设置 -->
        <div class="top-box">
          <div class="flex-row-left item">
            <div class="line-view"></div>
            <span class="font-size-lg bold">任务条件设置</span>
          </div>
          <!-- 上传资料 -->
          <div v-if="rwInfoData.finishType == 10">
            <el-form-item label="企业类型：" class="from-item-bottom">
              <el-checkbox-group v-model="rwInfoData.enterpriseType" :disabled="disabled">
                <el-checkbox 
                  v-for="item in companyType" 
                  v-show="item.value != 1 && item.value != 2"
                  :label="item.value" 
                  :key="item.value">
                  {{ item.label }}
                </el-checkbox>
              </el-checkbox-group>
            </el-form-item>
          </div>
          <!-- 交易量 -->
          <div v-if="rwInfoData.finishType == 2 || rwInfoData.finishType == 10">
            <el-form-item label="支付方式：" class="from-item-bottom" v-if="rwInfoData.finishType == 2">
              <el-checkbox-group v-model="rwInfoData.payment" :disabled="disabled">
                <el-checkbox 
                  v-for="item in paymentMethod"
                  :key="item.value" 
                  :label="item.value">
                  {{ item.label }}
                </el-checkbox>
              </el-checkbox-group>
            </el-form-item>
            <el-form-item label="是否会员：" class="from-item-bottom" v-if="rwInfoData.finishType == 2">
              <el-radio-group v-model="rwInfoData.member" :disabled="disabled">
                <el-radio :label="''">全部</el-radio>
                <el-radio :label="1">是</el-radio>
                <el-radio :label="0">否</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="销售条件：" class="from-item-bottom" prop="xstjNum">
              <el-radio-group v-model="rwInfoData.xstjNum" :disabled="disabled" @change="xstjNumChange">
                <el-radio :label="1">阶梯条件</el-radio>
                <el-radio :label="2">非阶梯条件</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="任务条件计算：" class="from-item-bottom" v-if="rwInfoData.finishType == 2">
              多品计算（所选商品综合计算，即任务所选品类的销售条件之和达到标准就算完成任务）
            </el-form-item>
            <!-- 阶梯条件 -->
            <el-form-item label="具体条件：" v-if="rwInfoData.xstjNum == 1">
              <div v-for="(item,index) in rwInfoData.xsjtDataList" :key="index" class="conter_div">
                <el-form-item 
                  :label="'阶梯' + (index + 1)" 
                  :prop="'xsjtDataList.' + index + '.commissionCondition'" 
                  :rules="{ required: true, message: '销售盒不能为空', trigger: 'blur'}" 
                  style="display: inline-block">
                    <span>销售：</span> 
                    <el-input 
                      :disabled="disabled" 
                      class="el_input" 
                      v-model="item.commissionCondition" 
                      maxlength="50" 
                      @input="e => (item.commissionCondition = checkInput(e))"/> 
                    盒
                </el-form-item>
                <el-form-item 
                  class="form-item-left" 
                  :prop="'xsjtDataList.' + index + '.commission'" 
                  :rules="{ required: true, message: '每盒佣金奖励不能为空', trigger: 'blur'}"
                  style="display: inline-block">
                    <span style="padding-left:20px">
                    每盒给予 
                    <el-input 
                      @input="e => (item.commission = checkInput(e))" 
                      :disabled="disabled" 
                      class="el_input" 
                      v-model="item.commission" 
                      maxlength="50"/> 
                      元作为佣金奖励
                    </span>
                  <span v-show="index != 0 && index != 1 && disabled == false" @click="deletejtClick(item, index)" class="icon-span">
                    <i class="el-icon-error icon_style"></i>
                  </span>
                </el-form-item>
              </div>
              <br/>
              <div class="add_conter" v-if="disabled == false" @click="addJttjClick">新增阶梯条件</div>
            </el-form-item>
            <!-- 非阶梯条件 -->
            <div v-if="rwInfoData.xstjNum == 2">
              <el-form-item label="具体条件：" prop="jttjNum">
                总计销售 
                <el-input 
                  :disabled="disabled"
                  @input="e => (rwInfoData.jttjNum = checkInput(e))" 
                  style="width:200px" 
                  v-model="rwInfoData.jttjNum" 
                  maxlength="10" 
                  placeholder="请输入销售额"/> 
                  盒
              </el-form-item>
            </div>
          </div>
          <!-- 新户推广 , 新人推广 -->
          <div v-if="rwInfoData.finishType == 3 || rwInfoData.finishType == 7">
            <el-form-item :label="rwInfoData.finishType == 3 ? '新客户条件限制：' : (rwInfoData.finishType == 7 ? '新人条件限制：' : '')" class="from-item-bottom">
              <el-radio-group v-model="rwInfoData.tjxzNum" :disabled="disabled" @change="tjxzNumChange">
                <el-radio :label="1">有限制</el-radio>
                <el-radio :label="-1">{{ rwInfoData.finishType == 3 ? '无限制(系统将按照审核通过后进行累计计算)' : (rwInfoData.finishType == 7 ? '无限制（注册成功即可）' : '') }}</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="支付方式：" class="from-item-bottom" v-if="rwInfoData.finishType == 3 && rwInfoData.tjxzNum == 1">
              <el-checkbox-group v-model="rwInfoData.payment" :disabled="disabled">
                <el-checkbox 
                  v-for="item in paymentMethod"
                  :key="item.value" 
                  :label="item.value">
                  {{ item.label }}
                </el-checkbox>
              </el-checkbox-group>
            </el-form-item>
            <el-form-item 
              label="任务条件计算：" 
              class="from-item-bottom" 
              v-if="rwInfoData.tjxzNum == 1" 
              prop="rwtjxz">
              <el-radio-group v-model="rwInfoData.rwtjxz" :disabled="disabled">
                <el-radio :label="0">首单</el-radio>
                <el-radio :label="1">累计</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="" class="from-item-bottom" v-if="rwInfoData.tjxzNum == 1" prop="amount">
              <span class="item-text-span"></span>
              采购以岭品达到 
              <el-input 
                size="mini" 
                :disabled="disabled" 
                v-model="rwInfoData.amount" 
                style="width: 150px;" 
                placeholder="请输入" 
                maxlength="10" 
                @input="e => (rwInfoData.amount = checkInput(e))">
              </el-input> 
              元
            </el-form-item>
          </div>
          <!-- 新人推广 -->
          <!-- <div v-if="rwInfoData.finishType == 7">
            <div class="font-size-base font-title-color item-text">
              是否有条件限制：无限制（注册成功即可）
            </div>
          </div> -->
          <!-- 购买会员 -->
          <div v-if="rwInfoData.finishType == 8">
            <el-row :gutter="20">
              <el-col :span="8">
                <el-form-item label="会员名称：" prop="yyName">
                  <el-select v-model="rwInfoData.yyName" :disabled="disabled">
                    <el-option
                      v-for="item in memberName"
                      :key="item.memberId"
                      :label="item.name"
                      :value="item.memberId"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="获得条件：" prop="hdName">
                  <el-select v-model="rwInfoData.hdName" :disabled="disabled" @change="hdNameChange">
                    <el-option
                      v-for="item in activityData"
                      :key="item.memberStageId"
                      :label="item.price"
                      :value="item.memberStageId"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="4">
                <div class="term-time" v-if="termTime != ''">有效期：{{ termTime }} 天</div>
              </el-col>
            </el-row>
            <el-form-item label="推广海报：" class="from-item-bottom" prop="pic">
              <yl-upload
                :default-url="rwInfoData.pic"
                :extral-data="{type: 'taskMemberShare'}"
                @onSuccess="onSuccess"
              />
            </el-form-item>
          </div>
        </div>
        <!-- 佣金设置 -->
        <div class="top-box">
          <div class="flex-row-left item">
            <div class="line-view"></div>
            <span class="font-size-lg bold">佣金设置</span>
          </div>
          <!-- 交易量 -->
          <el-form-item label="佣金政策：" class="from-item-bottom" v-if="rwInfoData.finishType == 2 || rwInfoData.finishType == 10">
            {{ rwInfoData.xstjNum == 1 ? '统一执行' : '单独执行' }}
          </el-form-item>
          <!-- 新户/新人 推广 -->
          <el-form-item label="佣金设置：" class="from-item-bottom" v-if="rwInfoData.finishType != 2 && rwInfoData.finishType != 10" prop="reward">
            {{ rwInfoData.finishType == 8 ? '购买后' : '' }} 
            每{{ rwInfoData.finishType == 3 ? '户' : '人' }}给予 
            <el-input 
              size="mini" 
              :disabled="disabled" 
              v-model="rwInfoData.reward" 
              style="width: 110px;" 
              placeholder="请输入" 
              maxlength="10" 
              @input="e => (rwInfoData.reward = checkInput(e))">
            </el-input> 
            元作为佣金奖励
          </el-form-item>
          <el-form-item label="上下线分成：" class="from-item-bottom" prop="fc">
            <el-radio-group v-model="rwInfoData.fc" :disabled="disabled" @change="fcChange">
              <el-radio :label="1" v-if="rwInfoData.finishType != 10">有</el-radio>
              <el-radio :label="2">无</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item 
            label="上线分成比例:" 
            class="from-item-bottom" 
            v-if="rwInfoData.fc == 1" 
            prop="scbl">
            <el-input 
              style="width:80px" 
              :disabled="disabled" 
              v-model="rwInfoData.scbl" 
              maxlength="20" 
              @input="e => (rwInfoData.scbl = checkInput(e))" /> % 
              (举例说明：一个商品下线分销成功，佣金为10元，按照0.2的比例来算，上线将获得2元，下线获得8元。)
          </el-form-item>
        </div>
      </el-form>
      <!-- 底部 商品设置 -->
      <div v-if="rwInfoData.finishType == 2 || rwInfoData.finishType == 10">
        <div class="down-box clearfix" style="margin-top:20px;text-align: right">
          <div class="btn">
            <ylButton type="primary" plain @click="exportProduct">导出B2B以岭品</ylButton>
            <ylButton type="primary" v-if="disabled == false" plain @click="importTemp">导入商品</ylButton>
            <ylButton type="primary" v-if="disabled == false || buttomType == 3" plain @click="addTemp">添加商品</ylButton>
          </div>
        </div>
        <div class="mar-t-8 bottom-content-view" style="margin-top:11px; background: #FFFFFF">
          <div class="top-box" style="margin-bottom:0; padding:16px 16px 0 16px">
            <div class="flex-row-left item">
              <div class="line-view"></div>
              <span class="font-size-lg bold">商品设置</span>
            </div>
          </div>
          <div class="mar-t-8 pad-b-10 order-table-view">
            <yl-table
              ref="table"
              :list="dataList"
              :cell-class-name="() => 'border-1px-b'">
              <el-table-column>
                <template slot-scope="{ row, $index }">
                  <div class="table-view" :key="$index">
                    <div class="content flex-row-left">
                      <div class="content-left">
                        <div class="content-left-title">{{ row.goodsName }}</div>
                        <div class="item" style="font-size:14px;font-weight: normal">
                          <span class="font-title-color">商品规格：</span>
                          {{ row.sellSpecifications }}
                        </div>
                      </div>
                      <div class="content-center font-size-base font-important-color">
                        <div class="item"></div>
                        <div class="item">
                          <span class="font-title-color">商品基价：</span>
                          {{ row.price | toThousand('￥') }}
                        </div>
                      </div>
                      <div class="content-center font-size-base font-important-color">
                        <div class="item"></div>
                        <div class="item">
                          <span class="font-title-color">出货价：</span>
                          {{ row.outPrice | toThousand('￥') }}
                        </div>
                      </div>
                      <div class="content-center font-size-base font-important-color">
                        <div class="item"></div>
                        <div class="item">
                          <span class="font-title-color">商销价：</span>
                          {{ row.sellPrice | toThousand('￥') }}
                        </div>
                      </div>
                      <div class="font-size-base font-important-color">
                        <div class="item"></div>
                        <div class="item-commission" v-if="rwInfoData.xstjNum == 2">
                          <span class="font-title-color">佣金政策：</span>
                          每盒给予 
                          <span v-if="buttomType == 1"> {{ row.commission }} 元作为佣金奖励</span>
                          <div v-if="buttomType == 3" class="item-display">
                            <span v-if="row.taskStatus == 1"> {{ row.commission }} </span>
                            <el-input 
                              v-else
                              size="mini" 
                              v-model="row.commission" 
                              @input="e => (rwInfoData.reward = checkInput(e))" 
                              style="width: 80px" 
                              placeholder="请输入" 
                              maxlength="10">
                            </el-input>
                            元作为佣金奖励
                          </div>
                          <div v-if="buttomType == 0 || buttomType == 2" class="item-display">
                            <el-input
                              size="mini" 
                              v-model="row.commission" 
                              @input="e => (rwInfoData.reward = checkInput(e))" 
                              style="width: 80px" 
                              placeholder="请输入" 
                              maxlength="10">
                            </el-input>
                            元作为佣金奖励
                          </div>
                        </div>
                      </div>
                      <div class="content-center font-size-base font-important-color mar-left" v-if="row.createTime">
                        <div class="item"></div>
                        <div class="item">
                          <span>添加商品时间：</span>
                          {{ row.createTime | formatDate }}
                        </div>
                      </div>
                      <div class="content-right flex-column-center table-button" v-if="disabled == false">
                        <yl-button type="text" @click="removeClick(row,$index)">移除</yl-button>
                      </div>
                      <div class="content-right flex-column-center table-button" v-if="row.taskStatus != 1 && buttomType == 3">
                        <yl-button type="text" @click="removeClick(row,$index)">移除</yl-button>
                      </div>
                    </div>
                  </div>
                </template>
              </el-table-column>
            </yl-table>
          </div>
        </div>
      </div>
    </div>
    <!-- 底部 发布任务 -->
    <div class="flex-row-center bottom-view" v-if="buttomType == 0">
      <yl-button plain type="primary" @click="$router.go(-1)">取消任务</yl-button>
      <yl-button type="primary" @click="preservationClick">发布任务</yl-button>
    </div>
    <div class="flex-row-center bottom-view" v-if="buttomType == 1">
      <yl-button plain type="primary" @click="$router.go(-1)">返回</yl-button>
    </div>
    <div class="flex-row-center bottom-view" v-if="buttomType == 2 || buttomType == 3">
      <yl-button plain type="primary" @click="$router.go(-1)">返回</yl-button>
      <yl-button type="primary" @click="preservationClick2">保存</yl-button>
    </div>
    <!-- 选择配送商 的弹窗 -->
    <!-- 弹窗 -->
    <div v-if="showDialog" class="dialog-top">
      <yl-dialog 
        :title="dialogTitle" 
        :show-footer="true" 
        @confirm="pssConfirm" 
        :show-cancle="false" 
        width="890px" 
        :visible.sync="showDialog">
        <div class="dialogTc">
          <div class="dialogTc-top"></div>
          <div class="common-box" v-if="buttomType != 1">
            <div class="search-box">
              <el-row class="box">
                <el-col :span="12">
                  <div class="title">企业名称</div>
                  <el-input v-model.trim="dialog.name" placeholder="请输入企业名称" />
                </el-col>
                <el-col :span="12">
                  <div class="title">是否管控</div>
                  <el-select class="select-width" v-model="dialog.type" placeholder="请选择">
                    <el-option :key="0" label="全部" :value="0"></el-option>
                    <el-option :key="1" label="管控" :value="1"></el-option>
                    <el-option :key="2" label="不管控" :value="2"></el-option>
                  </el-select>
                </el-col>
              </el-row>
            </div>
            <div class="search-box mar-t-16">
              <el-row class="box">
                <el-col :span="16">
                  <yl-search-btn :total="dialog.total" @search="handleSearch" @reset="handleReset"/>
                </el-col>
              </el-row>
            </div>
          </div>
          <!-- 下部 表格 -->
          <div class="search-bar" v-if="buttomType != 1">
            <yl-table 
              border 
              :show-header="true" 
              :list="dialogDataList" 
              :total="dialog.total" 
              :page.sync="dialog.page" 
              :limit.sync="dialog.limit" 
              :loading="dialogLoading" 
              @getList="distributorPageApi">
              <el-table-column align="center" min-width="200" label="企业名称" prop="name">
              </el-table-column>
              <el-table-column align="center" min-width="80" label="是否管控">
                <template slot-scope="{ row }">
                  <div>
                    {{ row.type == 1 ? '管控' : '不管控' }}
                  </div>
                </template>
              </el-table-column>
              <el-table-column fixed="right" align="center" label="操作" min-width="80">
                <template slot-scope="{ row }">
                  <div>
                    <yl-button type="text" @click="addClick(row)">
                      <span :style="{color: row.zt == 1 ? '#1790ff' : '#c8c9cc'}">
                        {{ row.zt == 1 ? '添加' : (row.zt == 2 ? '已添加' : '') }}
                      </span>
                    </yl-button>
                  </div>
                </template>
              </el-table-column>
            </yl-table>
          </div>
          <!-- 已添加 -->
          <div class="added-conter" v-show="rwInfoData.dialogDataList2.length > 0">
            <div class="flex-row-left item">
              <div class="line-view"></div>
              <span class="font-size-lg bold">已添加的配送商</span>
            </div>
            <yl-table border :show-header="true" :list="rwInfoData.dialogDataList2">
              <el-table-column align="center" min-width="200" label="企业名称" prop="name">
              </el-table-column>
              <el-table-column align="center" min-width="80" label="是否管控">
                <template slot-scope="{ row }">
                  <div>
                    {{ row.type == 1 ? '管控' : '不管控' }}
                  </div>
                </template>
              </el-table-column>
              <el-table-column 
                fixed="right" 
                align="center" 
                label="操作" 
                min-width="80" 
                >
                <template slot-scope="{ row, $index }">
                  <div v-if="row.taskStatus != 1">
                    <yl-button type="text" @click="deleteClick(row, $index)">
                      删除
                    </yl-button>
                  </div>
                </template>
              </el-table-column>
            </yl-table>
          </div>
        </div>
      </yl-dialog>
    </div>
    <!-- 投放区域 -->
    <CitySelect 
      platform="admin" 
      @choose="choose" 
      :show.sync="show" 
      :title="areaSelectTitle" 
      :init-ids="cityData" 
      :deep="2"
      :show-all-check="true" 
      :show-confirm="buttomType == 1 || buttomType == 3 ? false : true">
    </CitySelect>
    <!-- 添加商品 -->
    <commodity-con 
      v-if="addItemShow" 
      @addCommodity="addCommodity" 
      :data-commodity="dataList" 
      :show.sync="addItemShow">
    </commodity-con>
    <!-- 导入商品 -->
    <yl-dialog title="导入" :visible.sync="importDialog" aria-atomic="" :show-footer="false">
      <div class="import-dialog-content flex-row-center">
        <yl-upload-file
          :action="info.action"
          :extral-data="info.extralData"
          @onSuccess="importSuccess"
        />
      </div>
    </yl-dialog>
    <!-- 导入商品结果 -->
    <yl-dialog
      title="批量导入结果"
      :show-footer="false"
      width="585px"
      :visible.sync="importShow">
      <div class="import-content">
        <div class="content">
          <div class="content-top">
            <div>本次预导入数据：<span class="font-important-color">{{ importData.total }}</span> 条</div>
            <div>成功导入：<span class="important-success-color">{{ importData.successCount }}条</span></div>
            <div>导入失败：<span class="important-fail-color">{{ importData.failCount }}条</span></div>
          </div>
        </div>
        <div class="down-btn" >
          <yl-button type="primary" @click="handleDownLoad(importData)" v-if="importData.failCount">
            导出失败结果
          </yl-button>
          <yl-button type="primary" @click="confirmImport">
            确认
          </yl-button>
        </div>
      </div>
    </yl-dialog>
    <!-- 添加部门 -->
    <!-- <department
      v-if="departmentShow"
      :show.sync="departmentShow"
      @departmentConfirm="departmentConfirm"
      :department-data="rwInfoData.departmentData"
      :disabled="disabled">
    </department> -->
  </div>
</template>

<script>
  import { 
    getDetailById, 
    queryTaskArea, 
    queryTaskDistributorPage, 
    queryDistributorPage, 
    deleteTaskDistributor, 
    addRenwu, 
    queryMember, 
    queryMemberStage, 
    update, 
    deleteGoods
  } from '@/subject/admin/api/views_sale/task_administration.js';
  import CitySelect from '@/subject/admin/components/CitySelect';
  import commodityCon from './components/commodity';
  import { ylUpload, ylUploadFile } from '@/subject/admin/components';
  import { formatDate } from '@/subject/admin/utils';
  import { createDownLoad } from '@/subject/admin/api/common'
  import { assistantPaymentMethod } from '@/subject/admin/busi/sale/task';
  import { enterpriseType } from '@/subject/admin/utils/busi'
  // import department from './components/department'
  import departmentTree from './components/departmentTree'
export default {
  name: 'Establishs',
  components: {
    CitySelect, 
    commodityCon, 
    ylUpload,
    ylUploadFile,
    // department,
    departmentTree
  },
  computed: {
    paymentMethod() {
      return assistantPaymentMethod()
    },
    companyType() {
      return enterpriseType()
    }
  },
  data() {
    return {
      // 任务信息
      rwInfoData: {
        //任务主体 0 平台任务 1企业任务
        status: 0, 
        //任务类型
        finishType: 0, 
        //任务名称
        taskName: '', 
        //任务说明
        taskDesc: '',
        //任务id
        taskID: '',
        // 参与条件
        //1 小三元 2 自然人
        cyStatus: [], 
        cyStatusID: '',
        //参与配送商
        dialogDataList2: [],
        //几家配送商
        dialogDataNum: '', 
        // 投放条件
        //投放时间
        launchTime: [], 
        desc: '',
        // 任务条件设置
        // 1 阶梯条件 2 非阶梯条件
        xstjNum: 1,
        // 支付方式
        payment: [],
        // 支付方式ID
        paymentID: '',
        // 会员
        member: '',
        // 会员ID
        membersID: '',
        xstjNumID: '',
        // 阶梯条件下 的具体条件
        xsjtDataList: [ 
          {
            commissionCondition: '',
            commission: ''
          },
          {
            commissionCondition: '',
            commission: ''
          }
        ],
        // 非阶梯下的销售总额
        jttjNum: '', 
        jttjNumID: '',
        // 1有限制 2无限制 //新户推广
        tjxzNum: 1, 
        tjxzNumID: '',
        // 0 首单 1累计 //新户推广
        rwtjxz: 0, 
        rwtjxzID: '',
        //采购额达到 //新户推广
        amount: '', 
        amountID: '',
        //会员名称 //会员推广
        yyName: '', 
        //获得条件 //会员推广
        hdName: '', 
        //推广海报 //会员推广
        pic: '', 
        //会员id 会员推广
        memberID: '',
        // 佣金设置
        //是否有上下线分成 1有 2无
        fc: 1, 
        fcID: '',
        //分成比例
        scbl: '', 
        //每户给予 //新户推广
        reward: '' ,
        rewardID: '',
        //佣金政策id
        executeID: '',
        //上传资料
        //部门数据
        departmentData: [],
        //部门数据name
        departmentDataName: [],
        //企业类型
        enterpriseType: []
      },
      //是否可以编辑 false 可以编辑 true不能编辑
      disabled: false, 
      //按钮状态 0 创建 1查看 2修改 3只能添加商品和配送商
      buttomType: 0, 
      //查看，编辑的数据
      objData: {}, 
      rules: {
        taskName: [{ required: true, message: '请输入任务名称', trigger: 'blur' }],
        taskDesc: [{ required: true, message: '请输入任务说明', trigger: 'blur' }],
        cyStatus: [{ required: true, message: '请选择任务承接人群', trigger: 'change' }],
        launchTime: [{ required: true, message: '请选择投放时间', trigger: 'blur' }],
        desc: [{ required: true, message: '投放区域不能为空', trigger: 'change' }],
        xstjNum: [{ required: true, message: '请选择销售条件', trigger: 'change' }],
        xsjtDataList: [{ required: true, message: '请输入阶梯条件', trigger: 'blur' }],
        jttjNum: [{ required: true, message: '请输入总计销售额', trigger: 'blur' }],
        fc: [{ required: true, message: '请选择上下分成', trigger: 'change' }],
        scbl: [{ required: true, message: '请输入上线分成比例', trigger: 'blur' }],
        // 新户推广
        tjxzNum: [{ required: true, message: '请选择新客户是否有条件限制', trigger: 'change' }],
        rwtjxz: [{ required: true, message: '请选择任务条件计算', trigger: 'change' }],
        amount: [{ required: true, message: '请输入采购额', trigger: 'blur' }],
        reward: [{ required: true, message: '请输入佣金奖励', trigger: 'blur' }],
        // 会员推广
        yyName: [{ required: true, message: '请选择会员名称', trigger: 'change' }],
        hdName: [{ required: true, message: '请选择获得条件', trigger: 'change' }],
        pic: [{ required: true, message: '请上传推广海报', trigger: 'blur' }],
        departmentData: [{ required: true, message: '任务发布部门不能为空', trigger: 'change' }]
      },
      // 参与条件
      dialogDataList: [],
      dialogLoading: false,
      dialogTitle: '选取配送商',
      // 是否将添加的配送商信息展示出来
      dialogDataList2Type: false, 
      //配送商弹窗是否显示
      showDialog: false, 
      //配送商 弹窗内容条件
      dialog: { 
        name: '',
        type: 0,
        total: 0,
        page: 1,
        limit: 10
      },
      // 投放条件
      //区域弹窗
      show: false, 
      areaSelectTitle: '设置区域',
      //投放区域
      regionData: [], 
      cityData: [],
      // 商品设置
      dataList: [],
      // 添加商品弹窗
      addItemShow: false,
      // 会员推广
      // 会员名称
      memberName: [],
      // 活动信息
      activityData: [],
      // 查看/修改
      //投放区域，不修改投放区域时所用
      cityData2: [], 
      termTime: '',
      // 选择当前日期之后的时间
      pickerOptions: {
        disabledDate(time) {
          //如果没有后面的-8.64e7就是不可以选择今天的
          return time.getTime() < Date.now() - 8.64e7;
        }
      },
      //为了判断编辑时是否修改了购买会员的 推广海报，1代表修改了 2是没有
      imgType: 2,
      // 导入商品弹窗
      importDialog: false,
      info: {
        action: '/salesAssistant/api/v1/task/importTaskGoods',
        extralData: {}
      },
      // 导入结果
      importData: {
        // 总条数
        total: 0,
        // 成功条数
        successCount: 0,
        // 失败条数
        failCount: 0,
        // 失败条数链接
        failUrl: '',
        // 名称
        sheetName: ''
      },
      importShow: false,
      // 是否展示添加商品按钮
      showCommodity: false,
      //部门弹窗
      departmentShow: false,
      pickerOption2: {
        onPick: ({ maxDate, minDate }) => {
          this.choiceDate = minDate.getTime()
          if (maxDate) {
            this.choiceDate = ''
          }
        },
        disabledDate: time => {
          if (this.choiceDate) {
            const startDay =
              (new Date(this.choiceDate).getDate() - 1) * 24 * 3600 * 1000
            const endDay =
              (new Date(
                new Date(this.choiceDate).getFullYear(),
                new Date(this.choiceDate).getMonth() + 1,
                0
              ).getDate() -
                new Date(this.choiceDate).getDate()) * 24 * 3600 * 1000
            const minTime = this.choiceDate - startDay
            const maxTime = this.choiceDate + endDay
            return time.getTime() < minTime || time.getTime() > maxTime
          } else {
            return time.getTime() < Date.now() - 8.64e7
          }
        }
      }
    }
  },
  watch: {
    'rwInfoData.dialogDataList2': {
      handler(newVal, oldVal) {
        this.rwInfoData.dialogDataNum = newVal && newVal.length == 0 ? '' : newVal.length;
      },
      deep: true
    },
    'rwInfoData.finishType': {
      handler(newVal, oldVal) {
        if (newVal == 8) {
          //调取会员名称 数据
          this.memberApi(); 
        } else if (newVal == 10) {
          this.rwInfoData.fc = 2
        }
        this.rwInfoData.launchTime = []
      },
      deep: true
    },
    'rwInfoData.yyName': {
      handler(newVal, oldVal) {
        //选择条件
        this.queryMemberStageApi();
      }
    }
  },
  mounted() {
    let query = this.$route.params;
    if (query) {
      // type 0 创建 1查看 2修改 3 只能添加商品
      this.buttomType = query.type;
      if (query.type != 0) {
        this.rwInfoData.taskID = query.id;
        //获取数据
        this.getDetailByIdApi(); 
        if (query.type == 1) {
          this.disabled = true;
        } else if (query.type == 2) {
          this.disabled = false;
        } else if (query.type == 3) {
          this.disabled = true;
        }
      } else {
        this.rwInfoData.finishType = 2;
      }
    }
  },
  methods: {
    // 获取投放区域
    async getQueryTaskAreaApi() {
      this.cityData = [];
      this.$common.showLoad();
      let data = await queryTaskArea(this.rwInfoData.taskID);
      this.$common.hideLoad();
      if (data !== undefined) {
        this.cityData2 = data.list;
        for (let i = 0; i < data.list.length; i ++) {
          this.cityData.push(...data.list[i].children)
        }
      }
    },
    // 获取详情以及编辑信息
    async getDetailByIdApi() {
      let data = await getDetailById(this.rwInfoData.taskID)
      if (data) {
        this.rwInfoData.finishType = data.finishType;
        this.rwInfoData.taskName = data.taskName;
        this.rwInfoData.taskDesc = data.taskDesc;
        // 任务承接人群
        for (let i = 0; i < data.takeRuleVOList.length; i ++) {
          if (data.takeRuleVOList[i].ruleKey == 'TAKE_USER_GROUP') {
            this.rwInfoData.cyStatus = data.takeRuleVOList[i].ruleValue.split(',');
            this.rwInfoData.cyStatusID = data.takeRuleVOList[i].id
          }
        }
        // 投放时间
        this.rwInfoData.launchTime = [formatDate(data.startTime, 'yyyy-MM-dd') , formatDate(data.endTime, 'yyyy-MM-dd')]
        // 投放区域
        this.rwInfoData.desc = data.taskArea;
        // 佣金设置
        for (let y = 0; y < data.commissionRuleVOList.length; y ++) {
          if (data.finishType != 2) {
            if (data.commissionRuleVOList[y].ruleKey == 'COMMISSION') {
              this.rwInfoData.reward = data.commissionRuleVOList[y].ruleValue
              this.rwInfoData.rewardID = data.commissionRuleVOList[y].id
            }
          }
          if (data.commissionRuleVOList[y].ruleKey == 'GIVE_INVITEE_AWARD') {
            this.rwInfoData.fcID = data.commissionRuleVOList[y].id;
            if (data.commissionRuleVOList[y].ruleValue == '-1') {
              this.rwInfoData.fc = 2;
              this.rwInfoData.scbl = ''
            } else {
              this.rwInfoData.fc = 1;
              this.rwInfoData.scbl = data.commissionRuleVOList[y].ruleValue
            }
          }
          if (data.finishType == 2 || data.finishType == 10) {
            if (data.commissionRuleVOList[y].ruleKey == 'EXECUTE_TYPE') {
              this.rwInfoData.executeID = data.commissionRuleVOList[y].id
            }
          }
        }
        if (data.finishType == 2 || data.finishType == 10) {
          // 获取配送商 查看 编辑
          this.pssApi(data.id, data.taskStatus)
          // 任务阶梯
          for (let z = 0; z < data.finishRuleVOList.length; z ++) {
            if (data.finishRuleVOList[z].ruleKey == 'STEP_CONDITION') {
              this.rwInfoData.xstjNum = parseInt(data.finishRuleVOList[z].ruleValue)
              this.rwInfoData.xstjNumID = data.finishRuleVOList[z].id
            }
            if (data.finishRuleVOList[z].ruleKey == 'SALE_CONDITION') {
              this.rwInfoData.jttjNum = parseInt(data.finishRuleVOList[z].ruleValue)
              this.rwInfoData.jttjNumID = data.finishRuleVOList[z].id
            }
            if (data.finishRuleVOList[z].ruleKey == 'PAYMENT_METHOD') {
              this.rwInfoData.payment = [];
              this.rwInfoData.paymentID = data.finishRuleVOList[z].id;
              let val = data.finishRuleVOList[z].ruleValue == '' ? [] : data.finishRuleVOList[z].ruleValue.split(',');
              val.forEach((item) => {
                this.rwInfoData.payment.push(parseInt(item))
              })
            }
            if (data.finishRuleVOList[z].ruleKey == 'IS_MEMBER') {
              this.rwInfoData.member = data.finishRuleVOList[z].ruleValue == '' ? '' : parseInt(data.finishRuleVOList[z].ruleValue);
              this.rwInfoData.membersID = data.finishRuleVOList[z].id
            }
            if (data.finishType == 10) {
              this.rwInfoData.departmentDataName = []
              this.rwInfoData.departmentData = []
              for (let i = 0; i < data.taskDeptVOS.length; i ++) {
                this.rwInfoData.departmentDataName.push(data.taskDeptVOS[i].name)
                this.rwInfoData.departmentData.push({
                  id: data.taskDeptVOS[i].deptId,
                  name: data.taskDeptVOS[i].name
                })
              }
              this.rwInfoData.enterpriseType = []
              let valType = data.enterpriseType == '' ? [] : data.enterpriseType.split(',');
              valType.forEach((item) => {
                this.rwInfoData.enterpriseType.push(parseInt(item))
              })
            }
          }
          this.rwInfoData.xsjtDataList = data.commRuleVOList;
          // 商品
          this.dataList = [];
          for (let r = 0; r < data.taskGoodsList.length; r ++) {
            this.dataList.push({
              ...data.taskGoodsList[r],
              taskStatus: data.taskStatus
            })
          }
        } else if (data.finishType == 3 || data.finishType == 7) {
          for (let x = 0; x < data.finishRuleVOList.length; x ++) {
            if (data.finishRuleVOList[x].ruleKey == 'NEW_CUSTOMER_LIMIT') {
              this.rwInfoData.tjxzNum = parseInt(data.finishRuleVOList[x].ruleValue)
              this.rwInfoData.tjxzNumID = data.finishRuleVOList[x].id
            }
            if (data.finishRuleVOList[x].ruleKey == 'NEW_CUSTOMER_CONDITION') {
              this.rwInfoData.rwtjxz = parseInt(data.finishRuleVOList[x].ruleValue)
              this.rwInfoData.rwtjxzID = data.finishRuleVOList[x].id
            }
            if (data.finishRuleVOList[x].ruleKey == 'NEW_CUSTOMER_AMOUNT') {
              this.rwInfoData.amount = data.finishRuleVOList[x].ruleValue
              this.rwInfoData.amountID = data.finishRuleVOList[x].id
            }
            // 支付方式
            if (data.finishType == 3) {
              if (data.finishRuleVOList[x].ruleKey == 'PAYMENT_METHOD') {
                this.rwInfoData.payment = [];
                this.rwInfoData.paymentID = data.finishRuleVOList[x].id;
                let val = data.finishRuleVOList[x].ruleValue == '' ? [] : data.finishRuleVOList[x].ruleValue.split(',');
                val.forEach((item) => {
                  this.rwInfoData.payment.push(parseInt(item))
                })
              }
            }
          }
        } else if (data.finishType == 8) {
          //会员名称
          this.rwInfoData.yyName = data.taskMember.memberId;
          this.rwInfoData.hdName = data.taskMember.memberStageId;
          this.rwInfoData.pic = data.taskMember.playbill;
          this.rwInfoData.memberID = data.taskMember.id;
        }
      }
    },
    // 参与条件
    // 点击配送商 显示弹窗内容
    distributorClick() {
      if (this.buttomType == 3 && this.rwInfoData.dialogDataNum == '') {
        this.$common.warn('不可添加配送商！')
      } else {
        this.showDialog = true;
        //获取配送商
        this.distributorPageApi(); 
      }
    },
    // 参与配送商弹窗
    // 点击确定
    pssConfirm() {
      this.dialogDataList2Type = true;
      this.showDialog = false;
    },
    handleSearch() {
      this.dialog.page = 1;
      this.distributorPageApi();
    },
    handleReset() {
      this.dialog = {
        name: '',
        type: 0,
        total: 0,
        page: 1,
        limit: 10
      }
    },
    // 获取配送商
    async distributorPageApi() {
      this.dialogDataList = [];
      this.dialogLoading = true;
      let dialog = this.dialog;
      this.$common.showLoad();
      let data = await queryDistributorPage(
        dialog.page,
        dialog.name,
        dialog.limit,
        dialog.type
      )
      this.$common.hideLoad();
      if (data !== undefined) {
        if (this.rwInfoData.dialogDataList2 && this.rwInfoData.dialogDataList2.length > 0) {
          for (let i = 0; i < data.records.length; i ++) {
            for (let y = 0; y < this.rwInfoData.dialogDataList2.length; y ++) {
              if (this.rwInfoData.dialogDataList2[y].distributorEid == data.records[i].distributorEid) {
                data.records[i].zt = 2
                break;
              } else {
                data.records[i].zt = 1
              }
            }
          }
          this.dialogDataList = data.records;
        } else {
          for (let i = 0; i < data.records.length; i ++) {
            this.dialogDataList.push({
              name: data.records[i].name,
              distributorEid: data.records[i].distributorEid,
              type: data.records[i].type,
              id: data.records[i].id,
              zt: 1
            })
          }
        }
        this.dialog.total = data.total;
      }
      this.dialogLoading = false;
    },
    // 点击添加
    addClick(row) {
      if (row.zt == 1) {
        this.rwInfoData.dialogDataList2.push(row)
      }
      row.zt = 2;
    },
    // 点击 删除
    async deleteClick(row, index) {
      if (row.id != 0) {
        this.$common.showLoad();
        let data = await deleteTaskDistributor(row.id)
        this.$common.hideLoad();
        if (data !== undefined) {
          this.$common.n_success('删除成功!');
          this.rwInfoData.dialogDataList2.splice(index, 1);
        }
      } else {
        row.zt = 1;
        this.rwInfoData.dialogDataList2.splice(index, 1);
      }
      for (let i = 0; i < this.dialogDataList.length; i ++) {
        if (this.rwInfoData.dialogDataList2 && this.rwInfoData.dialogDataList2.length > 0) {
          for (let y = 0; y < this.rwInfoData.dialogDataList2.length; y ++) {
            if (this.dialogDataList[i].distributorEid == this.rwInfoData.dialogDataList2[y].distributorEid){
              this.dialogDataList[i].zt = 2;
              break;
            } else {
              this.dialogDataList[i].zt = 1;
            }
          }
        } else {
          this.dialogDataList[i].zt = 1;
        }
      }
    },
    // 查看 编辑 参与配送商
    async pssApi(id, taskStatus) {
      this.$common.showLoad();
      let data = await queryTaskDistributorPage(
        id,
        1,
        200,
        0
      )
      this.$common.hideLoad();
      if (data !== undefined) {
        this.rwInfoData.dialogDataList2 = [];
        for (let r = 0; r < data.records.length; r ++) {
            this.rwInfoData.dialogDataList2.push({
              ...data.records[r],
              taskStatus: taskStatus
            })
          }
        this.dialogDataList2Type = true;
        if (this.dialogDataList && this.dialogDataList.length > 0) {
          for (let i = 0; i < this.dialogDataList.length; i ++) {
            for (let y = 0; y < this.rwInfoData.dialogDataList2.length; y ++) {
              if (this.dialogDataList[i].distributorEid == this.rwInfoData.dialogDataList2[y].distributorEid) {
                this.dialogDataList[i].zt = 2;
                this.dialogDataList[i].taskStatus = taskStatus
                break
              } else {
                this.dialogDataList[i].zt = 1;
              }
            }
          }
        }
      }
    },
    // 投放条件
    // 点击选择区域
    selectAreaClick() {
      if (this.regionData && this.regionData.length > 0) {
        this.cityData = [];
        let data = this.regionData;
        for (let i = 0; i < data.length; i ++) {
          for (let y = 0; y < data[i].children.length; y ++) {
            this.cityData.push(data[i].children[y].code)
          }
        }
      } else {
        if (this.buttomType) {
          //获取投放区域
          this.getQueryTaskAreaApi(); 
        }
      }
      this.show = true;
    },
    // 点击选择区域
    choose(val) {
      if (val.nodes && val.nodes.length > 0) {
        this.rwInfoData.desc = val.desc;
      } else {
        this.rwInfoData.desc = ''
      }
      this.regionData = val.nodes;
      this.show = false;
    },
    //任务条件设置
    // 新增阶梯条件
    addJttjClick() {
      this.rwInfoData.xsjtDataList.push({
        commissionCondition: '',
        commission: ''
      })
    },
     //删除多余的阶梯
    deletejtClick(item, index) {
      //删除阶梯
      this.rwInfoData.xsjtDataList.splice(index, 1);
    },
    // 调取会员名称接口
    async memberApi() {
      this.$common.showLoad();
      let data = await queryMember()
      this.$common.hideLoad();
      if (data !== undefined) {
        this.memberName = data.list;
      }
    },
    // 调取会员 获得条件接口
    async queryMemberStageApi() {
      this.$common.showLoad();
      let data = await queryMemberStage(this.rwInfoData.yyName);
      this.$common.hideLoad();
      if (data !== undefined) {
        this.activityData = data.list;
      }
      if (this.buttomType != 0) {
        for (let i = 0; i < data.list.length; i ++) {
          if (this.rwInfoData.hdName == data.list[i].memberStageId) {
            this.termTime = data.list[i].validTime
          }
        }
      }
    },
     // 海报上传
    onSuccess(val) {
      this.rwInfoData.pic = val.key;
      if (this.buttomType == 2) {
        this.imgType = 1;
      }
    },
    // 点击新客户条件设置
    tjxzNumChange() {
      this.rwInfoData.rwtjxz = 0;
      this.rwInfoData.amount = '';
      this.rwInfoData.payment = []
    },
    // 有效期
    hdNameChange(val) {
      for (let i = 0; i < this.activityData.length; i ++) {
        if (val == this.activityData[i].memberStageId) {
          this.termTime = this.activityData[i].validTime
        }
      }
    },
    // 点击阶梯条件 与非阶梯条件
    xstjNumChange() {
      this.rwInfoData.xsjtDataList = [
        {
          commissionCondition: '',
          commission: ''
        },
        {
          commissionCondition: '',
          commission: ''
        }
      ];
      this.rwInfoData.jttjNum = ''
    },
    // 佣金设置
    //点击上线分成比例
    fcChange() {
      this.rwInfoData.scbl = '';
    },
    //点击发布任务
    preservationClick() {
      this.$refs['dataForm'].validate( async(valid) => {
        if (valid) {
          let fbData = this.rwInfoData;
          // 省市区
          let city = [];
          for (let i = 0; i < this.regionData.length; i ++) {
            city.push({
              code: this.regionData[i].code,
              children: []
            })
            if (this.regionData[i].children && this.regionData[i].children.length > 0) {
              for (let y = 0; y < this.regionData[i].children.length; y ++) {
                city[i].children.push(
                  this.regionData[i].children[y].code
                )
              }
            }
          }
          // 交易量
          if (fbData.finishType == 2 || fbData.finishType == 10) {
            // 判断商品是否选择
            if (this.dataList && this.dataList.length > 0) {
              // 0 不调 发布接口 1 调取
              let type = 0; 
              // 商品集合
              let commodityData = [];
              for (let l = 0; l < this.dataList.length; l ++) {
                if (fbData.xstjNum == 2 && this.dataList[l].commission == '') {
                  type = 0;
                  this.$common.warn('请完整配置商品 的佣金政策！')
                  break;
                } else {
                  commodityData.push({
                    commission: this.dataList[l].commission,
                    goodsId: this.dataList[l].goodsId,
                    goodsName: this.dataList[l].goodsName,
                    salePrice: this.dataList[l].price,
                    outPrice: this.dataList[l].outPrice,
                    sellPrice: this.dataList[l].sellPrice
                  })
                  type = 1
                }
              }
              if (type == 1) {
                // 配送商集合
                let distributor = [];
                if (this.dialogDataList2Type) {
                  fbData.dialogDataList2.forEach( el => {
                    distributor.push({
                      distributorEid: el.distributorEid,
                      name: el.name,
                      type: el.type
                    })
                  })
                }
                // 阶梯条件下的 销售总额。 后端需要加的
                let totalSales = [];
                if (fbData.xstjNum == 1) {
                  fbData.xsjtDataList.forEach( el => {
                    totalSales.push(el.commissionCondition)
                  })
                }
                //上传资料
                let department = {}
                if (fbData.finishType == 10) {
                  let deptData = [];
                  fbData.departmentData.forEach( el => {
                    deptData.push(el.id)
                  })
                  department = {
                    deptIdList: deptData,
                    enterpriseType: fbData.enterpriseType.toString()
                  }
                }
                //交易量
                let transactionData = [];
                let transactionData2 = {};
                if (fbData.finishType == 2) {
                  transactionData.push(
                    
                    {
                      // 支付方式
                      ruleKey: 'PAYMENT_METHOD', 
                      ruleType: 1,
                      ruleValue: fbData.payment.toString()
                    },
                    {
                      // 是否会员
                      ruleKey: 'IS_MEMBER', 
                      ruleType: 1,
                      ruleValue: fbData.member
                    }
                  )
                  transactionData2 = {
                    addTaskAreaList: city
                  }
                }
               
                this.$common.showLoad();
                let data = await addRenwu({
                  taskType: fbData.status,
                  finishType: fbData.finishType,
                  taskName: fbData.taskName,
                  taskDesc: fbData.taskDesc,
                  ...department,
                  addTaskRuleList: [
                    {
                      // 任务承接人群
                      ruleKey: 'TAKE_USER_GROUP',
                      ruleType: 0,
                      ruleValue: fbData.cyStatus.toString()
                    },
                    {
                      //销售条件
                      ruleKey: 'STEP_CONDITION', 
                      ruleType: 1,
                      ruleValue: fbData.xstjNum
                    },
                   
                    {
                      // 非阶梯下的销售总额
                      ruleKey: 'SALE_CONDITION', 
                      ruleType: 1,
                      ruleValue: fbData.xstjNum == 1 ? totalSales.toString() : fbData.jttjNum
                    },
                    {
                      // 佣金政策
                      ruleKey: 'EXECUTE_TYPE', 
                      ruleType: 2,
                      ruleValue: fbData.xstjNum == 1 ? 0 : 1
                    },
                    {
                      // 是否有上下线分成
                      ruleKey: 'GIVE_INVITEE_AWARD', 
                      ruleType: 2,
                      ruleValue: fbData.fc == 1 ? fbData.scbl : -1
                    },
                    ...transactionData
                  ],
                  //参与配送商
                  addTaskDistributorList: distributor, 
                  startTime: fbData.launchTime && fbData.launchTime.length > 0 ? fbData.launchTime[0] : '',
                  endTime: fbData.launchTime && fbData.launchTime.length > 0 ? fbData.launchTime[1] : '',
                  ...transactionData2,
                  addCommissionRuleFormList: fbData.xstjNum == 1 ? fbData.xsjtDataList : [],
                  addTaskGoodsRelationList: commodityData
                })
                this.$common.hideLoad();
                if (data !== undefined) {
                  this.$common.alert('保存成功', r => {
                    this.$router.go(-1)
                  })
                }
              }
            } else {
              this.$common.warn('请添加商品！')
            }
            //新户推广
          } else if (fbData.finishType == 3) { 
            this.$common.showLoad();
            let data3 = await addRenwu({
              taskType: fbData.status,
              finishType: fbData.finishType,
              taskName: fbData.taskName,
              taskDesc: fbData.taskDesc,
              addTaskRuleList: [
                {
                  // 任务承接人群
                  ruleKey: 'TAKE_USER_GROUP', 
                  ruleType: 0,
                  ruleValue: fbData.cyStatus.toString()
                },
                {
                  //新客户是否有条件限制
                  ruleKey: 'NEW_CUSTOMER_LIMIT', 
                  ruleType: 1,
                  ruleValue: fbData.tjxzNum
                },
                {
                  // 支付方式
                  ruleKey: 'PAYMENT_METHOD', 
                  ruleType: 1,
                  ruleValue: fbData.payment.toString()
                },
                {
                  //0-首单 1-累计
                  ruleKey: 'NEW_CUSTOMER_CONDITION',
                  ruleType: 1,
                  ruleValue: fbData.tjxzNum == 1 ? fbData.rwtjxz : ''
                },
                {
                  // 采购额满足多少元
                  ruleKey: 'NEW_CUSTOMER_AMOUNT', 
                  ruleType: 1,
                  ruleValue: fbData.tjxzNum == 1 ? fbData.amount : ''
                },
                {
                  // 佣金设置
                  ruleKey: 'COMMISSION', 
                  ruleType: 2,
                  ruleValue: fbData.reward
                },
                {
                  // 是否有上下线分成
                  ruleKey: 'GIVE_INVITEE_AWARD', 
                  ruleType: 2,
                  ruleValue: fbData.fc == 1 ? fbData.scbl : -1
                }
              ],
              startTime: fbData.launchTime && fbData.launchTime.length > 0 ? fbData.launchTime[0] : '',
              endTime: fbData.launchTime && fbData.launchTime.length > 0 ? fbData.launchTime[1] : '',
              addTaskAreaList: city
            })
            this.$common.hideLoad();
            if (data3 !== undefined) {
              this.$common.alert('保存成功', r => {
                this.$router.go(-1)
              })
            }
            //新人推广
          } else if (fbData.finishType == 7) { 
            this.$common.showLoad();
            let data7 = await addRenwu({
              taskType: fbData.status,
              finishType: fbData.finishType,
              taskName: fbData.taskName,
              taskDesc: fbData.taskDesc,
              addTaskRuleList: [
                {
                  // 任务承接人群
                  ruleKey: 'TAKE_USER_GROUP', 
                  ruleType: 0,
                  ruleValue: fbData.cyStatus.toString()
                },
                {
                  //新客户是否有条件限制
                  ruleKey: 'NEW_CUSTOMER_LIMIT', 
                  ruleType: 1,
                  ruleValue: fbData.tjxzNum
                },
                {
                  //0-首单 1-累计
                  ruleKey: 'NEW_CUSTOMER_CONDITION',
                  ruleType: 1,
                  ruleValue: fbData.tjxzNum == 1 ? fbData.rwtjxz : ''
                },
                {
                  // 采购额满足多少元
                  ruleKey: 'NEW_CUSTOMER_AMOUNT', 
                  ruleType: 1,
                  ruleValue: fbData.tjxzNum == 1 ? fbData.amount : ''
                },
                {
                  // 佣金设置
                  ruleKey: 'COMMISSION', 
                  ruleType: 2,
                  ruleValue: fbData.reward
                },
                {
                  // 是否有上下线分成
                  ruleKey: 'GIVE_INVITEE_AWARD', 
                  ruleType: 2,
                  ruleValue: fbData.fc == 1 ? fbData.scbl : -1
                }
              ],
              startTime: fbData.launchTime && fbData.launchTime.length > 0 ? fbData.launchTime[0] : '',
              endTime: fbData.launchTime && fbData.launchTime.length > 0 ? fbData.launchTime[1] : '',
              addTaskAreaList: city
            })
            this.$common.hideLoad();
            if (data7 !== undefined) {
              this.$common.alert('保存成功', r => {
                this.$router.go(-1)
              })
            }
            //购买会员
          } else if (fbData.finishType == 8) { 
            this.$common.showLoad();
            let data8 = await addRenwu({
              taskType: fbData.status,
              finishType: fbData.finishType,
              taskName: fbData.taskName,
              taskDesc: fbData.taskDesc,
              addTaskRuleList: [
                {
                  // 任务承接人群
                  ruleKey: 'TAKE_USER_GROUP', 
                  ruleType: 0,
                  ruleValue: fbData.cyStatus.toString()
                },
                {
                  // 佣金设置
                  ruleKey: 'COMMISSION', 
                  ruleType: 2,
                  ruleValue: fbData.reward
                },
                {
                  // 是否有上下线分成
                  ruleKey: 'GIVE_INVITEE_AWARD', 
                  ruleType: 2,
                  ruleValue: fbData.fc == 1 ? fbData.scbl : -1
                }
              ],
              startTime: fbData.launchTime && fbData.launchTime.length > 0 ? fbData.launchTime[0] : '',
              endTime: fbData.launchTime && fbData.launchTime.length > 0 ? fbData.launchTime[1] : '',
              addTaskAreaList: city,
              addTaskMember: {
                memberId: fbData.yyName,
                memberStageId: fbData.hdName,
                playbill: fbData.pic
              }
            })
            this.$common.hideLoad();
            if (data8 !== undefined) {
              this.$common.alert('保存成功', r => {
                this.$router.go(-1)
              })
            }
          }
        }
      })
    },
    // 点击保存任务
    preservationClick2() {
      this.$refs['dataForm'].validate( async(valid) => {
        if (valid) {
          let fbData = this.rwInfoData;
          // 省市区
          let city = [];
          if (this.regionData && this.regionData.length > 0) {
            for (let i = 0; i < this.regionData.length; i ++) {
              city.push({
                code: this.regionData[i].code,
                children: []
              })
              if (this.regionData[i].children && this.regionData[i].children.length > 0) {
                for (let y = 0; y < this.regionData[i].children.length; y ++) {
                  city[i].children.push(
                    this.regionData[i].children[y].code
                  )
                }
              }
            }
          }
          // 交易量/资料上传
          if (fbData.finishType == 2 || fbData.finishType == 10) {
            if (this.dataList && this.dataList.length > 0) {
              // 0 不调 发布接口 1 调取
              let type = 0; 
              // 商品集合
              let commodityData = [];
              for (let l = 0; l < this.dataList.length; l ++) {
                if (fbData.xstjNum == 2 && this.dataList[l].commission == '') {
                  type = 0;
                  this.$common.warn('请完整配置商品 的佣金政策！')
                  break;
                } else {
                  commodityData.push({
                    commission: this.dataList[l].commission,
                    goodsId: this.dataList[l].goodsId,
                    goodsName: this.dataList[l].goodsName,
                    salePrice: this.dataList[l].price,
                    taskGoodsId: this.dataList[l].taskGoodsId,
                    outPrice: this.dataList[l].outPrice,
                    sellPrice: this.dataList[l].sellPrice
                  })
                  type = 1
                }
              }
              if (type == 1) {
                // 配送商集合
                let distributor = [];
                if (this.dialogDataList2Type) {
                  fbData.dialogDataList2.forEach( el => {
                    if (el.id == 0) {
                      distributor.push({
                        distributorEid: el.distributorEid,
                        name: el.name,
                        type: el.type
                      })
                    }
                  })
                }
                // 阶梯条件下的 销售总额。 后端需要加的
                let totalSales = [];
                if (fbData.xstjNum == 1) {
                  fbData.xsjtDataList.forEach( el => {
                    totalSales.push(el.commissionCondition)
                  })
                }
                //上传资料
                let department = {}
                if (fbData.finishType == 10) {
                  let deptData = [];
                  fbData.departmentData.forEach( el => {
                    deptData.push(el.id)
                  })
                  department = {
                    deptIdList: deptData,
                    enterpriseType: fbData.enterpriseType.toString()
                  }
                }
                //交易量
                let transactionData = [];
                let transactionData2 = {};
                if (fbData.finishType == 2) {
                  transactionData.push(
                    {
                      // 支付方式
                      ruleKey: 'PAYMENT_METHOD', 
                      ruleType: 1,
                      taskRuleId: fbData.paymentID,
                      ruleValue: fbData.payment.toString()
                    },
                    {
                      // 是否会员
                      ruleKey: 'IS_MEMBER', 
                      ruleType: 1,
                      taskRuleId: fbData.membersID,
                      ruleValue: fbData.member
                    }
                  )
                  transactionData2 = {
                    updateTaskAreaList: this.regionData && this.regionData.length > 0 ? city : this.cityData2
                  }
                }
                // 任务承接人群
                this.$common.showLoad();
                let data = await update({
                  taskId: fbData.taskID,
                  taskName: fbData.taskName,
                  taskDesc: fbData.taskDesc,
                  ...department,
                  updateTaskRuleList: [
                    {
                      // 任务承接人群
                      ruleKey: 'TAKE_USER_GROUP', 
                      ruleType: 0,
                      taskRuleId: fbData.cyStatusID,
                      ruleValue: fbData.cyStatus.toString()
                    },
                    {
                      //销售条件
                      ruleKey: 'STEP_CONDITION', 
                      ruleType: 1,
                      taskRuleId: fbData.xstjNumID,
                      ruleValue: fbData.xstjNum
                    },
                    {
                      // 非阶梯下的销售总额
                      ruleKey: 'SALE_CONDITION', 
                      ruleType: 1,
                      taskRuleId: fbData.jttjNumID,
                      ruleValue: fbData.xstjNum == 1 ? totalSales.toString() : fbData.jttjNum
                    },
                    {
                      // 佣金政策
                      ruleKey: 'EXECUTE_TYPE', 
                      ruleType: 2,
                      taskRuleId: fbData.executeID,
                      ruleValue: fbData.xstjNum == 1 ? 0 : 1
                    },
                    {
                      // 是否有上下线分成
                      ruleKey: 'GIVE_INVITEE_AWARD', 
                      ruleType: 2,
                      taskRuleId: fbData.fcID,
                      ruleValue: fbData.fc == 1 ? fbData.scbl : -1
                    },
                    ...transactionData
                  ],
                  updateTaskDistributorList: distributor,
                  startTime: fbData.launchTime && fbData.launchTime.length > 0 ? fbData.launchTime[0] : '',
                  endTime: fbData.launchTime && fbData.launchTime.length > 0 ? fbData.launchTime[1] : '',
                  ...transactionData2,
                  addCommissionRuleFormList: fbData.xstjNum == 1 ? fbData.xsjtDataList : [],
                  updateTaskGoodsRelationList: commodityData
                })
                this.$common.hideLoad();
                if (data !== undefined) {
                  this.$common.alert('保存成功', r => {
                    this.$router.go(-1)
                  })
                }
              }
            } else {
              this.$common.warn('请添加商品！')
            }
            //新户推广
          } else if (fbData.finishType == 3) { 
            this.$common.showLoad();
            let data3 = await update({
              taskId: fbData.taskID,
              taskName: fbData.taskName,
              taskDesc: fbData.taskDesc,
              updateTaskRuleList: [
                {
                  // 任务承接人群
                  ruleKey: 'TAKE_USER_GROUP',
                  ruleType: 0,
                  taskRuleId: fbData.cyStatusID,
                  ruleValue: fbData.cyStatus.toString()
                },
                {
                  //新客户是否有条件限制
                  ruleKey: 'NEW_CUSTOMER_LIMIT', 
                  ruleType: 1,
                  taskRuleId: fbData.tjxzNumID,
                  ruleValue: fbData.tjxzNum
                },
                {
                  // 支付方式
                  ruleKey: 'PAYMENT_METHOD', 
                  ruleType: 1,
                  taskRuleId: fbData.paymentID,
                  ruleValue: fbData.payment.toString()
                },
                {
                  //0-首单 1-累计
                  ruleKey: 'NEW_CUSTOMER_CONDITION', 
                  ruleType: 1,
                  taskRuleId: fbData.rwtjxzID,
                  ruleValue: fbData.tjxzNum == 1 ? fbData.rwtjxz : ''
                },
                {
                  // 采购额满足多少元
                  ruleKey: 'NEW_CUSTOMER_AMOUNT', 
                  ruleType: 1,
                  taskRuleId: fbData.amountID,
                  ruleValue: fbData.tjxzNum == 1 ? fbData.amount : ''
                },
                {
                  // 佣金设置
                  ruleKey: 'COMMISSION', 
                  ruleType: 2,
                  taskRuleId: fbData.rewardID,
                  ruleValue: fbData.reward
                },
                {
                  // 是否有上下线分成
                  ruleKey: 'GIVE_INVITEE_AWARD', 
                  ruleType: 2,
                  taskRuleId: fbData.fcID,
                  ruleValue: fbData.fc == 1 ? fbData.scbl : -1
                }
              ],
              startTime: fbData.launchTime && fbData.launchTime.length > 0 ? fbData.launchTime[0] : '',
              endTime: fbData.launchTime && fbData.launchTime.length > 0 ? fbData.launchTime[1] : '',
              updateTaskAreaList: this.regionData && this.regionData.length > 0 ? city : this.cityData2
            })
            this.$common.hideLoad();
            if (data3 !== undefined) {
              this.$common.alert('保存成功', r => {
                this.$router.go(-1)
              })
            }
            //新人推广
          } else if (fbData.finishType == 7) { 
            this.$common.showLoad();
            let data7 = await update({
              taskId: fbData.taskID,
              taskName: fbData.taskName,
              taskDesc: fbData.taskDesc,
              updateTaskRuleList: [
                {
                  // 任务承接人群
                  ruleKey: 'TAKE_USER_GROUP', 
                  ruleType: 0,
                  taskRuleId: fbData.cyStatusID,
                  ruleValue: fbData.cyStatus.toString()
                },
                {
                  //新客户是否有条件限制
                  ruleKey: 'NEW_CUSTOMER_LIMIT', 
                  ruleType: 1,
                  taskRuleId: fbData.tjxzNumID,
                  ruleValue: fbData.tjxzNum
                },
                {
                  //0-首单 1-累计
                  ruleKey: 'NEW_CUSTOMER_CONDITION', 
                  ruleType: 1,
                  taskRuleId: fbData.rwtjxzID,
                  ruleValue: fbData.tjxzNum == 1 ? fbData.rwtjxz : ''
                },
                {
                  // 采购额满足多少元
                  ruleKey: 'NEW_CUSTOMER_AMOUNT', 
                  ruleType: 1,
                  taskRuleId: fbData.amountID,
                  ruleValue: fbData.tjxzNum == 1 ? fbData.amount : ''
                },
                {
                  // 佣金设置
                  ruleKey: 'COMMISSION', 
                  ruleType: 2,
                  taskRuleId: fbData.rewardID,
                  ruleValue: fbData.reward
                },
                {
                  // 是否有上下线分成
                  ruleKey: 'GIVE_INVITEE_AWARD', 
                  ruleType: 2,
                  taskRuleId: fbData.fcID,
                  ruleValue: fbData.fc == 1 ? fbData.scbl : -1
                }
              ],
              startTime: fbData.launchTime && fbData.launchTime.length > 0 ? fbData.launchTime[0] : '',
              endTime: fbData.launchTime && fbData.launchTime.length > 0 ? fbData.launchTime[1] : '',
              updateTaskAreaList: this.regionData && this.regionData.length > 0 ? city : this.cityData2
            })
            this.$common.hideLoad();
            if (data7 !== undefined) {
              this.$common.alert('保存成功', r => {
                this.$router.go(-1)
              })
            }
            //购买会员
          } else if (fbData.finishType == 8) {
            let memberData = {
              taskId: fbData.taskID,
              taskName: fbData.taskName,
              taskDesc: fbData.taskDesc,
              updateTaskRuleList: [
                {
                  // 任务承接人群
                  ruleKey: 'TAKE_USER_GROUP', 
                  ruleType: 0,
                  taskRuleId: fbData.cyStatusID,
                  ruleValue: fbData.cyStatus.toString()
                },
                {
                  // 佣金设置
                  ruleKey: 'COMMISSION', 
                  ruleType: 2,
                  taskRuleId: fbData.rewardID,
                  ruleValue: fbData.reward
                },
                {
                  // 是否有上下线分成
                  ruleKey: 'GIVE_INVITEE_AWARD', 
                  ruleType: 2,
                  taskRuleId: fbData.fcID,
                  ruleValue: fbData.fc == 1 ? fbData.scbl : -1
                }
              ],
              startTime: fbData.launchTime && fbData.launchTime.length > 0 ? fbData.launchTime[0] : '',
              endTime: fbData.launchTime && fbData.launchTime.length > 0 ? fbData.launchTime[1] : '',
              updateTaskAreaList: this.regionData && this.regionData.length > 0 ? city : this.cityData2
            }
            let data8 = '';
            this.$common.showLoad();
            if (this.imgType == 1) {
              data8 = await update({
                ...memberData,
                updateTaskMember: {
                  id: fbData.memberID,
                  memberId: fbData.yyName,
                  memberStageId: fbData.hdName,
                  playbill: fbData.pic
                }
              })
            } else {
              data8 = await update({
                ...memberData
              })
            }
            this.$common.hideLoad();
            if (data8 !== undefined) {
              this.$common.alert('保存成功', r => {
                this.$router.go(-1)
              })
            }
          }
        }
      })
    },
    // 商品设置
    // 添加商品
    addTemp() {
      this.addItemShow = true;
    },
    // 从父级传递过来的商品信息
    addCommodity(row) {
      this.dataList = row;
      this.addItemShow = false;
    },
    // 点击 商品的移除
    async removeClick(row, index) {
      row.zt = 1;
      this.dataList.splice(index, 1)
      if (row.taskGoodsId) {
        this.$common.showLoad();
        let data = await deleteGoods(row.taskGoodsId)
        this.$common.hideLoad();
        if (data !== undefined) {
          this.$common.n_success('移除成功!');
        }
      }
    },
    // 验证
    checkInput(val) {
      if (val.search(/^[+]?\d*\.?\d{0,5}$/) == 0) {
        return val
      } else {
        return ''
      }
    },
    // 导入商品
    importTemp() {
      this.importDialog = true;
    },
    // 导入商品
    importSuccess(data) {
      this.$log(data)
      this.importData = {
        // 总条数
        total: data.successCount + data.failCount,
        // 成功条数
        successCount: data.successCount,
        // 失败条数
        failCount: data.failCount,
        // 失败条数链接
        failUrl: data.failUrl,
        // 名称
        sheetName: data.sheetName
      }
      this.importDialog = false;
      this.importShow = true;
      if (data && data.successList.length > 0) {
        let currentArr = this.dataList
        if (currentArr && currentArr.length > 0) {
          data.successList.forEach(item => {
            let hasIndex = currentArr.findIndex(obj => {
              return obj.goodsId == item.goodsId;
            });
            // 已经存在
            if (hasIndex != -1) { 
              item.isSelect = true;
            } else {
              item.isSelect = false;
              currentArr.push(this.$common.clone(item))
            }
          });
          this.dataList = currentArr
        } else {
          this.dataList = data.successList
        }
      } 
    },
    // 点击导入结果确认
    confirmImport() {
      this.importShow = false;
    },
    // 下载导入失败的数据
    handleDownLoad(val) {
      if (val && val.failUrl) {
        const xRequest = new XMLHttpRequest()
        xRequest.open('GET', val.failUrl, true)
        xRequest.responseType = 'blob'
        xRequest.onload = () => {
          const url = window.URL.createObjectURL(xRequest.response)
          const a = document.createElement('a')
          a.href = url
          a.download = val.sheetName ? `${ val.sheetName }.xls` : ''
          a.click()
        }
        xRequest.send()
      }
    },
    // 导出商品 
    async exportProduct() {
      this.$common.showLoad()
      let data = await createDownLoad({
        className: 'exportB2BYilingGoodsService',
        fileName: '商品信息',
        groupName: '商品信息导出',
        menuName: '销售助手 - 任务管理',
        searchConditionList: []
      })
      this.$common.hideLoad()
      if (data && data.result) {
        this.$common.n_success('创建下载任务成功，请在下载中心查看')
      }
    },
    //点击任务发布部门
    addDepartment() {
      this.departmentShow = true
    },
    //获取到的部门选择信息
    // departmentConfirm(val) {
    //   this.rwInfoData.departmentData = val;
    //   this.departmentShow = false;
    // },
    // 表单部门点击
    handleNodeFormClick(data) {
      if (this.rwInfoData.departmentData.findIndex(item => item.id == data.id) === -1) {
        this.rwInfoData.departmentDataName.push(data.name)
        this.rwInfoData.departmentData.push(data)
      }
    },
     // select框移除
    removeTag(tag) {
      const index = this.rwInfoData.departmentData.findIndex(item => item.name === tag)
      if (index > -1) {
        this.rwInfoData.departmentData.splice(index, 1)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
