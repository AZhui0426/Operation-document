package com.shenlan.prometheus.client.httpserver.http_server;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.HTTPServer;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * @author guomaofei
 * @date 2021/6/8 11:49
 */
public class WebServer {

    private String host;

    private int port;

    public WebServer(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void startPrometheusClientServer() throws IOException {
        InetSocketAddress socket = new InetSocketAddress(host, port);
        new HTTPServer(socket, CollectorRegistry.defaultRegistry);
    }
}
