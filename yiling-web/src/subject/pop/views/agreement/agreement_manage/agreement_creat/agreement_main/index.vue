<template>
  <el-form :model="agreementMainTerms" :rules="agreementMainTermsRules" ref="dataForm" class="agreement-main" label-position="left">
    <!-- step1 -->
    <div>
      <div class="top-bar">
        <div class="header-bar header-renative">
          <div class="sign"></div>基本信息
        </div>
        <div class="content-box my-form-box">
          <el-row class="box">
            <el-col :span="7">
              <div class="title"><span class="red-text">*</span>甲方类型</div>
              <el-form-item prop="firstType">
                <el-select v-model="agreementMainTerms.firstType" placeholder="请选择甲方类型" @change="firstTypeChange">
                  <el-option v-for="item in agreementFirstType" :key="item.value" :label="item.label"
                    :value="item.value"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="7">
              <div class="title"><span class="red-text">*</span>协议类型</div>
              <el-form-item prop="agreementType">
                <el-select v-model="agreementMainTerms.agreementType" placeholder="请选择协议类型" @focus="agreementTypeFocus" @change="agreementTypeChange">
                  <el-option v-for="item in ableSelectedAgreementType" :key="item.value" :label="item.label"
                    :value="item.value"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="7">
              <div class="title">签订日期</div>
              <el-form-item>
                <el-date-picker
                  v-model="agreementMainTerms.signTime"
                  type="date"
                  format="yyyy/MM/dd"
                  value-format="yyyy-MM-dd"
                  placeholder="选择返签订日期"
                  >
                </el-date-picker>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row class="box">
            <el-col :span="7">
              <el-form-item label="甲方" prop="ename" ref="firstPartyItem">
                <yl-button type="text" @click="selectFirstPartyClick">{{ agreementMainTerms.ename || '关联甲方' }}</yl-button>
              </el-form-item>
            </el-col>
            <el-col :span="7">
              <el-form-item label="乙方" prop="secondName" ref="secondPartyItem" class="flex-row-left">
                <yl-button type="text" @click="selectSecondPartyClick">{{ agreementMainTerms.secondName || '关联乙方' }}</yl-button>
                <yl-button v-if="agreementMainTerms.agreementType == 5" type="text" @click="secondNameDindClick"><svg-icon class="svg-container" icon-class="bind" /></yl-button>
              </el-form-item>
            </el-col>
            <el-col :span="7">
              <div class="title"><span class="red-text">*</span>生效日期</div>
              <el-form-item prop="effectTime" >
                <el-date-picker
                  v-model="agreementMainTerms.effectTime"
                  type="daterange"
                  format="yyyy/MM/dd"
                  value-format="yyyy-MM-dd"
                  range-separator="至"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期"
                  @change="effectTimeChange"
                  >
                </el-date-picker>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row class="box">
            <el-col :span="7">
              <el-form-item label="甲方协议签订人:" label-width="150px">
                <yl-button type="text" @click="firstSignUserNameClick">{{ agreementMainTerms.firstSignUserName || '关联签订人' }}</yl-button>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item label="联系电话:">
                {{ agreementMainTerms.firstSignUserPhone || '- -' }}
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item label="签订人所属部门:">
                - -
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item>
                <el-radio v-model="agreementMainTerms.mainUser" :label="1">协议负责人</el-radio>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row class="box">
            <el-col :span="7">
              <el-form-item label="乙方协议签订人:" label-width="150px">
                <yl-button type="text" @click="secondSignUserNameClick">{{ agreementMainTerms.secondSignUserName || '关联签订人' }}</yl-button>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item label="联系电话:">
                {{ agreementMainTerms.secondSignUserPhone || '- -' }}
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item label="签订人所属部门:">
                - -
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item>
                <el-radio v-model="agreementMainTerms.mainUser" :label="2">协议负责人</el-radio>
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item label="是否提供流向:" prop="flowFlag" label-width="150px">
            <el-radio-group v-model="agreementMainTerms.flowFlag">
              <el-radio :label="1">是</el-radio>
              <el-radio :label="0">否</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="是否为草签协议:" prop="draftFlag" label-width="150px">
            <el-radio-group v-model="agreementMainTerms.draftFlag">
              <el-radio :label="1">是</el-radio>
              <el-radio :label="0">否</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="是否交保证金:" prop="marginFlag" label-width="150px">
            <el-radio-group v-model="agreementMainTerms.marginFlag" @change="marginFlagChange">
              <el-radio :label="1">是</el-radio>
              <el-radio :label="0">否</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="保证金支付方:" label-width="150px" prop="marginPayer" v-if="agreementMainTerms.marginFlag == 1">
            <el-radio-group v-model="agreementMainTerms.marginPayer" @change="marginPayerTypeChange">
              <el-radio v-for="item in agreementMarginPayer" :key="item.value" :label="item.value">
                {{ item.label }}
              </el-radio>
              <yl-button icon="el-icon-circle-plus-outline" type="primary" v-if="agreementMainTerms.marginPayer == 3" @click="addProviderClick">{{ agreementMainTerms.businessName || '指定商业公司' }}</yl-button>
            </el-radio-group>
          </el-form-item>
          <!-- 金额 -->
          <div class="activity-form-item money-form-item" v-if="agreementMainTerms.marginFlag == 1">
            <el-form-item label="保证金金额:" label-width="150px" prop="marginAmount" class="mar-r-16">
              <el-input v-model="agreementMainTerms.marginAmount"></el-input>
            </el-form-item>
            <el-form-item class="money-form-item mar-r-16 mar-l-16">
              <el-checkbox v-model="agreementMainTerms.backType">协议结束后返还</el-checkbox>
            </el-form-item>
            <el-form-item label="返还日期:" prop="marginBackDate" class="money-form-item" v-if="agreementMainTerms.marginBackType == 2">
              <el-date-picker
                v-model="agreementMainTerms.marginBackDate"
                type="date"
                format="yyyy/MM/dd"
                value-format="yyyy-MM-dd"
                placeholder="选择返还日期"
                >
              </el-date-picker>
            </el-form-item>
          </div>
          <!-- ka协议类型 -->
          <el-form-item label="ka协议类型:" label-width="150px" prop="kaAgreementType" v-if="agreementMainTerms.agreementType == 5">
            <el-radio-group v-model="agreementMainTerms.kaAgreementType">
              <el-radio v-for="item in kaAgreementType" :key="item.value" :label="item.value">
                {{ item.label }}
              </el-radio>
            </el-radio-group>
          </el-form-item>
          <!-- 是否活动协议 -->
          <div class="activity-form-item money-form-item" v-if="agreementMainTerms.agreementType == 3">
            <el-form-item label="是否活动协议:" label-width="150px" prop="activeFlag" class="mar-r-16" >
              <el-radio-group v-model="agreementMainTerms.activeFlag" @change="activeFlagChange">
                <el-radio :label="1">是</el-radio>
                <el-radio :label="0">否</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="商家运营签订人:" prop="businessOperatorName" ref="businessOperatorNameItem" class="money-form-item" v-if="agreementMainTerms.activeFlag == 1">
              <yl-button icon="el-icon-circle-plus-outline" type="primary" @click="selectOperatorClick">{{ agreementMainTerms.businessOperatorName || '指定活动负责人' }}</yl-button>
            </el-form-item>
          </div>
        </div>
      </div>
      <div class="top-bar">
        <div class="header-bar header-renative">
          <div class="sign"></div>协议附件
        </div>
        <div class="content-box my-form-box">
          <el-form-item label="协议附件类型:" label-width="150px">
            <el-radio-group v-model="agreementMainTerms.attachmentType">
              <el-radio v-for="item in agreementAttachmentType" :key="item.value" :label="item.value">
                {{ item.label }}
              </el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="上传图片:" label-width="150px">
            <div class="packing-img-box">
              <div class="packing-img" v-for="(item,index) in agreementMainTerms.agreementAttachmentList" :key="item.fileKey" @mouseenter="showDialogClick(index)" @mouseleave="hideDialog(index)">
                <el-image v-if="item.fileType == 1" class="packing-image" :src="item.fileUrl" fit="contain" :lazy="true"></el-image>
                <img v-if="item.fileType == 2" class="packing-image" src="@/subject/pop/assets/agreement/pdf.png" fit="contain"/>
                <div class="packing-cover" v-if=" packingShow && index === packingCurrent ">
                  <i v-if="item.fileType == 1" class="el-icon-zoom-in" @click="changeBigUrl(item.fileUrl)"></i>
                  <i v-if="item.fileType == 2" class="el-icon-download" @click="downloadClick(item)"></i>
                  <i class="el-icon-delete" @click="deletPackingImg(index)"></i>
                </div>
              </div>
              <yl-upload width="80px" height="80px" v-if="agreementMainTerms.agreementAttachmentList.length < 9" file-type="jpg,png,jpeg,pdf" :limit="9" :extral-data="{type: 'agreementAttachment'}" @onSuccess="onSuccess" :show-file-list="false" />
            </div>
          </el-form-item>
        </div>
      </div>
    </div>
    <!-- 关联甲方/乙方 -->
    <yl-dialog :title="currentParty == 1 ? '关联甲方' : '关联乙方'" :visible.sync="bindEnameVisible" width="966px" :show-footer="false">
      <div class="dialog-content-box-customer">
        <div class="dialog-content-top">
          <div class="search-box">
            <el-row class="box">
              <el-col :span="8">
                <div class="title">{{ currentParty == 1 ? '甲方名称' : '乙方名称' }}</div>
                <el-input v-model="bindEnameQuery.ename" placeholder="请输入名称" @keyup.enter.native="bindEnameHandleSearch" />
              </el-col>
            </el-row>
          </div>
          <div class="search-box">
            <el-row class="box">
              <el-col :span="16">
                <yl-search-btn
                  :total="bindEnameTotal"
                  @search="bindEnameHandleSearch"
                  @reset="bindEnameHandleReset"
                />
              </el-col>
            </el-row>
          </div>
        </div>
        <div class="mar-t-10" style="padding-bottom: 10px;background: #FFFFFF;">
          <yl-table
            :stripe="true"
            :show-header="true"
            :list="bindEnameList"
            :total="bindEnameTotal"
            :page.sync="bindEnameQuery.page"
            :limit.sync="bindEnameQuery.limit"
            :loading="loading1"
            @getList="getFirstParty"
          >
            <el-table-column label="企业ID" min-width="55" align="center">
              <template slot-scope="{ row }">
                <div class="font-size-base">{{ row.eid }}</div>
              </template>
            </el-table-column>
            <el-table-column :label="currentParty == 1 ? '甲方名称' : '乙方名称'" min-width="162" align="center">
              <template slot-scope="{ row }">
                <div class="goods-desc">
                  <div class="font-size-base">{{ row.ename }}</div>
                </div>
              </template>
            </el-table-column>
            <el-table-column :label="currentParty == 1 ? '甲方类型' : '乙方类型'" min-width="162" align="center">
              <template slot-scope="{ row }">
                <div class="goods-desc">
                  <div class="font-size-base">{{ row.type | dictLabel(agreementFirstType) }}</div>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="操作" min-width="55" align="center">
              <template slot-scope="{ row }">
                <div>
                  <yl-button class="view-btn" type="text" @click="bindFirstPartyClick(row)">添加</yl-button>
                </div>
              </template>
            </el-table-column>
          </yl-table>
        </div>
      </div>
    </yl-dialog>
    <!-- 乙方关联子公司 -->
    <yl-dialog title="关联子公司" :visible.sync="secondNameDindVisible" width="966px" @confirm="subEnterpriseConfirm">
      <div class="dialog-content-box-customer">
        <div class="dialog-content-top">
          <div class="search-box">
            <el-row class="box">
              <el-col :span="8">
                <div class="title">企业名称</div>
                <el-input v-model="bindSubEnterpriseQuery.name" placeholder="请输入名称" @keyup.enter.native="bindSubEnterpriseSearch" />
              </el-col>
            </el-row>
          </div>
          <div class="search-box">
            <el-row class="box">
              <el-col :span="16">
                <yl-search-btn
                  :total="bindSubEnterpriseTotal"
                  @search="bindSubEnterpriseSearch"
                  @reset="bindSubEnterpriseReset"
                />
              </el-col>
            </el-row>
          </div>
        </div>
        <div class="mar-t-10" style="padding-bottom: 10px;background: #FFFFFF;">
          <yl-table
            ref="multipleTable"
            :selection-change="handleSelectionChange"
            :stripe="true"
            :show-header="true"
            :list="bindSubEnterpriseList"
            :total="0"
            :loading="loading5"
            @getList="getSubEnterpriseListMethods"
          >
            <el-table-column type="selection" align="center" width="70"></el-table-column>
            <el-table-column label="企业名称" min-width="162" align="center">
              <template slot-scope="{ row }">
                <div class="goods-desc">
                  <div class="font-size-base">{{ row.ename }}</div>
                </div>
              </template>
            </el-table-column>
          </yl-table>
        </div>
      </div>
    </yl-dialog>
    <!-- 关联签订人 -->
    <yl-dialog :title="currentSign == 1 ? '甲方协议签订人' : '商家运营签订人'" :visible.sync="signUserVisible" width="966px" :show-footer="false">
      <div class="dialog-content-box-customer">
        <div class="dialog-content-top">
          <div class="search-box">
            <el-row class="box">
              <el-col :span="8">
                <div class="title">联系人</div>
                <el-input v-model="signUserQuery.name" placeholder="请输入联系人" @keyup.enter.native="signUserHandleSearch" />
              </el-col>
              <el-col :span="8">
                <div class="title">联系人电话</div>
                <el-input v-model="signUserQuery.mobile" placeholder="请输入联系人电话" @keyup.enter.native="signUserHandleSearch" />
              </el-col>
            </el-row>
          </div>
          <div class="search-box">
            <el-row class="box">
              <el-col :span="16">
                <yl-search-btn
                  :total="signUserTotal"
                  @search="signUserHandleSearch"
                  @reset="signUserHandleReset"
                />
              </el-col>
            </el-row>
          </div>
        </div>
        <div class="mar-t-10" style="padding-bottom: 10px;background: #FFFFFF;">
          <yl-table
            :stripe="true"
            :show-header="true"
            :list="signUserList"
            :total="signUserTotal"
            :page.sync="signUserQuery.page"
            :limit.sync="signUserQuery.limit"
            :loading="loading2"
            @getList="getStaffPage"
          >
            <el-table-column label="联系人" min-width="162" align="center">
              <template slot-scope="{ row }">
                <div class="goods-desc">
                  <div class="font-size-base">{{ row.userName }}</div>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="联系电话" min-width="162" align="center">
              <template slot-scope="{ row }">
                <div class="goods-desc">
                  <div class="font-size-base">{{ row.mobile }}</div>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="联系人邮箱" min-width="162" align="center">
              <template slot-scope="{ row }">
                <div class="goods-desc">
                  <div class="font-size-base">{{ row.email }}</div>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="操作" min-width="55" align="center">
              <template slot-scope="{ row }">
                <div>
                  <yl-button class="view-btn" type="text" @click="signUserItemClick(row)">添加</yl-button>
                </div>
              </template>
            </el-table-column>
          </yl-table>
        </div>
      </div>
    </yl-dialog>
    <!-- 指定商业公司 -->
    <yl-dialog title="供应商信息查询" :visible.sync="addProviderVisible" width="966px" :show-footer="false">
      <div class="dialog-content-box-customer">
        <div class="dialog-content-top">
          <div class="search-box">
            <el-row class="box">
              <el-col :span="8">
                <div class="title">供应商名称</div>
                <el-input v-model="addProviderQuery.ename" placeholder="请输入供应商名称" @keyup.enter.native="addProviderHandleSearch" />
              </el-col>
            </el-row>
          </div>
          <div class="search-box">
            <el-row class="box">
              <el-col :span="16">
                <yl-search-btn
                  :total="addProviderTotal"
                  @search="addProviderHandleSearch"
                  @reset="addProviderHandleReset"
                />
              </el-col>
            </el-row>
          </div>
        </div>
        <div class="mar-t-10" style="padding-bottom: 10px;background: #FFFFFF;">
          <yl-table
            :stripe="true"
            :show-header="true"
            :list="addProviderList"
            :total="addProviderTotal"
            :page.sync="addProviderQuery.page"
            :limit.sync="addProviderQuery.limit"
            :loading="loading3"
            @getList="getBusinessPage"
          >
            <el-table-column label="企业ID" min-width="55" align="center">
              <template slot-scope="{ row }">
                <div class="font-size-base">{{ row.eid }}</div>
              </template>
            </el-table-column>
            <el-table-column label="供应商名称" min-width="162" align="center">
              <template slot-scope="{ row }">
                <div class="goods-desc">
                  <div class="font-size-base">{{ row.ename }}</div>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="供应商类型" min-width="162" align="center">
              <template slot-scope="{ row }">
                <div class="goods-desc">
                  <div class="font-size-base">{{ row.type | dictLabel(enterpriseType) }}</div>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="地区" min-width="162" align="center">
              <template slot-scope="{ row }">
                <div class="goods-desc">
                  <div class="font-size-base">{{ row.address }}</div>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="操作" min-width="55" align="center">
              <template slot-scope="{ row }">
                <div>
                  <yl-button class="view-btn" type="text" @click="addProviderItemClick(row)">添加</yl-button>
                </div>
              </template>
            </el-table-column>
          </yl-table>
        </div>
      </div>
    </yl-dialog>
    <!-- 乙方协议签订人 -->
    <yl-dialog title="乙方协议签订人" :visible.sync="secondUserVisible" width="966px" :show-footer="false">
      <div class="dialog-content-box-customer">
        <div class="dialog-content-top">
          <div class="search-box">
            <el-row class="box">
              <el-col :span="8">
                <div class="title">联系人</div>
                <el-input v-model="secondUserQuery.name" placeholder="请输入联系人" @keyup.enter.native="secondUserHandleSearch" />
              </el-col>
              <el-col :span="8">
                <div class="title">联系人电话</div>
                <el-input v-model="secondUserQuery.mobile" placeholder="请输入联系人电话" @keyup.enter.native="secondUserHandleSearch" />
              </el-col>
              <el-col :span="8">
                <div class="title">所属厂家/商业</div>
                <el-input v-model="secondUserQuery.secondName" placeholder="请输入" @keyup.enter.native="secondUserHandleSearch" />
              </el-col>
            </el-row>
          </div>
          <div class="search-box">
            <el-row class="box">
              <el-col :span="16">
                <yl-search-btn
                  :total="secondUserTotal"
                  @search="secondUserHandleSearch"
                  @reset="secondUserHandleReset"
                />
              </el-col>
            </el-row>
          </div>
        </div>
        <div class="down-box clearfix">
        <div class="btn">
          <ylButton type="primary" plain @click="creatSecondClick">新建</ylButton>
        </div>
      </div>
        <div class="mar-t-10" style="padding-bottom: 10px;background: #FFFFFF;">
          <yl-table
            :stripe="true"
            :show-header="true"
            :list="secondUserList"
            :total="secondUserTotal"
            :page.sync="secondUserQuery.page"
            :limit.sync="secondUserQuery.limit"
            :loading="loading4"
            @getList="getSecondUserPage"
          >
            <el-table-column label="联系人" min-width="162" align="center">
              <template slot-scope="{ row }">
                <div class="goods-desc">
                  <div class="font-size-base">{{ row.name }}</div>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="联系电话" min-width="162" align="center">
              <template slot-scope="{ row }">
                <div class="goods-desc">
                  <div class="font-size-base">{{ row.mobile }}</div>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="所属厂家/商业" min-width="162" align="center">
              <template slot-scope="{ row }">
                <div class="goods-desc">
                  <div class="font-size-base">{{ row.secondName }}</div>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="联系人邮箱" min-width="162" align="center">
              <template slot-scope="{ row }">
                <div class="goods-desc">
                  <div class="font-size-base">{{ row.email }}</div>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="操作" min-width="150" align="center">
              <template slot-scope="{ row }">
                <div>
                  <yl-button class="view-btn" type="text" @click="secondSignDeleteClick(row)">删除</yl-button>
                  <yl-button class="view-btn" type="text" @click="secondSignUpdateClick(row)">修改</yl-button>
                  <yl-button class="view-btn" type="text" @click="secondSignSelectClick(row)">添加</yl-button>
                </div>
              </template>
            </el-table-column>
          </yl-table>
        </div>
      </div>
    </yl-dialog>
    <!-- 新建/修改 -->
    <yl-dialog title="新建" width="600px" @confirm="confirm" :visible.sync="showDialog">
      <div class="dialog-content">
        <el-form ref="form" :rules="rules" :model="form" label-width="100px" label-position="right">
          <el-form-item label="联系人" prop="name">
            <el-input v-model="form.name" placeholder="请输入联系人" />
          </el-form-item>
          <el-form-item label="联系人电话" prop="mobile">
            <el-input v-model="form.mobile" placeholder="请输入联系人电话" />
          </el-form-item>
          <el-form-item label="厂家类型">
            <el-input :value="form.manufacturerType" :maxlength="10" placeholder="请选择厂家类型" />
          </el-form-item>
          <el-form-item label="乙方" prop="secondName" ref="creatSecondPartyItem">
            <yl-button type="text" @click="creatSecondPartyClick">{{ form.secondName || '选择乙方' }}</yl-button>
          </el-form-item>
          <el-form-item label="联系人邮箱">
            <el-input v-model="form.email" placeholder="请输入联系人邮箱" />
          </el-form-item>
        </el-form>
      </div>
    </yl-dialog>
    <!-- 弹窗 -->
    <yl-dialog :visible.sync="show" title="图片预览" @confirm="showDialogConfirm" :show-cancle="false">
      <div class="dialog-content flex-row-center">
        <el-image :src="bigUrl" fit="contain" :lazy="true"></el-image>
      </div>
    </yl-dialog>
  </el-form>
</template>

<script>
import {
  getSubEnterpriseList,
  queryFirstParty,
  checkAgreementExist,
  queryStaffPage,
  queryBusinessPage,
  querySecondUserPage,
  saveAgreementSecondUser,
  deleteAgreementSecondUser
} from '@/subject/pop/api/agreement';
import { agreementFirstType, agreementType, agreementMarginPayer, channelType, agreementAttachmentType, enterpriseType, kaAgreementType } from '@/subject/pop/utils/busi';
import ylUpload from '@/subject/pop/components/CustomUpload';

export default {
  name: 'AgreementMain',
  components: {
    ylUpload
  },
  computed: {
    // 甲方类型
    agreementFirstType() {
      return agreementFirstType()
    },
    // 协议类型
    agreementType() {
      return agreementType()
    },
    // 保证金支付方
    agreementMarginPayer() {
      return agreementMarginPayer()
    },
    // 供应商类型
    channelType() {
      return channelType();
    },
    // 企业类型
    enterpriseType() {
      return enterpriseType();
    },
    // 协议附件类型
    agreementAttachmentType() {
      return agreementAttachmentType();
    },
    // KA协议类型
    kaAgreementType() {
      return kaAgreementType();
    },
    // 当前可选择协议类型 1-一级协议 2-二级协议 3-临时协议 4-商业供货协议 5-KA连锁协议 6-代理商协议
    ableSelectedAgreementType: function() {
      let agreementTypeArray = agreementType()
      let arr = []
      if (this.agreementMainTerms.firstType == 1 || this.agreementMainTerms.firstType == 2) {
        for (let i = 0; i < agreementTypeArray.length; i++) {
          let item = agreementTypeArray[i]
          if (item.value == 1 || item.value == 2 || item.value == 3 || item.value == 5) {
            arr.push(item)
          }
        }
        return arr
      } else if (this.agreementMainTerms.firstType == 3 ) {
        for (let i = 0; i < agreementTypeArray.length; i++) {
          let item = agreementTypeArray[i]
          if (item.value == 1 || item.value == 2 || item.value == 3 || item.value == 4 || item.value == 5) {
            arr.push(item)
          }
        }
        return arr
      } else if (this.agreementMainTerms.firstType == 4) {
        for (let i = 0; i < agreementTypeArray.length; i++) {
          let item = agreementTypeArray[i]
          if (item.value == 2 || item.value == 3) {
            arr.push(item)
          }
        }
        return arr
      } else {
        return []
      }
    }
  },
  watch: {
    // 监听 保证金返还方式：1-协议结束后返还 2-指定日期返还
    'agreementMainTerms.backType': {
        handler(newName, oldName) {
           if (newName) {
             this.agreementMainTerms.marginBackType = 1
             this.agreementMainTerms.marginBackDate = ''
           } else {
             this.agreementMainTerms.marginBackType = 2
           }
        },
        deep: true
      }
  },
  props: {
    mainTerms: {
      type: Object,
      default: () => {}
    }
  },
  data() {
    return {
      form: {
        name: '',
        mobile: '',
        manufacturerType: '商业公司',
        secondEid: '',
        secondName: '',
        email: ''
      },
      // 乙方 协议签订人 新增还是修改 1-新增 2-修改
      saveSecondUserType: 1,
      // 当前选择 1-甲方 还是 2-乙方
      currentParty: 1,
      // 关联甲/乙方
      bindEnameVisible: false,
      loading1: false,
      bindEnameQuery: {
        page: 1,
        limit: 10
      },
      bindEnameList: [],
      bindEnameTotal: 0,
      // 乙方关联子公司
      secondNameDindVisible: false,
      loading5: false,
      bindSubEnterpriseQuery: {
        name: ''
      },
      bindSubEnterpriseTotal: 0,
      bindSubEnterpriseList: [],
      // 表格选择
      multipleSelection: [],
      // 当前界面选择的
      currentPageSelection: [],
      // 协议签订人
      // 当前点击是甲方协议签订人 还是 商家运营签订人
      currentSign: 1,
      signUserVisible: false,
      loading2: false,
      signUserQuery: {
        page: 1,
        limit: 10
      },
      signUserList: [],
      signUserTotal: 0,
      // 乙方 协议签订人
      secondUserVisible: false,
      loading4: false,
      secondUserQuery: {
        page: 1,
        limit: 10
      },
      secondUserList: [],
      secondUserTotal: 0,
      showDialog: false,
      // 指定商业公司
      addProviderVisible: false,
      loading3: false,
      addProviderQuery: {
        page: 1,
        limit: 10
      },
      addProviderList: [],
      addProviderTotal: 0,

      // 图片遮罩层
      packingShow: false,
      packingCurrent: -1,
      // 图片预览
      show: false,
      bigUrl: '',

      agreementMainTerms: {
        // 甲方类型：1-工业-生产厂家 2-工业-品牌厂家 3-商业-供应商 4-代理商（见字典：agreement_first_type）
        firstType: '',
        // 协议类型：1-一级协议 2-二级协议 3-临时协议 4-商业供货协议 5-KA连锁协议 6-代理商协议（见字典：agreement_type）
        agreementType: '',
        // 签订日期
        signTime: '',
        // 关联甲方
        ename: '',
        eid: '',
        // 乙方名称
        secondName: '',
        // 乙方ID
        secondEid: '',
        // 乙方关联子公司集合
        secondSubEidList: [],
        // 生效日期
        effectTime: [],
        startTime: '',
        endTime: '',
        // 甲方协议签订人名称
        firstSignUserName: '',
        // 甲方协议签订人ID
        firstSignUserId: '',
        // 甲方协议签订人手机号
        firstSignUserPhone: '',
        // 乙方协议签订人名称
        secondSignUserName: '',
        // 乙方协议签订人ID
        secondSignUserId: '',
        // 乙方协议签订人手机号
        secondSignUserPhone: '',
        // 协议负责人：1-甲方联系人 2-乙方联系人
        mainUser: '',
        // 是否提供流向：0-否 1-是，默认为否
        flowFlag: 0,
        // 是否为草签协议：0-否 1-是
        draftFlag: 0,
        // 是否交保证金：0-否 1-是
        marginFlag: 0,
        // 保证金支付方：2-乙方 3-指定商业公司
        marginPayer: 2,
        // 指定商业公司ID
        businessEid: '',
        businessName: '',
        // 保证金金额
        marginAmount: '',
        // 保证金返还方式：1-协议结束后返还 2-指定日期返还
        backType: true,
        marginBackType: 1,
        // 保证金返还日期
        marginBackDate: '',
        // KA协议类型：1-统谈统签，统一支付 2-统谈统签，分开支付 3-统谈分签，分开支付 4-单独签订
        kaAgreementType: '',
        // 是否活动协议：0-否 1-是
        activeFlag: 0,
        // 协议附件类型：1-协议原件 2-协议复印件
        attachmentType: 1,
        // 商业运营签订人ID
        businessOperatorId: '',
        // 商业运营签订人名称
        businessOperatorName: '',
        // 协议附件集合
        agreementAttachmentList: []
      },
      agreementMainTermsRules: {
        firstType: [{ required: true, message: '请选择甲方类型', trigger: 'change' }],
        agreementType: [{ required: true, message: '请选择协议类型', trigger: 'change' }],
        // signTime: [{ required: true, message: '请选择签订日期', trigger: 'change' }],
        ename: [{ required: true, message: '请选择关联甲方', trigger: 'change' }],
        secondName: [{ required: true, message: '请选择关联乙方', trigger: 'change' }],
        effectTime: [{ required: true, message: '请选择生效日期', trigger: 'change' }],
        // firstSignUserName: [{ required: true, message: '请选择甲方协议签订人', trigger: 'change' }],
        // secondSignUserName: [{ required: true, message: '请选择乙方协议签订人', trigger: 'change' }],
        flowFlag: [{ required: true, message: '请选择是否提供流向', trigger: 'change' }],
        draftFlag: [{ required: true, message: '请选择是否为草签协议', trigger: 'change' }],
        marginFlag: [{ required: true, message: '请选择是否交保证金', trigger: 'change' }],
        marginPayer: [{ required: true, message: '请选择保证金支付方', trigger: 'change' }],
        marginAmount: [{ required: true, message: '请输入保证金金额', trigger: 'blur' }],
        marginBackDate: [{ required: true, message: '请选择返还日期', trigger: 'change' }],
        // attachmentType: [{ required: true, message: '请选择协议附件类型', trigger: 'change' }],
        kaAgreementType: [{ required: true, message: '请选择KA协议类型', trigger: 'change' }],
        activeFlag: [{ required: true, message: '请选择是否活动协议', trigger: 'change' }],
        businessOperatorName: [{ required: true, message: '请选择商家运营签订人', trigger: 'change' }]
        // agreementAttachmentList: [{ required: true, message: '请上传图片', trigger: 'change' }],
      },
      rules: {
        name: [{ required: true, message: '请输入联系人', trigger: 'blur' }],
        mobile: [{ required: true, message: '请输入联系人电话', trigger: 'blur' }],
        secondName: [{ required: true, message: '请选择乙方', trigger: 'change' }]
      }
    };
  },
  mounted() {

  },
  methods: {
    init(agreementMainTerms) {
      this.agreementMainTerms = agreementMainTerms
      this.$nextTick( () => {
        if (this.agreementMainTerms.ename) {
          this.$refs.firstPartyItem.$emit('el.form.change');
        }

        if (this.agreementMainTerms.secondName) {
          this.$refs.secondPartyItem.$emit('el.form.change');
        }

        // if (this.agreementMainTerms.firstSignUserName) {
        //   this.$refs.firstSignUserNameItem.$emit('el.form.change');
        // }

        // if (this.agreementMainTerms.secondSignUserName) {
        //   this.$refs.secondSignUserNameItem.$emit('el.form.change');
        // }

        // this.$refs.agreementAttachmentListRef.clearValidate()

      })
    },
    clearValidateMethods() {
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {
          this.$log('clearValidateMethods')
        } else {
          return false;
        }
      })
    },
    // 下一步点击
    stepClickMethods(callback) {
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {
          if (this.checkInputData()) {
            this.agreementMainTerms.startTime = this.agreementMainTerms.effectTime && this.agreementMainTerms.effectTime.length ? this.agreementMainTerms.effectTime[0] : undefined
            this.agreementMainTerms.endTime = this.agreementMainTerms.effectTime && this.agreementMainTerms.effectTime.length ? this.agreementMainTerms.effectTime[1] : undefined
            if (callback) callback(this.agreementMainTerms)
          }
        } else {
          return false;
        }
      })
    },
    // 是否活动协议
    activeFlagChange(type) {
      if (type == 0) {
        this.agreementMainTerms.businessOperatorId = ''
        this.agreementMainTerms.businessOperatorName = ''
      }
    },
    // 是否交保证金
    marginFlagChange(type) {
      this.$log(type)
      if (type == 0) {
        this.agreementMainTerms.marginPayer = 2
        this.agreementMainTerms.businessEid = ''
        this.agreementMainTerms.businessName = ''
        this.agreementMainTerms.marginAmount = ''
        this.agreementMainTerms.backType = true
        this.agreementMainTerms.marginBackType = 1
        this.agreementMainTerms.marginBackDate = ''
      }
    },
    // 保证金支付方企业
    marginPayerTypeChange(type) {
      this.$log(type)
      if (type == 2) {
        this.agreementMainTerms.businessEid = ''
        this.agreementMainTerms.businessName = ''
      }
    },
    // 甲方类型切换
    firstTypeChange() {
      this.agreementMainTerms.agreementType = ''
      // 关联甲方
      this.agreementMainTerms.ename = ''
      this.agreementMainTerms.eid = ''
      this.agreementMainTerms.effectTime = []
      this.agreementMainTerms.secondSubEidList = []
    },
    //协议类型切换
    agreementTypeChange(type) {
      this.$log(type)
      // 不为KA的协议
      if (type != 5) {
        this.agreementMainTerms.kaAgreementType = ''
        this.agreementMainTerms.secondSubEidList = []
      }
      this.agreementMainTerms.effectTime = []
    },
    // 协议类型 focus
    agreementTypeFocus() {
      if (!this.agreementMainTerms.firstType) {
        this.$common.warn('请先选择甲方类型')
      }
    },
    // 关联甲方
    selectFirstPartyClick() {
      if (!this.agreementMainTerms.firstType) {
        this.$common.warn('请先选择甲方类型')
        return
      }
      this.currentParty = 1
      this.bindEnameVisible = true
      this.getFirstParty()
    },
    bindEnameHandleSearch() {
      this.bindEnameQuery.page = 1
      this.getFirstParty()
    },
    bindEnameHandleReset() {
      this.bindEnameQuery = {
        page: 1,
        limit: 10
      }
    },
    // 查询甲方/乙方
    async getFirstParty() {
      this.loading1 = true
      let bindEnameQuery = this.bindEnameQuery
      let firstType = ''
      if (this.currentParty == 1) {
        firstType = this.agreementMainTerms.firstType
      } else {
        firstType = ''
      }
      let data = await queryFirstParty(
        bindEnameQuery.page,
        bindEnameQuery.limit,
        firstType,
        bindEnameQuery.ename
      );
      this.loading1 = false
      if (data) {
        this.bindEnameList = data.records
        this.bindEnameTotal = data.total
      }
    },
    // 关联甲方/乙方 点击
    bindFirstPartyClick(row) {
      if (this.currentParty == 1) {// 甲方
        this.agreementMainTerms.ename = row.ename
        this.agreementMainTerms.eid = row.eid

        this.$emit('firstPartyChange')
        this.$refs.firstPartyItem.$emit('el.form.change');
      } else if (this.currentParty == 2) {// 乙方
        this.agreementMainTerms.secondName = row.ename
        this.agreementMainTerms.secondEid = row.eid
        this.agreementMainTerms.secondSubEidList = []
        this.$refs.secondPartyItem.$emit('el.form.change');
      } else if (this.currentParty == 3) {// 新建乙方签订人  选择乙方
        this.form.secondName = row.ename
        this.form.secondEid = row.eid
        this.$refs.creatSecondPartyItem.$emit('el.form.change');
      }
      if (this.currentParty != 3) {
        this.agreementMainTerms.effectTime = []
      }

      this.bindEnameVisible = false
      this.bindEnameQuery = {
        page: 1,
        limit: 10
      }
      this.bindEnameList = []
    },
    // 关联乙方
    selectSecondPartyClick() {
      this.currentParty = 2
      this.bindEnameVisible = true
      this.getFirstParty()
    },
    // 乙方关联子公司
    secondNameDindClick() {
      if (!this.agreementMainTerms.secondEid) {
        this.$common.warn('请先选择乙方')
        return
      }
      this.multipleSelection = []
      this.secondNameDindVisible = true
      this.getSubEnterpriseListMethods()
    },
    async getSubEnterpriseListMethods() {
      this.loading5 = true
      let bindSubEnterpriseQuery = this.bindSubEnterpriseQuery
      let data = await getSubEnterpriseList(
        this.agreementMainTerms.secondEid,
        bindSubEnterpriseQuery.name
      );
      this.loading5 = false
      if (data) {
        this.bindSubEnterpriseList = data
        this.bindSubEnterpriseTotal = data.length
        this.$nextTick(() => {
          let arr = []
          this.bindSubEnterpriseList.forEach(row => {
            let hasIndex = this.agreementMainTerms.secondSubEidList.findIndex(eid => {
              return eid == row.eid;
            });
            if (hasIndex != -1) {
              this.$refs.multipleTable.toggleRowSelectionMethod(row);
              arr.push(row)
              this.currentPageSelection = arr
            }
          });
        })
      }
    },
    bindSubEnterpriseSearch() {
      this.getSubEnterpriseListMethods()
    },
    bindSubEnterpriseReset() {
      this.bindSubEnterpriseQuery = {
        name: ''
      }
    },
    // 表格全选
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    // 关联子企业item 点击
    bindSubEnterpriseClick(row) {
      this.$refs.multipleTable.toggleRowSelectionMethod(row)
    },
    subEnterpriseConfirm() {
      if (this.multipleSelection.length > 0) {
        let secondSubEidList = this.agreementMainTerms.secondSubEidList
        let currentPageSelection = this.currentPageSelection

        for (let i = 0; i < this.multipleSelection.length; i++) {
          let item = this.multipleSelection[i]

          let hasIndex = secondSubEidList.findIndex(eid => {
            return eid == item.eid;
          });
          if (hasIndex == -1) {
            secondSubEidList.push(item.eid)
            currentPageSelection.push(item)
          }
        }

        // 找到当前界面删除的row
        for (let i = 0; i < currentPageSelection.length; i++) {
          let item = currentPageSelection[i]
          let deleteIndex = this.multipleSelection.findIndex(current => {
            return current.eid == item.eid;
          });
          // 找到当前界面删除的row
          if (deleteIndex == -1) {
            console.log('delete:', item)
            secondSubEidList.splice(secondSubEidList.findIndex(subEid => subEid == item.eid), 1)
          }
        }

        this.agreementMainTerms.secondSubEidList = secondSubEidList
        this.secondNameDindVisible = false
      } else {
        this.$common.warn('请选择需要关联的企业')
      }

    },
    // 生效日期改变
    async effectTimeChange() {
      if (!this.agreementMainTerms.firstType) {
        this.$common.warn('请先选择甲方类型')
        return
      }
      if (!this.agreementMainTerms.agreementType) {
        this.$common.warn('请选择协议类型')
        return
      }
      if (!this.agreementMainTerms.eid) {
        this.$common.warn('请选择甲方')
        return
      }
      if (!this.agreementMainTerms.secondEid) {
        this.$common.warn('请选择乙方')
        return
      }
      let effectTime = this.agreementMainTerms.effectTime
      this.$log(effectTime)
      let params = {
        firstType: this.agreementMainTerms.firstType,
        agreementType: this.agreementMainTerms.agreementType,
        eid: this.agreementMainTerms.eid,
        secondEid: this.agreementMainTerms.secondEid
      }
      params.id = this.id
      if (effectTime) {
        let startTime = effectTime && effectTime.length ? effectTime[0] : undefined
        let endTime = effectTime && effectTime.length > 1 ? effectTime[1] : undefined
        this.$common.showLoad()
        let data = await checkAgreementExist(
          this.agreementMainTerms.firstType,
          this.agreementMainTerms.agreementType,
          this.agreementMainTerms.eid,
          this.agreementMainTerms.secondEid,
          startTime,
          endTime
        );
        this.$common.hideLoad()
        if (data) {
          this.$confirm(`该时间段内,存在同类型协议,请仔细核对协议信息,谢谢!同类型协议编号:${data}`, '提示', {
            confirmButtonText: '我知道了',
            type: 'warning'
          })
          .then( async() => {
            //确定
          })
          .catch(() => {
            //取消
          });
        }
      }
    },
    // 甲方协议签订人
    firstSignUserNameClick() {
      this.currentSign = 1
      this.signUserVisible = true
      this.getStaffPage()
    },
    // 指定活动负责人
    selectOperatorClick() {
      this.currentSign = 2
      this.signUserVisible = true
      this.getStaffPage()
    },
    signUserHandleSearch() {
      this.signUserQuery.page = 1
      this.getStaffPage()
    },
    signUserHandleReset() {
      this.signUserQuery = {
        page: 1,
        limit: 10
      }
    },
    // 查询协议签订人
    async getStaffPage() {
      this.loading2 = true
      let signUserQuery = this.signUserQuery
      let data = await queryStaffPage(
        signUserQuery.page,
        signUserQuery.limit,
        signUserQuery.name,
        signUserQuery.mobile
      );
      this.loading2 = false
      if (data) {
        this.signUserList = data.records
        this.signUserTotal = data.total
      }
    },
    // 点击添加协议签订人
    signUserItemClick(row) {
      if (this.currentSign == 1) {// 甲方协议签订人
        this.agreementMainTerms.firstSignUserId = row.userId
        this.agreementMainTerms.firstSignUserName = row.userName
        this.agreementMainTerms.firstSignUserPhone = row.mobile
        // this.$refs.firstSignUserNameItem.$emit('el.form.change');
      } else if (this.currentSign == 2) {// 商家运营签订人
        this.agreementMainTerms.businessOperatorId = row.userId
        this.agreementMainTerms.businessOperatorName = row.userName
        // this.$refs.businessOperatorNameItem.$emit('el.form.change');
      }
      this.signUserVisible = false
      this.signUserQuery = {
        page: 1,
        limit: 10
      }
      this.signUserList = []
    },
    // 指定商业公司
    addProviderClick() {
      this.addProviderVisible = true
      this.getBusinessPage()
    },
    addProviderHandleSearch() {
      this.addProviderQuery.page = 1
      this.getBusinessPage()
    },
    addProviderHandleReset() {
      this.addProviderQuery = {
        page: 1,
        limit: 10
      }
    },
    // 查询指定商业公司
    async getBusinessPage() {
      this.loading2 = true
      let addProviderQuery = this.addProviderQuery
      let data = await queryBusinessPage(
        addProviderQuery.page,
        addProviderQuery.limit,
        addProviderQuery.ename
      );
      this.loading2 = false
      if (data) {
        this.addProviderList = data.records
        this.addProviderTotal = data.total
      }
    },
    addProviderItemClick(row) {
      this.addProviderVisible = false
      this.agreementMainTerms.businessEid = row.eid
      this.agreementMainTerms.businessName = row.ename
    },
    // 乙方协议签订人
    secondSignUserNameClick() {
      this.secondUserVisible = true
      this.getSecondUserPage()
    },
    // 查询乙方协议签联系人
    async getSecondUserPage() {
      this.loading4 = true
      let secondUserQuery = this.secondUserQuery
      let data = await querySecondUserPage(
        secondUserQuery.page,
        secondUserQuery.limit,
        secondUserQuery.name,
        secondUserQuery.mobile,
        secondUserQuery.secondName
      );
      this.loading4 = false
      if (data) {
        this.secondUserList = data.records
        this.secondUserTotal = data.total
      }
    },
    // 新建乙方协议签订人
    creatSecondClick() {
      let form = {
        id: '',
        name: '',
        mobile: '',
        manufacturerType: '商业公司',
        secondEid: '',
        secondName: '',
        email: ''
      }
      this.form = form
      this.saveSecondUserType = 1
      this.showDialog = true
    },
    // 新建乙方协议签订人 修改
    secondSignUpdateClick(row) {
      let form = row
      form.manufacturerType = '商业公司'
      this.form = form
      this.saveSecondUserType = 2
      this.showDialog = true
    },
    // 新建乙方协议签订人 删除
    async secondSignDeleteClick(row) {
      this.$common.showLoad()
      let data = await deleteAgreementSecondUser(
        row.id
      );
      this.$common.hideLoad()
      if (typeof data !== 'undefined') {
        this.$common.n_success('删除成功')
        this.getSecondUserPage()
      }
    },
    // 新建乙方协议签订人 添加
    secondSignSelectClick(row) {
      this.agreementMainTerms.secondSignUserId = row.id
      this.agreementMainTerms.secondSignUserName = row.name
      this.agreementMainTerms.secondSignUserPhone = row.mobile
      // this.$refs.secondSignUserNameItem.$emit('el.form.change');
      this.secondUserVisible = false
    },
    // 新建 选择乙方
    creatSecondPartyClick() {
      this.currentParty = 3
      this.bindEnameVisible = true
      this.getFirstParty()
    },
    confirm() {
      this.$refs['form'].validate(async (valid) => {
        if (valid) {
          this.$common.showLoad()
          let form = this.form
          if (this.saveSecondUserType == 1) {
            form.id = ''
          }
          let data = await saveAgreementSecondUser(
            form.id,
            form.name,
            form.mobile,
            form.manufacturerType,
            form.secondEid,
            form.secondName,
            form.email
          );
          this.$common.hideLoad()
          if (typeof data !== 'undefined') {
            this.showDialog = false
            this.$common.n_success(this.saveSecondUserType === 1 ? '新建成功' : '修改成功')
            this.getSecondUserPage()
          }
        } else {
          return false;
        }
      })
    },
    secondUserHandleSearch() {
      this.secondUserQuery.page = 1
      this.getSecondUserPage()
    },
    secondUserHandleReset() {
      this.secondUserQuery = {
        page: 1,
        limit: 10
      }
    },
    // 上传图片
    onSuccess(data) {
      this.$log('data:',data)
      if (data.key) {
        data.attachmentType = this.agreementMainTerms.attachmentType
        data.fileKey = data.key
        data.fileUrl = data.url
        data.fileType = data.contentType.indexOf('pdf') != -1 ? 2 : 1
        this.agreementMainTerms.agreementAttachmentList.push(data)
      }
    },
    //显示操作项
    showDialogClick(index, item) {
      this.packingShow = true;
      this.packingCurrent = index;
    },
    //隐藏蒙层
    hideDialog(index, item) {
      this.packingShow = false;
      this.packingCurrent = -1;
    },
    deletPackingImg(index) {
      this.agreementMainTerms.agreementAttachmentList.splice(index, 1)
    },
    downloadClick(item) {
      let a = document.createElement('a')
      a.href = item.url
      a.target = '_blank'
      if (item.name) {
        a.download = item.name
      }
      a.click()
    },
    // 查看大图
    changeBigUrl(url) {
      this.show = true
      this.bigUrl = url
    },
    showDialogConfirm() {
      this.show = false
    },
    // 校验数据
    checkInputData() {
      // let agreementMainTerms = this.agreementMainTerms
      // if (!agreementMainTerms.mainUser){
      //   this.$common.warn('请选择协议负责人')
      //   return false
      // }

      return true
    }

  }
};
</script>

<style lang="scss" >
.my-form-box{
  .el-form-item{
    .el-form-item__label{
      color: $font-title-color;
    }
    label{
      font-weight: 400 !important;
    }
  }
  .my-form-item-right{
    label{
      font-weight: 400 !important;
    }
  }
}
.steps-view{
  .el-step{
    .is-finish{
      .el-step__icon{
        background: #1790ff;
      }
    }
    .el-step__icon{
      background: #CCCCCC;
      border: none;
      .el-step__icon-inner{
        color: $white;
      }
    }
    .el-step__title{
      color: #333;
    }
  }
}
</style>
<style lang="scss" scoped>
@import './index.scss';
</style>
