// 事件监听测试
var Web3 = require('web3');
var fs = require('fs')
var path = require('path');

const web3 = new Web3(new Web3.providers.WebsocketProvider('ws://121.52.224.93:8547'));
var eventName = 'TokenMinted(uint256,uint256,string)'
var contractAddress = '0x82035af4c426eaed9cdcaeb366b92ecf5166dbc2';
var topic = web3.eth.abi.encodeEventSignature(eventName);

// 订阅合约日志事件
// var subscription = web3.eth.subscribe('logs', {
//     address: contractAddress,
//     topics: [topic],
// }, function(error, result){
//     if (!error) {
//         console.log(result);
//     } else {
//         console.error(error)
//     }
// })
// .on("connected", subscriptionId => {
//     console.log(`Subscribed with ID: ${subscriptionId}`);
//   });

// 对已经存在的id做监听
const subscription = web3.eth.subscribe('logs', {
    subscription: 44
});

subscription.on('data', (log) => {
    console.log(log);
  });

// 取消订阅
// subscription.unsubscribe((error, success) => {
//     if (success) {
//       console.log(`Unsubscribed from ID: ${subscriptionId}`);
//     }
// });