package evolve.util.options;

public class BooleanSetting extends Setting<Boolean>{

	public BooleanSetting(Boolean value, String name, String description){
		super(value, value, name, description);
	}

	@Override
	public boolean setStringValue(String value){
		//if the value is null, return false, no change was made
		if(value != null){
			
			//if the string "true" or "false" is not found, then don't change the setting and return false
			//otherwise return true
			
			boolean t = value.equals("true");
			boolean f = value.equals("false");
			
			if(!t && !f) return false;
			else{
				setValue(t);
				return true;
			}
		}
		return false;
	}
	
	@Override
	public Boolean validValue(String value){
		//if the value is null, return false, no change was made
		if(value != null){
			
			//if the string "true" or "false" is not found, return null
			//otherwise return the value
			
			boolean t = value.equals("true");
			boolean f = value.equals("false");
			
			if(!t && !f) return null;
			else return t;
		}
		return null;
	}
	
}
