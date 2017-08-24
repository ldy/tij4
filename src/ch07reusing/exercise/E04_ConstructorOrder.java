package ch07reusing.exercise;

/**
 * Exercise 4
 * 
 * <pre>
 * Prove that base-class constructors are (a)
 * always called and (b) called before
 * derived-class constructors.
 *
 * Output:
 * Base1
 * Derived1
 * Derived2
 * </pre>
 */
class Base1 {
	public Base1() {
		System.out.println("Base1");
	}
}

class Derived1 extends Base1 {
	public Derived1() {
		System.out.println("Derived1");
	}
}

class Derived2 extends Derived1 {
	public Derived2() {
		System.out.println("Derived2");
	}
}

public class E04_ConstructorOrder {
	public static void main(String args[]) {
		new Derived2();
	}
}