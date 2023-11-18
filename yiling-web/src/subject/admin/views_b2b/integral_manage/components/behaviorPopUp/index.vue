<template>
  <div>
    <yl-dialog
      width="900px"
      title="选择行为"
      :show-footer="false"
      :visible.sync="showDialog"
      @close="close">
      <div class="pop-up">
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
            <el-table-column align="center" min-width="90" label="行为ID" prop="id"></el-table-column>
            <el-table-column align="center" min-width="120" label="适用平台">
              <template slot-scope="{ row }">
                <p v-for="(item, index) in row.platformList" :key="index">
                  {{ item | dictLabel(integralRuleUsePlatformType) }}
                </p>
              </template>
            </el-table-column>
            <el-table-column align="center" min-width="120" label="图标" v-if="type == 1">
              <template slot-scope="{ row }">
                <div class="imgUrl">
                  <el-image style="width: 100%; height: 100%" :src="row.icon"></el-image>
                </div>
              </template>
            </el-table-column>
            <el-table-column align="center" min-width="180" label="行为名称" prop="name"></el-table-column>
            <el-table-column align="center" min-width="220" label="行为描述" prop="description"></el-table-column>
            <el-table-column fixed="right" align="center" label="操作" min-width="100">
              <template slot-scope="{ row }">
                <yl-button type="text" v-if="dataType.type == 1" @click="seeClick(row)">
                  <span :style="{color: row.statusType == 1 ? '#1790ff' : '#c8c9cc'}">
                    {{ row.statusType == '1' ? '选择' : '已添加' }}
                  </span>
                </yl-button>
                <yl-button v-if="dataType.type == 2" type="text" @click="seeClick(row)" :disabled="row.id == 3 ? false : true">
                  <span :style="{color: row.id != 3 ? '#c8c9cc' : (row.statusType == 1 ? '#1790ff' : '#c8c9cc')}">
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
import { behaviorListPage } from '@/subject/admin/api/b2b_api/integral'
import { integralRuleUsePlatform } from '@/subject/admin/busi/b2b/integral'
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
    },
    dataType: {
      type: Object,
      default: () => {}
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
    integralRuleUsePlatformType() {
      return integralRuleUsePlatform()
    }
  },
  data() {
    return {
      loading: false,
      popUp: {
        total: 0,
        page: 1,
        limit: 10
      },
      dialogData: [],
      //行为类型：1-发放 2-消耗
      type: 0
    }
  },
  mounted() {
    let query = this.dataType;
    if (query.platform && query.type) {
      this.type = query.type;
      this.getList(query.platform);
    }
    
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
      this.$emit('addDoctor', this.seeData)
    },
    //点击顶部关闭
    close() {
      this.$emit('close')
    },
    async getList(platform) {
      this.loading = true;
      let dialog = this.popUp;
      this.dialogData = [];
      let data = await behaviorListPage(
        platform,
        this.type,
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
        this.$emit('addDoctor', row)
      }
    }
  }
}
</script>
<style lang="scss" scoped>
@import './index.scss';
</style>