var Web3 = require('web3');
var fs = require('fs')
var path = require('path');

var web3 = new Web3(new Web3.providers.HttpProvider("https://mainnet.bityuan.com/eth"));

var abi = JSON.parse(fs.readFileSync(path.join(__dirname, "../L6 web3.js合约部署/contract.abi")).toString())

var contractAddress = '0x1EBFD0431eB5d3AaF5256F2843fd0Ca01d4b9DAE';

var tx = new web3.eth.Contract(abi, contractAddress);

var tokenId = 10005
var supply = 19
var uri = "http://121.52.224.91:9000/chain33/aivr.json"

// mint操作
// var mintTx = tx.methods.mint(tokenId, supply, uri);

// const mint = async () => {

//   const createTransaction = await web3.eth.accounts.signTransaction(
//     {
//       to: contractAddress,
//       data: mintTx.encodeABI(),
//       gas: await mintTx.estimateGas({from: "0x4797A444f34C26e71803A1d98D5031a3cAE70650"}),
//     },
//     "0x76491916cf0e70437cbed8c2ce9ac2241221e56f8e64ec74e3282b07f24018e1"
//   );

//   const createReceipt = await web3.eth.sendSignedTransaction(createTransaction.rawTransaction);
//   console.log("通证mint完成");
//   console.log(createReceipt);
// };  

// mint();

// 转账操作
var transferTx = tx.methods.safeTransferFrom("0x4797A444f34C26e71803A1d98D5031a3cAE70650", '0x0000000000000000000000000000000000000000', tokenId, 1, "0x");
const transfer = async () => {

  const transferTransaction = await web3.eth.accounts.signTransaction(
    {
      to: contractAddress,
      data: transferTx.encodeABI(),
      gas: await transferTx.estimateGas({from: "0x4797A444f34C26e71803A1d98D5031a3cAE70650"}),
    },
    "0x76491916cf0e70437cbed8c2ce9ac2241221e56f8e64ec74e3282b07f24018e1"
  );

  const createReceipt = await web3.eth.sendSignedTransaction(transferTransaction.rawTransaction);
  console.log("通证transfer完成");
  console.log(createReceipt);
};

transfer();

// // 查询操作
// const getInfo = async () => {
//   // 查询from地址下的余额
//   const fromValue = await tx.methods.balanceOf("0x4797A444f34C26e71803A1d98D5031a3cAE70650", tokenId).call();
//   console.log(`The current balance of from is: ${fromValue}`);

//   // 查询to地址下的余额
//   const toValue = await tx.methods.balanceOf("0x0000000000000000000000000000000000000000", tokenId).call();
//   console.log(`The current balance of to: ${toValue}`);

//   // 查询URI信息
//   const uri = await tx.methods.uri(tokenId).call();
//   console.log(`The URI of token is: ${uri}`);
// };
// getInfo();