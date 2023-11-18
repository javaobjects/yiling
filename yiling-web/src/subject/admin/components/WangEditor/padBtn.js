import E from 'wangeditor'

// 获取必要的变量，这些在下文中都会用到
const {$, BtnMenu} = E

class AlertMenu extends BtnMenu {
  constructor(editor) {
    // data-title属性表示当鼠标悬停在该按钮上时提示该按钮的功能简述
    const $elem = E.$(`
      <div class="w-e-menu" data-title="上下左右边距（默认15px，文章写完最后再加）">
        <i class="el-icon-rank" style="font-size: 18px; font-weight: 600;"></i>
      </div>`
    )
    super($elem, editor)
  }

  // 菜单点击事件
  clickHandler() {
    this.command('15px')
  }

  command(value) {
    const editor = this.editor
    let textElem = editor.$textElem.elems[0]

    // 有些情况下 dom 可能为空，比如编辑器初始化
    if (textElem.length === 0) return

    let style = textElem.firstChild.getAttribute('style') ? textElem.firstChild.getAttribute('style') : ''

    //判断当前标签是否具有line-height属性
    if (style && style.indexOf('padding') !== -1) {
      let preDiv = textElem.firstChild
      $(preDiv).removeAttr('style');
      this.unActive()
    } else {
      let div = document.createElement('div');
      for (let i = 0; i < textElem.childNodes.length; i++) {
        let node = textElem.childNodes[i]
        div.appendChild(node.cloneNode(true))
        node.parentNode.removeChild(node)
        i--
      }
      $(div).css('padding', value)
      editor.$textElem.elems[0].appendChild(div)
      this.active()
    }

    // 获取顶级元素
    // $(editor.$textElem.elems[0]).css('padding', value)
  }

  // 菜单是否被激活（如果不需要，这个函数可以空着）
  // 1. 激活是什么？光标放在一段加粗、下划线的文本时，菜单栏里的 B 和 U 被激活，如下图
  // 2. 什么时候执行这个函数？每次编辑器区域的选区变化（如鼠标操作、键盘操作等），都会触发各个菜单的 tryChangeActive 函数，重新计算菜单的激活状态
  tryChangeActive() {
    const editor = this.editor
    let textElem = editor.$textElem.elems[0]
    // 有些情况下 dom 可能为空，比如编辑器初始化
    if (textElem.length === 0) return
    let style = textElem.firstChild.getAttribute('style') ? textElem.firstChild.getAttribute('style') : ''
    if (!style && this.isActive) {
      this.unActive()
    }
  }
}

export default AlertMenu
