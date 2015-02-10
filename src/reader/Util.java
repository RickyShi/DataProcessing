package reader;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Util {
	public static void writeMethod(String fileName, String toWrite) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(fileName, true));
			out.write(toWrite);
			out.newLine();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
