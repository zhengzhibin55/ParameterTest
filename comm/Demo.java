package comm;

public class Demo {
	public static void main(String[] args){
		
    	SerialPortTest example = new SerialPortTest();
     	
		example.StartWorking("S\n");
		System.out.println("��һ�βɼ�����");
		MATLAB.CalculateImpedance();
		
		try {
			Thread.sleep(20000);  //����20s
		}catch (Exception e) {
			System.out.println("��ʱ����");
		}
		
		example.StartWorking("D\n");
		System.out.println("�ڶ��βɼ�����");
		MATLAB.CalculateImpedance();
		
	}
	
}