package com;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.*;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.EthTransaction;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import com.google.gson.Gson;

/**
 * 通过JAVA代码同BTY区块链交互
 *
 */
public class L17 
{
    // 私钥，用于转账测试(from)
    private static final String privateKey = "0x13f9a8ae7ba07878fb37e93a5fd624e6fedd028d9556581618c29931855c52e6";
    // 私钥对应的地址，用于转账测试(from)
    private static final String address = "0xcccda5358462f3cb7bf781b1003da66498bf4a56";
    // to地址，用于转账测试(to)
    private static final String toaddress = "0xFd89C32962f19bcEA69B76093a64A03618cB33BE";

    public static void main( String[] args ) throws InterruptedException, ExecutionException, IOException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException
    {
        // 建立连接,此处用的是临时BTY节点，后面要替换成自己的BTY节点
        Web3j web3 = Web3j.build(new HttpService("https://mainnet.bityuan.com/eth"));

        // 获取区块高度
        EthBlockNumber  ethBlockNumber = web3.ethBlockNumber().sendAsync().get();
        BigInteger blockNumber = ethBlockNumber.getBlockNumber();
        System.out.println("BlockNumber:" + blockNumber + "\r\n");

        // 查询账户余额
        BigInteger balance = web3.ethGetBalance(address, DefaultBlockParameterName.LATEST).send().getBalance();
        System.out.println("Balance before format:" + balance + "\r\n");

        // 格式化账户余额
        String balanceStr = Convert.fromWei(balance.toString(), Convert.Unit.ETHER).toString();
        System.out.println("Balance after format:" + balanceStr + "\r\n");

        // 获取chainid
        BigInteger chainId = web3.ethChainId().send().getChainId();
        System.out.println("ChainId:" + chainId + "\r\n");

        // 根据区块号获取区块信息
        DefaultBlockParameterNumber defaultBlockParameterNumber = new DefaultBlockParameterNumber(26000000);
        EthBlock ethBlock = web3.ethGetBlockByNumber(defaultBlockParameterNumber,true).sendAsync().get();
        EthBlock.Block block = ethBlock.getBlock();
        Gson gson = new Gson();
        String info = gson.toJson(block);
        System.out.println("BlockInfo:" + info + "\r\n");

        // 获取区块交易信息
        List<EthBlock.TransactionResult> transactions = block.getTransactions();
        List<Transaction> txInfos = new ArrayList<>();
        transactions.forEach(txInfo->{
            Transaction transaction = (Transaction)txInfo;
            txInfos.add(transaction);
        });
        String transactionInfos = gson.toJson(txInfos);
        System.out.println("TransactionInfos:" + transactionInfos + "\r\n");
        // 获取第一笔交易的hash值，用于下面的查询交易信息
        String txHash = txInfos.get(0).getHash();

        // 根据hash获取交易信息
        EthTransaction transaction = web3.ethGetTransactionByHash(txHash).sendAsync().get();
        Optional<Transaction> optionalTransaction = transaction.getTransaction();
        StringBuilder txInfo = new StringBuilder();
        if(optionalTransaction.isPresent()){
            Transaction transactionInfo = optionalTransaction.get();
            txInfo.append(gson.toJson(transactionInfo));
        }
        System.out.println("TransactionInfo:" + txInfo.toString() + "\r\n");

        // 生成以太坊地址和私钥的方法
        // ECKeyPair keyPair = Keys.createEcKeyPair();
        // String privateKey = Numeric.toHexStringNoPrefix(keyPair.getPrivateKey());
        // Credentials credentials = Credentials.create(keyPair);
        // String address = credentials.getAddress();
        // System.out.println("address:" + address);
        // System.out.println("privateKey:" + privateKey);

        // 转账
        // 1. 加载转账所需的凭证，用私钥
        Credentials credentials = Credentials.create(privateKey);
        // 2. 获取nonce，交易笔数
        BigInteger nonce = web3.ethGetTransactionCount(address, DefaultBlockParameterName.LATEST).send().getTransactionCount();

        // 3. 手续费
        EthGasPrice ethGasPrice = web3.ethGasPrice().sendAsync().get();
        BigInteger gasPrice = ethGasPrice.getGasPrice();
        BigInteger gasLimit = BigInteger.valueOf(60000L);

        // 转账金额
        BigInteger value = Convert.toWei("0.1", Convert.Unit.ETHER).toBigInteger(); 

        // 4. 创建RawTransaction交易对象
        RawTransaction rawTransaction = RawTransaction.createEtherTransaction(nonce, gasPrice, gasLimit, toaddress, value);
        //签名Transaction，这里要对交易做签名
        byte[] signMessage = TransactionEncoder.signMessage(rawTransaction, chainId.longValue(), credentials);
        String hexValue = Numeric.toHexString(signMessage);
        //发送交易
        EthSendTransaction ethSendTransaction = web3.ethSendRawTransaction(hexValue).sendAsync().get();
        System.out.println("转账交易的hash值" + ethSendTransaction.getTransactionHash());

        // 确认交易上链
        for (int i = 0; i < 10; i++) {
            EthGetTransactionReceipt receipt = web3.ethGetTransactionReceipt(ethSendTransaction.getTransactionHash()).sendAsync().get();
            if (receipt.getTransactionReceipt().isPresent()) {
                // 查询转账后from地址下余额
                balance = web3.ethGetBalance(address, DefaultBlockParameterName.LATEST).send().getBalance();
                balanceStr = Convert.fromWei(balance.toString(), Convert.Unit.ETHER).toString();
                System.out.println("Balance after transfer:" + balanceStr + "\r\n");

                // 查询转账后to地址下余额
                balance = web3.ethGetBalance(toaddress, DefaultBlockParameterName.LATEST).send().getBalance();
                balanceStr = Convert.fromWei(balance.toString(), Convert.Unit.ETHER).toString();
                System.out.println("Balance after transfer:" + balanceStr + "\r\n");
                break;
            }
            Thread.sleep(5000);
        }

    }
}
