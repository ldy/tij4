package ch15generics;

import java.lang.reflect.*;
import java.util.*;
import static commons.util.Print.*;

/**
 * {main: ApplyTest}
 * 
 * <pre>
 * (Execute to see output)
 * </pre>
 */
public class D83_Apply {
	public static <T, S extends Iterable<? extends T>> void apply(S seq, Method f, Object... args) {
		try {
			for (T t : seq)
				f.invoke(t, args);
		} catch (Exception e) {
			// Failures are programmer errors
			throw new RuntimeException(e);
		}
	}
}

class Shape {
	public void rotate() {
		print(this + " rotate");
	}

	public void resize(int newSize) {
		print(this + " resize " + newSize);
	}
}

class Square extends Shape {
}

class FilledList<T> extends ArrayList<T> {
	public FilledList(Class<? extends T> type, int size) {
		try {
			for (int i = 0; i < size; i++)
				// Assumes default constructor:
				add(type.newInstance());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}

class ApplyTest {
	public static void main(String[] args) throws Exception {
		List<Shape> shapes = new ArrayList<Shape>();
		for (int i = 0; i < 10; i++)
			shapes.add(new Shape());
		D83_Apply.apply(shapes, Shape.class.getMethod("rotate"));
		D83_Apply.apply(shapes, Shape.class.getMethod("resize", int.class), 5);
		List<Square> squares = new ArrayList<Square>();
		for (int i = 0; i < 10; i++)
			squares.add(new Square());
		D83_Apply.apply(squares, Shape.class.getMethod("rotate"));
		D83_Apply.apply(squares, Shape.class.getMethod("resize", int.class), 5);

		D83_Apply.apply(new FilledList<Shape>(Shape.class, 10), Shape.class.getMethod("rotate"));
		D83_Apply.apply(new FilledList<Shape>(Square.class, 10), Shape.class.getMethod("rotate"));

		D83_SimpleQueue<Shape> shapeQ = new D83_SimpleQueue<Shape>();
		for (int i = 0; i < 5; i++) {
			shapeQ.add(new Shape());
			shapeQ.add(new Square());
		}
		D83_Apply.apply(shapeQ, Shape.class.getMethod("rotate"));
	}
}