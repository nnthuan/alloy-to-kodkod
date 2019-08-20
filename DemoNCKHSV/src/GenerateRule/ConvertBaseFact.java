package GenerateRule;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Converted into a simple fact for reading and translating into fact kodkod
 **/
public class ConvertBaseFact {
	/**
	 * Converted into a simple fact for reading and translating into fact kodkod
	 * 
	 * @parameter List<String> fact
	 * @return List<String> fact
	 **/
	public List<String> execute(List<String> factArr) {
		List<String> newFactArr = new ArrayList<String>();
		@SuppressWarnings("unused")
		String temp = "";
		@SuppressWarnings("unused")
		int indexEnd = -1;
		int indexStart = 0;
		int index = 0;
		String difVar = "";
		int size = factArr.size();
		if (factArr.isEmpty()) {
			return null;
		} else {
			for (int i = 0; i < size; i++) {

				// var sig
				if (factArr.get(index).equals("all") || factArr.get(index).equals("some")
						|| factArr.get(index).equals("one") || factArr.get(index).equals("lone")
						|| factArr.get(index).equals("no")) {
					index = i;
					while (!factArr.get(index).equals(":")) {
						difVar += factArr.get(index) + " ";
						index++;
						if(index > size-1)
							break;
					}
					difVar += factArr.get(index + 1);
					difVar = difVar.trim();
					// all a,b: List ===> all a:List, b:List
					if (Pattern.matches(SyntaxRegulars.getRegMulVarSig(), difVar)) {
						indexStart = i;
						if(factArr.get(indexStart + 1).equals(",")){
							newFactArr.add(factArr.get(indexStart));
							
						}
					}
					else{
						newFactArr.add(factArr.get(i));
					}
				} else {
					newFactArr.add(factArr.get(i));
				}
			}
		}
		@SuppressWarnings("unused")
		String a = "";
		return newFactArr;
	}
}