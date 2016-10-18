package com.nurkiewicz.rxjava;

import com.nurkiewicz.rxjava.util.Sleeper;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.Subscriptions;

import static com.nurkiewicz.rxjava.util.Threads.runInBackground;
import static java.time.Duration.ofSeconds;

/**
 * Presentation assistant
 * Key promoter
 */
@Ignore
public class R03_Unsubscription {
	
	private static final Logger log = LoggerFactory.getLogger(R03_Unsubscription.class);
	
	@Test
	public void unsubscriptionNotHandled() throws Exception {
		Observable<String> obs = Observable.create(sub -> pinger(sub));
		Subscription subscription = obs.subscribe(log::info);
		
		Sleeper.sleep(ofSeconds(3));
		subscription.unsubscribe();
		log.info("Unsubscribed?");
		Sleeper.sleep(ofSeconds(3));
	}
	
	private Thread pinger(Subscriber<? super String> sub) {
		return runInBackground(() -> {
			while (!sub.isUnsubscribed()) {
				sub.onNext("Ping!");
				Sleeper.sleep(ofSeconds(5));
			}
			log.info("Stopped");
		});
	}
	
	/**
	 * Hint: sub.add(Subscriptions.create(...))
	 * Hint: Thread.interrupt()
	 */
	@Test
	public void handleUnsubscription() throws Exception {
		Observable<String> obs = Observable.create(sub -> {
			Thread thread = pinger(sub);
			sub.add(Subscriptions.create(thread::interrupt));
		});
		Subscription subscription = obs.subscribe(log::info);
		
		Sleeper.sleep(ofSeconds(3));
		subscription.unsubscribe();
		log.info("Unsubscribed?");
		Sleeper.sleep(ofSeconds(3));
	}
	
}
