package reader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class DeviceChestSenor {
	public static final String ROOT_PATH = "C:/Users/Ricky/Desktop/Example/Craving Study Psychophys Data/";
	public static final String SUBJECT_PATH = "1004 Psychophys Data/";
	public static final String PATH = ROOT_PATH + SUBJECT_PATH;
	public static final String OUT = "Out/";
	// public static final String SUFFIX = ".txt";
	public static final String SUFFIX = ".csv";
	public static final String HEAD = "time,motion,body position,BR derived by Belt,HR derived by ECG,Belt Quality,ECG Quality,HR confidence,BR confidence,Skin Temperature,Day,Activity";
	public final String LINEBREAK = System.getProperty("line.separator");

	private String wFileName;

	public DeviceChestSenor() {
		wFileName = PATH + OUT + "Process" + SUFFIX;
	}

	public DeviceChestSenor(String fileName) {
		wFileName = PATH + OUT + fileName + SUFFIX;
	}

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
			data = br.readLine();
			while ((data = br.readLine()) != null) {
				String[] tmp = data.trim().split(",");
				System.out.println(data);
				String time = tmp[10];
				if (!time.equals("")) {
					time = time.split("\\.")[0];
					time = (time.length() == 7) ? "0".concat(time) : time;
					String date = (tmp[14].equals("")) ? "MISSING_DATE" : tmp[14];
					String skin = (tmp[1].equals("")) ? "-1" : tmp[1];
					String bodyPosition = convertPosition(tmp[2]);
					String ambulation = convertMotion(tmp[3]);
					String HRConfidence = (tmp[6].equals("")) ? "-1" : tmp[6];
					String BRConfidence = (tmp[8].equals("")) ? "-1" : tmp[8];
					// System.out.println("BR Con: " + BRConfidence);

					String HR = (tmp[15].equals("")) ? "-1" : tmp[15];
					String BR = (tmp[16].equals("")) ? "-1" : tmp[16];
					// System.out.println("BR: " + BR);
					if (Double.parseDouble(BRConfidence) <= 0.4 || Double.parseDouble(BR) == 0.0 || Double.parseDouble(BR) == -1.0) {
						BR = "N/A";
					}
					if (Double.parseDouble(HRConfidence) <= 0.4 || Double.parseDouble(HR) >= 180 || Double.parseDouble(HR) <= 40) {
						HR = "N/A";
					}
					if (Double.parseDouble(skin) <= 20) {
						skin = "N/A";
					}
					String activity = (tmp[17].equals("")) ? "N/A" : tmp[17];
					;
					toWrite = time.concat(",").concat(ambulation).concat(",").concat(bodyPosition).concat(",")
							.concat(BR).concat(",").concat(HR).concat(",").concat("-1").concat(",")
							.concat("-1").concat(",").concat(HRConfidence).concat(",").concat(BRConfidence).concat(",")
							.concat(skin).concat(",").concat(date).concat(",").concat(activity);
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
		default:
			rtn = "N/A";
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
		default:
			rtn = "N/A";
			break;
		}
		return rtn;
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
							writeMethod(fName, toWrite);
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

	public static void main(String[] args) throws IOException {
		String _rFileName = "1004 pre-cleaned 2-8-15.csv";
		DeviceChestSenor _chestSensor = new DeviceChestSenor(_rFileName);
		// _chestSensor.getChestData(_rFileName);
		_chestSensor.simpleProcessChestData(_rFileName);
		_chestSensor.splitFilesByTime(1);
	}
}
