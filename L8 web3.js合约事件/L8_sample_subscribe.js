var Web3 = require('web3');
var fs = require('fs')
var path = require('path');

const web3 = new Web3(new Web3.providers.WebsocketProvider('ws://172.22.16.19:8546'));
var eventName = 'TokenMinted(uint256,uint256,string)'
var contractAddress = '0x61b35E8804F87589fBb7da3A6B68c563C05Bb2f1';
console.log(web3.eth.abi.encodeEventSignature(eventName));

// 订阅合约日志事件
var subscription = web3.eth.subscribe('logs', {
    address: contractAddress,
    topics: ['0xc3d58168c5ae7397731d063d5bbf3d657854427343f4c083240f7aacaa2d0f62'],
}, function(error, result){
    if (!error) {
        console.log(result);
    } else {
        console.error(error)
    }
})
.on("connected", subscriptionId => {
    console.log(`Subscribed with ID: ${subscriptionId}`);
  });

// 取消订阅
// subscription.unsubscribe((error, success) => {
//     if (success) {
//       console.log(`Unsubscribed from ID: ${subscriptionId}`);
//     }
// });