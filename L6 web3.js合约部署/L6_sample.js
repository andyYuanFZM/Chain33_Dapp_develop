var Web3 = require('web3');
var fs = require('fs')
var path = require('path');

var web3 = new Web3(new Web3.providers.HttpProvider("https://mainnet.bityuan.com/eth"));

var abi = JSON.parse(fs.readFileSync(path.join(__dirname, "./contract.abi")).toString())
var bytecode = fs.readFileSync(path.join(__dirname, "./contract.code")).toString()

const deploy = async () => {
  
    const contract = new web3.eth.Contract(abi);
  
    const contractdeploy = contract.deploy({
      data: bytecode,
      arguments: ["aivr NFT"],
    });

const createTransaction = await web3.eth.accounts.signTransaction(
    {
      data: contractdeploy.encodeABI(),
      gas: await contractdeploy.estimateGas({from: "0x4797A444f34C26e71803A1d98D5031a3cAE70650"}),
    },
    "0x76491916cf0e70437cbed8c2ce9ac2241221e56f8e64ec74e3282b07f24018e1"
  );

const createReceipt = await web3.eth.sendSignedTransaction(createTransaction.rawTransaction);
console.log(createReceipt);
}

deploy();