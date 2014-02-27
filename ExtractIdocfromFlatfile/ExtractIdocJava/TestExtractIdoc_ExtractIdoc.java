import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class TestExtractIdoc_ExtractIdoc {

	public static String extractIdoc(String tx) {
		String test = " ";
		test = tx.substring(19, 29);

		FileWriter fstream;
		try {
			fstream = new FileWriter("/support/home/wmbadmin/idocOut/idocOut.txt", true);
			BufferedWriter fbw = new BufferedWriter(fstream);
			fbw.write(test);
			fbw.newLine();
			fbw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		return test;

	}

}
