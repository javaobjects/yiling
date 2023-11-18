let express = require('express')
let http = require('http')
const path = require('path')

let serve = express()
let port = 5000
let host = '192.168.31.242'

function resolve(dir) {
  return path.join(__dirname, dir)
}

// const projectNameList = process.argv.slice(-1)
// if(projectNameList.length == 0){
//   console.warn('请在npm run test后面带上项目名，比如: npm run test pop')
//   process.exit();
// }
// const projectName = projectNameList[0];

serve.use(express.static('./dist'))
serve.use('/', function (req, res, next) {
  res.sendFile(resolve('dist/index.html'))
})

let httpsServer = http.createServer(serve)
httpsServer.listen(port, function () {
  console.log('app listening at http://%s:%s', host, port)
});
