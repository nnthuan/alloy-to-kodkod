package GenerateRule;

import java.util.ArrayList;
import java.util.List;

import edu.mit.csail.sdg.alloy4.Err;
import edu.mit.csail.sdg.alloy4.SafeList;
import edu.mit.csail.sdg.alloy4compiler.ast.Module;
import edu.mit.csail.sdg.alloy4compiler.ast.Sig;
import edu.mit.csail.sdg.alloy4compiler.parser.CompUtil;
/**
 *control translate fact alloy into fact kodkod 
 **/
public class TransFactAlloyToKodKod {
	public boolean error = false;
	public void execute(String inputPath, String outputPath) {
		
		if(inputPath== null || outputPath == null){
			error=true;
			return;
		}
		// get list sig
		Module module;
		SafeList<Sig> sigs = new SafeList<Sig>();
		try {
			module = CompUtil.parseEverything_fromFile(null, null, inputPath);
			sigs = module.getAllSigs();
		} catch (Err e) {
			// TODO Auto-generated catch block
			error = true;
			e.printStackTrace();
			return;
		}
		// 
		try {
			GetFactAlloy getFact = new GetFactAlloy(inputPath);
			getFact.Reading();
			
			List<String> factAlloys = new ArrayList<String>();
			factAlloys = getFact.getFacts();
			TransToArray transToArr = new TransToArray();
			if (factAlloys != null) {
				for (String item : factAlloys) {
					TransToKodKod trans = new TransToKodKod();
					trans.execute(transToArr.execute(item), sigs, outputPath);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			error = true;
			e.printStackTrace();
			return;
		}
	}
}
