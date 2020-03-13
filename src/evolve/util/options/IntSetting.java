package evolve.util.options;

public class IntSetting extends Setting<Integer>{

	public IntSetting(Integer value, String name, String description){
		super(value, value, name, description);
	}
	
	@Override
	public Integer validValue(String value){
		if(value == null) return null;
		
		try{
			return Integer.parseInt(value);
		}catch(NumberFormatException e){
			return null;
		}
	}
	
}
