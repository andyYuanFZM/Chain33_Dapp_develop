##  L8 web3.js合约事件

事件是Solidity合约中的一种通信机制，它允许合约在执行过程中发出通知，以便应用程序或其他合约可以监听这些通知并采取相应的行动。

事件通常用于以下情况：
1. 交易完成后向外部系统发送通知。
2. 监听合约状态变化，例如合约中某个值的更新。
3. 记录合约的行为，以便后续分析或审计。

以前面介绍的NFT合约为例：
```  
event TokenMinted(uint256 indexed tokenId, uint256 supply, string uri);

function mint(uint256 tokenId, uint256 supply, string memory uri) public onlyOwner {
        _mint(msg.sender, tokenId, supply, "");
        _tokenSupply[tokenId] += supply;
        _tokenURIs[tokenId] = uri;
        emit TokenMinted(tokenId, supply, uri);
    }
```  
在上面的代码中，TokenMinted事件用于记录mint函数中的值更改，并向应用程序发出通知。indexed关键字用于指定事件参数在事件日志中进行索引，这使得应用程序可以更轻松地搜索和过滤事件日志。

通过监听事件，应用程序可以自动更新状态或响应合约的状态变化，这使得合约更加灵活和可扩展。

### 1. 查询合约历史事件  
web3.eth.getPastLogs()  
过滤器对象：
- fromBlock: 起始区块（最小值支持从1开始）
- toBlock: 终止区块， 这个参数不带代表一直到最大区块
- address: 合约地址
- topics: 日志项中的主题值数组

示例： 
```  
web3.eth.getPastLogs({
    address: contractAddress,
    topics: ['0x30b042f6d29a39b7c10c2dfbd81016db37ea3d11e72ec5764fb0d0f7a1012a3a'],
    fromBlock: 71,
})
.then(console.log);

[
  {
    address: '0x61b35E8804F87589fBb7da3A6B68c563C05Bb2f1',
    topics: [
      '0x30b042f6d29a39b7c10c2dfbd81016db37ea3d11e72ec5764fb0d0f7a1012a3a',
      '0x0000000000000000000000000000000000000000000000000000000000002718'
    ],
    data: '........',
    blockNumber: 79,
    transactionHash: '0xc5bb3cd573e04e5561d262c394218bc3ad3457f6f0aa2f8101464ebb108700fb',
    transactionIndex: 0,
    blockHash: '0x7b0b108c8b55bdefb5edd6a1ce1ec05180f3cd0b58aa373ff88e9682ab694b7a',
    logIndex: 1,
    removed: false,
    id: 'log_bab13c4d'
  }
]
```  

### 2. 订阅区块链中的指定事件  
web3.eth.subscribe(type [, options] [, callback]);  
- String - 订阅类型
- Mixed - (可选) 依赖于订阅类型的可选额外参数
- Function - (可选) 可选的回调函数，其第一个参数为错误对象，第二个参数为结果

```  
web3.eth.subscribe('logs', {
    address: contractAddress,
    topics: ['0xc3d58168c5ae7397731d063d5bbf3d657854427343f4c083240f7aacaa2d0f62'],
}, function(error, result) {
    if (!error) {
        console.log(result);
    } else {
        console.error(error)
    }
})
.on("connected", subscriptionId => {
    console.log(`Subscribed with ID: ${subscriptionId}`);
  });
```  

### 3. 根据事件签名计算topics的值  
```  
var eventName = 'TokenMinted(uint256,uint256,string)'
var topic = web3.eth.abi.encodeEventSignature(eventName)
```  
