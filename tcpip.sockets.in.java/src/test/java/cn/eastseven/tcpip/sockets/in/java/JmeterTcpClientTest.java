package cn.eastseven.tcpip.sockets.in.java;

import java.io.OutputStream;
import java.net.Socket;

import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.log4j.Logger;

public class JmeterTcpClientTest extends AbstractJavaSamplerClient {

	private static final Logger log = Logger.getLogger(JmeterTcpClientTest.class);

	private final String hostname = ConfigFileUtils.SERVER_HOST;
	private final int port = ConfigFileUtils.SERVER_PORT;
	
	public SampleResult runTest(JavaSamplerContext ctx) {
		log.info("test start");
		String message = "hello jmeter tcp test";

		SampleResult sr = new SampleResult();
		sr.setSampleLabel("Socket Test");
		try {
			Socket s = new Socket(hostname, port);
			OutputStream out = s.getOutputStream();
			out.write(message.getBytes());
			out.close();
			s.close();
		} catch (Exception e) {
			e.printStackTrace();
			sr.setSuccessful(false);
		} finally {
			sr.sampleEnd();
		}
		return sr;
	}

	@Override
	public void setupTest(JavaSamplerContext context) {
		log.info("test setup");
		super.setupTest(context);
	}

	@Override
	public void teardownTest(JavaSamplerContext context) {
		log.info("test teardown");
		super.teardownTest(context);
	}
}
