package com;

import java.math.BigInteger;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.EthLog;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.websocket.WebSocketService;

import com.alibaba.fastjson.JSON;


public class Filter {

	//系统可以的处理的results最大长度为10000,按照每个区块中最多同时有2笔相同的交易并发数，单次可以处理5000个区块高度,尽可能地减少查询次数
    // BTY限制800，否则取不到事件
    public static final BigInteger MAX_SIZE = BigInteger.valueOf(500l);

    // ERC721 转移事件  event Transfer(address indexed from,address indexed to,uint256 indexed tokenId);
    public static final Event TransferEvent = new Event("Transfer", Arrays.asList(
            new TypeReference<Address>(true) {},
            new TypeReference<Address>(true) {},
            new TypeReference<Uint256>(true) {}));

    public static final Event RegisteredEvent = new Event("NameRegistered", Arrays.asList(
            new TypeReference<Uint256>(true) {},
            new TypeReference<Utf8String>(false) {},
            new TypeReference<Address>(true) {},
            new TypeReference<Uint256>(false) {}));

    public static final Event RenewedEvent = new Event("NameRenewed", Arrays.asList(
            new TypeReference<Uint256>(true) {},
            new TypeReference<Utf8String>(false) {},
            new TypeReference<Uint256>(false) {}));


    //函数接口,用户处理解析后的日志数据
    @FunctionalInterface
    public interface Outflow {

        /**
         * 每个event需要实现如下的数据处理方法
         *
         * @param blockNumber 区块高度
         * @param logIndex 日志索引数
         * @param txIndex 交易索引序号
         * @param results 非索引字段解析值
         * @param indexed indexed索引字段解析值
         * @param address 合约地址
         * @param txHash  交易哈希
         */
        void outflow(BigInteger blockNumber, BigInteger logIndex, BigInteger txIndex, List<Type> results, List<Type> indexed, String address, String txHash);
    }

    //函数接口,处理更新区块高度，避免重复消费
    @FunctionalInterface
    public interface ICommonLogPro {
        /**
         * 处理更新区块高度，避免重复消费
         *
         * @param address     合约地址
         * @param blockNumber 区块高度
         * @param txIndex 交易索引
         */
        void BlockNumber(String address, BigInteger blockNumber, BigInteger txIndex);
    }

    /**
     * 新增合约订阅需要单独起一个线程进行订阅处理
     *
     * @param web3j            web3j客户端比如：Web3j web3j = Web3j.build(new HttpService(http://localhost:8545));
     * @param address          订阅合约地址
     * @param startBlockNumber 开始订阅的高度
     * @param overBlockNumber 结束处理高度
     * @param events           订阅事件
     * @param outflows         解析后event处理函数
     * @param commonLogPro 通用处理逻辑
     * @throws Exception
     */
    public static void Filter(Web3j web3j, String address, BigInteger startBlockNumber, BigInteger overBlockNumber, Event[] events) throws Exception {
        //先判断events长度必须等处理函数outflows长度
        if ( events.length == 0) {
            throw new Exception("invalid events or outflows!");
        }
        List<String> list = Arrays.stream(events).map(e -> EventEncoder.encode(e)).collect(Collectors.toList());
        BigInteger endBlockNumber = overBlockNumber;
        if(overBlockNumber == null){
            endBlockNumber = web3j.ethBlockNumber().send().getBlockNumber();
            System.out.println(endBlockNumber);
        }

        System.out.println("first get eth logs.....");
        //分批次处理事件日志，防止数据量过大造成宕机
        for (BigInteger i = startBlockNumber; i.compareTo(endBlockNumber) <= 0; i = i.add(MAX_SIZE)) {
            BigInteger finalHeight = i.add(MAX_SIZE);
            if(finalHeight.compareTo(endBlockNumber)> 0){
                finalHeight = endBlockNumber;
            }
            System.out.println("get logs from "+i+" to "+finalHeight);
            EthLog ethLog = web3j.ethGetLogs(new EthFilter(new DefaultBlockParameterNumber(i), new DefaultBlockParameterNumber(finalHeight), address).addOptionalTopics(
                    list.toArray(new String[list.size()])
            )).send();
            ethLog.getLogs().forEach((log) -> {
                logProcess((Log) log.get(), events);
            });
        }

        System.out.println("then subscribe eth log flow");
        // 只有需要监听最新事件时再监听处理
        if(overBlockNumber == null){
        	System.out.println("subscribe from "+endBlockNumber+" to latest");
            EthFilter filter = new EthFilter(new DefaultBlockParameterNumber(endBlockNumber), DefaultBlockParameterName.LATEST, address).addOptionalTopics(
                    list.toArray(new String[list.size()])
            );
            // FIXME 这里需要支持订阅的网络协议，比如YCC的订阅是通过WS协议实现，单独的服务接口
            web3j.ethLogFlowable(filter).subscribe(log -> {
                logProcess(log, events);
            });
        }
    }

    public static void logProcess(Log logInfo, Event[] events) {
        for (int i = 0; i < events.length; i++) {
        	System.out.println("-------"+logInfo.getTopics().get(0));
            if (logInfo.getTopics().get(0).equals(EventEncoder.encode(events[i]))) {
            	System.out.println(JSON.toJSONString(logInfo));
                //解析非索引字段
                List<Type> results = new ArrayList<Type>();
                if (logInfo.getData() != null) {
                    results = FunctionReturnDecoder.decode(logInfo.getData(), events[i].getNonIndexedParameters());
                }
                List<Type> indexed = new ArrayList<Type>();
                //解析索引字段
                if (logInfo.getTopics().size() >= 2) {
                    for (int j = 1; j < logInfo.getTopics().size(); j++) {
                        indexed.add(FunctionReturnDecoder.decodeIndexedValue(logInfo.getTopics().get(j), events[i].getIndexedParameters().get(j - 1)));
                    }
                }
//                //流出数据处理
//                outflows[i].outflow(logInfo.getBlockNumber(), logInfo.getTransactionIndex(), logInfo.getLogIndex(), results, indexed, logInfo.getAddress(), logInfo.getTransactionHash());
            }

            // 通用处理，避免过多区块中无注册日志信息，下次重头来过
            
//            commonLogPro.BlockNumber(logInfo.getAddress(), logInfo.getBlockNumber(), logInfo.getTransactionIndex());
        }
    }

    public static void main(String[] args) throws Exception{
    	CustomWebSocketClient customClient = new CustomWebSocketClient(URI.create("ws://121.52.224.90:8547")) {   		 
    		    @Override
    		    public void onError(Exception ex) {
    		        // 出现异常时的处理逻辑
    		        System.out.println("Error: " + ex.getMessage());
    		    }
    	};
    	WebSocketService webSocketService = new WebSocketService(customClient, false);
    	webSocketService.connect();
    	        
        Web3j web3jws = Web3j.build(webSocketService);
        String contractAddress = "0xEb1cB67CE18203db5532DD404669782DA1aFF974";
        Event[] events = new Event[]{RegisteredEvent, TransferEvent, RenewedEvent};
        Filter.Filter(web3jws, contractAddress, BigInteger.valueOf(27023000), null, events);
        
    }
}
