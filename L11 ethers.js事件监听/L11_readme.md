##  L11 ethers.js事件监听

 概念描述[[参考L8]](https://github.com/andyYuanFZM/Chain33_Dapp_develop/blob/main/L8%20web3.js%E5%90%88%E7%BA%A6%E4%BA%8B%E4%BB%B6/L8_readme.md) 

### 1. 查询合约历史事件  
provider.getLogs(filter)
过滤器对象：
- fromBlock: 起始区块（最小值支持从1开始）
- toBlock: 终止区块， 这个参数不带代表一直到最大区块
- address: 合约地址
- topics: 日志项中的主题值数组

示例： 
```  
// rpc连接
const provider = new ethers.providers.JsonRpcProvider("http://localhost:8545");

// 合约地址
var contractAddress = '0x61b35E8804F87589fBb7da3A6B68c563C05Bb2f1';

// topic值
const eventTopic = ethers.utils.id('TokenMinted(uint256,uint256,string)');

const filter = {
    address: contractAddress,
    fromBlock: 1, // 最小高度是1，不能从0开始
    toBlock: 'latest',
    topics: [eventTopic]
  };
  
  const logs = await provider.getLogs(filter);
  console.log(logs)

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
ethers.Contract.on("事件名", (args));  

示例：
```  
// websocket连接
const providerWs = new ethers.providers.WebSocketProvider('ws://localhost:8546');

// 创建合约实例
const contract = new ethers.Contract(contractAddress, abi, providerWs);

// 监听mint事件
contract.on("TokenMinted", (tokenId, supply, uri) => {
    console.log(`TokenMinted event received for ${tokenId}，supply: ${supply}, uri: ${uri}`);
});
```  

### 3. 根据事件签名计算topics的值  
```  
// 方式一：
const iface = new ethers.utils.Interface(abi);

const eventName = "TokenMinted";
const eventArgs = [{name: "tokenId", type: "uint256"}, {name: "supply", type: "uint256"},{name: "uri", type: "string"}];

const eventTopic = iface.getEventTopic(eventName, eventArgs);
console.log(eventTopic)

// 方法二：
const topic1 = ethers.utils.id('TokenMinted(uint256,uint256,string)');
console.log(topic1)
```  
