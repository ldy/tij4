package ch21concurrency;

import java.util.concurrent.*;
import static commons.util.Print.*;

/**
 * The producer-consumer approach to task cooperation.
 * 
 * <pre>
 * Output:
 * Order up! Waitperson got Meal 1
 * Order up! Waitperson got Meal 2
 * Order up! Waitperson got Meal 3
 * Order up! Waitperson got Meal 4
 * Order up! Waitperson got Meal 5
 * Order up! Waitperson got Meal 6
 * Order up! Waitperson got Meal 7
 * Order up! Waitperson got Meal 8
 * Order up! Waitperson got Meal 9
 * Out of food, closing
 * WaitPerson interrupted
 * Order up! Chef interrupted
 * </pre>
 */
class Meal {
	private final int orderNum;

	public Meal(int orderNum) {
		this.orderNum = orderNum;
	}

	public String toString() {
		return "Meal " + orderNum;
	}
}

class WaitPerson implements Runnable {
	private D48_Restaurant restaurant;

	public WaitPerson(D48_Restaurant r) {
		restaurant = r;
	}

	public void run() {
		try {
			while (!Thread.interrupted()) {
				synchronized (this) {
					while (restaurant.meal == null)
						wait(); // ... for the chef to produce a meal
				}
				print("Waitperson got " + restaurant.meal);
				synchronized (restaurant.chef) {
					restaurant.meal = null;
					restaurant.chef.notifyAll(); // Ready for another
				}
			}
		} catch (InterruptedException e) {
			print("WaitPerson interrupted");
		}
	}
}

class Chef implements Runnable {
	private D48_Restaurant restaurant;
	private int count = 0;

	public Chef(D48_Restaurant r) {
		restaurant = r;
	}

	public void run() {
		try {
			while (!Thread.interrupted()) {
				synchronized (this) {
					while (restaurant.meal != null)
						wait(); // ... for the meal to be taken
				}
				if (++count == 10) {
					print("Out of food, closing");
					restaurant.exec.shutdownNow();
				}
				printnb("Order up! ");
				synchronized (restaurant.waitPerson) {
					restaurant.meal = new Meal(count);
					restaurant.waitPerson.notifyAll();
				}
				TimeUnit.MILLISECONDS.sleep(100);
			}
		} catch (InterruptedException e) {
			print("Chef interrupted");
		}
	}
}

public class D48_Restaurant {
	Meal meal;
	ExecutorService exec = Executors.newCachedThreadPool();
	WaitPerson waitPerson = new WaitPerson(this);
	Chef chef = new Chef(this);

	public D48_Restaurant() {
		exec.execute(chef);
		exec.execute(waitPerson);
	}

	public static void main(String[] args) {
		new D48_Restaurant();
	}
}