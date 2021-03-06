package ru.sbt;

import java.util.concurrent.*;

/**
 * Created by Рябов Дмитрий on 13.09.2016.
 */
public class PingPongThread extends Thread {

	private final String text;
	private final boolean ping;
	private static boolean currentState;
	private static final Object lock = new Object();

	private static Executor executor = Executors.newSingleThreadExecutor();

	PingPongThread(boolean ping) {
		this.ping = ping;
		text = ping ? "Ping!" : "Pong!";
	}

	@Override
	public void run() {
		synchronized (lock) {
			while (true) {
				while (currentState != ping) {
					aWait();
				}
				executor.execute(() -> System.out.println(text));
				currentState = !currentState;
				lock.notify();
			}
		}
	}

	private void aWait() {
		try {
			lock.wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new PingPongThread(true).start();
		new PingPongThread(false).start();
	}
}
