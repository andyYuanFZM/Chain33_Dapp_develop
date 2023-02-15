##  L3 Chain33 DAPP开发工具和框架简介

### 1.多语言SDK

| 项目 | web3.js | ethers.js   | web3j | go-ethereum |
| ------ | ---------- | ---------- | ---------- |---------- |
| 开源地址| https://github.com/web3/web3.js	| https://github.com/ethers-io/ethers.js | https://github.com/web3j/web3j | https://github.com/ethereum/go-ethereum|
|分类 | Javascript sdk | Javascript sdk | Java sdk  | Go sdk
|发布时间 | 2015年 | 2016年 | 2016年 | 2014年
|贡献者 | 307 | - | 173 | 871 | 
|commit数量 | 3384 | 267 | 1851 | 13958 | 
|Github | fork数 | 4.4K | 1.4K | 1.5K | 15.9K | 

### 2.Web3.js和ethers.js对比
| 项目 | web3.js | ethers.js   |
| ------ | ---------- | ---------- | 
|支持方 | 以太坊基金会 | 加拿大个人程序员：Richard Moore | 
|社区 | 强大 | 一般 | 
|受欢迎程度 | 16.9K star | 6.3 star | 
|依赖大小 | 大 | 小 | 
|使用复杂度 | 相对复杂 | 简单直观 | 
|许可 | GNU LGPL | MIT | 

总结： 两者功能都很完备，都经过很多项目的验证，使用上都不会有问题。总体来说，如果您是初学者，可以考虑使用 ethers.js，因为它提供了更加易于使用的 API。如果您是高级开发者，则可以使用 web3.js，因为它提供了更多的功能和可定制性

###  3.Truffle和hardhat对比

问：有了web3.js这些SDK，为什么还需要truffle和hardhat等？

| 项目 | web3.js | ethers.js   |
| ------ | ---------- | ---------- | 
|合约语言支持 | Solidity,vyper | Solidity | 
|测试框架 | Truffle-test | 支持mocha | 
|项目部署流程 | 简单直接 | 复杂，可定制化的部署，支持多网络部署 | 
|文档和社区支持 | 优秀 | 优秀 | 
|功能 | 强大 | 强大 | 

总结：两者都能很好的满足开发需求，如果要支持solidity和vyper两种合约语言，那么就用truffle, 如果想要更多的定制能力，就用hardhat.