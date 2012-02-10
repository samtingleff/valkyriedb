package com.valkyrie.db.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.management.ManagementFactory;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

import org.apache.thrift.TProcessorFactory;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TTransportException;
import org.apache.thrift.transport.TTransportFactory;

import com.mtbaker.client.Configuration;
import com.mtbaker.client.ConfigurationClient;
import com.mtbaker.client.annotations.ConfigurationInjector;
import com.mtbaker.client.provider.io.FileInputStreamSource;
import com.mtbaker.client.provider.xml.XmlConfigurationClient;
import com.valkyrie.db.gen.ValkyrieDbService;

public class ValkyrieDbServer {

	public static void main(String[] args) throws Exception {
		ValkyrieDbServer db = new ValkyrieDbServer();
		db.init(args);
		db.start();
	}

	private ConfigurationClient conf;

	private PartitionedLocalStore localStorage;

	private ValkyrieDbServiceHandler service;

	private Thread serviceThread;

	public ValkyrieDbServer() {
		
	}

	public void init(String[] args) throws Exception {
		OptionParser parser = new OptionParser();
		OptionSpec<String> config = parser.accepts("c")
				.withRequiredArg().defaultsTo("/etc/valkyriedb.xml");
		OptionSpec<String> pid = parser.accepts("p")
				.withRequiredArg().defaultsTo("/var/run/valkyriedb.pid");
		OptionSet options = parser.parse(args);
		writePid(pid.value(options));
		String path = config.value(options);
		initConfiguration(path);
		initLocalStorage();
		initServiceHandler();
		initNetworkService();
	}

	public void start() {
		this.serviceThread.start();
	}

	public void stop() {
		this.serviceThread.stop();
	}

	private void writePid(String pidPath) throws IOException {
		String pid = getPid("valkyriedb");
		FileOutputStream fos = new FileOutputStream(new File(pidPath));
		try {
			fos.write(pid.getBytes());
		} finally {
			fos.close();
		}
	}

	private String getPid(String fallback) {
	    // Note: may fail in some JVM implementations
	    // therefore fallback has to be provided

	    // something like '<pid>@<hostname>', at least in SUN / Oracle JVMs
	    final String jvmName = ManagementFactory.getRuntimeMXBean().getName();
	    final int index = jvmName.indexOf('@');

	    if (index < 1) {
	        // part before '@' empty (index = 0) / '@' not found (index = -1)
	        return fallback;
	    }

	    try {
	        return Long.toString(Long.parseLong(jvmName.substring(0, index)));
	    } catch (NumberFormatException e) {
	        // ignore
	    	return fallback;
	    }
	}

	private void initConfiguration(String path) throws IOException {
		File f = new File(path);
		conf = new XmlConfigurationClient(new FileInputStreamSource(f));
	}

	private void initLocalStorage() throws Exception {
		localStorage = new PartitionedLocalStore(conf);
		ConfigurationInjector injector = new ConfigurationInjector(this.conf);
		injector.inject(localStorage);
		localStorage.init();
	}

	private void initServiceHandler() {
		service = new ValkyrieDbServiceHandler(localStorage);
		service.init();
	}

	private void initNetworkService() throws TTransportException, IOException {
		Configuration cc = conf.getConfiguration("server", 1000*60*60);
		ValkyrieDbService.Processor processor = new ValkyrieDbService.Processor(this.service);
		TProcessorFactory processorFactory = new TProcessorFactory(processor);

		TProtocolFactory pfactory = new TBinaryProtocol.Factory();
		TTransportFactory ttfactory = new TFramedTransport.Factory();
		TServerSocket serverTransport = new TServerSocket(
				cc.getInteger("port",
						com.valkyrie.db.gen.Constants.DEFAULT_PORT));
		TThreadPoolServer.Args options = new TThreadPoolServer.Args(serverTransport);
		options.minWorkerThreads = cc.getInteger("minthreads", 1);
		options.maxWorkerThreads = cc.getInteger("maxthreads", 10);
		options.processor(processor);
		options.processorFactory(processorFactory);
		options.protocolFactory(pfactory);
		options.transportFactory(ttfactory);

		final TServer server = new TThreadPoolServer(options);

		this.serviceThread = new Thread(new Runnable() {
			public void run() {
				server.serve();
			}
		}, "ValkyrieDbService");
		this.serviceThread.setDaemon(cc.getBoolean("daemonthread", false));
	}
}
