package br.ce.wcaquino.runners;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerScheduler;

public class ParallelRunner extends BlockJUnit4ClassRunner{

	public ParallelRunner(Class<?> testClass) throws InitializationError {
		super(testClass);
		setScheduler(new TheadPoll());
	}
	
	public static class TheadPoll implements RunnerScheduler {

		private ExecutorService executorService;
		
		public TheadPoll() {
			executorService = Executors.newFixedThreadPool(2);
		}

		public void schedule(Runnable runnable) {
			executorService.submit(runnable);
		}
		
		public void finished() {
			executorService.shutdown();
			try {
				executorService.awaitTermination(10, TimeUnit.MINUTES);
			} catch (InterruptedException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		
	}

}
