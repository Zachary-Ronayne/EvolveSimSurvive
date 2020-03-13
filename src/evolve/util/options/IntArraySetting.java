package evolve.util.options;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class IntArraySetting extends Setting<Integer[]>{

	public IntArraySetting(Integer[] value, String name, String description){
		super(value, value, name, description);
	}

	@Override
	public Integer[] validValue(String value){
		//value is null
		if(value == null) return null;

		//make a list of all the integers in the string
		ArrayList<Integer> values = new ArrayList<Integer>();
		
		//scan through the input string
		Scanner scan = new Scanner(value);
		while(scan.hasNext()){
			//if the value was able to be accepted, add it to the list
			try{
				values.add(scan.nextInt());
				
			}catch(InputMismatchException e){
				//otherwise, return null
				scan.close();
				return null;
			}
		}
		scan.close();
		
		Integer[] arr = new Integer[values.size()];
		for(int i = 0; i < arr.length; i++) arr[i] = values.get(i);
		return arr;
	}
	
	@Override
	public String toString(){
		String str = "";
		for(Integer i : value()){
			str += i + " ";
		}
		return str;
	}
	
	@Override
	public String getDefaultValueString(){
		String str = "";
		for(Integer i : getDefaultValue()){
			str += i + " ";
		}
		return str;
	}
	
}
