<template>
  <div class="app-container">
    <!-- 内容区域 -->
    <div class="app-container-content">
      <!-- 中部 搜索条件 -->
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">标题</div>
              <el-input v-model="query.title" @keyup.enter.native="searchEnter" placeholder="请输入标题名称"/>
            </el-col>
            <el-col :span="8">
              <div class="title">引用业务线</div>
              <el-select class="select-width" v-model="query.lineId" placeholder="请选择">
                <el-option label="全部" :value="''"></el-option>
                <el-option
                  v-for="item in businessData"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="8">
              <div class="title">创建时间</div>
              <el-date-picker
                v-model="query.establishTime"
                type="daterange"
                format="yyyy/MM/dd"
                value-format="yyyy-MM-dd"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期">
              </el-date-picker>
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="16">
              <yl-search-btn 
                :total="query.total" 
                @search="handleSearch" 
                @reset="handleReset" />
            </el-col>
          </el-row>
        </div>
      </div>
      <div class="down-box clearfix">
        <div class="btn">
          <yl-button type="primary" @click="establishClick">添加</yl-button>
        </div>
      </div>
      <!-- 底部列表 -->
      <div class="mar-t-8 order-table-view">
        <yl-table
          border 
          show-header 
          :list="dataList" 
          :total="query.total" 
          :page.sync="query.page" 
          :limit.sync="query.limit" 
          :loading="loading" 
          @getList="getList">
          <el-table-column align="center" min-width="160" label="视频">
            <template slot-scope="{ row, $index }">
              <div class="table-video">
                <video 
                  width="162" 
                  height="90" 
                  controls
                  disablePictureInPicture
                  controlslist="nodownload noplaybackrate">
                  <source :src="row.vedioFileUrl" type="video/mp4">
                </video>
                <div class="cover" v-show="row.statusType != 1">
                  <img :src="row.cover" alt="">
                  <div class="switch-i" @click="coverClick(row,$index)">
                    <img src="@/subject/admin/assets/content-assets/videoPlay.png" alt="">
                  </div>
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="150" label="标题" prop="title"></el-table-column>
          <el-table-column align="center" min-width="150" label="创建时间">
            <template slot-scope="{ row }">
              <div>{{ row.createTime | formatDate }}</div>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="80" label="发布状态" >
            <template slot-scope="{ row }">
              <div :style="{color: row.status == 2 ? '#52C41A' : '#E62412'}">{{ row.status == 2 ? '已发布' : '未发布' }}</div>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="80" label="阅读量(PV)" prop="pageView">
          </el-table-column>
          <el-table-column fixed="right" align="center" label="操作" min-width="180">
            <template slot-scope="{ row }">
              <div>
                <yl-button type="text" @click="modifyClick(row)">编辑</yl-button>
                <yl-button type="text" @click="staticClick(row)">{{ row.status == 1 ? '发布' : '取消发布' }}</yl-button>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
  </div>
</template>
<script>
import { displayLine } from '@/subject/admin/utils/busi'
import { queryContentPage, updateContent2 } from '@/subject/admin/api/content_api/article_video'
export default {
  name: 'VideoList',
  components: {},
  computed: {
    businessData() {
      return displayLine()
    }
  },
  data() {
    return {
      query: {
        title: '',
        lineId: '',
        establishTime: [],
        total: 0,
        page: 1,
        limit: 10
      },
      loading: false,
      dataList: []
    }
  },
  activated() {
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
    // 点击当前视频 开关
    coverClick(item,index) {
      document.getElementsByTagName('video')[index].play()
      item.statusType = 1;
    },
    // 获取数据
    async getList() {
      this.loading = true;
      this.dataList = [];
      let query = this.query;
      let data = await queryContentPage(
        '2',
        query.page,
        query.establishTime && query.establishTime.length > 0 ? query.establishTime[0] : '',
        query.establishTime && query.establishTime.length > 1 ? query.establishTime[1] : '',
        query.lineId,
        query.limit,
        query.title
      )
      if (data !== undefined) {
        for (let i = 0; i < data.records.length; i++) {
          this.dataList.push({
            ...data.records[i],
            statusType: 2
          });
        }
        this.query.total = data.total;
      }
      this.loading = false;
    },
    // 搜索
    handleSearch() {
      this.query.page = 1;
      this.getList();
    },
    // 重置
    handleReset() {
      this.query = {
        title: '',
        lineId: '',
        establishTime: [],
        total: 0,
        page: 1,
        limit: 10
      }
    },
    // 创建
    establishClick() {
      this.$router.push({
        name: 'AddEditVideo',
        params: { 
          id: 0,
          type: 1,
          // 1 文章 2视频
          category: 2 
        }
      });
    },
    // 编辑
    modifyClick(row) {
      this.$router.push({
        name: 'AddEditVideo',
        params: { 
          id: row.id,
          type: 2,
          category: 2 
        }
      });
    },
    // 取消发布 / 发布
    staticClick(row) {
      this.$confirm(`确认${row.status == 1 ? '发布' : '取消发布'} ${row.title} ！`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
      .then( async() => {
        this.$common.showLoad();
        let data = await updateContent2(
          row.id,
          row.isTop,
          row.status == 1 ? 2 : 1,
          '2',
          row.docId
        )
        this.$common.hideLoad();
        if (data !== undefined) {
          this.$common.n_success('操作成功!');
          this.getList();
        }
      })
      .catch(() => {
        //取消
      });
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>