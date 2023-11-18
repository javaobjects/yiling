<template>
  <no-if-dialog
    :title="title"
    :destroy-on-close="false"
    width="60%"
    @confirm="confirm"
    :show-confirm="showConfirm"
    :visible.sync="showDialog">
    <div>
      <div class="check-all" v-if="showAllCheck">
        <yl-button 
          type="primary" 
          size="mini" 
          class="button-mini" 
          @click="handleAllCheckChange(true)">
          全选
        </yl-button>
        <yl-button 
          plain 
          type="primary"
          size="mini" 
          class="button-mini" 
          @click="handleAllCheckChange(false)">
          取消全选
        </yl-button>
      </div>
      <div class="tree-container">
        <div class="tree-line"></div>
        <div class="tree-view" v-for="(item, index) in dataList" :key="index">
          <div class="flex-row-left tree-item pad-lr-16">
            <div @click.stop="handleExpandIconClick(item, index)">
              <span class="el-arrow" :class="['el-icon-arrow-right', item.expand ? 'arrow-down' : '']"></span>
              <el-checkbox
                v-model="item.checked"
                :indeterminate="item.indeterminate"
                :disabled="item.disabled"
                @click.native.stop
                @change="(v) => handleCheckChange(item, index)">
              </el-checkbox>
              <span class="font-important-color mar-l-10 bold touch">{{ item.name }}</span>
              <span class="font-title-color font-size-sm mar-l-10" v-show="item.chooseCity">
                已选择{{ item.chooseCity }}个市
                <span v-show="deep === 3">，{{ item.chooseArea }}个区/县</span>
              </span>
            </div>
          </div>
          <el-collapse-transition>
            <div v-show="item.expand">
              <div class="tree-item-pad pad-lr-16" v-for="(child, childIndex) in item.children" :key="childIndex">
                <div class="flex-row-left">
                  <div class="flex-row-left tree-item-level-2">
                    <el-checkbox
                      v-model="child.checked"
                      :indeterminate="child.indeterminate"
                      :disabled="item.disabled"
                      @click.native.stop
                      @change="(v) => handleCheckChange(child, index, childIndex)">
                    </el-checkbox>
                    <span class="font-important-color mar-l-10">{{ child.name }}</span>
                    <span class="font-title-color font-size-sm mar-l-10" v-show="child.chooseArea && deep === 3">已选择{{ child.chooseArea }}个区/县</span>
                  </div>
                  <div class="tree-item-level-3 flex-row-left flex-wrap" v-if="deep === 3">
                    <div class="flex-row-left tree-item-3" v-for="(childItem, childItemIndex) in child.children" :key="childItemIndex">
                      <el-checkbox
                        v-model="childItem.checked"
                        :disabled="item.disabled"
                        @click.native.stop
                        @change="(v) => handleCheckChange(childItem, index, childIndex)">
                      </el-checkbox>
                      <span class="font-title-color mar-l-10">{{ childItem.name }}</span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </el-collapse-transition>
        </div>
      </div>
    </div>
  </no-if-dialog>
</template>

<script>
  import { getLocationTree } from '@/subject/pop/api/common'
  import noIfDialog from '@/common/components/Dialog/dialogNoIf'

  export default {
    name: 'CitySelect',
    components: {
      noIfDialog
    },
    props: {
      // 深度 3：到区；2：到市
      deep: {
        type: Number,
        default: 3
      },
      // 是否展示
      show: {
        type: Boolean,
        default: true
      },
      // 标题
      title: {
        type: String,
        default: '设置区域'
      },
      // 回显数据-树-每条数据selected标识是否选中（bool）
      initData: {
        type: Array,
        default: () => []
      },
      // 回显数据-code字符串数组
      // 注：只需要最后一级-区code的字符串组合
      initIds: {
        type: Array,
        default: () => []
      },
      // 是否展示确认
      showConfirm: {
        type: Boolean,
        default: true
      },
      // 是否禁选
      disabled: {
        type: Boolean,
        default: false
      },
      // 是否展示全选
      showAllCheck: {
        type: Boolean,
        default: false
      }
    },
    computed: {
      showDialog: {
        get() {
          if (this.show) {
            this.getData()
          }
          return this.show
        },
        set(val) {
          this.$emit('update:show', val)
        }
      }
    },
    watch: {
      initData: {
        handler(newVal, oldVal) {
          this.getData()
        },
        deep: true
      },
      initIds: {
        handler(newVal, oldVal) {
          this.getData()
        },
        deep: true,
        immediate: true
      }
    },
    data() {
      return {
        dataList: [],
        allChecked: false
      }
    },
    mounted() {
      const session = sessionStorage.getItem('AREA_JSON_DATA')
      if (!session) {
        this.getData()
      }
    },
    methods: {
      // 全选、反选
      handleAllCheckChange(e) {
        if (e) {
          this.dataList = this.setChecked(this.$common.clone(this.dataList), e, false, true)
        } else {
          this.dataList = this.setChecked(this.$common.clone(this.dataList), e, false, true)
        }
        this.dataList.map(item => {
          this.getCheckedNum(item)
        })
      },
      // 数据处理器
      handleData(data = []) {
        let initData = this.$common.clone(this.initData)
        let handleData = []
        data.map((item, index) => {
          let level1 = initData.find(find => find.code == item.code)
          if (level1) {
            handleData.push(this.handleInitData(level1))
          } else {
            if (this.deep === 3) {
              handleData.push(this.handleInitData(item, this.$common.clone(this.initIds)))
            } else if (this.deep === 2){
              handleData.push(this.handleInitDataDeep2(item, this.$common.clone(this.initIds)))
            }
          }
        })
        return handleData
      },
      // 初始数据处理器
      handleInitData(item = {}, initIds = []) {
        let level2Choose = 0
        let level2HalfChoose = 0
        let level3ChooseTotal = 0
        if (Array.isArray(item.children) && item.children.length) {
          item.children = item.children.map((child, childIndex) => {
            let level3Choose = 0
            if (Array.isArray(child.children) && child.children.length) {
              child.children = child.children.map(childItem => {
                let isSelect = initIds.length ? initIds.findIndex(code => code == childItem.code) : -1
                childItem.level = 3
                if (isSelect > -1) {
                  childItem.checked = true
                  initIds.splice(isSelect, 1)
                } else {
                  childItem.checked = !!childItem.selected
                }
                if (childItem.checked) {
                  level3Choose++
                }
                childItem.disabled = this.disabled
                return childItem
              })
            }
            child.level = 2
            child.checked = level3Choose === child.children.length
            child.indeterminate = level3Choose !== child.children.length && level3Choose !== 0
            child.chooseArea = level3Choose
            if (child.checked) level2Choose++
            if (child.indeterminate) level2HalfChoose++
            level3ChooseTotal += level3Choose
            child.disabled = this.disabled
            return child
          })
        }
        item.level = 1
        item.checked = level2Choose === item.children.length
        item.indeterminate = !item.checked && (level2HalfChoose > 0 || level2Choose > 0)
        item.chooseCity = level2Choose + level2HalfChoose
        item.chooseArea = level3ChooseTotal
        item.disabled = this.disabled
        return item
      },
      // 初始数据处理器
      handleInitDataDeep2(item = {}, initIds = []) {
        let level2Choose = 0
        if (Array.isArray(item.children) && item.children.length) {
          item.children = item.children.map((child, childIndex) => {
            let isSelect = initIds.length ? initIds.findIndex(code => code == child.code) : -1
            child.level = 2
            if (isSelect > -1) {
              child.checked = true
            } else {
              child.checked = !!child.selected
            }
            if (child.checked) {
              level2Choose++
            }
            child.level = 2
            child.indeterminate = false
            child.disabled = this.disabled
            return child
          })
        }
        item.level = 1
        item.checked = level2Choose === item.children.length
        item.indeterminate = !item.checked && level2Choose > 0
        item.chooseCity = level2Choose
        item.disabled = this.disabled
        return item
      },
      // 获取数据源
      async getData() {
        const session = sessionStorage.getItem('AREA_JSON_DATA')
        if (session && session.indexOf('[') > -1) {
          this.dataList = this.handleData(JSON.parse(session))
        } else {
          let data = await getLocationTree(2)
          if (data && data.list && data.list.length) {
            sessionStorage.setItem('AREA_JSON_DATA', JSON.stringify(data.list))
            this.dataList = this.handleData(data.list)
          }
        }
        this.allChecked = false
      },
      // expand按钮点击
      handleExpandIconClick(item, index) {
        let data = this.$common.clone(this.dataList)
        if (data[index]) {
          data[index].expand = !data[index].expand
        }
        this.dataList = data
      },
      // 递归设置是否选中
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
      // 计算选中数量数据
      getCheckedNum(item) {
        let level2Choose = 0
        let level3Choose = 0
        if (Array.isArray(item.children) && item.children.length) {
          item.children.map(child => {
            let level3ChooseSinge = 0
            if (child.checked || child.indeterminate) {
              level2Choose++
            }
            if (Array.isArray(child.children) && child.children.length) {
              child.children.map(childItem => {
                if (childItem.checked) {
                  level3Choose++
                  level3ChooseSinge++
                }
              })
            }
            child.chooseArea = level3ChooseSinge
          })
          item.chooseCity = level2Choose
          item.chooseArea = level3Choose
        }
        return item
      },
      // checkbox选中处理
      handleCheckChange(item, index, childIndex) {
        this.dataList = this.checkChange(this.$common.clone(this.dataList), item, index, childIndex)
      },
      // checkbox选中处理数据
      checkChange(data, item, index, childIndex) {
        if (Array.isArray(data) && data.length) {
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
            let choose = levelItem1.children.filter(itm => itm.checked === true || itm.indeterminate === true)
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
                levelItem2.indeterminate = false
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
          levelItem1 = this.getCheckedNum(levelItem1)
        }
        return data
      },
      // 获取选中节点
      getChooseNodes() {
        let all = []
        let province = 0
        let city = 0
        let area = 0
        let data = this.$common.clone(this.dataList)
        data.map(item => {
          if (item.checked || item.indeterminate) {
            let itemObj = {
              code: item.code,
              name: item.name,
              children: []
            }
            if (Array.isArray(item.children) && item.children.length) {
              item.children.map(child => {
                if (child.checked || child.indeterminate) {
                  let obj = {
                    code: child.code,
                    name: child.name,
                    children: []
                  }
                  if (Array.isArray(child.children) && child.children.length && this.deep === 3) {
                    child.children.map(childItem => {
                      if (childItem.checked) {
                        obj.children.push({
                          code: childItem.code,
                          name: childItem.name,
                          children: []
                        })
                        area++
                      }
                    })
                  }
                  itemObj.children.push(obj)
                  city++
                }
              })
            }
            all.push(itemObj)
            province++
          }
        })
        let desc = ''
        if (this.deep === 3) {
          desc = `已选择${province}个省/直辖区，${city}个市，${area}个区/县`
        } else if (this.deep === 2) {
          desc = `已选择${province}个省/直辖区，${city}个市`
        }
        return {
          nodes: all,
          desc: desc
        }
      },
      confirm() {
        let data = this.getChooseNodes()
        this.$log(data)
        this.$emit('choose', data)
      }
    }
  }
</script>

<style lang="scss" scoped>
  $width_1: 340px;
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
  .check-all {
    height: 40px;
    line-height: 40px;
    padding-left: 38px;
    position: sticky;
    border-bottom: 1px solid #F0F2F5;
    span {
      color: #333333;
      font-weight: bold;
    }
    top: 0;
    z-index: 2;
    background: $white;
  }
  .button-mini{
    padding: 4px 8px;
  }
</style>
