const { ethers, formatEther } = require("ethers");

// rpc连接
const provider = new ethers.providers.JsonRpcProvider("http://localhost:8545");

async function get() {

// 查询区块高度
const blocknumber = await provider.getBlockNumber();
console.log(`区块高度：${blocknumber}`) 

// 查询账户余额
const balance = await provider.getBalance("0x4797A444f34C26e71803A1d98D5031a3cAE70650")

// 格式化账户余额
const balanceformat = ethers.utils.formatEther(balance)
console.log(`账户余额：${balanceformat}`)  

// 获得账户交易数
const txCount = await  provider.getTransactionCount("0x4797A444f34C26e71803A1d98D5031a3cAE70650")
console.log(`账户交易数：${txCount}`)  

// 获取链ID
const chainId = (await provider.getNetwork()).chainId
console.log(`ChainId：${chainId}`)

// 获取节点上账户信息
const listenerCount = await provider.listAccounts()
console.log(listenerCount)  

// 获取交易信息
const txInfo = await provider.getTransaction("0x1cfe5918bdd29772c0c57ae6f702c62636318dbe7f27145e3c386aeaa67a8bca")
console.log(txInfo)

// 获取交易回执
const txreceipt = await provider.getTransactionReceipt("0x1cfe5918bdd29772c0c57ae6f702c62636318dbe7f27145e3c386aeaa67a8bca")
console.log(txreceipt)

// 获取区块信息
const blockInfo = await provider.getBlock(10)
console.log(blockInfo) 

// 获取指定合约地址的合约bytecode
const code = await provider.getCode("0x61b35E8804F87589fBb7da3A6B68c563C05Bb2f1")
console.log(code)

}

// get()

async function transfer() {
    // 发送方私钥
    const privatekey = "0x76491916cf0e70437cbed8c2ce9ac2241221e56f8e64ec74e3282b07f24018e1";

    // 创建一个钱包
    const wallet = new ethers.Wallet(privatekey, provider);

    // 设置接收方地址
    const toAddress = '0xFd89C32962f19bcEA69B76093a64A03618cB33BE';

    // 设置转账数量
    const value = ethers.utils.parseEther('12345.0');

    // 创建交易对象
    const tx = {
        to: toAddress,
        value: value,
        gasLimit: 21000,
        gasPrice: ethers.utils.parseUnits("10", "gwei"),
        chainId: 999,
        nonce: await provider.getTransactionCount(wallet.address)
    };

  // 签名交易
    const signedTx = await wallet.signTransaction(tx);

    // 发送交易
    provider.sendTransaction(signedTx)
}

transfer()