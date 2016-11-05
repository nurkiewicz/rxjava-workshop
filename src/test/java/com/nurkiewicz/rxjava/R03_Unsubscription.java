package com.nurkiewicz.rxjava;

import com.nurkiewicz.rxjava.util.Sleeper;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.disposables.Disposable;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.nurkiewicz.rxjava.util.Threads.runInBackground;
import static java.time.Duration.ofSeconds;

@Ignore
public class R03_Unsubscription {
	
	private static final Logger log = LoggerFactory.getLogger(R03_Unsubscription.class);
	
	@Test
	public void unsubscriptionNotHandled() throws Exception {
		Observable<String> obs = Observable.create(sub -> pinger(sub));
		Disposable subscription = obs.subscribe(log::info);
		
		Sleeper.sleep(ofSeconds(3));
		subscription.dispose();
		log.info("Unsubscribed?");
		Sleeper.sleep(ofSeconds(3));
	}
	
	private Thread pinger(ObservableEmitter<? super String> sub) {
		return runInBackground(() -> {
			while (!sub.isDisposed()) {
				sub.onNext("Ping!");
				Sleeper.sleep(ofSeconds(5));
			}
			log.info("Stopped");
		});
	}
	
	/**
	 * Hint: sub.setCancellable(...)
	 * Hint: Thread.interrupt()
	 * Hint: Thread.currentThread().isInterrupted()
	 */
	@Test
	public void handleUnsubscription() throws Exception {
		Observable<String> obs = Observable.create(sub -> {
			Thread thread = pinger(sub);
			//...
		});
		Disposable subscription = obs.subscribe(log::info);
		
		Sleeper.sleep(ofSeconds(3));
		subscription.dispose();
		log.info("Unsubscribed?");
		Sleeper.sleep(ofSeconds(3));
	}
	
}
