var Web3 = require('web3');
var web3 = new Web3(new Web3.providers.HttpProvider("http://172.22.16.19:8545"));

web3.eth.getAccounts().then(console.log)
// web3.eth.personal.newAccount('test12345').then(console.log);
// web3.eth.personal.importRawKey("0x76491916cf0e70437cbed8c2ce9ac2241221e56f8e64ec74e3282b07f24018e1", "genesisKey").then(console.log);
// web3.eth.getAccounts().then(console.log)
// web3.eth.getBalance("0x4797A444f34C26e71803A1d98D5031a3cAE70650").then(console.log)

// var transactionOptions = {
//     from: "0x4797A444f34C26e71803A1d98D5031a3cAE70650",
//     to: '0xFd89C32962f19bcEA69B76093a64A03618cB33BE',
//     value: "1000000000000000000",
//     gas: 2000000,
// }

// web3.eth.accounts.signTransaction(transactionOptions, "0x76491916cf0e70437cbed8c2ce9ac2241221e56f8e64ec74e3282b07f24018e1").then(console.log);
// web3.eth.sendSignedTransaction("").on('receipt', console.log);
web3.eth.getTransaction("0xb5e8270e1a571851ca85a45fab70d9c5e25d0653f44a0b798050d34cc47243d3").then(console.log)