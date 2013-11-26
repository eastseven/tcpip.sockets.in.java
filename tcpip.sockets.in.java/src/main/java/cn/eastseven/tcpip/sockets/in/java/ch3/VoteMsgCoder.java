package cn.eastseven.tcpip.sockets.in.java.ch3;

import java.io.IOException;

public interface VoteMsgCoder {

	byte[] toWire(VoteMsg msg) throws IOException;
	
	VoteMsg formWire(byte[] input) throws IOException;
}
