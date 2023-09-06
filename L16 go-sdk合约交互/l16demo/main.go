package main

import (
	"context"
	"fmt"
	"log"
	"math/big"

	"github.com/ethereum/go-ethereum"
	"github.com/ethereum/go-ethereum/ethclient"
)

func main() {

	client, err := ethclient.Dial("http://121.52.224.91:8546")
	if err != nil {
		fmt.Println("Failed to connect to the Ethereum client: %v", err)
		return
	}

	// 需要合约地址
	// contractAddress := common.HexToAddress("0xa5019B724ddd70D7009AD1A3C5a7021492950a47")
	// // topic
	// topicHash := crypto.Keccak256Hash([]byte("NameRegistered(uint256,address,uint256)"))
	// 根据合约地址,过滤日志
	query := ethereum.FilterQuery{
		// Addresses: []common.Address{contractAddress},
		// Topics:    [][]common.Hash{{topicHash}},
		FromBlock: big.NewInt(26484000),
	}

	logs, err := client.FilterLogs(context.Background(), query)
	if err != nil {
		log.Fatal(err)
	}
	fmt.Println(logs)

	for _, vLog := range logs {
		fmt.Println(vLog)
	}
}
