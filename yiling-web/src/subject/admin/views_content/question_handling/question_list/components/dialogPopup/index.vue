<template>
  <div>
    <yl-dialog
      width="900px"
      :title="dialogTitle"
      :visible.sync="showDialog"
      @confirm="determine"
      :show-cancle="false"
      >
      <div class="pop-up">
        <div class="common-box">
          <div class="search-box">
            <el-row class="box">
              <el-col :span="8">
                <div class="title">{{ dialogType == 1 ? '文献名称' : (dialogType == 2 ? '药品名称' : '') }}</div>
                <el-input v-model="dialog.title" :placeholder="dialogType == 1 ? '请输入文献名称' : (dialogType == 2 ? '请输入药品名称' : '')"/>
              </el-col>
            </el-row>
          </div>
          <div class="search-box mar-t-16">
            <el-row class="box">
              <el-col :span="16">
                <yl-search-btn
                  :total="dialog.total" 
                  @search="handleSearch" 
                  @reset="handleReset"/>
              </el-col>
            </el-row>
          </div>
        </div>
        <div>
          <yl-table
          border 
          :list="dialogData"
          :total="dialog.total"
          :page.sync="dialog.page"
          :limit.sync="dialog.limit"
          :loading="loading"
          :horizontal-border="false"
          :cell-no-pad="true"
          :page-size-list="pageSizeList"
          @getList="getList">
            <el-table-column>
              <template slot-scope="{ row, $index }">
                <div class="table-view" :key="$index">
                  <div class="content flex-row-left">
                    <div class="content-left">
                      <div class="content-left-title">
                        {{ dialogType == 1 ? row.title : row.name }}
                      </div>
                      <div class="item" style="font-size:14px;font-weight: normal;">
                        <span class="font-title-color">{{ dialogType == 1 ? '作者：' : '批准文号：' }}</span>{{ dialogType == 1 ? row.author : row.licenseNo }}</div>
                    </div>
                    <div class="content-center-1 font-size-base font-important-color flex1">
                    </div>
                    <div class="content-right flex-column-center table-button">
                      <yl-button type="text" @click="seeClick(row)">
                        <span :style="{color: row.statusType == 1 ? '#1790ff' : '#c8c9cc'}">
                          {{ row.statusType == '1' ? '选择' : '已添加' }}
                        </span>
                      </yl-button>
                    </div>
                  </div>
                </div>
              </template>
            </el-table-column>
          </yl-table>
        </div>
      </div>
    </yl-dialog>
    <!-- <yl-dialog
      width="820px"
      :title="specificationsTitle" 
      :show-footer="true"
      :visible.sync="showSpecifications" 
      @confirm="confirm">
      <div class="dialog-specifications">
        <div class="title">请选择药品规格</div>
        <el-checkbox-group v-model="checkboxData">
          <el-checkbox
            class="dialog-checkbox" 
            v-for="(item,index) in specificationData" 
            :key="index" 
            :label="item">
            {{ item.sellSpecifications }}
          </el-checkbox>
        </el-checkbox-group>
      </div>
    </yl-dialog> -->
  </div>
</template>
<script>
import { standardGoods } from '@/subject/admin/api/content_api/question_handling'
import { queryContentPage } from '@/subject/admin/api/content_api/literature'
export default {
  props: {
    // 弹窗的标题
    dialogTitle: {
      type: String,
      default: ''
    },
    // 是否显示 弹窗
    show: {
      type: Boolean,
      default: true
    },
    // 从父级传递过来的 数据
    dataList: {
      type: Array,
      default: () => []
    },
    // 弹窗类型 1 文献资料 2 关联药品 
    dialogType: {
      type: Number,
      default: 0
    }
  },
  computed: {
    showDialog: {
      get() {
        return this.show
      },
      set(val) {
        this.$emit('update:show', val)
      }
    }
  },
  data() {
    return {
      dialogData: [],
      loading: false,
      dialog: {
        title: '',
        total: 0,
        page: 1,
        limit: 6
      },
       //药品规格弹窗
      showSpecifications: false,
      specificationData: [],
      //选中的药品规格数据
      checkboxData: [], 
      //规格弹窗 显示顶部标题
      specificationsTitle: '', 
      pageSizeList: [6]
    }
  },
  mounted() {
    this.getList();
  },
  methods: {
    //点击弹窗确定
    determine() {
      this.$emit('determine')
    },
    async drugsApi() {
      let dialog = this.dialog;
      this.loading = true;
      let data = await standardGoods(
        dialog.title,
        dialog.page,
        dialog.limit
      )
      if (data !== undefined) {
        for (let i = 0; i < data.records.length; i++) {
          this.dialogData.push({
            ...data.records[i],
            statusType: 1
          })
        }
        if (this.dataList && this.dataList.length > 0) {
          for (let l = 0; l < this.dialogData.length; l++) {
            for (let y = 0; y < this.dataList.length; y++) {
              if (this.dialogData[l].id == this.dataList[y].standardId) {
                this.dialogData[l].statusType = 2
                break;
              } else {
                this.dialogData[l].statusType = 1
              }
            }
          }
        }
        this.dialog.total = data.total;
      }
      this.loading = false;
    },
    // 获取文献资料数据
    async literatureApi() {
      let dialog = this.dialog;
      this.loading = true;
      let data = await queryContentPage(
        dialog.page,
        '',
        '',
        dialog.title,
        dialog.limit,
        '2'
      )
      if (data !== undefined) {
        for (let i = 0; i < data.records.length; i++) {
          this.dialogData.push({
            ...data.records[i],
            statusType: 1
          })
        }
        this.dialog.total = data.total;
        if (this.dataList && this.dataList.length > 0) {
          for (let l = 0; l < this.dialogData.length; l++) {
            for (let y = 0; y < this.dataList.length; y++) {
              if (this.dialogData[l].id == this.dataList[y].id) {
                this.dialogData[l].statusType = 2
                break;
              } else {
                this.dialogData[l].statusType = 1
              }
            }
          }
        }
      }
      this.loading = false;
    },
    getList() {
      this.dialogData = [];
      if (this.dialogType == 1) {
        //获取文献资料数据
        this.literatureApi(); 
      } else {
        //获取药品数据
        this.drugsApi(); 
      }
    },
    seeClick(row) {
      // if (this.dialogType == 2) {
      //   this.showSpecifications = true;
      //   //获取商品规格 数据
      //   this.specificationsApi(row); 
      // } else {
      if (row.statusType == 1) {
        this.$emit('addCommodity', row)
      }
      row.statusType = 2;
      // }
    },
    // 获取商品规格 数据
    // async specificationsApi(val) {
    //   this.specificationsTitle = val.name;
    //   this.$common.showLoad();
    //   let data = await specification(val.id)
    //   this.$common.hideLoad();
    //   if (data !== undefined) {
    //     this.specificationData = data;
    //     if (this.dataList && this.dataList.length > 0) {
    //       for (let l = 0; l < this.specificationData.length; l++) {
    //         for (let y = 0; y < this.dataList.length; y++) {
    //           if (this.specificationData[l].id == this.dataList[y].sellSpecificationsId || this.specificationData[l].id == this.dataList[y].id) {
    //             this.checkboxData.push(this.specificationData[l])
    //           }
    //         }
    //       }
    //     }
    //   }
    // },
    // 点击规格弹窗的确定
    // confirm() {
    //   if (this.checkboxData && this.checkboxData.length > 0) {
    //     this.showSpecifications = false;
    //     this.$emit('addCommodity', this.checkboxData);
    //   } else {
    //     this.$common.warn('请选择药品规格')
    //   }
    // },
    // 搜索
    handleSearch() {
      this.dialog.page = 1;
      this.getList();
    },
    // 重置
    handleReset() {
      this.dialog = {
        title: '',
        total: 0,
        page: 1,
        limit: 6
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>