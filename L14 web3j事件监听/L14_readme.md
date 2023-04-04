##  L14 web3j事件监听

 概念描述[[参考L8]](https://github.com/andyYuanFZM/Chain33_Dapp_develop/blob/main/L8%20web3.js%E5%90%88%E7%BA%A6%E4%BA%8B%E4%BB%B6/L8_readme.md) 

### 1. 查询合约历史事件  
web3j.ethGetLogs(filter)
过滤器对象：
- fromBlock: 起始区块（最小值支持从1开始）
- toBlock: 终止区块， 这个参数不带代表一直到最大区块
- address: 合约地址
- event: 事件名和参数

示例： 
```  
// 要查询的事件
Event event = new Event("TokenMinted", Arrays.asList(new TypeReference<Uint256>() {},
        new TypeReference<Uint256>() {},
        new TypeReference<Utf8String>() {}));

EthFilter filter = new EthFilter(
                DefaultBlockParameterName.EARLIEST, // 日志的起始区块
                DefaultBlockParameterName.LATEST, // 日志的结束区块
                contractAddress // 查询的智能合约地址
);

// 添加event类型及其参数
filter.addSingleTopic(EventEncoder.encode(event));

// 获取符合过滤器条件的所有日志
EthLog log = web3j.ethGetLogs(filter).send();
```  

### 2. 订阅区块链中的指定事件  
web3j.ethLogFlowable(filter).subscribe(log -> {})

示例：
```  
// 要监听的事件
Event event = new Event("TokenMinted", Arrays.asList(new TypeReference<Uint256>() {},
        new TypeReference<Uint256>() {},
        new TypeReference<Utf8String>() {}));

// 过滤开始区块，结束区块，合约地址
EthFilter filter = new EthFilter(
    DefaultBlockParameterName.EARLIEST, DefaultBlockParameterName.LATEST, contractAddress);
        
filter.addSingleTopic(EventEncoder.encode(event));

web3j.ethLogFlowable(filter).subscribe(log -> {

    System.out.println(log.getTopics());

    // 解码非index参数
    List<Type> results = FunctionReturnDecoder.decode(log.getData(), event.getNonIndexedParameters());
    BigInteger value = (BigInteger) results.get(0).getValue();
    System.out.println("supply is: " +  value);
});
```  
