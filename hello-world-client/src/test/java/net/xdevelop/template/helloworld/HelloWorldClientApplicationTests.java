package net.xdevelop.template.helloworld;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HelloWorldClientApplicationTests {

	@Autowired
	TestAsyncService testAsyncService;

	@Test
	public void contextLoads() {
	}

	@Test
	public void testAsyncService() throws Exception{
		testAsyncService.test();
		System.out.println("asyncServiceTest:"+Thread.currentThread().getId());
	}

	@Test
	public void testAsyncAnnotationForMethodsWithReturnType()
			throws InterruptedException, ExecutionException {
		System.out.println("Invoking an asynchronous method. "
				+ Thread.currentThread().getName());
		Future<String> future = testAsyncService.asyncMethodWithReturnType();

		while (true) {
			if (future.isDone()) {
				System.out.println("Result from asynchronous process - " + future.get());
				break;
			}
			System.out.println("Continue doing something else. ");
			Thread.sleep(1000);
		}
	}

}
