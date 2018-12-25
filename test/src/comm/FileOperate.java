package comm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;

/**
 * @description: txt�ļ������࣬����data.txt�ļ��������ݱ���
 * @author:zheng
 * @date:2018��12��20������9:41:18
 */
public class FileOperate {
	//Ĭ�ϵ��ļ���·��
	public final String DEFAULTFilePath = "C:/ParameterTest";
	
	//�������ݵ�txt�ļ���
	private final String FILE_NAME = "data.txt";
	//��Matlab���õ�txt�ļ�
	private final String TEP_NAME = "tep.txt";
	
	//���ò������ͱ��� Ĭ���ǵ���
	public String LineType = "S";
	
	/**
	 * @description ��java����ָ��ʱ���²�������λ�����ڴ�����ͬ��txt�ļ�
	 * @param LineType
	 */
	public void SetLineType(String LineType) {
		this.LineType = LineType;
	}
	
	/**
	 * @description �����ļ�������ʼ���ļ�
	 * @param FileName �ļ�·��+�ļ���
	 */
	public void InitFile(String FileName) {
		File file = new File(FileName);
		
		if (file.exists()) {
			System.out.println(FileName+"�ļ����ڣ�����ʼ��");
			file.delete();
		}
		try {
			file.createNewFile();
		}catch (Exception e) {
			System.out.println(FileName+"�ļ���������");
		}
	}
	
	/**
	 * @description ��ָ���ļ�·���´��������ļ� data.txt
	 */
	public void InitFilePath() {
		//�����ж��ļ����Ƿ���ڣ��������򴴽��ļ��У�Ȼ���ж��ļ��Ƿ���ڣ�������ɾ������󴴽��ļ�
				File filepath = new File(DEFAULTFilePath);
				if(!filepath.exists()) {
					//System.out.println("�������ļ���");
					filepath.mkdirs();
				}
				//�����ļ� 
				InitFile(DEFAULTFilePath+"/"+FILE_NAME);
			
	}
	
	
	public void WriteByte(String FilePath,byte[] Data) {
		File file = new File(FilePath);
		//���ļ�����д�������������ֽ�������˲���Fileoutputstream
		try {
			FileOutputStream out = new FileOutputStream(file,true);
			out.write(Data);
			out.close();
		}catch (Exception e) {
			System.out.println("txt�ļ�д����");
		}
	}
	
	/**
	 * @description ��Ĭ��·���¸�����·���ͽ���д����
	 * @param LineType ��·���� 'D'��˫�أ� 'S'�ǵ���
	 * @param Data �첽����ͨ������
	 */
	public void WriteFile(byte[] Data) {
		File file = new File(DEFAULTFilePath);
		//����д����  
		WriteByte(DEFAULTFilePath+"/"+FILE_NAME,Data);
	}
	
	
	/**
	 * @description ��data.txt�ļ����д��������µĿɹ�matlab���õ��ļ�tep.txt�ļ�
	 * 				����������ȷ���ļ��Ƿ���ڣ�������ɾ��Ȼ�����´�����Ȼ�������·���ͺ�
	 * 				��������ȷ��������ȷ��Ȼ�������µ�tep�����ļ������ļ���βд����·����
	 * 				��λΪ1��˫��Ϊ2
	 */             
	public void HandleData() {
		//��ʼ��tep�ļ�
		InitFile(DEFAULTFilePath+"/"+TEP_NAME);
		//ȷ��������ȷ�����tepд����
		try {
			FileReader fr = new FileReader(DEFAULTFilePath+"/"+FILE_NAME);
			//BufferedReader������ڲ�������ƣ���������Ϊ��λ��������
			BufferedReader br = new BufferedReader(fr);
			String s = "";
			//�жϽ�β��Fʱ��������ת������
			while(!(s=br.readLine()).equals("F")) {
				//System.out.println(s);
				WriteByte(DEFAULTFilePath+"/"+TEP_NAME, (s+"\n").getBytes());
			}
			//������·���Ͳ�ͬ���Խ�β���в�ͬ�ĸ�ֵ,���ؽ�β��ֵ1��˫�ؽ�β��ֵ2
			System.out.println("��·����Ϊ��"+LineType);
			
			if(LineType.equals("S")) {
				WriteByte(DEFAULTFilePath+"/"+TEP_NAME, ("1"+"\n").getBytes());
			}else if(LineType.equals("D")) {
				WriteByte(DEFAULTFilePath+"/"+TEP_NAME, ("2"+"\n").getBytes());
			}else {
				System.out.println("FileOperate.HandleData�����ڴ����ļ�ʱ����·���͸�ֵ����");
			}
			
			//�ر���
			br.close();
			fr.close();
		}catch (Exception e) {
			System.out.println("FileOperate.HandleData������ȡdata�ļ�������tep�ļ����̳���");
		}
		
		
		
		
	}
	
	
	
	
	
	/**
	 * @description ��FileOperate�ĺ������в���
	 * @param args
	 */
	/*
	public static void main(String[] args) {
		FileOperate example = new FileOperate();
		example.SetLineType("D");
		example.InitFilePath();
		byte[] test = "D\nabcd\n13456\n12\n".getBytes();
		example.WriteFile(test);
		
	}
	*/
	
	
}
