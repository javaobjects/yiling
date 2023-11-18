<template>
  <div class="app-container">
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">商品名称</div>
              <el-input v-model="query.name" @keyup.enter.native="searchEnter" placeholder="商品名称" />
            </el-col>
            <el-col :span="6">
              <div class="title">批准文号/注册证编号</div>
              <el-input v-model="query.licenseNo" @keyup.enter.native="searchEnter" placeholder="请填写唯一编号" />
            </el-col>
            <el-col :span="6">
              <div class="title">生产厂家</div>
              <el-input v-model="query.manufacturer" @keyup.enter.native="searchEnter" placeholder="生产厂家" />
            </el-col>
            <el-col :span="6">
              <div class="title">生产地址</div>
              <el-input v-model="query.manufacturerAddress" @keyup.enter.native="searchEnter" placeholder="生产地址" />
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">一级分类</div>
              <el-select v-model="query.standardCategoryId1" @change="selectChange" placeholder="请选择一级分类">
                <el-option label="全部" :value="0"></el-option>
                <el-option v-for="item in category" :key="item.id" :label="item.name" :value="item.id">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="6">
              <div class="title">二级分类</div>
              <el-select v-model="query.standardCategoryId2" placeholder="请选择二级分类">
                <el-option label="全部" :value="0"></el-option>
                <el-option 
                  v-for="item in cateChild" 
                  :key="item.id" 
                  :label="item.name" 
                  :value="item.id">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="6">
              <div class="title">管制类型</div>
              <el-select v-model="query.controlType" placeholder="管制类型">
                <el-option label="全部" :value="0"></el-option>
                <el-option 
                  v-for="item in standardGoodsControlType" 
                  :key="item.value" 
                  :label="item.label" 
                  :value="item.value"></el-option>
              </el-select>
            </el-col>
            <el-col :span="6">
              <div class="title">特殊成分</div>
              <el-select v-model="query.specialComposition" placeholder="特殊成分">
                <el-option label="全部" :value="0"></el-option>
                <el-option 
                  v-for="item in standardGoodsSpecialComposition" 
                  :key="item.value" 
                  :label="item.label" 
                  :value="item.value"></el-option>
              </el-select>
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">录入类型</div>
              <el-select v-model="query.goodsType" placeholder="录入类型">
                <el-option label="全部" :value="0"></el-option>
                <el-option 
                  v-for="item in standardGoodsType" 
                  :key="item.value" 
                  :label="item.label" 
                  :value="item.value"></el-option>
              </el-select>
            </el-col>
            <el-col :span="6">
              <div class="title">是否医保</div>
              <el-select v-model="query.isYb" placeholder="是否医保">
                <el-option label="全部" :value="0"></el-option>
                <el-option 
                  v-for="item in standardGoodsIsYb" 
                  :key="item.value" 
                  :label="item.label" 
                  :value="item.value"></el-option>
              </el-select>
            </el-col>
            <el-col :span="6">
              <div class="title">处方类型</div>
              <el-select v-model="query.otcType" placeholder="处方类型">
                <el-option label="全部" :value="0"></el-option>
                <el-option 
                  v-for="item in standardGoodsOtcType" 
                  :key="item.value" 
                  :label="item.label" 
                  :value="item.value"></el-option>
              </el-select>
            </el-col>
            <el-col :span="6">
              <div class="title">有无图片</div>
              <el-select v-model="query.pictureFlag" placeholder="有无图片">
                <el-option 
                  v-for="item in pictureFlagList" 
                  :key="item.value" 
                  :label="item.label" 
                  :value="item.value"></el-option>
              </el-select>
            </el-col>
          </el-row>
          <div class="search-box mar-t-16">
            <el-row class="box company-checkbox-group">
              <el-col :span="6">
                <div class="title">标准库ID</div>
                <el-input v-model="query.standardId" @keyup.enter.native="searchEnter" placeholder="标准库ID" />
              </el-col>
              <el-col :span="18">
                <div class="title">标签</div>
                <div class="checkbox-group">
                  <el-checkbox-group v-model="query.tagIds" size="mini">
                    <el-checkbox 
                      border 
                      v-for="(itm, idx) in labelData" 
                      :key="idx" 
                      :label="itm.id">
                      {{ itm.name }}
                    </el-checkbox>
                  </el-checkbox-group>
                </div>
              </el-col>
            </el-row>
          </div>
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
          <yl-button class="mar-r-10 mar-b-8" type="primary" @click="add">新建</yl-button>
        </div>
        <div class="btn">
          <el-link 
            class="mar-r-10" 
            type="primary" 
            :underline="false"
            :data-url="'NO_4' | template"
            data-name="国标药品导入模板"
            @click="downloadClick"
            >下载模板</el-link>
          <el-link 
            class="mar-r-10" 
            type="primary" 
            :underline="false"
            @click="goImport"
          >导入模版</el-link>
        </div>
      </div>

      <div>
        <yl-table 
          border 
          :show-header="true" 
          :list="dataList" 
          :total="query.total" 
          :page.sync="query.page" 
          :limit.sync="query.limit" 
          :loading="loading" 
          @getList="getList">
          <el-table-column align="center" label="标准库ID" prop="id"></el-table-column>
          <el-table-column 
            align="center" 
            min-width="200" 
            label="商品名称" 
            prop="name"></el-table-column>
          <el-table-column 
            align="center" 
            min-width="200" 
            label="批准文号/生产许可证号/备案凭证编号/注册证编号" 
            prop="licenseNo"></el-table-column>
          <el-table-column 
            align="center" 
            min-width="260" 
            label="生产厂家" 
            prop="manufacturer"></el-table-column>
          <el-table-column 
            align="center" 
            min-width="260" 
            label="生产地址" 
            prop="manufacturerAddress"></el-table-column>
          <el-table-column 
            align="center" 
            min-width="200" 
            label="分类" 
            prop="standardCategoryName"></el-table-column>
          <el-table-column align="center" min-width="100" label="包含规格">
            <template slot-scope="{ row }">
              <div>
                <yl-button type="text" @click="clickListCount(row)"> {{ row.specificationsCount }}</yl-button>
              </div>
            </template>

          </el-table-column>
          <el-table-column align="left" min-width="260" label="最后操作信息">
            <template slot-scope="{ row }">
              <div>
                <div>最后操作人：{{ row.userName }}</div>
                <div>最后操作时间：{{ row.updateTime | formatDate }}</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column fixed="right" align="center" label="操作" width="120">
            <template slot-scope="{ row }">
              <div>
                <yl-button type="text" @click="edit(row)">编辑</yl-button>
              </div>
              <div>
                <yl-button type="text" @click="editLabel(row)">编辑标签</yl-button>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>

      <!-- 弹窗 -->
      <yl-dialog 
        :visible.sync="show" 
        title="包装规格" 
        @confirm="confirm" 
        :show-cancle="false">
        <div class="dialog-content">
          <div class="normls-title">
            <span>标准库ID:</span>
            <span>{{ showId }}</span>
            <span>商品名称：</span>
            <span>{{ showName }}</span>
          </div>
          <yl-table :list="specificationsList">
            <el-table-column align="center">
              <template slot-scope="{ row }">
                <div>
                  <span>规格名称 {{ row.sellSpecifications }}</span>
                </div>
              </template>
            </el-table-column>
            <el-table-column align="center">
              <template slot-scope="{ row }">
                <div>
                  <span>单位 {{ row.unit }}</span>
                </div>
              </template>
            </el-table-column>

          </yl-table>
        </div>
      </yl-dialog>
      <!-- 编辑标签弹窗 -->
      <yl-dialog 
        title="编辑标签" 
        @confirm="confirmLable" 
        width="570px" 
        :visible.sync="showDialog">
        <div class="period-form company-checkbox-group">
          <el-checkbox-group v-model="editLabelList" size="mini">
            <el-checkbox 
              border 
              v-for="(itm, idx) in labelData" 
              :key="idx" 
              :label="itm.id">
              {{ itm.name }}
            </el-checkbox>
          </el-checkbox-group>
      </div>
    </yl-dialog>
    </div>
  </div>
</template>

<script>
import { paymentMethod, standardGoodsControlType, standardGoodsSpecialComposition, standardGoodsType, standardGoodsIsYb, standardGoodsOtcType } from '@/subject/admin/utils/busi'
import { getStandardProductsList, getGoodsSpecificationById, getProductCategory, listByEid, saveEnterpriseTags, options } from '@/subject/admin/api/quality'
export default {
  name: 'BasicDrugsIndex',
  computed: {
    // 支付方式
    payType() {
      return paymentMethod()
    },
    // 管制类型
    standardGoodsControlType() {
      return standardGoodsControlType()
    },
    // 特殊成分
    standardGoodsSpecialComposition() {
      return standardGoodsSpecialComposition()
    },
    // 录入类型， 药品类型
    standardGoodsType() {
      return standardGoodsType()
    },
    // 是否医保
    standardGoodsIsYb() {
      return standardGoodsIsYb()
    },
    // 处方类型
    standardGoodsOtcType() {
      return standardGoodsOtcType()
    }
  },
  data() {
    return {
      query: {
        page: 1,
        limit: 20,
        total: 0,
        controlType: 0,
        goodsType: 0,
        isYb: 0,
        otcType: 0,
        pictureFlag: 0,
        specialComposition: 0,
        standardId: '',
        tagIds: []
      },
      // 一级分类
      category: [],
      // 二级分类
      cateChild: [],
      //  有无图片：1-有 2-无	 全部不传
      pictureFlagList: [
        { value: 0, label: '全部' },
        { value: 1, label: '有' },
        { value: 2, label: '无' }
      ],
      dataList: [],
      loading: false,
      show: false,
      showId: '',
      showName: '',
      // 商品规格 弹窗数据
      specificationsList: [],
      labelData: [],
      //编辑标签弹窗
      showDialog: false, 
      //存储编辑标签 与企业建立关联关系
      editLabelList: [], 
      //缓存 当前点击 编辑标签 的企业id
      qyId: ''
    }
  },
  activated() {
    this.getTables();
    this.getList()
    //获取一级分类
    this.getCate();
  },
  methods: {
    // Enter
    searchEnter(e) {
      const keyCode = window.event ? e.keyCode : e.which;
      if (keyCode === 13) {
        this.getList()
      }
    },
    // 获取基础药品列表
    async getList() {
      this.loading = true
      let query = this.query
      let data = await getStandardProductsList(
        query.page,
        query.limit,
        // 管制类型 管制类型：0-非管制 1-管制
        query.controlType,
        // 商品类别 商品类别：1-普通药品 2-中药饮片 3-中药材 4-消杀 5-保健食品 6-食品
        query.goodsType,
        // 是否医保：1-是 2-非 3-未采集到相关信息
        query.isYb,
        // 注册证号
        query.licenseNo,
        // 生产厂家
        query.manufacturer,
        // 生产地址
        query.manufacturerAddress,
        // 商品名称
        query.name,
        // 处方类型：1-处方药 2-甲类非处方药 3-乙类非处方药 4-其他
        query.otcType,
        // 有无图片：1-有 2-无
        query.pictureFlag,
        // 特殊成分：0-不含麻黄碱 1-含麻黄碱
        query.specialComposition,
        // 标准库一级分类id
        query.standardCategoryId1,
        // 标准库二级分类id
        query.standardCategoryId2,
        // 标准库规格id
        query.standardId,
        // 标签id
        query.tagIds
      )
      this.loading = false
      if (data) {
        this.dataList = data.records
        this.query.total = data.total
      }
    },
    handleSearch() {
      this.query.page = 1
      this.query.limit = 20
      this.getList()
    },
    handleReset() {
      this.query = Object.assign({}, {
        page: 1,
        limit: 20,
        controlType: 0,
        goodsType: 0,
        isYb: 0,
        otcType: 0,
        pictureFlag: 0,
        specialComposition: 0,
        standardId: '',
        tagIds: []
      })
    },
    add() {
      this.$router.push({
        name: 'BasicDrugsEdit',
        params: { id: 0, type: 'add' }
      })
    },
    // 编辑
    edit(row) {
      this.$router.push({
        name: 'BasicDrugsEdit',
        params: { id: row.id, type: 'edit' }
      })
    },
    goImport() {
      this.$router.push({
        path: '/importFile/importFile_index',
        query: {
          action: '/dataCenter/api/v1/standard/goods/import'
        }
      })
    },
    // 获取分类
    async getCate() {
      if (this.category.length === 0) {
        let { list } = await getProductCategory()
        this.category = list
        this.$log(list)
      }
    },
    // 选择分类大类
    selectChange(value) {
      this.$log(value)
      if (this.query.standardCategoryId2) {
        this.query.standardCategoryId2 = null
      }
      let data = this.category.find(item => item.id === value)
      if (data.children && data.children.length) {
        this.cateChild = data.children
      }
    },
    // 点击列表规格
    async clickListCount(row) {
      this.showId = row.id
      this.showName = row.name
      let data = await getGoodsSpecificationById(row.id)
      if (data && data.list) {
        this.specificationsList = data.list
        this.show = true
      }
    },
    // 关闭规格弹窗
    confirm() {
      this.show = false
    },
    async getTables() {
      let data = await options();
      this.labelData = data.list;
    },
    // 编辑标签
    async editLabel(row) {
      console.log('row', row);
      this.editLabelList = [];
      this.standardIdRow = row.id;
      this.showDialog = true;
      this.$common.showLoad();
      let data = await listByEid(row.id)
      this.$common.hideLoad();
      if (data !== undefined) {
        for (let i = 0; i < data.list.length; i++) {
          if (data.list[i].checked == true) {
            this.editLabelList.push(data.list[i].id);
          }
        }
      }
    },
    async confirmLable() {
      this.$common.showLoad();
      console.log('this.editLabelList', this.editLabelList);
      let data = await saveEnterpriseTags(this.standardIdRow, this.editLabelList)
      this.$common.hideLoad();
      if (data !== undefined) {
        this.$common.n_success('保存成功!');
        this.showDialog = false;
        this.getList();
      }
    },
    // 点击下载/预览文件
    downloadClick(e) {
      let url = e.currentTarget.getAttribute('data-url')
      let name = e.currentTarget.getAttribute('data-name')
      const xRequest = new XMLHttpRequest()
      xRequest.open('GET', url, true)
      xRequest.responseType = 'blob'
      xRequest.onload = () => {
        const url = window.URL.createObjectURL(xRequest.response)
        const a = document.createElement('a')
        a.href = url
      if (name) {
          a.download = name
      }
        a.click()
      }
      xRequest.send()
      return false
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
