package main.java.com.jbutton.commons.threadpool;


/**
 * @author lee
 * @version 1.0
 * @date 2020/6/10 10:19
 */
public interface CustomRejectedExecutionHandler {
    void rejectedExecution(Runnable r, CustomThreadPoolExecutor executor);
}
