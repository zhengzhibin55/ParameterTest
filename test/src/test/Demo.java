package test;

import MatlabProgram.Calculation;

public class Demo {
	
	public static void main(String[] args){
	
		try{
			Calculation example = new Calculation();
			example.Algorithm();
		}catch(Exception e){
			System.out.println(e);
		}
	}
}
