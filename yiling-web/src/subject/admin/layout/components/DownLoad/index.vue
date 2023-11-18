<template>
  <div class="yl-down">
    <div class="pop-go flex-row-center" @click="showDownLoad">
      <img class="pop-go-img" src="@/common/assets/topbar/btn-download.png">
      <div class="box border-1px-l">
        下载中心
      </div>
    </div>
    <el-drawer
      title="下载中心"
      :visible.sync="show"
      direction="rtl"
      @opened="opened"
      size="50%">
      <yl-table
        ref="table"
        :height="tableHeight"
        :list="dataList"
        :total="total"
        :page-count="5"
        :page.sync="query.page"
        :limit.sync="query.limit"
        :loading="loading"
        @getList="getList">
        <el-table-column>
          <template slot-scope="{ row, $index }">
            <div class="table-view border-1px-b" :key="$index">
              <div class="session font-size-base">
                <span>
                  任务状态：
                  <span :class="row.status === 1 ? 'col-down' : (row.status === 0 ? 'col-yellow' : 'col-up')">
                    {{ row.status | dictLabel(downStatus) }}
                    <el-popover
                      v-show="row.status === -1"
                      placement="top-start"
                      width="200"
                      trigger="hover"
                      :content="`失败原因：${row.remark}`">
                      <i slot="reference" class="el-icon-warning-outline" style="margin-left: 8px;"></i>
                    </el-popover>
                  </span>
                </span>
                <span>创建时间：{{ row.createTime | formatDate }}</span>
              </div>
              <div class="content flex-row-left">
                <div class="flex1">
                  <div class="item font-size-base font-title-color"><span class="font-light-color">文件名称：</span>{{ row.fileName }}</div>
                  <div class="item font-size-base font-title-color"><span class="font-light-color">所属组：</span>{{ row.groupName }}</div>
                </div>
                <div class="flex-column-center">
                  <div v-if="row.status === 1">
                    <yl-button type="text" :loading="row.loading" @click="downLoad(row, $index)">下载</yl-button>
                  </div>
                </div>
              </div>
            </div>
          </template>
        </el-table-column>
      </yl-table>
    </el-drawer>
  </div>
</template>

<script>
  import { getDownLoadList, downLoadFile } from '@/subject/admin/api/common'
  import { downLoadStatus } from '@/subject/admin/utils/busi'

  export default {
    name: 'DownLoad',
    data() {
      return {
        query: {
          page: 1,
          limit: 10
        },
        show: false,
        dataList: [],
        loading: false,
        total: 0,
        tableHeight: null
      }
    },
    computed: {
      downStatus() {
        return downLoadStatus()
      }
    },
    mounted() {

    },
    methods: {
      showDownLoad() {
        this.show = true
        this.getList()
      },
      async getList() {
        this.loading = true
        let data = await getDownLoadList(this.query.page, this.query.limit)
        this.loading = false
        if (data) {
          this.dataList = data.records.map(item => {
            item.loading = false
            return item
          })
          this.total = data.total
        }
      },
      confirm() {
        this.show = false
      },
      opened() {
        this.$nextTick(() => {
          this.tableHeight = window.innerHeight - this.$refs.table.$el.offsetTop - 50;
        })
      },
      async downLoad(row, index) {
        if (row.id) {
          this.dataList[index].loading = true
          let data = await downLoadFile(row.id)
          if (data && data.url) {
            const xRequest = new XMLHttpRequest()
            xRequest.open('GET', data.url, true)
            xRequest.responseType = 'blob'
            xRequest.onload = () => {
              this.dataList[index].loading = false
              const url = window.URL.createObjectURL(xRequest.response)
              const a = document.createElement('a')
              a.href = url
              a.download = row.fileName
              a.click()
            }
            xRequest.send()
          }
        }
      }
    }
  }
</script>
<style lang="scss">
  .yl-down {
    :focus {
      outline: 0;
    }
  }
</style>
<style lang="scss" scoped>
  @import "~@/common/styles/mixin.scss";
  .pop-go {
    width: 100px;
    height: 24px;
    background: #FFFFFF;
    border-radius: 2px;
    opacity: 0.51;
    border: 1px solid #AEAEAE;
    cursor: pointer;
    &-img {
      width: 14px;
      height: 14px;
      line-height: 10px;
      margin-right: 5px;
    }
    .box {
      padding-left: 5px;
      font-size: 14px;
      font-weight: 400;
      color: #333333;
      line-height: 10px;
    }
    &:hover {
      border: 1px solid $primary;
    }
  }
  .down-view {
    width: 100px;
    height: 100%;
    i {
      font-size: 24px;
    }
  }
  .table-view {
    display: flex;
    flex-direction: column;
    justify-content: flex-start;
    align-items: flex-start;
    padding: 5px 6px;
    @include clamp(2);

    >.session {
      width: 100%;
      margin-bottom: 10px;
      span:first-child {
        float: left;
      }
      span:last-child {
        float: right;
      }
      .col-yellow {
        color: #FAAB0C;
      }
    }
    .content {
      width: 100%;
      position: relative;
      .table-img {
        width: 220px;
        img {
          width: 220px;
          height: 90px;
        }
      }
      .edit-btn {
        width: 100px;
      }
    }
  }
</style>
