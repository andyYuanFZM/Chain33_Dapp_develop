let Web3 = require('web3');
var web3 = new Web3(new Web3.providers.HttpProvider("http://192.168.3.158:8545"));

// console.log(Web3.modules)
// console.log(Web3.version)
// console.log(web3.currentProvider)
// console.log(web3.eth.currentProvider)
// web3.eth.getChainId().then(console.log)
// web3.eth.net.getId().then(console.log)
// web3.eth.net.isListening().then(console.log)
// web3.eth.getHashrate().then(console.log)
// web3.eth.getGasPrice().then(console.log)
// web3.eth.getBlockNumber().then(console.log)
// web3.eth.getBlock(2).then(console.log)
// web3.eth.getBlockTransactionCount(0).then(console.log)
//web3.eth.getTransaction("0x6fb16893c86e3afb683c424a95e85f5963fb8159d4d1d674e06217a8b35f3e46").then(console.log)
// web3.eth.getTransactionReceipt("0x80278bbdb9b8164b17e35e0bff06fbaa515a28a71b2c1101df796e0ea132e673").then(console.log)
web3.eth.getTransactionCount("0x4797A444f34C26e71803A1d98D5031a3cAE70650").then(console.log)