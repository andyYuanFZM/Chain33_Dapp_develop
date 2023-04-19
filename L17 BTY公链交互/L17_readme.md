##  L17 通过SDK同BTY公链交互

下文介绍在兼容以太坊接口之后，如何使用各语言SDK同BTY主链进行交互。  

### 1.BTY环境说明

 可以通过BTY公共节点进行交互；也可以自行部署BTY节点，再和自建节点交互。 前者使用比较方便，但有时受限于网络情况，不能保证一直稳定，可用于开发验证；后者需要自备服务器同步节点，但是连接稳定。  
 - 公共节点： 
   rpc接口：https://mainnet.bityuan.com/eth  
   websocket接口： wss://mainnet.bityuan.com/ethws  
   
 - 自建节点：
   1. 节点下载地址： https://github.com/bityuan/bityuan/releases  (从0开始同步，需要耗时一周以上)
   2. 以太坊兼容端口（可以用ethers.js, web3j, go-ethereum等SDK包进行交互）, 如果允许远程访问，需要修改配置文件中以下两个端口参数（去掉前面的localhost）
```  
[rpc.sub.eth]
httpAddr=":8546"
wsAddr=":8547"
``` 
   3. 区块链节点分全节点和分片节点（根据配置文件中的配置项区分）   
      3.1 全节点：存储全量信息，目前占用空间300G左右  
      3.2 分片节点： 会删除一些历史日志信息，目前占用空间100G左右  
      3.3 注意事项： 由于分片节点删除历史日志信息，会影响合约历史事件日志的查询；所以在需要用到智能合约事件监听以及历史事件日志查询的情况下，要用全节点。  其它情况下，分片节点就能满足使用要求。 


### 2. 通过web3j和BTY区块链交互
web3j是为JAVA语言提供的SDK  

2.1 添加web3j依赖  
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

2.2 常用接口：  
web3j本身接口不止以下这些，但并不是所有接口都适用于BTY， 下面列出的是比较常用的接口：  
 - web3.ethGetBalance("地址", DefaultBlockParameterName.LATEST): 获取账户余额
 - Convert.fromWei(balance.toString(), Convert.Unit.ETHER).toString()： 格式化余额（wei-->bty）
 - web3.ethChainId(): 获取chainid
 - web3.ethGetBlockByNumber(): 根据区块号获取区块信息
 - web3.ethGetTransactionByHash("交易hash"): 根据交易hash获取交易内容
 - web3.ethGasPrice()： 获取gasprice
 - Keys.createEcKeyPair().getPrivateKey(): 获取用户私钥
 - Credentials.create("用户私钥"): 根据私钥获取转账凭证
 - Credentials.create("用户私钥").getAddress()： 获取用户地址
 - Convert.toWei("转账数量", Convert.Unit.ETHER).toBigInteger()： 格式化转帐金额 (bty-->wei)
 - RawTransaction.createEtherTransaction(nonce, gasPrice, gasLimit, to, value)： 创建交易对象
 - TransactionEncoder.signMessage(交易对象, chainid, 转账凭证)： 签名交易 
 - web3.ethSendRawTransaction()： 交易上链  

 注意事项： BTY原来采用的是BTC的地址格式，如果资产在BTC地址格式下， 同样可以通过使用该地址的私钥签名，转给以太坊格式的地址（0x）开头。 

 2.3 使用参考： 
 ```  
 /**
 * 通过JAVA代码同BTY区块链交互
 *
 */
public class L17 
{
    // 私钥，用于转账测试(from)
    private static final String privateKey = "0x13f9a8ae7ba07878fb37e93a5fd624e6fedd028d9556581618c29931855c52e6";
    // 私钥对应的地址，用于转账测试(from)
    private static final String address = "0xcccda5358462f3cb7bf781b1003da66498bf4a56";
    // to地址，用于转账测试(to)
    private static final String toaddress = "0xFd89C32962f19bcEA69B76093a64A03618cB33BE";

    public static void main( String[] args ) throws InterruptedException, ExecutionException, IOException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException
    {
        // 建立连接,此处用的是临时BTY节点，后面要替换成自己的BTY节点
        Web3j web3 = Web3j.build(new HttpService("https://mainnet.bityuan.com/eth"));

        // 获取区块高度
        EthBlockNumber  ethBlockNumber = web3.ethBlockNumber().sendAsync().get();
        BigInteger blockNumber = ethBlockNumber.getBlockNumber();
        System.out.println("BlockNumber:" + blockNumber + "\r\n");

        // 查询账户余额
        BigInteger balance = web3.ethGetBalance(address, DefaultBlockParameterName.LATEST).send().getBalance();
        System.out.println("Balance before format:" + balance + "\r\n");

        // 格式化账户余额
        String balanceStr = Convert.fromWei(balance.toString(), Convert.Unit.ETHER).toString();
        System.out.println("Balance after format:" + balanceStr + "\r\n");

        // 获取chainid
        BigInteger chainId = web3.ethChainId().send().getChainId();
        System.out.println("ChainId:" + chainId + "\r\n");

        // 根据区块号获取区块信息
        DefaultBlockParameterNumber defaultBlockParameterNumber = new DefaultBlockParameterNumber(26000000);
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
        // 获取第一笔交易的hash值，用于下面的查询交易信息
        String txHash = txInfos.get(0).getHash();

        // 根据hash获取交易信息
        EthTransaction transaction = web3.ethGetTransactionByHash(txHash).sendAsync().get();
        Optional<Transaction> optionalTransaction = transaction.getTransaction();
        StringBuilder txInfo = new StringBuilder();
        if(optionalTransaction.isPresent()){
            Transaction transactionInfo = optionalTransaction.get();
            txInfo.append(gson.toJson(transactionInfo));
        }
        System.out.println("TransactionInfo:" + txInfo.toString() + "\r\n");

        // 生成以太坊地址和私钥的方法
        // ECKeyPair keyPair = Keys.createEcKeyPair();
        // String privateKey = Numeric.toHexStringNoPrefix(keyPair.getPrivateKey());
        // Credentials credentials = Credentials.create(keyPair);
        // String address = credentials.getAddress();
        // System.out.println("address:" + address);
        // System.out.println("privateKey:" + privateKey);

        // 转账
        // 1. 加载转账所需的凭证，用私钥
        Credentials credentials = Credentials.create(privateKey);
        // 2. 获取nonce，交易笔数
        BigInteger nonce = web3.ethGetTransactionCount(address, DefaultBlockParameterName.LATEST).send().getTransactionCount();

        // 3. 手续费
        EthGasPrice ethGasPrice = web3.ethGasPrice().sendAsync().get();
        BigInteger gasPrice = ethGasPrice.getGasPrice();
        BigInteger gasLimit = BigInteger.valueOf(60000L);

        // 转账金额
        BigInteger value = Convert.toWei("0.1", Convert.Unit.ETHER).toBigInteger(); 

        // 4. 创建RawTransaction交易对象
        RawTransaction rawTransaction = RawTransaction.createEtherTransaction(nonce, gasPrice, gasLimit, toaddress, value);
        //签名Transaction，这里要对交易做签名
        byte[] signMessage = TransactionEncoder.signMessage(rawTransaction, chainId.longValue(), credentials);
        String hexValue = Numeric.toHexString(signMessage);
        //发送交易
        EthSendTransaction ethSendTransaction = web3.ethSendRawTransaction(hexValue).sendAsync().get();
        System.out.println("转账交易的hash值" + ethSendTransaction.getTransactionHash());

        // 确认交易上链
        for (int i = 0; i < 10; i++) {
            EthGetTransactionReceipt receipt = web3.ethGetTransactionReceipt(ethSendTransaction.getTransactionHash()).sendAsync().get();
            if (receipt.getTransactionReceipt().isPresent()) {
                // 查询转账后from地址下余额
                balance = web3.ethGetBalance(address, DefaultBlockParameterName.LATEST).send().getBalance();
                balanceStr = Convert.fromWei(balance.toString(), Convert.Unit.ETHER).toString();
                System.out.println("Balance after transfer:" + balanceStr + "\r\n");

                // 查询转账后to地址下余额
                balance = web3.ethGetBalance(toaddress, DefaultBlockParameterName.LATEST).send().getBalance();
                balanceStr = Convert.fromWei(balance.toString(), Convert.Unit.ETHER).toString();
                System.out.println("Balance after transfer:" + balanceStr + "\r\n");
                break;
            }
            Thread.sleep(5000);
        }

    }
}

 ```  
