##  L7 web3.js合约读写

### 1. 向合约内写入数据
#### 通证mint
mint函数调用流程  

```  
// 1. 读取ABI文件
var abi = JSON.parse(fs.readFileSync(path.join(__dirname, "../contract.abi")).toString())

// 2. provider连接
var web3 = new Web3(new Web3.providers.HttpProvider("http://172.22.16.19:8545"));

// 3. 获得合约地址
var contractAddress = '合约地址';

// 4. 创建合约instance
var tx = new web3.eth.Contract(abi, contractAddress);

// 5. mint操作
var mintTx = tx.methods.mint(tokenId, supply, uri);
const mint = async () => {
  const createTransaction = await web3.eth.accounts.signTransaction(
    {
      to: contractAddress,
      data: mintTx.encodeABI(),
      gas: await mintTx.estimateGas({from: "来源方地址"}),
    },
    "来源方私钥"
  );
  const createReceipt = await web3.eth.sendSignedTransaction(createTransaction.rawTransaction);
  console.log(createReceipt);
};
mint();
```  

mint函数并传入以下三个参数(tokenId: 通证的编号，用整数表示; supply: 通证的供应量，整数; uri: 通证元数据信息）。 其中uri代表通证元数据信息，它的标准参考： https://eips.ethereum.org/EIPS/eip-1155#metadata, 以下是一个uri示例，但实际的URI格式各有区别，具体取决于通证发行者的需求。 
```  
{
  "name": "MyToken",  -- 通证名称
  "description": "My custom ERC1155 token",  -- 通证的描述
  "image": "https://example.com/token-image.png",  -- 通证图像的URL
  "external_url": "https://example.com/token",  -- 指向通证相关网站的URL，没有的话，这个字段可以删除
  "attributes": [                               -- 通证的属性数组
    {
      "trait_type": "color",  -- 属性的类型
      "value": "blue"         -- 属性的值
    },
    {
      "trait_type": "size",
      "value": "medium"
    }
  ]
}
```  

#### 通证tranfer
- 通过合约实例使用methods.safeTransferFrom函数并传入参数(from: 通证来源方地址; to: 通证去向方地址: id: 通证ID, amount: 要转移的通证数量)来生成转移通证的交易
- 再通过from地址的私钥，签名上述交易并发送到链上，实现通证转移

transfer函数调用流程  
```  
// 转账操作
var transferTx = tx.methods.safeTransferFrom("来源地址", "去向地址", 通证ID, 转让数量, "0x");

const transfer = async () => {

  const transferTransaction = await web3.eth.accounts.signTransaction(
    {
      to: contractAddress,
      data: transferTx.encodeABI(),
      gas: await transferTx.estimateGas({from: "来源地址"}),
    },
    "来源地址钥"
  );

  const createReceipt = await web3.eth.sendSignedTransaction(transferTransaction.rawTransaction);
};
transfer();
```  

### 2. 合约信息查询
```  
// 查询操作
const getInfo = async () => {
  // 查询from地址下的余额
  const fromValue = await tx.methods.balanceOf("地址", 通证ID).call();
  console.log(`The current balance of from is: ${fromValue}`);

  // 查询to地址下的余额
  const toValue = await tx.methods.balanceOf("地址", 通证ID).call();
  console.log(`The current balance of to: ${toValue}`);

  // 查询URI信息
  const uri = await tx.methods.uri(tokenId).call();
  console.log(`The current balance of to: ${uri}`);
};
getInfo();
```  
