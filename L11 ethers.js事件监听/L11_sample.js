var ethers = require("ethers");
var fs = require('fs')
var path = require('path');

// rpc连接
const provider = new ethers.providers.JsonRpcProvider("http://localhost:8545");

// abi信息
var abi = JSON.parse(fs.readFileSync(path.join(__dirname, "../L6 web3.js合约部署/contract.abi")).toString())

// 合约地址
var contractAddress = '0x61b35E8804F87589fBb7da3A6B68c563C05Bb2f1';

// 获取topic信息
// 方式一：
const iface = new ethers.utils.Interface(abi);

const eventName = "TokenMinted";
const eventArgs = [{name: "tokenId", type: "uint256"}, {name: "supply", type: "uint256"},{name: "uri", type: "string"}];

const eventTopic = iface.getEventTopic(eventName, eventArgs);  
console.log(eventTopic)

// 方法二：
// const topic1 = ethers.utils.id('TokenMinted(uint256,uint256,string)');
// console.log(topic1)

async function getLogs() {

const filter = {
    address: contractAddress,
    fromBlock: 1, // 最小高度是1，不能从0开始
    toBlock: 'latest',
    topics: [eventTopic]
  };
  
  const logs = await provider.getLogs(filter);
  
  console.log(logs);
}

// getLogs()

 // websocket连接
 const providerWs = new ethers.providers.WebSocketProvider('ws://localhost:8546');

 // 创建合约实例
 const contract = new ethers.Contract(contractAddress, abi, providerWs);

function listen() {
     
    // 监听mint事件
    contract.on("TokenMinted", (tokenId, supply, uri) => {
        console.log(`TokenMinted event received for ${tokenId}，supply: ${supply}, uri: ${uri}`);
    });
}


listen()