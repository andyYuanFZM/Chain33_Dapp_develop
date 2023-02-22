var Web3 = require('web3');
var web3 = new Web3(new Web3.providers.HttpProvider("http://192.168.3.158:8545"));

// web3.eth.getAccounts().then(console.log)
// web3.eth.personal.newAccount('test12345').then(console.log);
//web3.eth.personal.importRawKey("0x76491916cf0e70437cbed8c2ce9ac2241221e56f8e64ec74e3282b07f24018e1", "genesisKey").then(console.log);
// web3.eth.getAccounts().then(console.log)
// web3.eth.getBalance("0x4797A444f34C26e71803A1d98D5031a3cAE70650").then(console.log)

// var transactionOptions = {
//     from: "0x4797A444f34C26e71803A1d98D5031a3cAE70650",
//     to: '0xFd89C32962f19bcEA69B76093a64A03618cB33BE',
//     value: "1000000000000000000",
//     gas: 2000000,
// }

// web3.eth.accounts.signTransaction(transactionOptions, "0x76491916cf0e70437cbed8c2ce9ac2241221e56f8e64ec74e3282b07f24018e1").then(console.log);
// web3.eth.sendSignedTransaction("0xf86f098502540be400831e848094fd89c32962f19bcea69b76093a64a03618cb33be880de0b6b3a7640000808207f2a0ab1a33ffb2179ad7fd19f5ab2cffff07c6333d0b8a905480851dbdfa90151c0fa022b34758fd9e942aad65fddd5d738a08ead59d58ec84299a1fa160b0f97e3849").on('receipt', console.log);
web3.eth.getTransaction("0xaf35e2873abbb0c2bd641e78b41f08554ab440c8f71b36e1a25ac1e9cfd5f098").then(console.log)