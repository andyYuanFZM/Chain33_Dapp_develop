##  L13 web3j合约部署和读写

### 1. 合约编写
- 编写一个ERC1155的智能合约，继承了OpenZeppelin ERC1155和Ownable合约
- 合约中有两个私有映射变量 _tokenSupply 和 _tokenURIs，分别用于存储每个ERC1155代币的供应量和URI
- 在构造函数中，传入了基础URI，用于确定代币的元数据
- mint 函数可供所有者调用来铸造新的ERC1155代币。该函数调用了 _mint 函数将新代币的供应量添加到特定帐户中
- _tokenSupply 和 _tokenURIs 映射都会更新，并触发 TokenMinted 事件。 该事件记录了代币 ID，代币供应量和代币 URI。tokenId 参数使用 indexed 修饰符进行标记，以便在事件日志中可以更快速地搜索并访问该参数。 
```  
// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

import "https://github.com/OpenZeppelin/openzeppelin-contracts/blob/v4.3.0/contracts/token/ERC1155/ERC1155.sol";
import "https://github.com/OpenZeppelin/openzeppelin-contracts/blob/v4.3.0/contracts/access/Ownable.sol";

contract MyERC1155 is ERC1155, Ownable {

    mapping(uint256 => uint256) private _tokenSupply;
    mapping(uint256 => string) private _tokenURIs;

    event TokenMinted(uint256 indexed tokenId, uint256 supply, string uri);

    constructor(string memory uri) ERC1155(uri) {}

    function mint(uint256 tokenId, uint256 supply, string memory uri) public onlyOwner {
        _mint(msg.sender, tokenId, supply, "");
        _tokenSupply[tokenId] += supply;
        _tokenURIs[tokenId] = uri;
        emit TokenMinted(tokenId, supply, uri);
    }

    function tokenSupply(uint256 tokenId) public view returns (uint256) {
        return _tokenSupply[tokenId];
    }

    function uri(uint256 tokenId) public view override returns (string memory) {
        return _tokenURIs[tokenId];
    }
}
```  

### 2. 合约编译
编译工具: 
- [[在线remix]](https://remix.ethereum.org/)
- solc工具

编译结果
- 字节码(bytecode): 是solidity代码被翻译以后的信息，包含了二进制的计算机指令。
- ABI: 应用程序二进制接口,以json文件表示。

### 3. 合约部署和读写操作
 - Keys.createEcKeyPair(): 生成eckeypair,用于下面的私钥和地址生成
 - Numeric.toHexStringNoPrefix(eckeypair.getPrivateKey()): 生成私钥
 - Numeric.toHexStringNoPrefix(eckeypair.getPublicKey()): 生成公钥
 - Credentials.create(eckeypair).getAddress(): 获取地址
 - FunctionEncoder.encodeConstructor: 合约构造函数encode
 - RawTransaction.createContractTransaction: 创建部署合约交易对象
 - RawTransaction.createTransaction: 创建调用合约交易对象
 - Transaction.createEthCallTransaction：查询合约方法

 具体使用可以参考同级目录下的: \src\main\java\com\DeployAndCall.java

