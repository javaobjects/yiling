<template>
  <div>
    <yl-dialog
      width="900px"
      title="选择活动"
      :show-footer="false"
      :visible.sync="showDialog"
      @close="close">
      <div class="pop-up">
        <!-- 搜索 -->
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">活动形式</div>
              <el-select class="select-width" v-model="popUp.type" placeholder="请选择">
                <el-option
                  v-for="item in typeData"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="8">
              <div class="title">活动名称</div>
              <el-input v-model="popUp.activityName" @keyup.enter.native="searchEnter" placeholder="请输入标题" />
            </el-col>
            <el-col :span="8">
              <div class="title">活动进度</div>
              <el-select class="select-width" v-model="popUp.progress" placeholder="请选择">
                <el-option label="全部" value=""></el-option>
                <el-option
                  v-for="item in integralRuleProgressType"
                  v-show="item.value != 3"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="16">
              <yl-search-btn :total="popUp.total" @search="handleSearch" @reset="handleReset" />
            </el-col>
          </el-row>
        </div>
        <div class="table-conter">
          <yl-table
            border
            show-header
            :list="dialogData"
            :total="popUp.total"
            :page.sync="popUp.page"
            :limit.sync="popUp.limit"
            :loading="loading"
            @getList="getList">
            <el-table-column align="center" min-width="90" label="活动ID" prop="id"></el-table-column>
            <el-table-column align="center" min-width="180" label="活动名称" prop="activityName"></el-table-column>
            <el-table-column align="center" min-width="90" label="活动进度">
              <template slot-scope="{ row }">
                {{ row.progress | dictLabel(integralRuleProgressType) }}
              </template>
            </el-table-column>
            <el-table-column align="center" min-width="110" label="活动开始时间">
              <template slot-scope="{ row }">
                {{ row.startTime | formatDate }}
              </template>
            </el-table-column>
            <el-table-column fixed="right" align="center" label="操作" min-width="100">
              <template slot-scope="{ row }">
                <yl-button type="text" @click="seeClick(row)">
                  <span :style="{color: row.statusType == 1 ? '#1790ff' : '#c8c9cc'}">
                    {{ row.statusType == '1' ? '选择' : '已添加' }}
                  </span>
                </yl-button>
              </template>
            </el-table-column>
          </yl-table>
        </div>
      </div>
    </yl-dialog>
  </div>
</template>
<script>
import { lotteryActivityPage } from '@/subject/admin/api/b2b_api/consume_manage'
import { integralRuleProgress } from '@/subject/admin/busi/b2b/integral'
export default {
  props: {
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
    },
    integralRuleProgressType() {
      return integralRuleProgress()
    }
  },
  data() {
    return {
      loading: false,
      popUp: {
        type: 1,
        // 平台类型：1-B端 2-C端
        platformType: 1,
        activityName: '',
        progress: '',
        //不等于该活动进度：1-未开始 2-进行中 3-已结束
        neProgress: 3,
        total: 0,
        page: 1,
        limit: 10
      },
      dialogData: [],
      typeData: [
        {
          value: 1,
          label: '抽奖活动'
        }
        // {
        //   value: 2,
        //   label: '患教活动'
        // },
        // {
        //   value: 3,
        //   label: '义诊活动'
        // }
      ]
    }
  },
  mounted() {
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
    //点击确定
    confirm() {
      this.$emit('addLuckDraw', this.seeData)
    },
    //点击顶部关闭
    close() {
      this.$emit('close')
    },
    handleSearch() {
      this.popUp.page = 1;
      this.getList()
    },
    handleReset() {
      this.popUp = {
        type: 1,
        // 平台类型：1-B端 2-C端
        platformType: 1,
        activityName: '',
        progress: '',
        neProgress: 3,
        total: 0,
        page: 1,
        limit: 10
      }
    },
    async getList() {
      this.loading = true;
      let dialog = this.popUp;
      this.dialogData = [];
      let data = await lotteryActivityPage(
        dialog.platformType,
        dialog.activityName,
        dialog.progress,
        dialog.neProgress,
        dialog.page,
        dialog.limit
      )
      if (data) {
        for (let i = 0; i < data.records.length; i++) {
          this.dialogData.push({
            ...data.records[i],
            statusType: 1
          })
        }
        this.popUp.total = data.total;
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
      if (row.statusType == 1) {
        row.statusType = 2;
        this.$emit('addLuckDraw', row)
      }
    }
  }
}
</script>
<style lang="scss" scoped>
@import './index.scss';
</style>