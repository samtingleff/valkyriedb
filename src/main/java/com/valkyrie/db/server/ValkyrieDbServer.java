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
import com.othersonline.kv.gen.Constants;
import com.othersonline.kv.gen.KeyValueService;
import com.valkyrie.db.gen.ValkyrieDbS2SService;

public class ValkyrieDbServer {

	public static void main(String[] args) throws Exception {
		ValkyrieDbServer db = new ValkyrieDbServer();
		db.init();
		db.start();
	}

	private Configuration conf;

	private PartitionedLocalStore localStorage;

	private ThriftServiceHandler service;

	private ValkyrieS2SServiceHandler s2s;

	private Thread serviceThread;

	private Thread s2sServiceThread;

	public ValkyrieDbServer() {
		
	}

	public void init() throws Exception {
		initConfiguration();
		initLocalStorage();
		initS2SServiceHandler();
		initS2SNetworkService();
		initServiceHandler();
		initNetworkService();
	}

	public void start() {
		this.serviceThread.start();
		this.s2sServiceThread.start();
	}

	public void stop() {
		this.serviceThread.stop();
		this.s2sServiceThread.stop();
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

	private void initServiceHandler() throws Exception {
		service = new ThriftServiceHandler(conf, localStorage);
		service.init();
	}

	private void initS2SServiceHandler() {
		s2s = new ValkyrieS2SServiceHandler(conf, localStorage);
		s2s.init();
	}

	private void initNetworkService() throws TTransportException, IOException {
		KeyValueService.Processor processor = new KeyValueService.Processor(this.service);
		TProcessorFactory processorFactory = new TProcessorFactory(processor);

		TProtocolFactory pfactory = new TBinaryProtocol.Factory();
		TTransportFactory ttfactory = new TFramedTransport.Factory();
		TServerSocket serverTransport = new TServerSocket(
				conf.getInteger("valkyrie.server.port", Constants.DEFAULT_PORT));
		TThreadPoolServer.Args options = new TThreadPoolServer.Args(serverTransport);
		options.minWorkerThreads = conf.getInteger("valkyrie.server.minthreads", 1);
		options.maxWorkerThreads = conf.getInteger("valkyrie.server.maxthreads", 100);
		options.processor(processor);
		options.processorFactory(processorFactory);
		options.protocolFactory(pfactory);
		options.transportFactory(ttfactory);

		final TServer server = new TThreadPoolServer(options);

		this.serviceThread = new Thread(new Runnable() {
			public void run() {
				server.serve();
			}
		}, "KeyValueService");
		this.serviceThread.setDaemon(conf.getBoolean("valkyrie.server.daemon", false));
	}

	private void initS2SNetworkService() throws TTransportException, IOException {
		ValkyrieDbS2SService.Processor processor = new ValkyrieDbS2SService.Processor(this.s2s);
		TProcessorFactory processorFactory = new TProcessorFactory(processor);

		TProtocolFactory pfactory = new TBinaryProtocol.Factory();
		TTransportFactory ttfactory = new TFramedTransport.Factory();
		TServerSocket serverTransport = new TServerSocket(
				conf.getInteger("valkyrie.server.s2s.port",
						com.valkyrie.db.gen.Constants.DEFAULT_PORT));
		TThreadPoolServer.Args options = new TThreadPoolServer.Args(serverTransport);
		options.minWorkerThreads = conf.getInteger("valkyrie.server.s2s.minthreads", 1);
		options.maxWorkerThreads = conf.getInteger("valkyrie.server.s2s.smaxthreads", 100);
		options.processor(processor);
		options.processorFactory(processorFactory);
		options.protocolFactory(pfactory);
		options.transportFactory(ttfactory);

		final TServer server = new TThreadPoolServer(options);

		this.s2sServiceThread = new Thread(new Runnable() {
			public void run() {
				server.serve();
			}
		}, "ValkyrieDbService");
		this.s2sServiceThread.setDaemon(conf.getBoolean("valkyrie.server.daemon", false));
	}
}
