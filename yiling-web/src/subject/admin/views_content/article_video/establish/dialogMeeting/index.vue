<template>
  <div>
    <yl-dialog
      width="900px"
      :title="dialogTitle"
      :show-footer="false"
      :visible.sync="showDialog">
      <div class="pop-up">
        <div class="common-box">
          <div class="search-box">
            <el-row class="box">
              <el-col :span="8">
                <div class="title">会议名称</div>
                <el-input v-model="dialog.title" placeholder="请输入会议名称"/>
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
                      <div class="content-left-title">{{ row.title }}</div>
                      <div class="item" style="font-size:14px;font-weight: normal;">
                        <span class="font-title-color">活动ID：</span>
                        {{ row.id }}
                      </div>
                      <div class="item" style="font-size:14px;font-weight: normal;">
                        <span class="font-title-color">活动时间段：</span>
                        {{ row.activityStartTime | formatDate }} 至 {{ row.activityEndTime | formatDate }}
                      </div>
                      <div class="item" style="font-size:14px;font-weight: normal">
                        <span class="font-title-color">活动创建时间：</span>
                        {{ row.createTime | formatDate }}
                      </div>
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
  </div>
</template>
<script>
import { queryMeetingList } from '@/subject/admin/api/content_api/article_video'
export default {
  props: {
    // 弹窗的标题
    dialogTitle: {
      type: String,
      default: '关联会议'
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
      pageSizeList: [6]
    }
  },
  mounted() {
    this.getList();
  },
  methods: {
    async getList() {
      this.loading = true;
      let dialog = this.dialog;
      this.dialogData = [];
      let data = await queryMeetingList(
        dialog.title,
        dialog.page,
        dialog.limit
      )
      if (data) {
        for (let i = 0; i < data.records.length; i ++) {
          this.dialogData.push({
            ...data.records[i],
            statusType: 1
          })
        }
        this.dialog.total = data.total;
        if (this.dataList && this.dataList.length > 0) {
          for (let l = 0; l < this.dialogData.length; l ++) {
            for (let y = 0; y < this.dataList.length; y ++) {
              if (this.dialogData[l].id == this.dataList[y].id) {
                this.dialogData[l].statusType = 2;
                break;
              } else {
                this.dialogData[l].statusType = 1;
              }
            }
          }
        }
      }
      this.loading = false;
    },
    seeClick(row) {
      this.$emit('addMeeting', row)
    },
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