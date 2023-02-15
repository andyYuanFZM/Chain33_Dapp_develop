var Web3 = require('web3');
var web3 = new Web3(new Web3.providers.HttpProvider("http://172.22.16.19:8545"));

// console.log(Web3.modules)
// console.log(Web3.version)
// web3.eth.getChainId().then(console.log)
// web3.eth.net.getId().then(console.log)
web3.eth.net.isListening().then(console.log)
// web3.eth.getHashrate().then(console.log)
// web3.eth.getGasPrice().then(console.log)
// web3.eth.getAccounts().then(console.log)
// web3.eth.getBlockNumber().then(console.log)
// web3.eth.getBalance("0x4797A444f34C26e71803A1d98D5031a3cAE70650").then(console.log)
// web3.eth.getBlock(1).then(console.log)
// web3.eth.getBlockTransactionCount(0).then(console.log)
// web3.eth.getTransaction("0x80278bbdb9b8164b17e35e0bff06fbaa515a28a71b2c1101df796e0ea132e673").then(console.log)
// web3.eth.getTransactionReceipt("0x80278bbdb9b8164b17e35e0bff06fbaa515a28a71b2c1101df796e0ea132e673").then(console.log)
// web3.eth.getTransactionCount("0x4797A444f34C26e71803A1d98D5031a3cAE70650").then(console.log)