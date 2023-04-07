##  L15 go-sdk基本开发环境搭建及常用方法

### 1.环境准备

 - 安装 golang 1.18+，下载地址： https://go.dev/dl/  
```  
go version
```  

 - vscode中安装go语言插件

 - 新建目录

 - 新建go模块
 ```  
go mod init 模块名（比如：com/l15demo）
```  

### 2. 安装go-ethereum依赖
```  
# 根据实际需要安装所需的包
go get github.com/ethereum/go-ethereum/ethclient
```  

### 3. go-sdk常用查询和基础转账 
 具体可以参考同级目录下的: l15demo/main.go文件

