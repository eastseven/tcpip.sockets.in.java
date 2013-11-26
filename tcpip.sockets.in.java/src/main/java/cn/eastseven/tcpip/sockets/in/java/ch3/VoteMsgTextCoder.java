package cn.eastseven.tcpip.sockets.in.java.ch3;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class VoteMsgTextCoder implements VoteMsgCoder {

	public static final String MAGIC = "Voting";
	public static final String VOTE_STR = "v";
	public static final String INQ_STR = "i";
	public static final String RESPONSE_STR = "R";
	
	public static final String CHARSET_NAME = "utf-8";
	public static final String DELIMSTR = " ";
	public static final int MAX_WIRE_LENGTH = 2000;
	private Scanner s;
	
	public byte[] toWire(VoteMsg msg) throws IOException {
		String msgString = MAGIC + DELIMSTR + (msg.isInquiry() ? INQ_STR : VOTE_STR);
		msgString += DELIMSTR + (msg.isResponse() ? RESPONSE_STR + DELIMSTR : "");
		msgString += Integer.toString(msg.getCandidateID()) + DELIMSTR;
		msgString += Long.toString(msg.getVoteCount());
		byte[] data = msgString.getBytes(CHARSET_NAME);
		return data;
	}

	public VoteMsg formWire(byte[] message) throws IOException {
		ByteArrayInputStream msgStream = new ByteArrayInputStream(message);
		s = new Scanner(new InputStreamReader(msgStream, CHARSET_NAME));
		boolean isInquiry, isResponse;
		int candidateID;
		long voteCount;
		String token;
		
		token = s.next();
		if(!token.contains(MAGIC)) {
			throw new IOException("bad magic string: " + token);
		}
		
		token = s.next();
		if(token.equals(VOTE_STR)) {
			isInquiry = false;
		} else if(!token.equals(INQ_STR)) {
			throw new IOException("bad vote/inq indicator: " + token);
		} else {
			isInquiry = true;
		}

		token = s.next();
		if(token.equals(RESPONSE_STR)) {
			isResponse = true;
			token = s.next();
		} else {
			isResponse = false;
		}
		
		candidateID = Integer.parseInt(token);
		if(isResponse) {
			token = s.next();
			voteCount = Long.parseLong(token);
		} else {
			voteCount = 0;
		}
		
		s.close();
		
		return new VoteMsg(isResponse, isInquiry, candidateID, voteCount);
	}

}
