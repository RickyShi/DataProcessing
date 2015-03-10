package reader;


public class Test {
	public static String Date = "Thu Oct 09 08:28:46 CDT 2014";

	public static String convertPosition(String position) {
		String rtn = "";
		switch (position) {
		case "Stationary":
			rtn = "10";
		case "MovingSlowly":
			rtn = "40";
		case "MovingFast":
			rtn = "70";
		}
		return rtn;
	}

	private String getStrTime(String date) {
		String time = date.substring(11, 19);
		return time;
	}

	private static int getCalTime(String time) {
		String h = time.substring(0, 2);
		String m = time.substring(3, 5);
		String s = time.substring(6, 8);
		return Integer.parseInt(h) * 3600 + Integer.parseInt(m) * 60 + Integer.parseInt(s);
	}
	public static String getDMYDate(String date) {
		String Mon = date.substring(4, 7);
		String Day = date.substring(8, 10);
		String Year = date.substring(24);
		String time = date.substring(11, 20);
		String h = date.substring(11, 13);
		String m = date.substring(14, 16);
		String s = date.substring(17, 19);

		return Day + "/" + Mon + "/" + Year + " " + h + "," + m + "," + s;
	}

	public static boolean compareTime(String sTime, String eTime) {
		if (getCalTime(eTime) - getCalTime(sTime) >= 5 * 60) {
			return false;
		} else {
			return true;
		}
	}
	public static void main(String[] args) {
		// System.out.print(getDMYDate(Date));
		// System.out.print(getCalTime("08:28:46"));
		// System.out.println(compareTime("08:28:46", "08:30:51"));
		// System.out.println(compareTime("08:28:46", "08:38:51"));

		System.out.println(Double.parseDouble("-1"));

	}
}
