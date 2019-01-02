package hiscore.net.nio.handlers;

import java.nio.channels.SelectionKey;

abstract class OperationHandler implements Runnable {

    final SelectionKey key;

    OperationHandler(SelectionKey key) {
        this.key = key;
        key.interestOps(0);
    }

    final void interestOps(int operations) {
        key.interestOps(operations);
        key.selector().wakeup();
    }
}