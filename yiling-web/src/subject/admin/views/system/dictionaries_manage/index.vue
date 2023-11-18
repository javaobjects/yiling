<template>
  <div class="app-container">
    <div class="app-container-content">
      <div class="search-bar">
        <el-row>
          <el-col :span="13">
            <el-card class="box-card">
              <el-row>
                <el-col :span="10">
                  <div class="title">字典名称</div>
                  <el-input v-model="query.name" placeholder="请输入字典名称" @keyup.enter.native="handleSearch"/>
                </el-col>
              </el-row>
              <div class="mar-tb-16">
                <yl-search-btn
                  :total="query.total"
                  @search="handleSearch"
                  @reset="handleReset"
                />
              </div>
              <div class="mar-tb-16">
                <yl-button type="primary" @click="addDicClick">新增字典</yl-button>
              </div>
              <!-- 字典表格 -->
              <yl-table
                border
                :show-header="true"
                :list="dataLists"
                :total="query.total"
                :page.sync="query.page"
                :limit.sync="query.limit"
                :loading="loading"
                :row-class-name="rowClassName"
                :row-style="rowStyle"
                @row-click="rowClick"
                @getList="getList">
                <el-table-column label="字典名称" prop="name">
                </el-table-column>
                <el-table-column label="描述" align="center" min-width="60" prop="description" >
                </el-table-column>
                <!-- <el-table-column align="center" label="备注" >
                </el-table-column> -->
                <el-table-column
                  align="center"
                  label="操作"
                  width="200">
                  <template slot-scope="{ row, $index }">
                    <div>
                      <el-switch
                        v-model="row.status"
                        :active-value="1"
                        active-color="#1790ff"
                        inactive-color="#dcdfe6" @change="changSwich(row)">
                      </el-switch>
                      <yl-button type="text" style="margin:0 10px" @click="edit(row)">修改</yl-button>
                      <el-popconfirm
                        class="popconfirm"
                        @confirm="deleteClick(row, $index)"
                        title="确定删除吗,如果存在下级节点则一并删除，此操作不能撤销">
                        <yl-button slot="reference" type="text">删除</yl-button>
                      </el-popconfirm>

                    </div>
                  </template>
                </el-table-column>
              </yl-table>
            </el-card>
          </el-col>
          <el-col :span="10" style="margin-left:40px">
            <el-card class="box-card">
              <div slot="header" class="clearfix">
                <span>字典详情</span>
                <yl-button style="float: right; padding: 3px 0" type="text" @click="xqAddClick">新增</yl-button>
              </div>
              <yl-table
                border
                :show-header="true"
                :list="xqdataList"
                :loading="loading2"
                >
                <el-table-column label="字典标签" prop="label">
                </el-table-column>
                <el-table-column label="字典键值" prop="value" align="center">
                </el-table-column>
                <el-table-column
                  align="center"
                  label="操作"
                  width="200">
                  <template slot-scope="{ row, $index }">
                    <div>
                      <el-switch
                        v-model="row.status"
                        :active-value="1"
                        active-color="#1790ff"
                        inactive-color="#dcdfe6" @change="xqchangSwich(row)">
                      </el-switch>
                      <yl-button style="margin:0 10px" type="text" @click="edit2(row)">修改</yl-button>
                      <el-popconfirm
                        class="popconfirm"
                        @confirm="deleteClick2(row, $index)"
                        title="确定删除吗,此操作不能撤销">
                        <yl-button slot="reference" type="text">删除</yl-button>
                      </el-popconfirm>

                    </div>
                  </template>
                </el-table-column>
              </yl-table>
            </el-card>
          </el-col>
        </el-row>
      </div>
    </div>
    <!-- 新增字典弹窗 -->
    <yl-dialog :title="modifyType==0?'新增字典弹窗':'修改字典弹窗'" @confirm="confirm" :visible.sync="showDialog" >
      <div class="dialog-content">
        <el-form
          ref="dataForm"
          class="recome-view-form"
          :rules="rules"
          :model="form"
          label-width="80px"
          label-position="right">
          <el-row>
            <el-col :span="12">
              <el-form-item label="名称" prop="name">
                <el-input v-model="form.name" placeholder="请输入字典名称" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="描述" prop="name">
                <el-input v-model="form.description" placeholder="请输入字典描述" />
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </div>
    </yl-dialog>
    <!-- 新增字典详情 -->
    <yl-dialog :title="modifyType2==0?'新增字典详情':'修改字典详情'" @confirm="confirm2" :visible.sync="showDialog2" >
      <div class="dialog-content">
        <el-form
          ref="dataForm2"
          class="recome-view-form"
          :rules="rules2"
          :model="form2"
          label-width="80px"
          label-position="right">
          <el-row>
            <el-col :span="12">
              <el-form-item label="字典标签" prop="label">
                <el-input v-model="form2.label" placeholder="请输入字典标签" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="字典键值" prop="value">
                <el-input v-model="form2.value" placeholder="请输入字典键值" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="字典描述" >
                <el-input v-model="form2.description" placeholder="请输入字典描述" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="字典排序" >
                <el-input type="number" v-model="form2.sort" placeholder="请输入字典排序" />
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </div>
    </yl-dialog>
  </div>
</template>

<script>
import { dicType, dicGetData, stopType, enabledType, stopData, enabledData, saveType, saveData, dataDelete, typeDelete, updateType, updateData } from '@/subject/admin/api/role'
export default {
  name: 'DicManage',
  data() {
    return {
      query: {
        name: '',
        total: 0,
        page: 1,
        limit: 10
      },
      loading: false,
      dataLists: [],
      xqdataList: [], //右侧 字典详情
      showDialog: false , //新增字典弹窗
      loading2: false,
      showDialog2: false,
      // 新增
      form: {
        name: '',
        description: ''
      },
      // 右侧新增
      form2: {
        label: '',
        value: '',
        description: '',
        sort: ''
      },
      rules: {
        name: [{ required: true, message: '请输入字典名称', trigger: 'blur' }],
        description: [{ required: true, message: '请输入字典描述', trigger: 'blur' }]
      },
      rules2: {
        label: [{ required: true, message: '请输入字典标签', trigger: 'blur' }],
        value: [{ required: true, message: '请输入字典键值', trigger: 'blur' }]
      },
      typeId: '',// 选中的左侧字典id
      modifyData: {},//左边字典修改 缓存数据
      modifyType: 0, //0 为新增 1为修改
      modifyData2: {}, //右边字典详情 缓存数据
      modifyType2: 0, //右边 0 为新增 1为修改
      getIndex: ''//当前 左边点击 哪一行
    }
  },
  activated() {
    this.getList()
  },
  mounted() {
    this.getList(); //获取字典列表
  },
  methods: {
    rowClassName({row, rowIndex}) {
      row.index = rowIndex;
    },
    rowStyle({row, rowIndex}) {
      if (this.getIndex === rowIndex) {
        return {
          'background-color': '#f4f4f4'
        }
      }
    },
    //搜索
    handleSearch() {
      this.query.page = 1;
      // 获取表格信息
      this.getList()
    },
    // 重置
    handleReset() {
      this.query = {
        page: 1,
        size: 10,
        status: 0,
        name: ''
      }
    },
    // 新增字典
    addDicClick() {
      this.form = {
        name: '',
        description: ''
      };
      this.modifyType = 0;
      this.showDialog = true;
    },
    // 字典详情 新增
    xqAddClick() {
      if (this.typeId == '') {
        this.$common.warn('请先选择左侧字典');
      } else {
        this.showDialog2 = true;
        this.modifyType2 = 0;
        this.form2 = {
          label: '',
          value: '',
          description: '',
          sort: ''
        };
      }

    },
    // 点击左边表格
    async rowClick(row, column, event) {
      this.getIndex = row.index;
      if (row) {
        this.typeId = row.id;
        this.$common.showLoad();
        let data = await dicGetData(
          row.id
        )
        this.$common.hideLoad();
        if (data) {
          this.xqdataList = data.list;
        }
      }

    },
    // 分页
    async getList() {
      this.loading = true;
      this.typeId = '';
      this.getIndex = '';
      let query = this.query;
      let data = await dicType(
        query.page,
        query.name,
        query.limit
      );
      this.loading = false;
      if (data) {
        this.dataLists = data.records;
        this.query.total = data.total;
      }
    },
    // 点击左侧的 开关
    async changSwich(row) {
      let data = null;
      this.$common.showLoad();
      if (row.status == false) {
        data = await stopType(
          row.id
        )
      } else {
         data = await enabledType(
          row.id
        )
      }
      this.$common.hideLoad();
      if (data) {
        if (row.status == false) {
          this.$common.n_warn('已禁用');
        } else {
          this.$common.n_success('已启用');
        }
      }
    },
    // 点击右侧的 开关
    async xqchangSwich(row) {
      let data = null;
      this.$common.showLoad();
      if (row.status == false) {
        data = await stopData(
          row.id
        )
      } else {
         data = await enabledData(
          row.id
        )
      }
      this.$common.hideLoad();
      if (data) {
        if (row.status == false) {
          this.$common.n_warn('已禁用');
        } else {
          this.$common.n_success('已启用');
        }
      }
    },
    //左侧字典类型 删除
    async deleteClick(row, index) {
      this.$common.showLoad();
      let data = await typeDelete(
        row.id
      )
      this.$common.hideLoad();
      if (data) {
        this.$common.n_success('删除成功!');
        this.dataLists.splice(index,1);
        this.xqdataList = [];
        this.typeId = '';
      }
    },
    // 右侧 字典详情 删除
    async deleteClick2(row, index) {
      this.$common.showLoad();
      let data = await dataDelete(
        row.id
      )
       this.$common.hideLoad();
      if (data) {
        this.$common.n_success('删除成功!');
        this.xqdataList.splice(index,1);
      }
    },
    // 左边 字典类型 修改
    edit(row) {
      this.showDialog = true; //弹窗显示
      this.modifyData = row;
      this.modifyType = 1;
      this.form = {
        name: row.name,
        description: row.description
      }
    },
    // 右边 字典详情 修改
    edit2(row) {
      this.showDialog2 = true; //弹窗显示
      this.modifyData2 = row;
      this.modifyType2 = 1;
      this.form2 = {
        label: row.label,
        value: row.value,
        description: row.description,
        sort: row.sort
      }
    },
    // 左侧新增 确定
    confirm() {
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {
          // 新增
          let form = this.form;
          if (this.modifyType == 0) {
            this.$common.showLoad();
            let data = await saveType(
              form.description,
              form.name
            )
             this.$common.hideLoad();
            if (data) {
              this.$common.n_success('新增成功！')
              this.showDialog = false;
              // this.query.page = 1;
              this.getList(); //获取字典列表
            }
          } else { //修改
            this.$common.showLoad();
            let val = await updateType(
              form.description,
              this.modifyData.id,
              form.name
            )
             this.$common.hideLoad();
            if (val) {
              this.$common.n_success('修改成功!');
              this.showDialog = false;
              // this.query.page = 1;
              this.getList(); //获取字典列表
            }
          }
        }
      })
    },
    // 右侧新增 确定
    confirm2() {
      this.$refs['dataForm2'].validate(async (valid) => {
        if (valid) {
          let form = this.form2;
          // 新增
          if (this.modifyType2 == 0) {
            if (this.typeId == '') {
              this.$common.warn('请先选择左侧字典');
            } else {
              this.$common.showLoad();
              let data = await saveData(
                form.description,
                form.label,
                this.typeId,
                form.value,
                form.sort
              )
              this.$common.hideLoad();
              if (data) {
                this.$common.n_success('新增成功！')
                this.$common.showLoad();
                let val = await dicGetData(
                  this.typeId
                )
                this.$common.hideLoad();
                if (val) {
                  this.xqdataList = val.list;
                }
                this.showDialog2 = false;
              }
            }
          } else { //修改
            this.$common.showLoad();
            let val = await updateData(
              form.description,
              this.modifyData2.id,
              form.label,
              form.sort,
              this.typeId,
              form.value
            )
            this.$common.hideLoad();
            if (val) {
              this.$common.n_success('修改成功!');
              let val = await dicGetData(
                this.typeId
              )
              if (val) {
                this.xqdataList = val.list;
              }
              this.showDialog2 = false;
            }
          }
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
  @import "./index.scss";
</style>