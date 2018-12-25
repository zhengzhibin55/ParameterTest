package comm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;

/**
 * @description: txt文件操作类，创建data.txt文件进行数据保存
 * @author:zheng
 * @date:2018年12月20日上午9:41:18
 */
public class FileOperate {
	//默认的文件夹路径
	public final String DEFAULTFilePath = "C:/ParameterTest";
	
	//保存数据的txt文件名
	private final String FILE_NAME = "data.txt";
	//供Matlab调用的txt文件
	private final String TEP_NAME = "tep.txt";
	
	//设置参数类型变量 默认是单回
	public String LineType = "S";
	
	/**
	 * @description 在java发送指令时更新参数类型位，用于创建不同的txt文件
	 * @param LineType
	 */
	public void SetLineType(String LineType) {
		this.LineType = LineType;
	}
	
	/**
	 * @description 根据文件名，初始化文件
	 * @param FileName 文件路径+文件名
	 */
	public void InitFile(String FileName) {
		File file = new File(FileName);
		
		if (file.exists()) {
			System.out.println(FileName+"文件存在，并初始化");
			file.delete();
		}
		try {
			file.createNewFile();
		}catch (Exception e) {
			System.out.println(FileName+"文件创建出错");
		}
	}
	
	/**
	 * @description 在指定文件路径下创建数据文件 data.txt
	 */
	public void InitFilePath() {
		//首先判断文件夹是否存在，不存在则创建文件夹，然后判断文件是否存在，存在则删除。最后创建文件
				File filepath = new File(DEFAULTFilePath);
				if(!filepath.exists()) {
					//System.out.println("创建新文件夹");
					filepath.mkdirs();
				}
				//创建文件 
				InitFile(DEFAULTFilePath+"/"+FILE_NAME);
			
	}
	
	
	public void WriteByte(String FilePath,byte[] Data) {
		File file = new File(FilePath);
		//对文件进行写操作，由于是字节流，因此采用Fileoutputstream
		try {
			FileOutputStream out = new FileOutputStream(file,true);
			out.write(Data);
			out.close();
		}catch (Exception e) {
			System.out.println("txt文件写出错");
		}
	}
	
	/**
	 * @description 在默认路径下根据线路类型进行写操作
	 * @param LineType 线路类型 'D'是双回； 'S'是单回
	 * @param Data 异步串口通信数据
	 */
	public void WriteFile(byte[] Data) {
		File file = new File(DEFAULTFilePath);
		//进行写操作  
		WriteByte(DEFAULTFilePath+"/"+FILE_NAME,Data);
	}
	
	
	/**
	 * @description 将data.txt文件进行处理，生成新的可供matlab调用的文件tep.txt文件
	 * 				操作包括：确认文件是否存在，存在则删除然后重新创建，然后根据线路类型和
	 * 				数据数量确认数据正确，然后生成新的tep数据文件，在文件结尾写上线路类型
	 * 				单位为1，双回为2
	 */             
	public void HandleData() {
		//初始化tep文件
		InitFile(DEFAULTFilePath+"/"+TEP_NAME);
		//确认数据正确后进行tep写操作
		try {
			FileReader fr = new FileReader(DEFAULTFilePath+"/"+FILE_NAME);
			//BufferedReader类具有内部缓存机制，可以以行为单位进行输入
			BufferedReader br = new BufferedReader(fr);
			String s = "";
			//判断结尾是F时结束整个转换过程
			while(!(s=br.readLine()).equals("F")) {
				//System.out.println(s);
				WriteByte(DEFAULTFilePath+"/"+TEP_NAME, (s+"\n").getBytes());
			}
			//根据线路类型不同，对结尾进行不同的赋值,单回结尾赋值1，双回结尾赋值2
			System.out.println("线路类型为："+LineType);
			
			if(LineType.equals("S")) {
				WriteByte(DEFAULTFilePath+"/"+TEP_NAME, ("1"+"\n").getBytes());
			}else if(LineType.equals("D")) {
				WriteByte(DEFAULTFilePath+"/"+TEP_NAME, ("2"+"\n").getBytes());
			}else {
				System.out.println("FileOperate.HandleData函数在创建文件时，线路类型赋值错误");
			}
			
			//关闭流
			br.close();
			fr.close();
		}catch (Exception e) {
			System.out.println("FileOperate.HandleData函数读取data文件，创建tep文件过程出错");
		}
		
		
		
		
	}
	
	
	
	
	
	/**
	 * @description 对FileOperate的函数进行测试
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
