package net.xdevelop.template.helloworld;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

@Service
public class TestAsyncService {
    @Async
    public void test() throws Exception {
        int a = 1/0;
        Thread.sleep(10000);//让线程休眠，根据输出结果判断主线程和从线程是同步还是异步
        System.out.println("异步threadId:"+Thread.currentThread().getId());
    }

    @Async
    public Future<String> asyncMethodWithReturnType() {
        System.out.println("Execute method asynchronously - "
                + Thread.currentThread().getName());
        try {
            Thread.sleep(5000);
            return null;
//            return new AsyncResult<String>("hello world !!!!");
        } catch (InterruptedException e) {
            //
        }
        return null;
    }
}
