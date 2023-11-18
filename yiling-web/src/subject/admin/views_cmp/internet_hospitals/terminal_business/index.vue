<template>
  <div class="app-container">
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">终端商家</div>
              <el-select v-model="query.eid" placeholder="请选择">
                <el-option
                  v-for="item in businessData"
                  :key="item.eid"
                  :label="item.ename"
                  :value="item.eid">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="8">
              <div class="title">状态</div>
              <el-select v-model="query.status" placeholder="请选择">
                <el-option label="全部" value=""></el-option>
                <el-option label="合作" value="1"></el-option>
                <el-option label="不合作" value="2"></el-option>
              </el-select>
            </el-col>
            <el-col :span="8">
              <div class="title">创建时间</div>
              <el-date-picker
                v-model="query.time"
                type="daterange"
                format="yyyy/MM/dd"
                value-format="yyyy-MM-dd"
                range-separator="-"
                start-placeholder="开始日期"
                end-placeholder="结束日期">
              </el-date-picker>
            </el-col>
          </el-row>
        </div>
        <div class="search-btn-box mar-t-16">
          <el-row class="box">
            <el-col :span="16">
              <yl-search-btn :total="query.total" @search="handleSearch" @reset="handleReset" />
            </el-col>
          </el-row>
        </div>
      </div>
      <div class="down-box">
        <div class="btn">
          <yl-button type="primary" @click="addClick">添加</yl-button>
        </div>
      </div>
      <!-- 下部列表 -->
      <div class="search-bar mar-t-8">
        <yl-table 
          border 
          :show-header="true" 
          :list="dataList" 
          :total="query.total" 
          :page.sync="query.page" 
          :limit.sync="query.limit" 
          :loading="loading" 
          @getList="getList">
          <el-table-column align="center" min-width="160" label="终端商家" prop="ename">
          </el-table-column>
          <el-table-column align="center" min-width="80" label="终端商家id" prop="eid">
          </el-table-column>
          <el-table-column align="center" min-width="160" label="关联IH配送商" prop="ihEname">
            <template slot-scope="{ row }">
              <div v-if="row.ihEname">{{ row.ihEname }}（{{ row.ihEid }}）</div>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="100" label="合作状态">
            <template slot-scope="{ row }">
              <div>{{ row.status == 1 ? '合作' : '不合作' }}</div>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="140" label="创建时间">
            <template slot-scope="{ row }">
              <div>{{ row.createTime | formatDate }}</div>
            </template>
          </el-table-column>
          <el-table-column fixed="right" align="center" label="操作" min-width="140">
            <template slot-scope="{ row }">
              <yl-button type="text" @click="statusClick(row)">{{ row.status == 1 ? '不合作' : '合作' }}</yl-button>
              <yl-button type="text" @click="publishClick(row)">问诊码</yl-button>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
    <yl-dialog 
      title="选择商家" 
      :show-footer="true"
      :show-cancle="false"
      width="800px"
      @close="close"
      @confirm="close"
      :visible.sync="showDialog">
      <div class="dialogTc">
        <div class="dialogTc-top"></div>
        <div class="common-box">
          <div class="search-box">
            <el-row class="box">
              <el-col :span="12">
                <div class="title">商家名称</div>
                <el-input v-model.trim="dialog.ename" @keyup.enter.native="searchEnter2" placeholder="请输入商家名称" />
              </el-col>
            </el-row>
          </div>
          <div class="search-box mar-t-16">
            <el-row class="box">
              <el-col :span="16">
                <yl-search-btn :total="dialog.total" @search="handleSearchDialog" @reset="handleResetDialog"/>
              </el-col>
            </el-row>
          </div>
        </div>
        <!-- 下部表格 -->
        <div class="search-bar">
          <yl-table 
            border 
            :show-header="true" 
            :list="dialogData" 
            :total="dialog.total" 
            :page.sync="dialog.page" 
            :limit.sync="dialog.limit" 
            :loading="dialogLoading" 
            @getList="getPopList">
            <el-table-column align="center" min-width="200" label="商家名称" prop="name">
            </el-table-column>
            <el-table-column align="center" min-width="120" label="许可证号" prop="licenseNumber"> 
            </el-table-column>
            <el-table-column fixed="right" align="center" label="操作" min-width="70">
              <template slot-scope="{ row }">
                <div>
                  <yl-button type="text" @click="addPopClick(row)">
                    添加
                  </yl-button>
                </div>
              </template>
            </el-table-column>
          </yl-table>
        </div>
      </div>
    </yl-dialog>
    <el-image-viewer v-if="showViewer" :on-close="onClose" :url-list="imageList"/>
  </div>
</template>
<script>
import { pageEnterprise } from '@/subject/admin/api/cmp_api/benefit_plan'
import { 
  pharmacyList, 
  pharmacyPageList, 
  savePharmacy,
  pharmacyQrCode,
  updatePharmacyStatus
} from '@/subject/admin/api/cmp_api/internet_hospitals'
import ElImageViewer from 'element-ui/packages/image/src/image-viewer';
export default {
  name: 'TerminalBusiness',
  computed: {},
  components: {
    ElImageViewer
  },
  data() {
    return {
      query: {
        eid: '',
        status: '',
        time: [],
        total: 0,
        page: 1,
        limit: 10
      },
      dataList: [],
      loading: false,
      businessData: [],
      // 商家弹窗loading
      dialogLoading: false,
      showDialog: false,
      dialog: {
        ename: '',
        page: 1,
        limit: 10,
        total: 0
      },
      // 商家弹窗数据
      dialogData: [],
      //图片放大弹窗
      showViewer: false,
      imageList: []
    }
  },
  activated() {
    //获取终端商家数据
    this.getBusiness();
    this.getList();
  },
  methods: {
    // Enter
    searchEnter(e) {
      const keyCode = window.event ? e.keyCode : e.which;
      if (keyCode === 13) {
        this.getList()
      }
    },
    searchEnter2(e) {
      const keyCode = window.event ? e.keyCode : e.which;
      if (keyCode === 13) {
        this.getPopList()
      }
    },
    async getBusiness() {
      let data = await pharmacyList()
      if (data) {
        this.businessData = data
      }
    },
    // 获取数据列表
    async getList() {
      let query = this.query;
      this.loading = true;
      let data = await pharmacyPageList(
        query.eid,
        query.status,
        query.page,
        query.limit
      )
      if (data) {
        this.dataList = data.records;
        this.query.total = data.total;
      }
      this.loading = false;
    },
    //点击添加
    addClick() {
      this.showDialog = true;
      this.getPopList();
    },
    // 获取商家数据
    async getPopList() {
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
        this.dialogData = data.records
        this.dialog.total = data.total
      }
      this.dialogLoading = false;
    },
    async addPopClick(row) {
      this.$common.showLoad();
      let data = await savePharmacy(
        row.id,
        row.name
      )
      this.$common.hideLoad();
      if (data !== undefined) {
        this.$common.n_success('添加成功！');
      }
    },
    //点击问诊码
    async publishClick(row) {
      this.imageList = [];
      this.$common.showLoad();
      let data = await pharmacyQrCode(row.id)
      this.$common.hideLoad();
      if (data !== undefined) {
        this.showViewer = true
        this.imageList.push(data)
      }
     
    },
    close() {
      this.showDialog = false;
      this.getList();
      this.getBusiness();
    },
    //点击合作或者不合作
    statusClick(row) {
      this.$confirm(`确定将终端商家 ${ row.ename } 与 关联IH配送商 ${ row.ihEname }的合作状态变更为 ${ row.status == 1 ? '不合作' : '合作' }！`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }) 
      .then( async() => {
        this.$common.showLoad();
        let data = await updatePharmacyStatus(
          row.id,
          row.status == 1 ? 2 : 1
        )
        this.$common.hideLoad();
        if (data !== undefined) {
          this.$common.n_success('操作成功！');
          this.getList()
        }
      })
      .catch(() => {
        //取消
      });
    },
    // 搜索
    handleSearch() {
      this.query.page = 1;
      this.getList();
    },
    // 重置
    handleReset() {
      this.query = {
        eid: '',
        status: '',
        time: [],
        total: 0,
        page: 1,
        limit: 10
      }
    },
    handleSearchDialog() {
      this.dialog.page = 1;
      this.getPopList();
    },
    handleResetDialog() {
      this.dialog = {
        ename: '',
        page: 1,
        limit: 10,
        total: 0
      }
    },
    onClose() {
      this.imageList = [];
      this.showViewer = false;
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
