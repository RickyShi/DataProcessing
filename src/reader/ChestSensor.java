package reader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;

public class ChestSensor {

	public ChestSensor() {
	}

	public ChestSensor(String fileName) {
	}

	private int convertPosition(String position) {
		int rtn = -1;
		switch (position) {
		case "Stationary":
			rtn = 10;
		case "MovingSlowly":
			rtn = 40;
		case "MovingFast":
			rtn = 70;
		}
		return rtn;
	}

	private int convertMotion(String motion) {
		int rtn = -1;
		switch (motion) {
		case "Inverted":
			rtn = 10;
		case "Prone":
			rtn = 20;
		case "Side":
			rtn = 30;
		case "Supine":
			rtn = 40;
		case "Unknown":
			rtn = 50;
		case "Upright":
			rtn = 60;
		}
		return rtn;
	}

	/**
	 *
	 * @param rFileName
	 * @param wFileName
	 *            file: 0: time; 1: motion; 2: position; 3: BR; 4:
	 *
	 * @return
	 */
	private boolean processChestData(String rFileName, String wFileName) {
		BufferedReader br;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(rFileName)));
			BufferedWriter out = new BufferedWriter(new FileWriter(wFileName, true));
			String data = null;
			while ((data = br.readLine()) != null) {
				String[] tmp = data.trim().split(",");
				if (!tmp[0].equals("time")) {
					// TODO: losts of dealing
				}
			}
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private LinkedList<LinkedList<String>> getChestData(String rFileName) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(rFileName)));
		String data = null;
		LinkedList<String> arrRow = null;
		LinkedList<LinkedList<String>> processedData = new LinkedList<LinkedList<String>>();
		while ((data = br.readLine()) != null) {
			String[] tmp = data.trim().split(",");
			if (!tmp[0].equals("time")) {
				arrRow = new LinkedList<String>(Arrays.asList(tmp));
				processedData.add(arrRow);
				Iterator<String> iter = arrRow.iterator();
				while (iter.hasNext()) {
					System.out.print(iter.next());
					System.out.println("");
				}
			}
		}
		br.close();
		return processedData;
	}
	public static void main(String[] args) throws IOException {
		ChestSensor _chestSensor = new ChestSensor();
		_chestSensor.getChestData("C:/Users/Ricky/Desktop/Example/chestsensor.1001.EQ02_0913006.O_09.txt");
	}
}
