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
    <version>5.0.0</version>
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
 
```  
// 建立连接
Web3j web3 = Web3j.build(new HttpService("http://localhost:8545"));

// 获取区块高度
EthBlockNumber  ethBlockNumber = web3.ethBlockNumber().sendAsync().get();
BigInteger blockNumber = ethBlockNumber.getBlockNumber();
System.out.println("BlockNumber:" + blockNumber + "\r\n");

// 获取链上所有账户
EthAccounts ethAccounts = web3.ethAccounts().sendAsync().get();
List<String> accounts = ethAccounts.getAccounts();
System.out.println("Accounts:" + accounts + "\r\n");

// 查询账户余额
BigInteger balance = web3.ethGetBalance("0x4797A444f34C26e71803A1d98D5031a3cAE70650", DefaultBlockParameterName.LATEST).send().getBalance();
System.out.println("Balance before format:" + balance + "\r\n");

// 格式化账户余额
String balanceStr = Convert.fromWei(balance.toString(), Convert.Unit.ETHER).toString();
System.out.println("Balance after format:" + balanceStr + "\r\n");

// 获取chainid
BigInteger chainId = web3.ethChainId().send().getChainId();
System.out.println("ChainId:" + chainId + "\r\n");

// 根据区块号获取区块信息
DefaultBlockParameterNumber defaultBlockParameterNumber = new DefaultBlockParameterNumber(100);
EthBlock ethBlock = web3.ethGetBlockByNumber(defaultBlockParameterNumber,true).sendAsync().get();
EthBlock.Block block = ethBlock.getBlock();
Gson gson = new Gson();
String info = gson.toJson(block);
System.out.println("BlockInfo:" + info + "\r\n");

// 获取区块交易信息
List<EthBlock.TransactionResult> transactions = block.getTransactions();
List<Transaction> txInfos = new ArrayList<>();
transactions.forEach(txInfo->{
    Transaction transaction = (Transaction)txInfo;
    txInfos.add(transaction);
});
String transactionInfos = gson.toJson(txInfos);
System.out.println("TransactionInfos:" + transactionInfos + "\r\n");

// 根据hash获取交易信息
EthTransaction transaction = web3.ethGetTransactionByHash("0x6eb176f7a3c0b94ac7109912e3742484727a1e8af6e5b870a80afacedda18ffa").sendAsync().get();
Optional<Transaction> optionalTransaction = transaction.getTransaction();
StringBuilder txInfo = new StringBuilder();
if(optionalTransaction.isPresent()){
    Transaction transactionInfo = optionalTransaction.get();
    txInfo.append(gson.toJson(transactionInfo));
}
System.out.println("TransactionInfo:" + txInfo.toString() + "\r\n");

// 转账
// 1. 加载转账所需的凭证，用私钥
Credentials credentials = Credentials.create("0x76491916cf0e70437cbed8c2ce9ac2241221e56f8e64ec74e3282b07f24018e1");
// 2. 获取nonce，交易笔数
BigInteger nonce = web3.ethGetTransactionCount("0x4797A444f34C26e71803A1d98D5031a3cAE70650", DefaultBlockParameterName.LATEST).send().getTransactionCount();

// 3. 手续费
EthGasPrice ethGasPrice = web3.ethGasPrice().sendAsync().get();
BigInteger gasPrice = ethGasPrice.getGasPrice();
// 注意手续费的设置，这块很容易遇到问题
BigInteger gasLimit = BigInteger.valueOf(60000L);
// 转账地址
String to = "0xFd89C32962f19bcEA69B76093a64A03618cB33BE";
// 转账金额
BigInteger value = Convert.toWei("100", Convert.Unit.ETHER).toBigInteger(); 
// 4. 创建RawTransaction交易对象
RawTransaction rawTransaction = RawTransaction.createEtherTransaction(nonce, gasPrice, gasLimit, to, value);
//签名Transaction，这里要对交易做签名
byte[] signMessage = TransactionEncoder.signMessage(rawTransaction, chainId.longValue(), credentials);
String hexValue = Numeric.toHexString(signMessage);
//发送交易
EthSendTransaction ethSendTransaction = web3.ethSendRawTransaction(hexValue).sendAsync().get();
String tx = gson.toJson(ethSendTransaction);
System.out.println(tx);
``` 

