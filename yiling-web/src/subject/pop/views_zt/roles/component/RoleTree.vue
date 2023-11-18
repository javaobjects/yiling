<template>
  <div>
    <div class="data-role-view" v-if="showRole">
      <div class="header">
        数据权限
      </div>
      <div class="flex-row-left role">
        <el-radio-group v-model="form.role">
<!--          <el-radio :label="0">未设置</el-radio>-->
          <el-radio :label="1">本人</el-radio>
          <el-radio :label="2">本部门</el-radio>
          <el-radio :label="3">本部门及下级部门</el-radio>
          <el-radio :label="4">全部数据</el-radio>
        </el-radio-group>
      </div>
    </div>
    <div v-show="dataList.length">
      <div class="header">
        操作权限
      </div>
      <div class="tree-container">
        <div class="tree-line"></div>
        <div class="tree-view" v-for="(item, index) in dataList" :key="index">
          <div class="flex-row-left tree-item pad-lr-16" @click.stop="handleExpandIconClick(item, index)">
            <span class="el-arrow" :class="['el-icon-arrow-right', item.expand ? 'arrow-down' : '']"></span>
            <el-checkbox
              v-model="item.checked"
              :indeterminate="item.indeterminate"
              @click.native.stop
              @change="(v) => handleCheckChange(item, index)">
            </el-checkbox>
            <span class="font-important-color mar-l-10 bold touch">{{ item.menuName }}</span>
          </div>
          <el-collapse-transition>
            <div v-show="item.expand">
              <div class="tree-item-pad pad-lr-16" v-for="(child, childIndex) in item.children" :key="childIndex">
                <div class="flex-row-left">
                  <div class="flex-row-left tree-item-level-2">
                    <el-checkbox
                      v-model="child.checked"
                      :indeterminate="child.indeterminate"
                      @click.native.stop
                      @change="(v) => handleCheckChange(child, index, childIndex)">
                      <span class="font-important-color" style="font-weight: 400;">{{ child.menuName }}</span>
                    </el-checkbox>

                  </div>
                  <div class="tree-item-level-3 flex-row-left flex-wrap">
                    <div class="flex-row-left tree-item-3" v-for="(childItem, childItemIndex) in child.children" :key="childItemIndex">
                      <el-checkbox
                        v-model="childItem.checked"
                        @click.native.stop
                        @change="(v) => handleCheckChange(childItem, index, childIndex, childItemIndex)">
                        <span class="font-title-color" style="font-weight: 400;">{{ childItem.menuName }}</span>
                      </el-checkbox>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </el-collapse-transition>
        </div>
      </div>
    </div>
    <div class="empty-box flex-row-center" v-show="!dataList.length">
      <yl-empty description="暂无可操作页面权限"></yl-empty>
    </div>
  </div>
</template>

<script>
  import ylEmpty from '@/common/components/Empty'

  export default {
    name: 'RoleTree',
    components: {
      ylEmpty
    },
    props: {
      // 操作权限数据
      data: {
        type: Array,
        default: () => []
      },
      // 数据权限
      role: {
        type: Number,
        default: null
      },
      // 是否展示数据权限选择框
      showRole: {
        type: Boolean,
        default: false
      }
    },
    watch: {
      data: {
        handler(newVal, oldVal) {
          this.handleData(this.$common.clone(newVal))
        },
        deep: true
      },
      role(newVal) {
        this.form.role = newVal
      }
    },
    data() {
      return {
        dataList: [],
        form: { role: null }
      }
    },
    mounted() {
      if (this.data.length) {
        this.handleData(this.$common.clone(this.data))
        this.form.role = this.role
      }
    },
    methods: {
      // 数据处理器
      handleData(data = []) {
        data = data.map(item => {
          let level2Choose = 0
          let level2HalfChoose = 0
          if (Array.isArray(item.children) && item.children.length) {
            item.children = item.children.map(child => {
              let level3Choose = 0
              if (Array.isArray(child.children) && child.children.length) {
                child.children = child.children.map(childItem => {
                  childItem.level = 3
                  childItem.checked = !!childItem.selected
                  if (childItem.checked) {
                    level3Choose++
                  }
                  return childItem
                })
              }
              child.level = 2
              child.checked = !!child.selected && level3Choose === child.children.length
              child.indeterminate = !!child.selected && level3Choose !== child.children.length
              if (child.checked) level2Choose++
              if (child.indeterminate) level2HalfChoose++
              return child
            })
          }
          item.level = 1
          // console.log(item.menuName, level2Choose, item.children.length, !!item.selected)
          item.checked = !!item.selected && level2Choose === item.children.length
          item.indeterminate = !!item.selected && !item.checked && (level2HalfChoose > 0 || level2Choose > 0)
          return item
        })
        this.dataList = data
      },
      // 点击文字选中
      handleCheckClick(item, index, childIndex) {
        let data = this.$common.clone(this.dataList)
        if (data[index]) {
          data[index].children = !data[index].expand
        }
        this.dataList = data
      },
      // expand按钮点击
      handleExpandIconClick(item, index) {
        let data = this.$common.clone(this.dataList)
        if (data[index]) {
          data[index].expand = !data[index].expand
        }
        this.dataList = data
      },
      // 设置是否选中
      setChecked(data, check, indeterminate, all = false) {
        if (data && data.length) {
          data = data.map(item => {
            if (all) {
              if (Array.isArray(item.children) && item.children.length) {
                this.setChecked(item.children, check, indeterminate, all)
              }
            }
            item.checked = check
            item.indeterminate = indeterminate
            return item
          })
        }
        return data
      },
      // checkbox选中
      handleCheckChange(item, index, childIndex, childItemIndex) {
        let data = this.$common.clone(this.dataList)
        let levelItem1 = data[index]
        if (item.level === 1) {
          if (item.checked) {
            levelItem1.children = this.setChecked(levelItem1.children, !!item.checked, false, true)
            levelItem1.indeterminate = false
          } else {
            levelItem1.children = this.setChecked(levelItem1.children, !!item.checked, false, true)
            levelItem1.indeterminate = false
          }
        } else if (item.level === 2) {
          let choose = levelItem1.children.filter(itm => itm.checked === true)
          let levelItem2 = levelItem1.children[childIndex]
          if (item.checked) {
            levelItem1.indeterminate = choose.length !== levelItem1.children.length
            levelItem1.checked = choose.length === levelItem1.children.length
            levelItem2.children = this.setChecked(levelItem2.children, !!item.checked, !item.checked, true)
            levelItem2.indeterminate = false
          } else {
            levelItem1.indeterminate = true
            levelItem1.checked = false
            levelItem2.children = this.setChecked(levelItem2.children, !!item.checked, false, true)
            choose = levelItem1.children.filter(itm => itm.checked === true || itm.indeterminate === true)
            if (choose.length === 0) {
              levelItem1.checked = false
              levelItem1.indeterminate = false
            }
          }
        } else if (item.level === 3) {
          let levelItem2 = levelItem1.children[childIndex]
          let level3Choose = levelItem2.children.filter(itm => itm.checked === true)
          if (item.checked) {
            levelItem2.indeterminate = level3Choose.length !== levelItem2.children.length
            levelItem2.checked = !levelItem2.indeterminate
            let choose = levelItem1.children.filter(itm => itm.checked === true)
            levelItem1.indeterminate = choose.length !== levelItem1.children.length
            levelItem1.checked = !levelItem1.indeterminate
          } else {
            levelItem2.indeterminate = true
            levelItem2.checked = false
            levelItem1.indeterminate = true
            levelItem1.checked = false
            if (level3Choose.length === 0) {
              levelItem2.checked = false
              levelItem2.indeterminate = true
            }
            let choose = levelItem1.children.filter(itm => itm.checked === true)
            let halfChoose = levelItem1.children.filter(itm => itm.indeterminate === true)
            if (choose.length === 0 && halfChoose.length === 0) {
              levelItem1.checked = false
              levelItem1.indeterminate = false
            } else {
              levelItem1.checked = choose.length === levelItem1.children.length
              levelItem1.indeterminate = !levelItem1.checked
            }
          }
        }
        this.dataList = data
      },
      // 获取选中节点
      getChooseNodes(appId) {
        let all = []
        this.dataList.map(item => {
          if (item.checked || item.indeterminate) {
            all.push(item.id)
          }
          if (Array.isArray(item.children) && item.children.length) {
            item.children.map(child => {
              if (child.checked || child.indeterminate) {
                all.push(child.id)
              }
              if (Array.isArray(child.children) && child.children.length) {
                child.children.map(childItem => {
                  if (childItem.checked) {
                    all.push(childItem.id)
                  }
                })
              }
            })
          }
        })
        return {
          appId,
          menuIds: all,
          dataScope: this.form.role
        }
      }
    }
  }
</script>

<style lang="scss" scoped>
  $width_1: 300px;
  .tree-container {
    position: relative;
    z-index: 1;
    .tree-view {
      background: $white;
      border-bottom-left-radius: 4px;
      border-bottom-right-radius: 4px;
      font-size: 14px;
      .tree-item {
        height: 36px;
        border-bottom: 1px solid #F0F2F5;
        &-level-2 {
          height: 36px;
          width: $width_1 - 62px;
          margin-right: 20px;

        }
        &-level-3 {
          min-height: 36px;
          flex: 1;
          .tree-item-3 {
            min-width: 80px;
            margin-right: 8px;
          }
        }
        .el-arrow {
          font-size: 14px;
          color: #666666;
          margin-right: 8px;
          cursor: pointer;
        }
        .el-icon-arrow-right {
          transition: all .5s;
        }
        .arrow-down {
          transform: rotate(90deg);
        }
      }
      .tree-item-pad {
        border-bottom: 1px solid #F0F2F5;
        padding-left: 62px;
        line-height: 36px;
        padding-right: 16px;
      }
    }
    .tree-line {
      z-index: 2;
      position: absolute;
      top: 0;
      bottom: 0;
      left: $width_1;
      width: 1px;
      background: $border-line;
    }
  }
  .header {
    height: 38px;
    background: #FAFAFA;
    border-radius: 4px 4px 0px 0px;
    line-height: 38px;
    text-align: center;
    color: $font-important-color;
    font-weight: 500;
  }
  .data-role-view {
    .role {
      background: $white;
      padding: 0 16px;
      height: 36px;
      margin-bottom: 16px;
      .el-checkbox {
        margin-right: 16px;
      }
    }
  }
  .empty-box {
    height: 20vh;
  }
</style>
