package com;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.EthLog;
import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.websocket.WebSocketService;

/**
 * Hello world!
 *
 */
public class FilterAndEvent {
    public static void main(String[] args) throws IOException, InterruptedException {
        // 连接到web3j节点，注意修改节点的URL和端口号
        Web3j web3j = Web3j.build(new HttpService("http://localhost:8545"));

        // 要监听的合约地址
        String contractAddress = "0x59b34dd4c55b62ed3b6f46140805235f3d841366";

        // 要监听的事件
        Event event = new Event("TokenMinted", Arrays.asList(new TypeReference<Uint256>() {},
                new TypeReference<Uint256>() {},
                new TypeReference<Utf8String>() {}));

        // 获取智能合约的历史日志
        // getHistoryLog(web3j, contractAddress, event);

        WebSocketService webSocketService = new WebSocketService("ws://localhost:8546", true);
        webSocketService.connect();
        
        Web3j web3jws = Web3j.build(webSocketService);

        // 事件监听
        eventListener(web3jws, contractAddress, event);

        // 等待事件触发
        Thread.sleep(100000);

        webSocketService.close();
        
    }

    /**
     * 获取智能合约的历史日志
     * @param web3j
     * @param contractAddress
     * @param event
     * @throws IOException
     */
    private static void getHistoryLog(Web3j web3j, String contractAddress, Event event) throws IOException {
        // 构造过滤器对象，指定查询条件
        EthFilter filter = new EthFilter(
                DefaultBlockParameterName.EARLIEST, // 日志的起始区块
                DefaultBlockParameterName.LATEST, // 日志的结束区块
                contractAddress // 查询的智能合约地址
        );

        // 添加event类型及其参数
        filter.addSingleTopic(EventEncoder.encode(event));

        // 获取符合过滤器条件的所有日志
        EthLog log = web3j.ethGetLogs(filter).send();

        // 处理查询结果
        List<EthLog.LogResult> resultList = log.getLogs();
        for (EthLog.LogResult result : resultList) {
            EthLog.LogObject logObject = (EthLog.LogObject) result.get();
            System.out.println("Received log: " + logObject.toString() + "\r\n");

            // 获取topic
            List<String> topics = logObject.getTopics();
            System.out.println("topic is: " + topics  + "\r\n");

            // 解码非index参数
            List<Type> results = FunctionReturnDecoder.decode(logObject.getData(), event.getNonIndexedParameters());
            BigInteger value = (BigInteger) results.get(0).getValue();
            System.out.println("supply is: " +  value);
        }
    }

    /**
     * 事件监听
     * @param web3j
     * @param contractAddress
     * @param event
     */
    private static void eventListener(Web3j web3j, String contractAddress, Event event) {
        EthFilter filter = new EthFilter(
            DefaultBlockParameterName.EARLIEST, DefaultBlockParameterName.LATEST, contractAddress);
        
        filter.addSingleTopic(EventEncoder.encode(event));

        web3j.ethLogFlowable(filter).subscribe(log -> {

            System.out.println(log.getTopics());

            // 解码非index参数
            List<Type> results = FunctionReturnDecoder.decode(log.getData(), event.getNonIndexedParameters());
            BigInteger value = (BigInteger) results.get(0).getValue();
            System.out.println("supply is: " +  value);
        });

    }
}
