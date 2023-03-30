##  L12 web3j基本开发环境搭建及常用查询函数

### 1.环境准备

 - 安装 Java Development Kit (JDK)， 在Windows上，还需要设置JAVA_HOME环境变量，以便在VS Code中正确识别JDK
```  
java -version
```  
 - 安装 Maven， Maven是一个用于构建Java项目的工具。可以从Maven官网下载并安装它
```  
mvn -version
```  
 - 安装Visual Studio Code   
 - 在vs code中安装以下插件：  
   Extension Pack for Java: 该插件包括许多有用的Java开发工具，如语法高亮、代码自动完成和调试支持等。  
   Maven for Java: 支持从Maven原型生成项目， 支持产生有效的POM等  
   Debugger for Java: 支持Java的调试  
   
 - 创建maven项目， 
 在VS Code中，右键"Create Maven Project",  再输入group id, artifact id等

### 2. 安装web3j
```  
# 在pom.xml中添加以下依赖
<!-- web3j库 -->
<dependency>
    <groupId>org.web3j</groupId>
    <artifactId>core</artifactId>
    <version>4.9.7</version>
</dependency>
<!-- gson库 -->
<dependency>
  <groupId>com.google.code.gson</groupId>
  <artifactId>gson</artifactId>
  <version>2.9.0</version>
</dependency>
```  

### 3. web3j常用查询和基础转账
 web3j文档： https://docs.web3j.io/4.9.7/getting_started/run_node_locally/
 
 - Web3j.build(new HttpService("URL")): 建立rpc连接
 - web3.ethBlockNumber(): 获取区块高度
 - web3.ethAccounts(): 获取链上账户信息
 - web3.ethGetBalance("地址", DefaultBlockParameterName.LATEST): 获取账户余额
 - Convert.fromWei(balance.toString(), Convert.Unit.ETHER).toString()： 格式化余额
 - web3.ethChainId(): 获取chainid
 - web3.ethGetBlockByNumber(): 根据区块号获取区块信息
 - web3.ethGetTransactionByHash("交易hash"): 根据交易hash获取交易内容
 - web3.ethGasPrice()： 获取gasprice
 - Credentials.create("用户私钥"): 根据私钥获取转账凭证
 - Convert.toWei("转账数量", Convert.Unit.ETHER).toBigInteger()： 格式化转帐金额
 - RawTransaction.createEtherTransaction(nonce, gasPrice, gasLimit, to, value)： 创建交易对象
 - TransactionEncoder.signMessage(交易对象, chainid, 转账凭证)： 签名交易
 - web3.ethSendRawTransaction()： 交易上链
 
 具体使用可以参考同级目录下的: L12.java文件

