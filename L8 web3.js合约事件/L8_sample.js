var Web3 = require('web3');
var fs = require('fs')
var path = require('path');

var web3 = new Web3(new Web3.providers.HttpProvider("http://172.22.16.19:8545"));

var abi = JSON.parse(fs.readFileSync(path.join(__dirname, "../L6 web3.js合约部署/contract.abi")).toString())

var contractAddress = '0x61b35E8804F87589fBb7da3A6B68c563C05Bb2f1';

var tx = new web3.eth.Contract(abi, contractAddress);

var tokenId = 10008
var supply = 18
var uri = "http://121.52.224.91:9000/chain33/aivr.json"

// mint操作
var mintTx = tx.methods.mint(tokenId, supply, uri);

const mint = async () => {

  const createTransaction = await web3.eth.accounts.signTransaction(
    {
      to: contractAddress,
      data: mintTx.encodeABI(),
      gas: await mintTx.estimateGas({from: "0x4797A444f34C26e71803A1d98D5031a3cAE70650"}),
    },
    "0x76491916cf0e70437cbed8c2ce9ac2241221e56f8e64ec74e3282b07f24018e1"
  );

  const createReceipt = await web3.eth.sendSignedTransaction(createTransaction.rawTransaction);
  console.log("通证mint完成");
  console.log(createReceipt.logs);
};  

// mint();

// 查询合约日志
web3.eth.getPastLogs({
    address: contractAddress,
    topics: ['0x30b042f6d29a39b7c10c2dfbd81016db37ea3d11e72ec5764fb0d0f7a1012a3a'],
    fromBlock: 79,
})
.then(console.log);