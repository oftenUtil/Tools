package main.java.com.jbutton.commons.threadpool;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author lee
 * @version 1.0
 * @date 2020/6/10 9:36
 */
public class CustomScheduledThreadPool {
    public static ScheduledExecutorService newDefaultSingleThreadScheduledExecutor() {
        return new DelegatedScheduledExecutorService
                (new ScheduledThreadPoolExecutor(1));
    }
    public static ScheduledExecutorService newSingleThreadScheduledExecutor() {
        return new DelegatedScheduledExecutorService
                (new CustomScheduledThreadPoolExecutor(1, 5));
    }

    static class DelegatedScheduledExecutorService
            extends DelegatedExecutorService
            implements ScheduledExecutorService {
        private final ScheduledExecutorService e;
        DelegatedScheduledExecutorService(ScheduledExecutorService executor) {
            super(executor);
            e = executor;
        }
        @Override
        public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
            return e.schedule(command, delay, unit);
        }
        @Override
        public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
            return e.schedule(callable, delay, unit);
        }
        @Override
        public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
            return e.scheduleAtFixedRate(command, initialDelay, period, unit);
        }
        @Override
        public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
            return e.scheduleWithFixedDelay(command, initialDelay, delay, unit);
        }
    }
    static class DelegatedExecutorService extends AbstractExecutorService {
        private final ExecutorService e;
        DelegatedExecutorService(ExecutorService executor) { e = executor; }
        @Override
        public void execute(Runnable command) { e.execute(command); }
        @Override
        public void shutdown() { e.shutdown(); }
        @Override
        public List<Runnable> shutdownNow() { return e.shutdownNow(); }
        @Override
        public boolean isShutdown() { return e.isShutdown(); }
        @Override
        public boolean isTerminated() { return e.isTerminated(); }
        @Override
        public boolean awaitTermination(long timeout, TimeUnit unit)
                throws InterruptedException {
            return e.awaitTermination(timeout, unit);
        }
        @Override
        public Future<?> submit(Runnable task) {
            return e.submit(task);
        }
        @Override
        public <T> Future<T> submit(Callable<T> task) {
            return e.submit(task);
        }
        @Override
        public <T> Future<T> submit(Runnable task, T result) {
            return e.submit(task, result);
        }
        @Override
        public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks)
                throws InterruptedException {
            return e.invokeAll(tasks);
        }
        @Override
        public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks,
                                             long timeout, TimeUnit unit)
                throws InterruptedException {
            return e.invokeAll(tasks, timeout, unit);
        }
        @Override
        public <T> T invokeAny(Collection<? extends Callable<T>> tasks)
                throws InterruptedException, ExecutionException {
            return e.invokeAny(tasks);
        }
        @Override
        public <T> T invokeAny(Collection<? extends Callable<T>> tasks,
                               long timeout, TimeUnit unit)
                throws InterruptedException, ExecutionException, TimeoutException {
            return e.invokeAny(tasks, timeout, unit);
        }
    }
}
