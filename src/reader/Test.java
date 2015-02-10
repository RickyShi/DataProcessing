package reader;

import java.util.ArrayList;
import java.util.Iterator;

public class Test {
	public static void main(String[] args) {
		// String s = "-39.0";
		// double d = Double.parseDouble(s);
		// System.out.println(d);
		ArrayList<Integer> aList = new ArrayList<Integer>(2);
		aList.add(1);
		aList.add(2);
		aList.add(3);

		Iterator<Integer> it = aList.iterator();
		while (it.hasNext()) {
			System.out.println(it.next());
		}
	}
}
