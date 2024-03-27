package e.helper;

import java.util.concurrent.Executor;

public class MultiRequestableOnceExecutor implements Executor {
    private Runnable command;
    private Thread thread;
    
    public MultiRequestableOnceExecutor(Runnable command) {
        this.command = command;
    }
    
    @Override
    public void execute(Runnable command) {
        this.thread = new Thread() {};
        this.thread.start();
    }
    
    public void request() {
    }
}
