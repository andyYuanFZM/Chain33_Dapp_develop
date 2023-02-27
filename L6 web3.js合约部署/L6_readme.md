##  L6 web3.js合约部署

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

### 3. 合约部署
可以查看此目录下的完整脚本： L6_sample.js  
涉及到的接口:
- eth.web3.estimateGas({"from address"}): 估算交易的GAS费, from address是必须的参数
- web3.eth.Contract(abi): 自动将所有的调用转换为基于 RPC 的底层 ABI 调用 
- web3.eth.accounts.signTransaction: 本地签名交易
- web3.eth.sendSignedTransaction: 发送交易
