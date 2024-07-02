package e.helper;

import java.util.concurrent.Executor;

public class MultiRequestableOnceExecutor implements Executor {
    private Runnable command;
    private volatile boolean requested;
    private Thread thread;
    private int sleep = 1000;
    
    public MultiRequestableOnceExecutor(Runnable command) {
        this.command = command;
    }

    public MultiRequestableOnceExecutor(Runnable command, int sleep) {
        this.command = command;
        this.sleep = sleep;
    }
    
    public void setSleep(int sleep) {
        this.sleep = sleep;
    }
    
    @Override
    public void execute(Runnable command) {
        synchronized (this) {
            if (this.thread != null) {
                return;
            }
            this.thread = new Thread() {
                @Override
                public void run() {
                    while (true) {
                        synchronized (MultiRequestableOnceExecutor.this) {
                            if (!requested) {
                                thread = null;
                                MultiRequestableOnceExecutor.this.notifyAll();
                                break;
                            }
                        }
                        try {
                            Thread.sleep(sleep);
                        } catch (InterruptedException exception) {
                            throw new RuntimeException(exception);
                        }
                        requested = false;
                        command.run();
                    }
                }
            };
        }
        this.thread.start();
    }

    public void request() {
        this.requested = true;
        this.execute(this.command);
    }
    
    public void waitIfBusy() {
        synchronized (this) {
            if (!this.requested && this.thread == null) {
                return;
            }
            try {
                this.wait();
            } catch (InterruptedException exception) {
                throw new RuntimeException(exception);
            }
        }
    }
}
