<template>
   <div class="app-container">
    <div class="app-container-content has-bottom-bar">
      <div class="c-box">
        <el-form 
          :model="form" 
          :rules="rules" 
          ref="dataForm" 
          label-width="160px" 
          class="demo-ruleForm">
          <el-form-item label="终端商家：" prop="ename">
            <el-input style="display:none" v-model="form.ename"/>
            <el-tag class="tag-margin" v-show="form.ename">{{ form.ename }}</el-tag>
            <span class="add-color" @click="addBusiness">{{ form.ename == '' ? '添加' : '修改' }}</span>
          </el-form-item>
          <el-form-item label="用药福利计划名称：" prop="drugWelfareId">
            <el-select v-model="form.drugWelfareId" filterable placeholder="请选择">
              <el-option
                v-for="item in benefitPlanData"
                :key="item.id"
                :label="item.name"
                :value="item.id">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="备注：">
            <el-input 
              type="textarea" 
              style="width:360px" 
              show-word-limit 
              :rows="5"
              maxlength="255" 
              placeholder="请输入备注" 
              v-model="form.remake">
            </el-input>
          </el-form-item>
        </el-form>
      </div>
    </div>
    <div class="flex-row-center bottom-view" >
      <yl-button type="primary" plain @click="$router.go(-1)">返回</yl-button>
      <yl-button type="primary" class="bottom-margin" @click="preservationClick">保存</yl-button>
    </div>
    <yl-dialog 
      title="选择商家" 
      :show-footer="false" 
      :show-cancle="false" 
      width="700px" 
      :visible.sync="showDialog">
      <div class="dialogTc">
        <div class="dialogTc-top"></div>
        <div class="common-box">
          <div class="search-box">
            <el-row class="box">
              <el-col :span="12">
                <div class="title">商家名称</div>
                <el-input v-model.trim="dialog.ename" @keyup.enter.native="searchEnter" placeholder="请输入企业名称" />
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
        <!-- 下部表格 -->
        <div class="search-bar" >
          <yl-table 
            border 
            :show-header="true" 
            :list="dialogData" 
            :total="dialog.total" 
            :page.sync="dialog.page" 
            :limit.sync="dialog.limit" 
            :loading="dialogLoading" 
            @getList="getList">
            <el-table-column align="center" min-width="200" label="商家名称" prop="name">
            </el-table-column>
            <el-table-column align="center" min-width="120" label="许可证号" prop="licenseNumber"> 
            </el-table-column>
            <el-table-column fixed="right" align="center" label="操作" min-width="70">
              <template slot-scope="{ row }">
                <div>
                  <yl-button type="text" @click="addClick(row)">
                    <span :style="{color: row.typeNum == 1 ? '#1790ff' : '#c8c9cc'}">
                      {{ row.typeNum == 1 ? '添加' : (row.typeNum == 2 ? '已添加' : '') }}
                    </span>
                  </yl-button>
                </div>
              </template>
            </el-table-column>
          </yl-table>
        </div>
      </div>
    </yl-dialog>
  </div>
</template>
<script>
import { pageEnterprise, queryDrugWelfareList, save } from '@/subject/admin/api/cmp_api/benefit_plan'
export default {
  name: 'TerminalMerchantsAdd',
  data() {
    return {
      form: {
        // 商家名称	
        ename: '',
        // 商家id	
        eid: '',
        // 福利计划id	
        drugWelfareId: '',
        // 备注
        remake: ''
      },
      // 用药福利计划名称
      benefitPlanData: [],
      rules: {
        ename: [{ required: true, message: '请选择终端商家', trigger: 'change' }],
        drugWelfareId: [{ required: true, message: '请选择用药福利计划名称', trigger: 'change' }]
      },
      // 是否显示弹窗
      showDialog: false,
      // 商家弹窗loading
      dialogLoading: false,
      dialog: {
        ename: '',
        page: 1,
        limit: 10,
        total: 0
      },
      // 商家弹窗数据
      dialogData: []
    }
  },
  mounted() {
    // 获取用药福利计划名称
    this.getBenefitPlan();
  },
  methods: {
    // Enter
    searchEnter(e) {
      const keyCode = window.event ? e.keyCode : e.which;
      if (keyCode === 13) {
        this.getList()
      }
    },
    // 获取用药福利计划名称
    async getBenefitPlan() {
      let data = await queryDrugWelfareList()
      if (data) {
        this.benefitPlanData = data.list;
      }
    },
    // 点击添加商家
    addBusiness() {
      this.showDialog = true;
      this.getList();
    },
    // 点击保存
    preservationClick() {
      this.$refs['dataForm'].validate( async(valid) => {
        if (valid) {
          let form = this.form;
          this.$common.showLoad();
          let data = await save(
            form.ename,
            form.eid,
            form.drugWelfareId,
            form.remake
          )
          this.$common.hideLoad();
          if (data !== undefined) {
            this.$common.alert('保存成功', r => {
              this.$router.go(-1)
            })
          }
        }
      })
    },
    // 获取商家数据
    async getList() {
      this.dialogData = [];
      this.dialogLoading = true;
      let dialog = this.dialog;
      this.$common.showLoad();
      let data = await pageEnterprise(
        dialog.ename,
        dialog.page,
        dialog.limit
      )
      this.$common.hideLoad();
      if (data !== undefined) {
        data.records.forEach(element => {
          this.dialogData.push({
            ...element,
            typeNum: 1
          })
        });
        this.dialog.total = data.total;
        if (this.form.eid){
          for (let i = 0; i < this.dialogData.length; i ++) {
            if (this.form.eid == this.dialogData[i].id) {
              this.dialogData[i].typeNum = 2
            }
          }
        }
      }
      this.dialogLoading = false;
    },
    // 点击操作添加
    addClick(row) {
      if (row.typeNum == 1) {
        this.form.ename = row.name;
        this.form.eid = row.id;
        this.showDialog = false;
      }
    },
    // 点击搜索
    handleSearch() {
      this.dialog.page = 1;
      this.getList();
    },
    // 清空
    handleReset() {
      this.dialog ={
        ename: '',
        page: 1,
        limit: 10,
        total: 0
      }
    }
  }
}
</script>

<style lang="scss" scoped>
  @import './index.scss';
</style>