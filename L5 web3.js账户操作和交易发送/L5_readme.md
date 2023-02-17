##  L5 web3.js账户操作和发送交易

### 1. 账户信息操作
- web3.eth.getAccounts():  返回当前节点控制的账户，如果本地节点没有创建账户, 则报错返回：ErrAccountNotExist
- 节点上通过chain33-cli命令行工具创建钱包
```  
# 生成助记词
./chain33-cli.exe seed generate -l 0
# 保存助记词
./chain33-cli.exe seed save -s "上一步返回" -p test1234
# 解锁钱包
./chain33-cli.exe wallet unlock -p test1234 -t 0
# 创建账户
./chain33-cli.exe account create -l test1234 -t 2
```  
- web3.eth.personal.newAccount("账户别名") ： 返回创建的账户地址，如果别名有重复，则报错返回：ErrLabelHasUsed，注意：参数和web3.js原生的有区别
- web3.eth.personal.importRawKey("用户私钥", "账户别名")： 将给定的私钥导入节点上的钱包，返回导入私钥的账户地址
- web3.eth.getBalance("用户地址"): 返回用户地址下的余额， 精度10的18次方

### 2. 签名发送转账交易
方式一（不推荐）：使用保存在节点上的私钥签名并发送交易上链。  私钥存在节点上不是安全的做法，容易导致私钥泄漏。   
方式二（推荐）： 利用私钥在链下签名，签名完成后再通过接口发送到区块链上。  

- web3.eth.accounts.signTransaction： 本地（链下）签名交易
- web3.eth.sendSignedTransaction： 发送已经签好名的交易到区块链上

