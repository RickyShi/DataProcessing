package reader;

import java.io.IOException;

public class PreProcessor {

	public void preProcessWebData(String rFileName) {
		WebChestSensor _chestSensor = new WebChestSensor(rFileName);
		_chestSensor.simpleProcessChestData(rFileName);
		_chestSensor.splitFilesByTime(1);
	}

	public void preProcessPSYData(String rFileName) {
		DeviceChestSenor _chestSensor = new DeviceChestSenor(rFileName);
		_chestSensor.simpleProcessChestData(rFileName);
		_chestSensor.splitFilesByTime(1);
	}


	public static void main(String[] args) throws IOException {
		// String _rPsyFileName = "1004 pre-cleaned 11-10-14.csv";
		String _rWebFileName = "chestsensor.9998.EQ02_3112228.M_18.txt";
		PreProcessor prePro = new PreProcessor();
		 prePro.preProcessWebData(_rWebFileName);
		// for (int i = 0; i <= 8; i++) {
		// System.out.println("1005 pre-cleaned 12-1" + i + "-14.csv");
		// prePro.preProcessPSYData("1005 pre-cleaned 12-1" + i + "-14.csv");
		// }
	}
}
