package comm;

public class Demo {
	public static void main(String[] args){
		
    	SerialPortTest example = new SerialPortTest();
     	
		example.StartWorking("S\n");
		System.out.println("第一次采集结束");
		MATLAB.CalculateImpedance();
		
		try {
			Thread.sleep(20000);  //休眠20s
		}catch (Exception e) {
			System.out.println("延时出错");
		}
		
		example.StartWorking("D\n");
		System.out.println("第二次采集结束");
		MATLAB.CalculateImpedance();
		
	}
	
}
