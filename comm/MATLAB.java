package ParameterTest;


import MatlabProgram.Calculation;

public class MATLAB {
	
	/** 
	 * 调用Matlab解决线路阻抗
	 */
	public static void CalculateImpedance(){
		try{
			Calculation example = new Calculation();
			example.Algorithm();
		}catch(Exception e){
			System.out.println("调用matlab求解阻抗出错");
		}
	}
	
	public static void main(String[] args){
		MATLAB.CalculateImpedance();
	}
	
}
