package GenerateRule;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Translate fact alloy into fact kodkod and create file class rule4fact
 **/
public class TransToArray {

	public TransToArray() {
	}

	@SuppressWarnings("static-access")
	public List<String> execute(String strFact) {
		List<String> factArr = new ArrayList<String>();
		if (strFact.isEmpty()) {
			factArr = null;
		} else {
			int index = 0;
			int indexEnd = strFact.length();
			String pattern = "[\\w']*";
			Pattern regex = null;
			String temp = "";
			while (index < indexEnd) {
				if (regex.matches(pattern, Character.toString(strFact.charAt(index)))) {
					temp += Character.toString(strFact.charAt(index));
					if (index < indexEnd)
						index++;
					if(index == indexEnd)
					{
						if (!temp.isEmpty()) {
							factArr.add(temp);
							temp = "";
						}
						break;
					}
				} else {
					if (!temp.isEmpty()) {
						factArr.add(temp);
						temp = "";
					}
					if (Character.toString(strFact.charAt(index)).equals(" ")
							|| Character.toString(strFact.charAt(index)).equals("\t")
							|| Character.toString(strFact.charAt(index)).equals("\n")) {
						if (index < indexEnd)
							index++;
						while (Character.toString(strFact.charAt(index)).equals(" ")
								|| Character.toString(strFact.charAt(index)).equals("\t")
								|| Character.toString(strFact.charAt(index)).equals("\n")) {
							if (index < indexEnd)
								index++;
						}
					} else {
						temp += Character.toString(strFact.charAt(index));
						if (index < indexEnd)
							index++;
						// && or ||
						if (temp.equals("&") || temp.equals("|")) {
							if (Character.toString(strFact.charAt(index)).equals(temp)) {
								temp += temp;
								if (index < indexEnd)
									index++;
							}
						}
						// >=
						else if (temp.equals(">")) {
							if (Character.toString(strFact.charAt(index)).equals("=")) {
								temp += "=";
								if (index < indexEnd)
									index++;
							}
							// >>
							else if (Character.toString(strFact.charAt(index)).equals(">")) {
								temp += ">";
								if (index < indexEnd)
									index++;
								// >>>
								if (Character.toString(strFact.charAt(index)).equals(">")) {
									temp += ">";
									if (index < indexEnd)
										index++;
								}
							}
						}
						// <=
						else if (temp.equals("<")) {
							if (Character.toString(strFact.charAt(index)).equals("=")) {
								temp += "=";
								if (index < indexEnd)
									index++;
								// <=>
								if (temp.equals("<=")) {
									if (Character.toString(strFact.charAt(index)).equals(">")) {
										temp += ">";
										if (index < indexEnd)
											index++;
									}
								}
							}
							// <<
							else if (Character.toString(strFact.charAt(index)).equals("<")) {
								temp += "<";
								if (index < indexEnd)
									index++;
							}
						}
						// !=
						else if (temp.equals("!")) {
							if (Character.toString(strFact.charAt(index)).equals("=")) {
								temp += "=";
								if (index < indexEnd)
									index++;
							}
						}
						// =>
						else if (temp.equals("=")) {
							if (Character.toString(strFact.charAt(index)).equals(">")) {
								temp += ">";
								if (index < indexEnd)
									index++;
							}
						}
						// ++
						else if (temp.equals("+")) {
							if (Character.toString(strFact.charAt(index)).equals("+")) {
								temp += "+";
								if (index < indexEnd)
									index++;
							}
						}
						// ->
						else if (temp.equals("-")) {
							if (Character.toString(strFact.charAt(index)).equals(">")) {
								temp += ">";
								if (index < indexEnd)
									index++;
							}
						}

						// ---------
						if (!temp.isEmpty()) {
							factArr.add(temp);
							temp = "";
						}
					}
					//-------else----------
				}
				//-----else-------
			}
			
		}
		//change variable t', t'' into tt, tt
		int factSize = factArr.size();
		for(int i = 0; i< factSize; i++){
			if(Pattern.matches("^(\\w{1,})'$", factArr.get(i))){
			 factArr.set(i, factArr.get(i).replaceAll("'", "next"));
			}
		}
		// convert base fact
		//ConvertBaseFact convert = new ConvertBaseFact();
		//return convert.execute(factArr);
		return factArr;
	}
}
