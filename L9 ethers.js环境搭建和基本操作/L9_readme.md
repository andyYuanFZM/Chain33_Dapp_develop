##  L9 ethers.js环境搭建和基本操作

### 1. 安装ethers.js
```  
# 最新版本是6.x, 演示使用的是5.7.2版本
npm install ethers@5.7.2
```  

### 2. ethers.js常用查询  
 ethers.js接口文档： https://docs.ethers.org/v5/
 - 下面演示的接口都是基于V5的版本， V5和V6版本的接口有区别，要注意区分版本  

```  
# 连接区块链
const { ethers } = require("ethers");
// rpc连接
const provider = new ethers.JsonRpcProvider("http://localhost:8545");
``` 

以下列举的都是适用于chain33的常用查询接口  
```  
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
```  
 
 ### 3 ethers.js发送转账交易(rpc接口)
 TODO: 这种方式chain33支持上还有些问题, 发送交易返回的hash是chain33原生的,导致交易检查不通过
 已经提了issue让他们解决. 
 ```  
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
 ```  

 ### 4 ethers.js发送转账交易(metamask)
 1. 导入ethers.js
  ```  
 <script src="https://cdn.ethers.io/lib/ethers-5.5.2.umd.min.js"></script>
  ```  

  2. 连接matemask,签名并发送交易
 ```  
  <script>
      window.addEventListener('load', async () => {
        // Wait for MetaMask to be installed
        if (typeof window.ethereum === 'undefined') {
          alert('Please install MetaMask first.');
          return;
        }
        
        // Request account access from MetaMask
        await window.ethereum.enable();
        
        // Connect to the Ethereum network using ethers.js
        const provider = new ethers.providers.Web3Provider(window.ethereum);
        
        // Create a signer object to sign transactions with
        const signer = provider.getSigner();
        
        // Get the user's Ethereum address
        const address = await signer.getAddress();
        
        // Listen for form submission
        document.getElementById('send-form').addEventListener('submit', async (event) => {
          event.preventDefault();
          
          // Get form data
          const to = "to address";
          const value = "amount";
          
          // Create a transaction object
          const tx = {
            to,
            value
          };
          
          // Sign and send the transaction
          const result = await signer.sendTransaction(tx);
          console.log('Transaction result:', result);
        });
      });
    </script>
 ```  