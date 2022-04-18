# Java实现服务端程序说明

## 关于启动时参数设定
#### 1）端口
默认8080，且输入限定在了 `[1024, 49151]` 的范围内。
#### 2）ip
默认127.0.0.1
#### 3）线程数
线程数默认根据cpu核心数分配，指定不宜过大，可能会宕机
#### 4）代理服务器
尚未实现

## 需求分析
### GET
#### 请求中url的几种可能
1. 文件路径：如/index.html, /test/test.html，返回文件内容;
2. /api/check：返回`/data/data.txt`中的文件内容
3. /api/list：返回`/data/data.json`中的文件内容
4. /api/search?id=`value1`&name=`value2`：返回`/data/data.json`中匹配的所有条目，如果没有匹配的就返回`/data/not_found.json`中的所有条目
5. 其他情况，一律返回`/static/404.html`中的内容

#### Content-Type
1. text/html
2. text/javascript
3. text/css
4. application/json
5. text/plain