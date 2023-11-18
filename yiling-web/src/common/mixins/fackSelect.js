let lock = true;
let el = null;
// const MousedownEvent = new Event('mousedown', {bubbles:true});
// const MouseupEvent = new Event('mouseup', {bubbles:true});

const MouseEvent = function (eventType, params) {
  params = params || { bubbles: false, cancelable: false };
  var mouseEvent = document.createEvent('MouseEvent');
  mouseEvent.initMouseEvent(eventType, params.bubbles, params.cancelable, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);
  return mouseEvent;
}

const MousedownEvent = new MouseEvent('mousedown', {bubbles: true, cancelable: false});
const MouseupEvent = new MouseEvent('mouseup', {bubbles: true, cancelable: false});

const fakeClickOutSide = () => {
  document.dispatchEvent(MousedownEvent);
  document.dispatchEvent(MouseupEvent);
  lock = true; // console.log('dispatchEvent');
};
const mousedownHandle = e => {
  let classList = e.target.classList;
  if (classList.contains('el-select__caret') || classList.contains('el-input__inner') || classList.contains('el-range-input')) {
    lock = false;
    return;
  }
  if (lock) return;
  fakeClickOutSide();
};
const mousewheelHandle = e => {
  if (lock
    || !e.target.classList.length
    || e.target.classList.contains('el-select-dropdown__item')
    || e.target.parentNode.classList.contains('el-select-dropdown__item')
    || e.target.classList.contains('el-tree-node__content')
    || e.target.parentNode.classList.contains('el-tree-node__content')
    || e.target.classList.contains('el-time-spinner__item')
    ) return;
  fakeClickOutSide();
};
const eventListener = (type) => {
  el[type + 'EventListener']('mousedown', mousedownHandle);
  window[type + 'EventListener']('mousewheel', mousewheelHandle);
  window[type + 'EventListener']('DOMMouseScroll', mousewheelHandle); // fireFox 3.5+
}
export default {
  mounted() {
    el = this.$root.$el;
    el.addFakeClickOutSideEventCount = el.addFakeClickOutSideEventCount || 0;
    (! el.addFakeClickOutSideEventCount) && this.$nextTick(() => {
      eventListener('add');
    });
    el.addFakeClickOutSideEventCount += 1;
  },
  destroyed() {
    eventListener('remove');
    el.addFakeClickOutSideEventCount -= 1;
  }
}
