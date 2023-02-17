##  L4 web3.js基本开发环境搭建及常用查询函数

### 1.环境准备

 - 安装nodejs
```  
node -v
npm -v
```  
 - 安装Visual Studio Code
 - 在vs code中安装code runner插件

### 2. 安装web3.js
```  
npm install web3 
```  

### 3. web3.js常用查询  
 web3.js中文文档： https://learnblockchain.cn/docs/web3.js/  
 
```  
# 连接区块链
var Web3 = require('web3');
var web3 = new Web3(new Web3.providers.HttpProvider("区块链接口"));
``` 

以下列举的都是适用于chain33的常用查询接口  
 - Web3.modules: 返回包含所有子模块类的对象，主要用到Eth，Net，Personal子模块，Shh, Bzz这些用不上
   - Eth: 用来和区块链网络交互
   - Net: 网络属性相关
   - Persion: 区块链账户相关
 - Web3.version: 所引用的Web3的版本号  
 - web3.currentProvider：返回当前通信服务提供器
 - web3.eth.currentProvider：同上，chain33兼容eth的连接
 - web3.eth.getChainId(): 返回区块链节点的chainid
 - web3.eth.net.getId(): 获取当前网络的ID
 - web3.eth.net.isListening(): 当前节点是否处于监听连接状态，是的话返回true
 - web3.eth.net.getPeerCount(): 获取对等节点的数量
 - web3.eth.net.isListening(): 当前节点是否处于监听连接状态，是的话返回true
 - web3.eth.getHashrate():  返回区块的difficulty值
 - web3.eth.getGasPrice():  返回当前gas价格
 - web3.eth.getBlockNumber():  返回当前最大的区块高度
 - web3.eth.getBlock("区块高度/区块hash值", true/false): 返回区块信息,true/false: 要不要带交易详情
 - web3.eth.getBlockTransactionCount(区块高度): 返回对应区块中交易数量
 - web3.eth.getTransaction("交易hash"): 返回对应交易hash的交易对象
 - web3.eth.getTransactionReceipt("交易hash"): 返回交易收据
 - web3.eth.getTransactionCount("要查询的交易地址"): 返回交易数量
