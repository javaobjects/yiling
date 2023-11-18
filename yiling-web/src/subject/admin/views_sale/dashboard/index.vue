<template>
  <div class="app-container">
    <!-- 内容区域 -->
    <div class="app-container-content">
      <div class="app-title">本月任务进度</div>
      <el-row :gutter="16">
        <el-col :span="17">
          <el-row :gutter="16">
            <el-col :span="12">
              <div class="common-box box-total">
                <div class="header-bar-1"><div class="sign"></div>任务概况</div>
                <div class="flex-row-left">
                  <div class="box-1 flex-row-left flex1 color-1 mar-r-8">
                    <img class="img-1" src="@/subject/admin/assets/sale-dashboard/task-gk-1.png">
                    <div class="label-view">
                      <div class="title">3000万</div>
                      <div class="desc">交易额</div>
                    </div>
                  </div>
                  <div class="box-1 flex-row-left flex1 color-1">
                    <img class="img-1" src="@/subject/admin/assets/sale-dashboard/task-gk-2.png">
                    <div class="label-view">
                      <div class="title">3000万</div>
                      <div class="desc">交易额</div>
                    </div>
                  </div>
                </div>
                <div class="flex-row-left">
                  <div class="box-1 flex-row-left flex1 color-1 mar-r-8">
                    <img class="img-1" src="@/subject/admin/assets/sale-dashboard/task-gk-3.png">
                    <div class="label-view">
                      <div class="title">3000万</div>
                      <div class="desc">交易额</div>
                    </div>
                  </div>
                  <div class="box-1 flex-row-left flex1 color-1">
                    <img class="img-1" src="@/subject/admin/assets/sale-dashboard/task-gk-4.png">
                    <div class="label-view">
                      <div class="title">3000万</div>
                      <div class="desc">交易额</div>
                    </div>
                  </div>
                </div>
              </div>
            </el-col>
            <el-col :span="12">
              <div class="common-box box-total">
                <div class="header-bar-1"><div class="sign"></div>企业任务</div>
                <div class="flex-row-left">
                  <div class="box-2 flex-column-center flex1">
                    <div class="title">交易量</div>
                    <div class="chart">
                      <el-progress
                        type="circle"
                        :stroke-width="10"
                        :width="120"
                        :show-text="false"
                        color="#3366FF"
                        :percentage="25">
                      </el-progress>
                      <div class="chart-text label-view flex-column-center">
                        <div style="font-size: 28px;">66%</div>
                        <div class="desc">完成进度</div>
                      </div>
                    </div>
                    <div class="label-view">
                      <div>3000万</div>
                      <div class="desc">交易额</div>
                    </div>
                  </div>
                  <div class="box-2 flex-column-center flex1">
                    <div class="title">交易量</div>
                    <div class="chart">
                      <el-progress
                        type="circle"
                        :stroke-width="10"
                        color="#FFBA65"
                        :width="120"
                        :show-text="false"
                        :percentage="25">
                      </el-progress>
                      <div class="chart-text label-view flex-column-center">
                        <div style="font-size: 28px;">1266%</div>
                        <div class="desc">完成进度</div>
                      </div>
                    </div>
                    <div class="label-view">
                      <div>3000万</div>
                      <div class="desc">交易额</div>
                    </div>
                  </div>
                </div>
              </div>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <div class="common-box mar-t-16">
                <div class="header-bar-1"><div class="sign"></div>企业任务</div>
                <div class="flex-row-left">
                  <div class="box-3 flex1 color-1 mar-r-16">
                    <div class="flex-row-left pad-b-16">
                      <img class="img-2" src="@/subject/admin/assets/sale-dashboard/task-gk-1.png">
                      <div class="label-view">
                        <div class="title">3000万<span class="desc">（目标值/完成值）</span></div>
                        <div class="desc">交易额</div>
                      </div>
                    </div>
                    <div class="percent">
                      <div class="title">3000%<span class="desc">（完成度）</span></div>
                    </div>
                    <el-progress
                      color="#3366FF"
                      :stroke-width="10"
                      :show-text="false"
                      :percentage="70">
                    </el-progress>
                  </div>
                  <div class="box-3 color-3 flex1">
                    <div class="flex-row-left pad-b-16">
                      <img class="img-2" src="@/subject/admin/assets/sale-dashboard/task-gk-3.png">
                      <div class="label-view">
                        <div class="title">3000万<span class="desc">（目标值/完成值）</span></div>
                        <div class="desc">交易额</div>
                      </div>
                    </div>
                    <div class="percent">
                      <div class="title">3000%<span class="desc">（完成度）</span></div>
                    </div>
                    <el-progress
                      color="#FFBA65"
                      :stroke-width="10"
                      :show-text="false"
                      :percentage="70">
                    </el-progress>
                  </div>
                </div>
                <div class="flex-row-left">
                  <div class="box-3 flex1 color-2 mar-r-16">
                    <div class="flex-row-left pad-b-16">
                      <img class="img-2" src="@/subject/admin/assets/sale-dashboard/task-gk-2.png">
                      <div class="label-view">
                        <div class="title">3000万<span class="desc">（目标值/完成值）</span></div>
                        <div class="desc">交易额</div>
                      </div>
                    </div>
                    <div class="percent">
                      <div class="title">3000%<span class="desc">（完成度）</span></div>
                    </div>
                    <el-progress
                      color="#659EFF"
                      :stroke-width="10"
                      :show-text="false"
                      :percentage="70">
                    </el-progress>
                  </div>
                  <div class="box-3 color-4 flex1">
                    <div class="flex-row-left pad-b-16">
                      <img class="img-2" src="@/subject/admin/assets/sale-dashboard/task-gk-4.png">
                      <div class="label-view">
                        <div class="title">3000万<span class="desc">（目标值/完成值）</span></div>
                        <div class="desc">交易额</div>
                      </div>
                    </div>
                    <div class="percent">
                      <div class="title">3000%<span class="desc">（完成度）</span></div>
                    </div>
                    <el-progress
                      color="#FF9292"
                      :stroke-width="10"
                      :show-text="false"
                      :percentage="70">
                    </el-progress>
                  </div>
                </div>
              </div>
            </el-col>
          </el-row>
        </el-col>
        <el-col :span="7">
          <div class="common-box">
            <div class="header-bar-1"><div class="sign"></div>消息提醒</div>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
export default {
  name: 'SaleDashboard',
  components: {
  },
  computed: {
    ...mapGetters([
    ])
  }
}
</script>

<style lang="scss" scoped>
  .app-container {
    .app-title {
      height: 22px;
      font-size: 16px;
      font-weight: 600;
      color: #333333;
      line-height: 22px;
      margin-bottom: 8px;
    }
    .box-total {
      min-height: 292px;
      max-height: 292px;
    }
    .common-box {
      padding: 0 16px;
      .box-1 {
        height: 92px;
        padding: 16px;
        border-radius: 4px;
        min-width: 190px;
        margin-bottom: 8px;
        .label-view {
          margin-left: 12px;
          .title {
            height: 28px;
            font-size: 20px;
            font-weight: 600;
            color: $font-important-color;
            line-height: 28px;
          }
          .desc {
            height: 17px;
            font-size: $font-size-small;
            font-weight: 400;
            color: #8C8C8C;
            line-height: 17px;
          }
        }
      }
      .box-2 {
        padding-bottom: 16px;
        .chart {
          margin: 16px 0;
          position: relative;
          .chart-text {
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
          }
        }
        .title {
          height: 22px;
          font-size: 16px;
          color: #2C2C2C;
          line-height: 22px;
        }
        .label-view {
          font-size: 20px;
          font-weight: 600;
          color: #2C2C2C;
          line-height: 28px;
          text-align: center;
          .desc {
            color: #8C8C8C;
            font-size: 12px;
          }
        }
      }
      .box-3 {
        padding: 16px;
        margin-bottom: 16px;
        .label-view {
          margin-left: 10px;
          .title {
            height: 22px;
            font-size: 16px;
            color: #2C2C2C;
            line-height: 22px;
            font-weight: 600;
            .desc {
              color: #8C8C8C;
              font-size: 12px;
            }
          }
          .desc {
            color: #8C8C8C;
            font-size: 12px;
            line-height: 17px;
            height: 17px;
          }
        }
        .percent {
          height: 30px;
          font-size: 22px;
          font-weight: 600;
          margin-bottom: 8px;
          color: #2C2C2C;
          line-height: 30px;
          .desc {
            color: #8C8C8C;
            font-size: 12px;
          }
        }
      }
      .color-1 {
        background: #F7F9FF;
      }
      .color-2 {
        background: #ECF3FF;
      }
      .color-3 {
        background: #FFF7EE;
      }
      .color-4 {
        background: #FFF4F4;
      }
      .img-1 {
        width: 40px;
        height: 40px;
      }
      .img-2 {
        width: 32px;
        height: 32px;
      }
    }
  }
</style>
