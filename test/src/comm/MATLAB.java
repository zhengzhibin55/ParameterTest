package comm;

import MatlabProgram.Calculation;

public class MATLAB {
	
	/**
	 * ����Matlab�����·�迹
	 */
	public static void CalculateImpedance(){
		try{
			Calculation example = new Calculation();
			example.Algorithm();
		}catch(Exception e){
			System.out.println("����matlab����迹����");
		}
	}
	
}
