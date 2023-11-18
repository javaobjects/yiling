<template>
  <div class="app-container">
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">词语</div>
              <el-input v-model.trim="query.word" placeholder="请输入词语" @keyup.enter.native="handleSearch"/>
            </el-col>
            <el-col :span="6">
              <div class="title">词语类型</div>
              <el-select class="select-width" v-model="query.type" placeholder="请选择">
                <el-option
                  v-for="item in cyData"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
              <!-- <el-radio-group v-model="query.status">
                <el-radio :label="1">扩展词</el-radio>
                <el-radio :label="2">停止词</el-radio>
                <el-radio :label="3">单向同义词</el-radio>
                <el-radio :label="4">双向同义词</el-radio>
              </el-radio-group> -->
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="16">
              <yl-search-btn :total="query.total" @search="handleSearch" @reset="handleReset"/>
            </el-col>
          </el-row>
        </div>
      </div>
      <div class="mar-tb-16">
        <yl-button type="primary" @click="addClick">新增词语</yl-button>
        <yl-button type="primary" @click="refreshClick" plain>立即刷新索引</yl-button>
        <yl-button type="primary" @click="updateClick" plain>更新远程词库</yl-button>
        <yl-button class="button-right" type="primary" plain @click="downLoadTemp">在线词库下载</yl-button>
      </div>
      <div class="search-bar-table">
        <yl-table border :show-header="true" :list="dataList" :total="query.total" :page.sync="query.page" :limit.sync="query.limit" :loading="loading" @getList="getList">
          <el-table-column align="center" min-width="100" label="词语ID" prop="id"></el-table-column>
          <el-table-column align="center" min-width="150" label="词语" prop="word"></el-table-column>
          <el-table-column align="center" min-width="100" label="词语类型" prop="word">
            <template slot-scope="{ row }">
              <div>{{ row.type | dictLabel(cyData) }}</div>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="80" label="状态">
            <template slot-scope="{ row }">
              <div>{{ row.status == '0' ? '正常' : '停用' }}</div>
            </template>
          </el-table-column>
          <el-table-column fixed="right" align="center" label="操作" min-width="110">
            <template slot-scope="{ row }">
              <div>
                <yl-button type="text" @click="modifyClick(row)">详情</yl-button>
              </div>
              <div>
                <yl-button type="text" @click="editClick(row)">编辑</yl-button>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
  </div>
</template>
<script>
 import { eswordPageList, syncWordToEs, uploadWordDic, getDicDownloadUrl } from '@/subject/admin/api/role'
  export default {
    name: 'ThesaurusManage',
    data() {
      return {
        query: {
          word: '',
          type: '1',
          total: 0,
          page: 1,
          limit: 10
        },
        cyData: [
          {
            label: '扩展词',
            value: '1'
          },
          {
            label: '停止词',
            value: '2'
          },
          {
            label: '单向同义词',
            value: '3'
          },
          {
            label: '双向同义词',
            value: '4'
          }
        ],
        dataList: [],
        loading: false
      }
    },
    activated() {
      this.getList()
    },
    methods: {
      async getList() {
        this.loading = true;
        let query = this.query;
        let data = await eswordPageList(
          query.page,
          query.limit,
          query.type,
          query.word
        )
        if (data !== undefined) {
          this.dataList = data.records;
          this.query.total = data.total;
        }
        this.loading = false;
      },
      // 新增词语
      addClick() {
        this.$router.push({
          name: 'ThesaurusEstablis',
          params: {
            type: 0,
            word: '0'
          }
        });
      },
      // 搜索点击
      handleSearch() {
        this.query.page = 1;
        this.getList()
      },
      // 清空查询
      handleReset() {
        this.query = {
          word: '',
          type: '1',
          total: 0,
          page: 1,
          limit: 10
        }
      },
      // 点击详情
      modifyClick(row) {
        this.$router.push({
          name: 'ThesaurusEstablis',
          params: {
            type: 1,
            word: row.word
          }
        });
      },
      //点击编辑
      editClick(row) {
        this.$router.push({
          name: 'ThesaurusEstablis',
          params: {
            type: 2,
            word: row.word
          }
        });
      },
      // 词库下载
      async downLoadTemp() {
        this.$common.showLoad();
        let data = await getDicDownloadUrl(this.query.type)
        this.$common.hideLoad();
        if (data !== undefined) {
          let url = data.url;
          let xmlResquest = new XMLHttpRequest();
          xmlResquest.open('GET', url, true);
          // xmlResquest.setRequestHeader("Authorization", "bearer" + this.token); //token验证
          xmlResquest.responseType = 'blob';
          xmlResquest.onload = function(oEvent) { //接口响应后的处理
            var content = xmlResquest.response; // 组装a标签
            let elink = document.createElement('a');// 设置下载文件名
            elink.download = data.fileName;
            elink.style.display = 'none';
            let blob = new Blob([content]);
            elink.href = URL.createObjectURL(blob);
            document.body.appendChild(elink);
            elink.click();
            document.body.removeChild(elink);
            URL.revokeObjectURL(blob); //释放对象
          };
          xmlResquest.send();
          // let a = document.createElement('a');
          // a.setAttribute('href', data);
          // a.setAttribute('download', '在线词库');
          // a.setAttribute('target', '_blank');
          // a.click();
        }
      },
      // 刷新索引
      async refreshClick() {
        this.$common.showLoad();
        let data = await syncWordToEs(this.query.type)
        this.$common.hideLoad();
        if (data !== undefined) {
          this.$common.n_success(data)
        }
      },
      // 更新远程词库
      async updateClick() {
        this.$common.showLoad();
        let data = await uploadWordDic(this.query.type)
        this.$common.hideLoad();
        if (data !== undefined) {
          this.$common.n_success(data)
        }
      }
    }
  }
</script>
<style lang="scss" scoped>
  @import "./index.scss";
  .search-bar-table ::v-deep .el-table__fixed-right::before, .el-table__fixed::before{
    background-color: rgba(255,255,255,0) !important;
  }

</style>
