package cn.eastseven.tcpip.sockets.in.java.ch3;

import java.io.OutputStream;
import java.net.Socket;

import org.apache.log4j.Logger;

public class VoteClientTCP {

	private static final Logger log = Logger.getLogger(VoteClientTCP.class);
	
	public static final int CANDIDATE_ID = 888;
	
	public static void main(String[] args) throws Exception {
		String addr = "127.0.0.1";
		int port = 9527;
		
		Socket sock = new Socket(addr, port);
		OutputStream out = sock.getOutputStream();
		
		VoteMsgCoder coder = new VoteMsgBinCoder();
		Framer framer = new LengthFramer(sock.getInputStream());
		
		VoteMsg inquiryRequest = new VoteMsg(false, true, CANDIDATE_ID, 0);
		byte[] encodeMsg = coder.toWire(inquiryRequest);
		
		//查询请求
		log.info("查询请求 ( "+encodeMsg.length+" bytes): " + inquiryRequest);
		framer.frameMsg(encodeMsg, out);
		//查询响应
		encodeMsg = framer.nextMsg();
		VoteMsg inquiryResponse = coder.formWire(encodeMsg);
		log.info("查询响应 ( "+encodeMsg.length+" bytes): " + inquiryResponse);
		
		VoteMsg voteRequest = new VoteMsg(false, false, CANDIDATE_ID, 0);
		encodeMsg = coder.toWire(voteRequest);
		//投票请求
		log.info("投票请求 ( "+encodeMsg.length+" bytes): " + voteRequest);
		framer.frameMsg(encodeMsg, out);
		//投票响应
		encodeMsg = framer.nextMsg();
		VoteMsg voteResponse = coder.formWire(encodeMsg);
		log.info("投票响应 ( "+encodeMsg.length+" bytes): " + voteResponse);
		
		sock.close();
	}
}
