package GenerateRule;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
*Get content fact in file .als at path 
*@input path file .als
*@return 
*path, List<String> facts
**/
public class GetFactAlloy {
	private String path = null;
	private List<String> facts = new ArrayList<String>();

	public List<String> getFacts() {
		return facts;
	}

	public void setFacts(List<String> facts) {
		this.facts = facts;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public GetFactAlloy() {
		path = null;
		facts = null;
	}

	public GetFactAlloy(String pathFile) {
		path = pathFile;
		Reading();
	}
	/**
	 *Read file .als and get content fact
	 **/
	public void Reading() {
		// This will reference one line at a time
		String line = null;

		try {
			// FileReader reads text files in the default encoding.
			FileReader fileReader = new FileReader(path);

			// Always wrap FileReader in BufferedReader.
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			String result = "";
			Boolean flag = false;

			while ((line = bufferedReader.readLine()) != null) {

				if (line.trim().startsWith("fact")) {
					flag = !flag;
				}

				if (flag) {
					if (line.contains("//"))
						continue;
					result += line;
				}

				if (result.contains("}")) {
					if (line.contains("//"))
						continue;
					facts.add((result.substring(0, result.indexOf('}') + 1)).toString());
					result = "";
					flag = !flag;
				}
			}

			// Always close files.
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + path + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + path + "'");
		}
	}
}
