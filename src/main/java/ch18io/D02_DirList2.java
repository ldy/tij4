package ch18io;

import java.util.regex.*;
import java.io.*;
import java.util.*;

/**
 * Uses anonymous inner classes. <br>
 * {Args: "D.*\.java"}
 * 
 * <pre>
 * Output:
 * DirectoryDemo.java
 * DirList.java
 * DirList2.java
 * DirList3.java
 * </pre>
 */
public class D02_DirList2 {
	public static FilenameFilter filter(final String regex) {
		// Creation of anonymous inner class:
		return new FilenameFilter() {
			private Pattern pattern = Pattern.compile(regex);

			public boolean accept(File dir, String name) {
				return pattern.matcher(name).matches();
			}
		}; // End of anonymous inner class
	}

	public static void main(String[] args) {
		File path = new File(".");
		String[] list;
		if (args.length == 0)
			list = path.list();
		else
			list = path.list(filter(args[0]));
		Arrays.sort(list, String.CASE_INSENSITIVE_ORDER);
		for (String dirItem : list)
			System.out.println(dirItem);
	}
}