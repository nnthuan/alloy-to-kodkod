package GenerateRule;

public class VarSig {
	String name;
	String sigName;
	
	
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getSigName() {
		return sigName;
	}


	public void setSigName(String sigName) {
		this.sigName = sigName;
	}


	public VarSig(){
		this.name = null;
		this.sigName = null;
	}


	public VarSig(String name, String sigName) {
		super();
		this.name = name;
		this.sigName = sigName;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VarSig other = (VarSig) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (sigName == null) {
			if (other.sigName != null)
				return false;
		} else if (!sigName.equals(other.sigName))
			return false;
		return true;
	}
}
