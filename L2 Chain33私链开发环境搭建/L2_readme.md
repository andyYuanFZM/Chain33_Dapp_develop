## L2 Chain33私链开发环境搭建
### 1.单节点环境意义：
可以进行快速验证，节省时间和成本。

### 2.环境准备：
准备一台Window，Linux或Mac的机器  
golang 1.17+：  下载地址：https://go.dev/dl/  
Git 工具  

### 3.下载代码
```  
#下载插件库代码
git clone https://github.com/33cn/plugin.git
#下载chain33代码
git clone https://github.com/33cn/chain33.git
```  

### 4.编译代码和CLI工具
在plugin目录下编译
```  
# Windows
set GOPROXY=https://mirrors.aliyun.com/goproxy
go env -w CGO_ENABLED=0
go build -o chain33.exe
go build -o chain33-cli.exe github.com/33cn/plugin/cli

# Linux/Unix
export GOPROXY=https://mirrors.aliyun.com/goproxy
make
```  

### 5.修改配置文件

### 6.运行区块链程序
```  
./chain33.exe -f chain33.solo
```  

### 7.通过命令行检查区块链状态
```  
./chain33-cli net peer info
```  

### 8.metamask连接区块链节点
