let callbacks = []
const qrCodeCDN = 'https://yl-public.oss-cn-zhangjiakou.aliyuncs.com/web/prd/package/qrcode.min.js'

/*
* 使用示例
*
* html:
* <div ref="qrCode"></div>
*
* js:
loadQrCode(() => {
  new window.QRCode(this.$refs.qrCode, {
    width: 120,
    height: 120,
    text: 'http://www.baidu.com',
    correctLevel: 3
  })
})
* */

function loadedScript() {
  return window.QRCode
}

const dynamicLoadScript = (callback, src = qrCodeCDN) => {
  const existingScript = document.getElementById(src)
  const cb = callback || function() {}
  if (!existingScript) {
    const script = document.createElement('script')
    script.src = src // src url for the third-party library being loaded.
    script.id = src
    document.body.appendChild(script)
    callbacks.push(cb)
    const onEnd = 'onload' in script ? stdOnEnd : ieOnEnd
    onEnd(script)
  }
  if (existingScript && cb) {
    if (loadedScript()) {
      cb(null, existingScript)
    } else {
      callbacks.push(cb)
    }
  }
  function stdOnEnd(script) {
    script.onload = function() {
      // this.onload = null here is necessary
      // because even IE9 works not like others
      this.onerror = this.onload = null
      for (const cb of callbacks) {
        cb(null, script)
      }
      callbacks = null
    }
    script.onerror = function() {
      this.onerror = this.onload = null
      cb(new Error('Failed to load ' + src), script)
    }
  }
  function ieOnEnd(script) {
    script.onreadystatechange = function() {
      if (this.readyState !== 'complete' && this.readyState !== 'loaded') return
      this.onreadystatechange = null
      for (const cb of callbacks) {
        cb(null, script) // there is no way to catch loading errors in IE8
      }
      callbacks = null
    }
  }
}

export default dynamicLoadScript
