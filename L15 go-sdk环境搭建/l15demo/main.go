package main

import (
	"context"
	"fmt"
	"math"
	"math/big"
	"time"

	"github.com/ethereum/go-ethereum/common"
	"github.com/ethereum/go-ethereum/core/types"
	"github.com/ethereum/go-ethereum/crypto"
	ethclient "github.com/ethereum/go-ethereum/ethclient"
)

func main() {
	client, err := ethclient.Dial("http://localhost:8545")
	if err != nil {
		fmt.Println("Failed to connect to the Ethereum client: %v", err)
		return
	}

	// 查询区块高度
	header, err := client.HeaderByNumber(context.Background(), nil)
	if err != nil {
		fmt.Println("HeaderByNumber err:", err)
	} else {
		fmt.Println("HeaderByNumber:", header.Number)
	}

	// 获取chainid
	chainID, err := client.NetworkID(context.Background())
	if err != nil {
		fmt.Println("NetworkID err:", err)
	} else {
		fmt.Println("NetworkID:", chainID)
	}

	// 查询账户余额
	address := common.HexToAddress("0x4797A444f34C26e71803A1d98D5031a3cAE70650")
	balanceWei, err := client.BalanceAt(context.Background(), address, nil)
	if err != nil {
		fmt.Println("BalanceAt err:", err)
	} else {
		fmt.Println("BalanceAt:", balanceWei)
	}

	// 格式化余额( wei -> eth)
	balances := new(big.Float).Quo(new(big.Float).SetInt(balanceWei), big.NewFloat(math.Pow10(18)))
	fmt.Println("ETH value is:", balances)

	// 根据区块高度查询区块信息
	blockNumber := big.NewInt(100)
	block, err := client.BlockByNumber(context.Background(), blockNumber)
	if err != nil {
		fmt.Println("BlockByNumber err:", err)
	} else {
		fmt.Println("BlockByNumber's block hash:", block.Hash())
		fmt.Println("BlockByNumber's block parent hash:", block.Header().ParentHash)
	}

	// 根据区块hash查询区块信息
	blockHash := common.HexToHash("0x096e43f3002854b6cf31425a4af141c33a0888dcf4cc1d6edd81f9cf3afdaf74")
	block2, err := client.BlockByHash(context.Background(), blockHash)
	if err != nil {
		fmt.Println("BlockByHash err:", err)
	} else {
		fmt.Println("BlockByHash's blocknumber", block2.Number())
	}

	// 获取gasPrice
	gasPrice, err := client.SuggestGasPrice(context.Background())
	if err != nil {
		fmt.Println("SuggestGasPrice err:", err)
	} else {
		fmt.Println("SuggestGasPrice:", gasPrice)
	}

	// 根据交易hash查询交易信息
	txHash := common.HexToHash("0x6eb176f7a3c0b94ac7109912e3742484727a1e8af6e5b870a80afacedda18ffa")
	tx, _, err := client.TransactionByHash(context.Background(), txHash)
	if err != nil {
		fmt.Println("TransactionByHash err:", err)
	} else {
		fmt.Println("TransactionByHash's tx hash:", tx.Hash())
	}

	// 根据交易hash查询交易收据
	receipt, err := client.TransactionReceipt(context.Background(), txHash)
	if err != nil {
		fmt.Println("TransactionReceipt err:", err)
	} else {
		fmt.Println("TransactionReceipt's TransactionIndex:", receipt.TransactionIndex)
	}

	// 转账
	privateKey, err := crypto.HexToECDSA("76491916cf0e70437cbed8c2ce9ac2241221e56f8e64ec74e3282b07f24018e1")
	toAddress := common.HexToAddress("0xcf3594c95b6a9ebe045fabce82a040c7b6a77d6b")
	// 将eth转换为wei
	value := 100.0
	valueWei := new(big.Int).Mul(big.NewInt(int64(1e18)), big.NewInt(int64(value)))
	if err != nil {
		fmt.Println("HexToECDSA err:", err)
		return
	}

	// 获取nonce
	nonce, err := client.PendingNonceAt(context.Background(), common.HexToAddress("0x4797A444f34C26e71803A1d98D5031a3cAE70650"))
	if err != nil {
		fmt.Println("PendingNonceAt err:", err)
		return
	}

	// 构造交易
	var data []byte
	txNew := types.NewTx(&types.LegacyTx{
		Nonce:    nonce,
		GasPrice: gasPrice,
		To:       &toAddress,
		Value:    valueWei,
		Data:     data})

	// 签名交易
	signedTx, err := types.SignTx(txNew, types.NewEIP155Signer(chainID), privateKey)
	if err != nil {
		fmt.Println("SignTx err:", err)
		return
	}

	// 发送交易
	err = client.SendTransaction(context.Background(), signedTx)
	if err != nil {
		fmt.Println("SendTransaction err:", err)
		return
	}

	// 获取交易hash
	fmt.Println("tx hash:", signedTx.Hash().Hex())

	// 等待交易确认
	time.Sleep(time.Duration(5) * time.Second)

	// 转账后再查询账户余额
	address = common.HexToAddress("0x4797A444f34C26e71803A1d98D5031a3cAE70650")
	balanceWei, err = client.BalanceAt(context.Background(), address, nil)
	if err != nil {
		fmt.Println("BalanceAt err:", err)
	}

	// 格式化余额
	balances = new(big.Float).Quo(new(big.Float).SetInt(balanceWei), big.NewFloat(math.Pow10(18)))
	fmt.Println("Amount after transfer is:", balances)

}
