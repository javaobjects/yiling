<template>
  <div class="app-container">
    <!-- 内容区域 -->
    <div class="app-container-content">
      <!-- 创建开屏位 -->
      <div>
        <ylButton type="primary" @click="addClick(1, 0)">创建开屏位</ylButton>
      </div>
      <div class="mar-t-16">
        <yl-table 
          border 
          :show-header="true" 
          :list="dataList" 
          :total="query.total" 
          :page.sync="query.page" 
          :limit.sync="query.limit" 
          :loading="loading" 
          @getList="getList">
          <el-table-column align="center" min-width="90" label="ID编码" prop="no"></el-table-column>
          <el-table-column align="center" min-width="150" label="标题" prop="title"></el-table-column>
          <el-table-column align="center" min-width="90" label="状态">
            <template slot-scope="{ row }">
              <span :style="{color: row.status == 1 ? '' : '#67C23A'}">{{ row.status	== 1 ? '未发布' : '已发布' }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="100" label="最后更新时间">
            <template slot-scope="{ row }">
              <p>{{ row.updateTime | formatDate }}</p>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="100" label="最后修改人" prop="updateUserName"></el-table-column>
          <el-table-column align="center" min-width="150" fixed="right" label="操作">
            <template slot-scope="{ row }">
              <div>
                <yl-button type="text" @click="seeClick(row)">查看</yl-button>
                <yl-button type="text" @click="addClick(2, row.id)">编辑</yl-button>
                <yl-button type="text" @click="statusClick(row)">{{ row.status	== 2 ? '下线' : '发布' }}</yl-button>
                <yl-button type="text" @click="deleteClick(row)">删除</yl-button>
              </div>
            </template>
          </el-table-column>
      </yl-table>
      </div>
    </div>
    <el-image-viewer v-if="showViewer" :on-close="onClose" :url-list="imageList"/>
  </div>
</template>
<script>
import { queryListPage, deleteOpenPosition, updateStatus } from '@/subject/admin/api/views_sale/app_maintain';
import ElImageViewer from 'element-ui/packages/image/src/image-viewer';
export default {
  name: 'HomePage',
  components: {
    ElImageViewer
  },
  data() {
    return {
      dataList: [],
      query: {
        page: 1,
        limit: 10,
        total: 0
      },
      loading: false,
      showViewer: false,
      imageList: []
    }
  },
  activated() {
    this.getList();
  },
  methods: {
    //新增开屏位
    addClick(type, id) {
      //type, 1新增 2编辑 3查看
      this.$router.push({
        name: 'HomePageEdit',
        params: {
          type: type,
          id: id
        }
      })
    },
    async getList() {
      this.loading = true;
      let query = this.query;
      let data = await queryListPage(
        query.page,
        query.limit
      )
      if (data) {
        this.dataList = data.records;
        this.query.total = data.total;
      }
      this.loading = false;
    },
    //更新状态
    statusClick(row) {
      this.$confirm(`${row.status	== 2 ? '下线后' : '发布后'},大运河APP商城日常登陆时将${row.status	== 2 ? '不展示' : '展示'}此开屏广告`, `确定${row.status	== 2 ? '下线' : '发布'}选中的方案吗？`, {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
      .then( async() => {
        //确定
        this.$common.showLoad();
        let data = await updateStatus(
          row.id,
          row.status == 1 ? 2 : 1
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
    },
    //删除
    deleteClick(row) {
      this.$confirm('删除后,已发布的方案将会强制下线且不能恢复！', '确定删除选中的方案吗？', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
      .then( async() => {
        //确定
        this.$common.showLoad();
        let data = await deleteOpenPosition(
          row.id
        )
        this.$common.hideLoad();
        if (data !== undefined) {
          this.$common.n_success('删除成功!');
          this.getList();
        }
      })
      .catch(() => {
        //取消
      });
    },
    seeClick(val) {
      if (val.pictureUrl != '') {
        this.imageList.push(val.pictureUrl)
      }
      this.showViewer = true;
    },
    // 关闭图片放大弹窗
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