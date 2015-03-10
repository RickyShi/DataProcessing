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
		String _rPsyFileName = "1004 pre-cleaned 2-7-15.csv";
		String _rWebFileName = "chestsensor.9998.EQ02_3112228.M_04.txt";
		PreProcessor prePro = new PreProcessor();
		// prePro.preProcessWebData(_rWebFileName);
		prePro.preProcessPSYData(_rPsyFileName);

	}
}
