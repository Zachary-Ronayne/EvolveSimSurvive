package evolve.util.options;

public class StringSetting extends Setting<String>{

	public StringSetting(String value, String name, String description){
		super(value, value, name, description);
	}
	
	@Override
	public String validValue(String value){
		if(value == null) return null;
		else return value;
	}
	
}
