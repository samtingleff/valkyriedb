package com.valkyrie.db.server;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

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
import com.mtbaker.client.provider.properties.PropertiesConfigurationClient;
import com.valkyrie.db.gen.ValkyrieDbService;

public class ValkyrieDbServer {

	public static void main(String[] args) throws Exception {
		ValkyrieDbServer db = new ValkyrieDbServer();
		db.init();
		db.start();
	}

	private Configuration conf;

	private PartitionedLocalStore localStorage;

	private ValkyrieDbServiceHandler service;

	private Thread serviceThread;

	public ValkyrieDbServer() {
		
	}

	public void init() throws Exception {
		initConfiguration();
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

	private void initConfiguration() throws IOException {
		Properties props = new Properties();
		InputStream is = new FileInputStream("/etc/valkyrie.properties");
		try {
			props.load(is);
			PropertiesConfigurationClient client = new PropertiesConfigurationClient(
					props);
			conf = client.getConfiguration(null, 10);
		} finally {
			is.close();
		}
	}

	private void initLocalStorage() throws Exception {
		localStorage = new PartitionedLocalStore(conf);
		localStorage.init();
	}

	private void initServiceHandler() {
		service = new ValkyrieDbServiceHandler(conf, localStorage);
		service.init();
	}

	private void initNetworkService() throws TTransportException, IOException {
		ValkyrieDbService.Processor processor = new ValkyrieDbService.Processor(this.service);
		TProcessorFactory processorFactory = new TProcessorFactory(processor);

		TProtocolFactory pfactory = new TBinaryProtocol.Factory();
		TTransportFactory ttfactory = new TFramedTransport.Factory();
		TServerSocket serverTransport = new TServerSocket(
				conf.getInteger("valkyrie.server.port",
						com.valkyrie.db.gen.Constants.DEFAULT_PORT));
		TThreadPoolServer.Args options = new TThreadPoolServer.Args(serverTransport);
		options.minWorkerThreads = conf.getInteger("valkyrie.server.minthreads", 1);
		options.maxWorkerThreads = conf.getInteger("valkyrie.server.smaxthreads", 100);
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
		this.serviceThread.setDaemon(conf.getBoolean("valkyrie.server.daemon", false));
	}
}
