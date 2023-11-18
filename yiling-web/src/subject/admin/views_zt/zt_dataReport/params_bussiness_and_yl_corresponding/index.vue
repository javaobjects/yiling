<!--
 * @Description:
 * @Author: xuxingwang
 * @Date: 2022-02-22 18:07:11
 * @LastEditTime: 2022-03-01 10:40:32
 * @LastEditors: xuxingwang
-->
<template>
  <div class="app-container">
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">商业名称</div>
              <el-select
              v-model="query.eid"
              clearable
              filterable
              remote
              :remote-method="remoteSearchEnterprise"
              @clear="clearEnterprise"
              @blur="blurEnterpriseInput"
              :loading="searchLoading"
              placeholder="请搜索并选择商业">
                <el-option
                  v-for="item in sellerEnameOptions"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="8">
              <div class="title">操作时间</div>
              <el-date-picker v-model="query.time" type="daterange" format="yyyy 年 MM 月 dd 日" value-format="yyyy-MM-dd" range-separator="至" start-placeholder="开始时间" end-placeholder="结束时间">
              </el-date-picker>
            </el-col>
            <el-col :span="8">
              <div class="title">以岭品名称</div>
              <el-input v-model="query.ylGoodsName" placeholder="请输入" @keyup.enter.native="handleSearch"></el-input>
            </el-col>
          </el-row>
          <el-row class="box mar-t-16">
            <el-col :span="8">
              <div class="title">操作人</div>
              <el-input v-model="query.updateUser" placeholder="请输入" @keyup.enter.native="handleSearch"></el-input>
            </el-col>
            <el-col :span="8">
              <div class="title">有无商品关系</div>
              <!-- 	有无商品关系：0-无, 1-有 -->
              <el-radio-group v-model="query.relationFlag">
                <el-radio :label="1">有</el-radio>
                <el-radio :label="0">无</el-radio>
              </el-radio-group>
            </el-col>
            <el-col :span="8">
              <div class="title">商品名称</div>
              <el-input v-model="query.goodsName" placeholder="请输入商品名称" @keyup.enter.native="handleSearch"></el-input>
            </el-col>
          </el-row>
          <el-row class="box mar-t-16">
            <el-col :span="8">
              <div class="title">生产厂家</div>
              <el-input v-model="query.goodsManufacturer" placeholder="请输入生产厂家" @keyup.enter.native="handleSearch"></el-input>
            </el-col>
            <el-col :span="8">
              <div class="title">商品关系标签</div>
              <el-checkbox-group v-model="query.goodsRelationLabelList">
                <el-checkbox class="option-class" v-for="item in flowGoodsRelationLabel" :label="item.value" :key="item.value">{{ item.label }}</el-checkbox>
              </el-checkbox-group>
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="24">
              <yl-search-btn :total="query.total" @search="handleSearch" @reset="handleReset" />
            </el-col>
          </el-row>
        </div>
      </div>
      <div class="down-box">
        <div>
        </div>
        <div class="btn">
          <yl-button type="primary" plain @click="downLoadTemp">导出</yl-button>
          <ylButton type="primary" plain @click="importClick">导入商品关系</ylButton>
        </div>
      </div>
      <div class="mar-t-8 pad-b-100">
        <yl-table border :show-header="true" :list="dataList" :total="query.total" :page.sync="query.page" :limit.sync="query.limit" :loading="loading" @getList="getList">
          <el-table-column min-width="100" align="center" label="商业ID" prop="eid"></el-table-column>
          <el-table-column min-width="100" align="center" label="商业名称" prop="ename"></el-table-column>
          <el-table-column min-width="100" align="center" label="商品名称" prop="goodsName"></el-table-column>
          <el-table-column min-width="100" align="center" label="商品内码" prop="goodsInSn"></el-table-column>
          <el-table-column min-width="100" align="center" label="商品规格" prop="goodsSpecifications"></el-table-column>
          <el-table-column min-width="100" align="center" label="生产厂家" prop="goodsManufacturer"></el-table-column>
          <el-table-column min-width="100" align="center" label="商品关系标签" prop="goodsRelationLabel">
            <template slot-scope="{ row }">
              <div>
                {{ row.goodsRelationLabel | dictLabel(flowGoodsRelationLabel) }}
              </div>
            </template>
          </el-table-column>
          <el-table-column min-width="100" align="center" label="以岭商品ID" prop="ylGoodsId"></el-table-column>
          <el-table-column min-width="100" align="center" label="以岭品名称" prop="ylGoodsName"></el-table-column>
          <el-table-column min-width="100" align="center" label="以岭品规格" prop="ylGoodsSpecifications"></el-table-column>
          <el-table-column min-width="100" align="center" label="操作时间" prop="opTime">
            <template slot-scope="{ row }">
              <div>
                {{ row.opTime | formatDate }}
              </div>
            </template>
          </el-table-column>
          <el-table-column min-width="100" align="center" label="操作人" prop="opUserName"></el-table-column>
          <el-table-column align="center" label="操作">
            <template slot-scope="{ row }">
              <yl-button type="text" @click="edit(row)">修改</yl-button>
            </template>
          </el-table-column>
        </yl-table>
      </div>
      <div class="bottom-view flex-row-center">
        <yl-button type="primary" plain @click="goBack">返回</yl-button>
      </div>
      <!-- 添加商品 -->
      <yl-dialog :visible.sync="addShow" width="1152px" title="商家品与以岭品" @confirm="confirm">
        <div class="dialog-content">
          <el-form class="pad-t-8" ref="form" :rules="formRules" :model="form" label-width="120px">
            <el-row>
              <el-col :span="12" :offset="0">
                <el-form-item label="商业名称：" prop="bussinessName">
                  <el-input v-model="form.bussinessName" disabled></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12" :offset="0">
                <el-form-item label="商业ID：" prop="bussinessId">
                  <el-input v-model="form.bussinessId" disabled></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12" :offset="0">
                <el-form-item label="商品名称：" prop="goodsName">
                  <el-input v-model="form.goodsName" disabled></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12" :offset="0">
                <el-form-item label="商品规格：" prop="goodsSpecifications">
                  <el-input v-model="form.goodsSpecifications" placeholder="请输入内容" disabled></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12" :offset="0">
                <el-form-item label="商品内码：" prop="goodsInSn">
                  <el-input v-model="form.goodsInSn" placeholder="请输入内容" disabled></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12" :offset="0">
                <el-form-item label="商品关系标签：" prop="goodsRelationLabel">
                  <el-select v-model="form.goodsRelationLabel" clearable placeholder="请选择商品关系">
                    <el-option
                      v-for="item in flowGoodsRelationLabel"
                      v-show="item.value !== 0"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12" :offset="0">
                <el-form-item label="以岭品名称：" prop="ylGoodsName">
                  <el-select v-model="form.ylGoodsName" filterable remote reserve-keyword placeholder="请输入关键词" :remote-method="remoteMethod" :loading="goodsloading" @change="selectGoods" clearable @clear="clear">
                    <el-option v-for="item in options" :key="item.id" :label="item.name" :value="item.id">
                      <span>{{ item.name }}---{{ item.sellSpecifications }}</span>
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12" :offset="0">
                <el-form-item label="以岭品ID：" prop="ylGoodsId">
                  <el-input v-model="form.ylGoodsId" placeholder="请输入内容" disabled></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12" :offset="0">
                <el-form-item label="以岭品规格：" prop="ylGoodsSpecifications">
                  <el-input v-model="form.ylGoodsSpecifications" placeholder="请输入内容" disabled></el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </div>
      </yl-dialog>
      <!-- 导入 -->
      <import-send-dialog :visible.sync="importFlowGoodsVisible" :excel-code="info.excelCode" ref="importFlowGoodsRef"></import-send-dialog>
    </div>
  </div>
</template>

<script>
import {
  getBussinessYLCorrespondingList,
  editBussinessYLCorrespondingList,
  getYLGoodsList,
  queryEnterpriseList
} from '@/subject/admin/api/zt_api/dataReport'
import { createDownLoad } from '@/subject/admin/api/common'
import { flowGoodsRelationLabel } from '@/subject/admin/busi/zt/dataReport'
import ImportSendDialog from '@/subject/admin/components/ImportSendDialog'

export default {
  name: 'ZtParamsBussinessAndYLCorresponding',
  components: {
    ImportSendDialog
  },
  computed: {
    // 商品关系标签
    flowGoodsRelationLabel() {
      const goodsRelationLabel = flowGoodsRelationLabel()
      goodsRelationLabel.forEach(item => {
        item.value = Number(item.value)
      })
      return goodsRelationLabel
    }
  },
  data() {
    return {
      query: {
        total: 0,
        page: 1,
        limit: 10,
        eid: '',
        updateUser: '',
        ylGoodsName: '',
        relationFlag: '',
        time: [],
        // 商品名称
        goodsName: '',
        // 生产厂家
        goodsManufacturer: '',
        // 商品关系标签列表
        goodsRelationLabelList: []
      },
      loading: false,
      dataList: [],
      addShow: false,
      form: {
        id: '',
        bussinessName: '',
        bussinessId: '',
        goodsName: '',
        goodsInSn: '',
        goodsSpecifications: '',
        ylGoodsId: '',
        ylGoodsName: '',
        ylGoodsSpecifications: '',
        goodsRelationLabel: ''
      },
      formRules: {
        goodsRelationLabel: [{ required: true, message: '请选择商品关系', trigger: 'change' }]
      },
      goodsloading: false,
      options: [],
      searchLoading: false,
      sellerEnameOptions: [],
      // 导入弹窗
      importFlowGoodsVisible: false,
      // 导入Code
      info: {
        excelCode: 'importFlowGoodsRelation'
      }
    };
  },
  mounted() {
    this.getList();
  },
  methods: {
    async getList() {
      let query = this.query;
      this.loading = true;
      let data = await getBussinessYLCorrespondingList(
        query.page,
        query.limit,
        query.eid,
        query.updateUser,
        query.relationFlag,
        query.ylGoodsName,
        query.time && query.time.length > 0 ? query.time[0] : undefined,
        query.time && query.time.length > 1 ? query.time[1] : undefined,
        query.goodsName,
        query.goodsManufacturer,
        query.goodsRelationLabelList
      );
      this.loading = false;
      if (data != undefined) {
        this.dataList = data.records;
        this.query.total = data.total;
      }
    },
    handleSearch() {
      this.query.page = 1;
      this.getList();
    },
    handleReset() {
      this.query = {
        page: 1,
        limit: 10,
        eid: '',
        updateUser: '',
        ylGoodsName: '',
        relationFlag: '',
        time: [],
        goodsName: '',
        goodsManufacturer: '',
        goodsRelationLabelList: []
      };
    },
    // 编辑
    edit(row) {
      this.form.id = row.id
      this.form.bussinessName = row.ename
      this.form.bussinessId = row.eid
      this.form.goodsName = row.goodsName
      this.form.goodsInSn = row.goodsInSn
      this.form.goodsSpecifications = row.goodsSpecifications
      this.form.ylGoodsId = row.ylGoodsId
      this.form.ylGoodsName = row.ylGoodsName
      this.form.ylGoodsSpecifications = row.ylGoodsSpecifications
      this.form.goodsRelationLabel = row.goodsRelationLabel
      this.addShow = true
    },
    // 清空(解绑以岭品)
    clear() {
      this.form.ylGoodsId = ''
      this.form.ylGoodsName = ''
      this.form.ylGoodsSpecifications = ''
    },
    // 添加商品
    confirm() {
      this.$refs['form'].validate(async (valid) => {
        if (valid) {
          if (this.form.goodsRelationLabel === 1 && (!this.form.ylGoodsName || !this.form.ylGoodsId)) {
            this.$common.warn('商品关系标签为以岭品时,以岭品名称和ID必填!')
            return false
          }
          this.$common.showLoad();
          let data = await editBussinessYLCorrespondingList(
            this.form.id,
            this.form.ylGoodsId,
            this.form.ylGoodsName,
            this.form.ylGoodsSpecifications,
            this.form.goodsRelationLabel
          );
          this.$common.hideLoad();
          if (data !== undefined) {
            this.$common.success('编辑成功');
            this.addShow = false;
            this.getList();
          }
        } else {
          console.log('error submit!!');
          // this.$common.error('请检查输入内容是否符合规范')
          return false;
        }
      });
    },
    // 远程搜索商品
    async remoteMethod(query) {
      if (query !== '') {
        this.goodsloading = true;
        let data = await getYLGoodsList(query);
        console.log(data);
        this.goodsloading = false;
        if (data !== undefined) {
          this.options = data.list;
        }
      } else {
        this.options = [];
      }
    },
    // 选择商品
    selectGoods(id) {
      if (id) {
        let item = this.options.find(function (obj) { return obj.id === id })
        this.form.ylGoodsId = item.id;
        this.form.ylGoodsName = item.name;
        this.form.ylGoodsSpecifications = item.sellSpecifications;
      }
    },
    async remoteSearchEnterprise(query) {
      if (query.trim() != '') {
        this.searchLoading = true
        let data = await queryEnterpriseList( 1, 10, query.trim() )
        this.searchLoading = false
        if (data) {
          this.sellerEnameOptions = data.records
        }
      } else {
        this.sellerEnameOptions = []
      }
    },
    clearEnterprise() {
      this.sellerEnameOptions = []
    },
    blurEnterpriseInput(e) {
      if (e.target.value == '') {
        this.sellerEnameOptions = []
      }
    },
    // 导入
    importClick() {
      this.importFlowGoodsVisible = true
      this.$nextTick( () => {
        this.$refs.importFlowGoodsRef.init()
      })
    },
    // 导出
    async downLoadTemp() {
      let query = this.query
      this.$common.showLoad()
      let data = await createDownLoad({
        className: 'flowGoodsRelationExportService',
        fileName: '商家品与以岭品对应关系导出',
        groupName: '商家品与以岭品对应关系导出',
        menuName: '数据报表-报表参数-商家品与以岭品对应关系',
        searchConditionList: [
          {
            desc: '商业名称',
            name: 'eid',
            value: query.eid || ''
          },
          {
            desc: '操作开始时间',
            name: 'createTimeStart',
            value: query.time && query.time.length ? query.time[0] : ''
          },
          {
            desc: '操作结束时间',
            name: 'createTimeEnd',
            value: query.time && query.time.length > 1 ? query.time[1] : ''
          },
          {
            desc: '以岭品名称',
            name: 'ylGoodsName',
            value: query.ylGoodsName || ''
          },
          {
            desc: '操作人',
            name: 'opUserName',
            value: query.updateUser || ''
          },
          {
            desc: '有无商品关系',
            name: 'relationFlag',
            value: query.relationFlag
          },
          {
            desc: '商品名称',
            name: 'goodsName',
            value: query.goodsName
          },
          {
            desc: '生产厂家',
            name: 'goodsManufacturer',
            value: query.goodsManufacturer
          },
          {
            desc: '商品关系标签',
            name: 'goodsRelationLabelStr',
            value: query.goodsRelationLabelList.join(',')
          }
        ]
      })
      this.$common.hideLoad()
      if (typeof data !== 'undefined') {
        this.$common.n_success('创建下载任务成功，请在下载中心查看')
      }
    },
    // 返回
    goBack() {
      this.$router.push({
        name: 'ZtDataReportList'
      })
    }
  }
};
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
