<template>
  <el-empty
    class="my-yl-cus-empty"
    image="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAIwAAABaCAMAAABdeU4+AAACjlBMVEUAAAD////////////////////////////f39/m5ub////q6ur////r6+v////t7e3////////h4eH////k5OTo6Ojg4ODj4+Pk5OTl5eX29vb29vbf39/i4uL4+Pjj4+Pj4+P4+Pjd3d3f39/g4ODh4eH5+fnh4eHd3d35+fn19fXe3t729vb29vbk5OT29vb29vbg4ODh4eHf39/h4eH4+Pj4+Pj4+Pje3t719fXf39/g4ODg4ODd3d3e3t7e3t7f39/f39/29vbf39/39/fe3t7e3t7d3d339/f39/f19fXf39/d3d319fXe3t719fXf39/d3d3d3d3e3t7e3t7f39/f39/d3d339/fe3t7e3t739/fe3t7e3t719fX29vbe3t729vbd3d329vbd3d3e3t7e3t7d3d339/f19fXe3t7e3t729vb29vbd3d329vbd3d3e3t7e3t7d3d3e3t7e3t729vbe3t729vb19fXd3d319fXe3t719fX19fXe3t719fXd3d3d3d3e3t7e3t729vbd3d329vb29vbe3t7e3t7e3t719fXe3t719fX19fXe3t7d3d3d3d3e3t719fXe3t7d3d3e3t729vb29vbe3t7d3d3d3d3d3d329vbd3d3e3t7e3t719fX19fX19fXd3d3e3t719fXe3t7e3t719fXe3t719fXd3d319fX29vbd3d3f39/e3t729vb29vbe3t7d3d329vbd3d329vbd3d329vbe3t729vbe3t7f39/29vbd3d3e3t7f39/g4ODh4eHi4uLj4+Pk5OTn5+fo6Ojp6enq6urr6+vs7Ozt7e3v7+/w8PDx8fHy8vLz8/P09PT19fX4+Pj5+fn6+vqzT75+AAAAwXRSTlMAAQIDBAUGBwgKCgwMDQ0ODg8RERMWGRscHR0eICMjJCUlJigpKiorLS40NjY3ODg5Ojs/Q0ZLTE5OUFFSU1RWV1hYWVtdXmJiZWZoampra3BxcnN1dnd4eXx9foSFhYiKjo+PkJGVlpeam6Cio6amp6iprbCxsbKys7a2t7m6u7u9vsHCxMXFxsjJys3Ozs/R09TW1tfa3d3f4OHi4+Pk5efn6Onq7Ozt7u7v7/Hx8vP09fX29/j4+fn6+/39/v7+sYPHxQAABC9JREFUaN7t2vdTE0EUB/AgKqKiUmwoKthQsYAVe8GCigW7KPYCYsGOir1QVCzYBSwooGIlNOORryUaxRZ1w/03BhkdSS7hyu6hM/d+vvf2k73b3XeZ0+n+02g4OfUxJMbj1MkNWVh8DkFWHPKhb3E/jKwxXlKzvMZk4bA7dcwa5LSVk9c2B2toW6JRHCovM6wYc+haRnPcWLm54zhuNE1LmB5z5WfPgz6MniUoH2uV5K9FfhAtS+tL2KFoRbjvwqXWdCye6UjzVFgiDemeVDYYGj/LNrm7aGw363CXwg0Puot1yqvMhz6UxgSH6rFAaY0JnGEUnUdvlIGboKzCwFLMorUoZ6F0oJL87vexkt52tRr3u8vPbnMV2xrQwzTYhqtt5CY3O4XjTWieKk2O41QzmZ3dHlz0o3ve+l3EPnmdXzzuBNDuRALuIF5O3iI87UO/YezzFIulZ02EYRiLVnqYAZOk5gwqxww2LxkzUD5IWkZwIZazeuVZjsJgSV10FhLdWGHcEpElobdveQZHPdi9DXocwZmWYi9udAAXfHQMw+c8DjQSee0G3OzI9k25401sEHflEjzp5yEUjeWM21iwVL8nWCoq3flrc8HWntIkPRMLnFdTiAGKR0qxjNC7qiUSwzuJH2aUdhFv6VwK8w9ntRRjeP49tojHbMJ755UoYKwVXDexlq5chZUpxjY10WIxs11NDBXMZ/H9SDw+M8Z8xXaxmB34yhjzHWfFYs7hO2OMFXqRG7FHMWdljOFNmCYOMx1vedaYb8hpJ6ZK+xv4xhzDv8O14XX+Bes14jre8ewxVhPAGeoIDnhrVQHDV30ycXX9Lc6ZPlXxamDohIbRMBpGw2gYDaNhNIyGURfTInxFhlqY8BauJP5RyWWEELUwpCw5yl9Y4h2ZQmpCNUx1pER6O1B6JRQRUh8YQooSetWihCSRv0JlDCHGpJA/FN84I6lXjI0T51tj6X+bkPrGEHK7f7VlSBH5FzCkaIhO1+Eh+Tcw5KG/bqODhbwCYW8heGU/7kZdtiPmDSzsMRa8sR83WwhjRiV7TCXMjhiB2/QFL62sLdaX+OJ4mwQeYGLCR9aYjzARxwdYYGnbpob1U2OBw8RUL22BTY+QD8AHhiuqpj4R2PQcjwNbVIJxVNYe789xYIu+SfYai/k1O8lrs6X2aH8dlPYthMph30L8aq6mptQHJWWqt5O2c+avtlO1KEue6e+6IV+V8UINyIuMVa4b8t/NVnjs/nssIff2x4b7Svl4InB87M7LRtoM4+WdseMD5X3y0bR3REzCiVwajNwTCTERvZsq/3KheY/BU2Li9p7OLpFKKMk+vTcuZsrgHs0ZfFDRqtOAoRFRC5et37z74LGTmVdu5T149KzkudH4vOTZowd5t65knjx2cPfm9csWRkUMHdCplbTqPwGLxHKZ/2HinQAAAABJRU5ErkJggg=="
    :description="description">
  </el-empty>
</template>

<script>
  export default {
    name: 'Empty',
    props: {
      // 描述
      description: {
        type: String,
        default: '暂无信息'
      }
    },
    methods: {
    }
  }
</script>

<style lang="scss" scoped>
  .my-yl-cus-empty {
    text-align: center;
    ::v-deep .el-empty__image {
      img {
        width: 70px;
      }
    }
    ::v-deep .el-empty__description {
      font-size: 12px;
      margin-top: 7px;
      color: $font-title-color;
    }
  }
</style>
