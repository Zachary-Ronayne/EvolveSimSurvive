package evolve.util.options;

public class DoubleSetting extends Setting<Double>{

	public DoubleSetting(Double value, String name, String description){
		super(value, value, name, description);
	}
	
	@Override
	public Double validValue(String value){
		if(value == null) return null;
		
		try{
			return Double.parseDouble(value);
		}catch(NumberFormatException e){
			return null;
		}
	}
	
}
