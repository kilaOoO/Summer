package com.bingsenh.Summer.connector.nio;

import java.nio.channels.ServerSocketChannel;

public interface Boss {

    public void registerAcceptChannelTask(ServerSocketChannel serverSocketChannel);
}
