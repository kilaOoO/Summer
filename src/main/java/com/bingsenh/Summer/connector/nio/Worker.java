package com.bingsenh.Summer.connector.nio;

import java.nio.channels.SocketChannel;

public interface Worker {
    public void registerNewChannelTask(SocketChannel socketChannel);
}
