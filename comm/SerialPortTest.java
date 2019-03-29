package ParameterTest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Enumeration;
import java.util.TooManyListenersException;

import javax.imageio.spi.ServiceRegistry;
import javax.sound.sampled.Port;
import javax.xml.stream.events.StartDocument;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

/**
 * @description: 首先初始化串口设置，然后发送线路类型指令，同时生成相应的txt文件，最后将返回的数据放入相应的文件中
 * @author:zheng
 * @date:2018年12月23日下午3:10:54
 */
public class SerialPortTest extends FileOperate implements SerialPortEventListener {
	//检测系统中可用的通讯端口
	private CommPortIdentifier portID;
	//枚举所有端口
	private Enumeration<CommPortIdentifier> portList;
	//串口
	private SerialPort serialPort;
	//输入输出流
	private InputStream inputStream;
	private OutputStream outputStream;
	//保存串口返回信息
	private String test = "";
	
	protected static String PortType = "COM3";
	
	/**
	 * @description 初始化端口，打开端口3
	 */
	public void init() {
		//获取系统中所有通讯端口
		portList = CommPortIdentifier.getPortIdentifiers();
		//循环通讯端口，找到并打开端口
		while (portList.hasMoreElements()) {
			portID = portList.nextElement();
			//对所有端口进行循环
			if(portID.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				//找到端口并打开
				if((portID.getName()).equals(PortType)) {
					System.out.println("找到"+PortType);
			
					try {
						//打开串口
						serialPort = (SerialPort) portID.open(Object.class.getName(), 100);
					
						//实例化输入流
						inputStream = serialPort.getInputStream();
						//设置串口监听
						serialPort.addEventListener(this);
						//设置串口数据时间有限
						serialPort.notifyOnDataAvailable(true);
						//设置串口参数,依次为 波特率，数据位，停止位和奇偶校验位
						serialPort.setSerialPortParams(115200, 8, 1, 0);
						
					}catch (PortInUseException e) {
						e.printStackTrace();
					} catch (TooManyListenersException e) {	
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (UnsupportedCommOperationException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	/**
	 * @description 实现接口SerialPortEventListener中的方法，读取串口信息
	 * @param arg0 串口事件
	 */
	@Override
	public void serialEvent(SerialPortEvent event) {
		switch (event.getEventType()) {
        case SerialPortEvent.BI: // 通讯中断
        case SerialPortEvent.OE: // 溢位错误
        case SerialPortEvent.FE: // 帧错误
        case SerialPortEvent.PE: // 奇偶校验错误
        case SerialPortEvent.CD: // 载波检测
        case SerialPortEvent.CTS: // 清除发送
        case SerialPortEvent.DSR: // 数据设备准备好
        case SerialPortEvent.RI: // 响铃侦测
        case SerialPortEvent.OUTPUT_BUFFER_EMPTY: // 输出缓冲区已清空
            break;
        case SerialPortEvent.DATA_AVAILABLE: // 有数据到达
            readComm();
            break;
        default:
            break;
        }
	}

	/**
	 * @description 读取串口返回数据，并打印
	 */
	public void readComm() {
		byte[] readBuffer = new byte[10240];
		try{
			//在init中已经将数据读入inputStream中
			inputStream = serialPort.getInputStream();
			int len =0 ;
			while((len = inputStream.read(readBuffer))!=-1) {
				//全部写完，然后结束，所以用break
				test = new String(readBuffer, 0, len);
				
				if(test.equals("F")) {
					if(LineType.equals("S")) {
						WriteFile("1".getBytes());
					}else if(LineType.equals("D")) {
						WriteFile("2".getBytes());
					}else {
						System.out.println("FileOperate.HandleData函数在创建文件时，线路类型赋值错误");
					}
					break;
				}

				WriteFile(test.getBytes());
				//System.out.println("test:"+test);
				break;
			}
			inputStream.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @description 关闭串口
	 */
	public void closeSerialPort() {
		serialPort.close();
	}

	/**
	 * @description 向串口发送数据
	 * @param information 发送的字符串
	 */
	public void sendMsg(String information) {
		//根据传递的信息决定线路类型,并初始化对应的文件路径
		
		LineType = information.trim();
		InitFilePath();
		try {
			//实例化输出流
			outputStream = serialPort.getOutputStream();
		}catch(IOException e) {
			e.printStackTrace();
		}
		try {
			outputStream.write(information.getBytes());
			outputStream.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void StartWorking(String Command) {
		Boolean FinishFlag = true;  //循环跳出标志位 
		//初始化端口设置
		init();
		//发送命令 单回："S\n"或者双回： "D\n"
		sendMsg(Command);
		//进行等待结尾截止符“F”到来，然后停止
		try{
			while(FinishFlag) {
				Thread.sleep(10);  //每10ms重启进程查看
				if(test.equals("F")){
					FinishFlag = false;
				}
			}
		}catch (Exception e) {
			System.out.println("上位机等待截止符F到来时出错");
		}
		
		
		//初始化标志位
		FinishFlag = true;
		test = "";
		//关闭串口
		closeSerialPort();
		System.out.println("已成功采集数据");

	}
 
	/*
	public static void main(String[] args)  {
		
    	SerialPortTest example = new SerialPortTest();
 	
		example.StartWorking("S\n");
		
		try {
			Thread.sleep(10000);  //休眠20s
		}catch (Exception e) {
			System.out.println("延时出错");
		}
		
		example.StartWorking("D\n");
	}
	*/
	
}
