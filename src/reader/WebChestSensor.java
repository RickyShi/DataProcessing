package reader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class WebChestSensor {

	public static final String PATH = "C:/Users/Ricky/Desktop/Example/";
	public static final String OUT = "OutWeb/";
	public static final String SUFFIX = ".csv";
	public static final String HEAD = "time,motion,body position,BR derived by Belt,HR derived by ECG,Belt Quality,ECG Quality,HR confidence,BR confidence,Skin Temperature,Day,DF";
	public final String LINEBREAK = System.getProperty("line.separator");
	private String wFileName;

	public WebChestSensor() {
		wFileName = PATH + OUT + "Process" + SUFFIX;
	}

	public WebChestSensor(String fileName) {
		wFileName = PATH + OUT + fileName + SUFFIX;
	}

	public void splitFilesByTime(int gapMinute) {
		BufferedReader br;
		String data;
		StringBuffer sb = new StringBuffer();
		String sTime = "";
		String pTime = "";
		String eTime = "";
		String day = "";
		int count = -1;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(wFileName)));
			while ((data = br.readLine()) != null) {
				String[] tmp = data.trim().split(",");
				if (!tmp[0].equals("time")) {
					count++;
					if (count == 0) {
						sTime = tmp[0];
						pTime = tmp[0];
						day = tmp[10];
						sb.append(data).append(LINEBREAK);
						eTime = pTime;
					} else {
						pTime = eTime;
						eTime = tmp[0];
						System.out.println("timeP_" + count + ": " + pTime);
						System.out.println("timeE_" + count + ": " + eTime);
						if (!compareTime(pTime, eTime, gapMinute)) {
							String fName = PATH + OUT + day + sTime.substring(0, 2) + "-" + eTime.substring(0, 2) + "_" + SUFFIX;
							String toWrite = sb.toString();
							writeMethod(fName,toWrite);
							System.out.println("count: " + count);
							pTime = "";
							eTime = "";
							sTime = "";
							count = -1;
							sb.setLength(0);
						}
						sb.append(data).append(LINEBREAK);
					}
				}
			}
			if (!sb.toString().equals("")) {
				String fName = PATH + OUT + day + sTime.substring(0, 2) + "-" + eTime.substring(0, 2) + "_" + SUFFIX;
				String toWrite = sb.toString();
				writeMethod(fName, toWrite);
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 *
	 * @param rFileName
	 * @param wFileName
	 *            file:
	 *            0: time; 1: motion; 2: position; 3: BR; 4: HR; 5: Belt
	 *            Quality; 6: ECG Quality; 7: HR Confidence; 8: BR Confidence,
	 *            9: Skin Temperature(added on 2/26) 10: Day
	 * @return
	 */
	public boolean simpleProcessChestData(String rFileName) {
		BufferedReader br;
		String data;
		String toWrite;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(PATH + rFileName)));
			File myPath = new File(PATH + OUT);
			if (!myPath.exists()) {
				myPath.mkdir();
				System.out.println("create new path: " + PATH + OUT);
			}
			BufferedWriter out = new BufferedWriter(new FileWriter(wFileName));
			out.write(HEAD);
			out.newLine();
			while ((data = br.readLine()) != null) {
				String[] tmp = data.trim().split(",");
				if (!tmp[0].equals("time")) {
					// check BR/HR Confidence &HR value
					if (Double.parseDouble(tmp[8]) <= 0.4 || Double.parseDouble(tmp[3]) == 0.0) {
						tmp[3] = "nan";
					}
					if (Double.parseDouble(tmp[7]) <= 0.4 || Double.parseDouble(tmp[4]) >= 180 || Double.parseDouble(tmp[4]) <= 40) {
						tmp[4] = "nan";
					}
					if (Double.parseDouble(tmp[9]) <= 20) {
						tmp[9] = "nan";
					}
					tmp[1] = convertMotion(tmp[1]);
					tmp[2] = convertPosition(tmp[2]);
					toWrite = getStrTime(tmp[0]).concat(",").concat(tmp[1]).concat(",").concat(tmp[2]).concat(",")
							.concat(tmp[3]).concat(",").concat(tmp[4]).concat(",").concat(tmp[5]).concat(",")
							.concat(tmp[6]).concat(",").concat(tmp[7]).concat(",").concat(tmp[8]).concat(",")
							.concat(tmp[9]).concat(",").concat(getDMYDate(tmp[0]));
					out.write(toWrite);
					out.newLine();
				}
			}
			br.close();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private void addSurveyScore(String fileName) {
		// TODO: add SurveyScore

	}

	private void addDrinkTime(String fileName) {
		// TODO: add DrinkTime
	}
	private void writeMethod(String fileName, String toWrite) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
			out.write(HEAD);
			out.newLine();
			out.write(toWrite);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String getDMYDate(String date) {
		String mon = date.substring(4, 7);
		String day = date.substring(8, 10);
		String year = date.substring(24);
		return day + "_" + mon + "_" + year;
	}

	private String getStrTime(String date) {
		String time = date.substring(11, 19);
		return time;
	}

	private int getCalTime(String time) {
		String h = time.substring(0, 2);
		String m = time.substring(3, 5);
		String s = time.substring(6, 8);
		return Integer.parseInt(h) * 3600 + Integer.parseInt(m) * 60 + Integer.parseInt(s);
	}

	/**
	 *
	 * @param sTime
	 * @param eTime
	 * @param gapMinute
	 *            : split files based on this time
	 * @return
	 */
	private boolean compareTime(String sTime, String eTime, int gapMinute) {
		if (Math.abs(getCalTime(eTime) - getCalTime(sTime)) >= gapMinute * 60) {
			return false;
		} else {
			return true;
		}
	}

	private String convertMotion(String position) {
		String rtn = "";
		switch (position) {
		case "Stationary":
			rtn = "1";
			break;
		case "MovingSlowly":
			rtn = "2";
			break;
		case "MovingFast":
			rtn = "3";
			break;
		}
		return rtn;
	}

	private String convertPosition(String motion) {
		String rtn = "";
		switch (motion) {
		case "Inverted":
			rtn = "10";
			break;
		case "Prone":
			rtn = "20";
			break;
		case "Side":
			rtn = "30";
			break;
		case "Supine":
			rtn = "40";
			break;
		case "Unknown":
			rtn = "50";
			break;
		case "Upright":
			rtn = "60";
			break;
		}
		return rtn;
	}

	/**
	 * Below Code Not Used for Now
	 * private LinkedList<LinkedList<String>> getChestData(String rFileName)
	 * throws IOException {
	 * BufferedReader br = new BufferedReader(new InputStreamReader(new
	 * FileInputStream(rFileName)));
	 * String data = null;
	 * LinkedList<String> arrRow = null;
	 * LinkedList<LinkedList<String>> processedData = new
	 * LinkedList<LinkedList<String>>();
	 * while ((data = br.readLine()) != null) {
	 * String[] tmp = data.trim().split(",");
	 * if (!tmp[0].equals("time")) {
	 * arrRow = new LinkedList<String>(Arrays.asList(tmp));
	 * processedData.add(arrRow);
	 * Iterator<String> iter = arrRow.iterator();
	 * while (iter.hasNext()) {
	 * System.out.print(iter.next());
	 * System.out.println("");
	 * }
	 * }
	 * }
	 * br.close();
	 * return processedData;
	 * }
	 **/
	public static void main(String[] args) throws IOException {
		String _rFileName = "chestsensor.9998.EQ02_3112228.M_02.txt";
		WebChestSensor _chestSensor = new WebChestSensor(_rFileName);
//		_chestSensor.getChestData(_rFileName);
		_chestSensor.simpleProcessChestData(_rFileName);
		_chestSensor.splitFilesByTime(1);
	}
}
