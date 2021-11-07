import java.io.IOException;

public class App {

	public static void main(String[] args) {
		
		LATimesParser lat = new LATimesParser();
				
		try {
			lat.InitializeParsing();
			System.out.println("The documents were parsed for the Los Angeles Times. Completed...");	
		} catch (IOException e) {
			e.printStackTrace();
		}
			
	}
}
