package comm;

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
 * @description: ���ȳ�ʼ���������ã�Ȼ������·����ָ�ͬʱ������Ӧ��txt�ļ�����󽫷��ص����ݷ�����Ӧ���ļ���
 * @author:zheng
 * @date:2018��12��23������3:10:54
 */
public class SerialPortTest extends FileOperate implements SerialPortEventListener {
	//���ϵͳ�п��õ�ͨѶ�˿�
	private CommPortIdentifier portID;
	//ö�����ж˿�
	private Enumeration<CommPortIdentifier> portList;
	//����
	private SerialPort serialPort;
	//���������
	private InputStream inputStream;
	private OutputStream outputStream;
	//���洮�ڷ�����Ϣ
	private String test = "";
	
	protected static String PortType = "COM3";
	
	/**
	 * @description ��ʼ���˿ڣ��򿪶˿�2
	 */
	public void init() {
		//��ȡϵͳ������ͨѶ�˿�
		portList = CommPortIdentifier.getPortIdentifiers();
		//ѭ��ͨѶ�˿ڣ��ҵ����򿪶˿�
		while (portList.hasMoreElements()) {
			portID = portList.nextElement();
			//�����ж˿ڽ���ѭ��
			if(portID.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				//�ҵ��˿ڲ���
				if((portID.getName()).equals(PortType)) {
					System.out.println("�ҵ�"+PortType);
			
					try {
						//�򿪴���
						serialPort = (SerialPort) portID.open(Object.class.getName(), 100);
					
						//ʵ����������
						inputStream = serialPort.getInputStream();
						//���ô��ڼ���
						serialPort.addEventListener(this);
						//���ô�������ʱ������
						serialPort.notifyOnDataAvailable(true);
						//���ô��ڲ���,����Ϊ �����ʣ�����λ��ֹͣλ����żУ��λ
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
	 * @description ʵ�ֽӿ�SerialPortEventListener�еķ�������ȡ������Ϣ
	 * @param arg0 �����¼�
	 */
	@Override
	public void serialEvent(SerialPortEvent event) {
		switch (event.getEventType()) {
        case SerialPortEvent.BI: // ͨѶ�ж�
        case SerialPortEvent.OE: // ��λ����
        case SerialPortEvent.FE: // ֡����
        case SerialPortEvent.PE: // ��żУ�����
        case SerialPortEvent.CD: // �ز����
        case SerialPortEvent.CTS: // �������
        case SerialPortEvent.DSR: // �����豸׼����
        case SerialPortEvent.RI: // �������
        case SerialPortEvent.OUTPUT_BUFFER_EMPTY: // ��������������
            break;
        case SerialPortEvent.DATA_AVAILABLE: // �����ݵ���
            readComm();
            break;
        default:
            break;
        }
	}

	/**
	 * @description ��ȡ���ڷ������ݣ�����ӡ
	 */
	public void readComm() {
		byte[] readBuffer = new byte[10240];
		try{
			//��init���Ѿ������ݶ���inputStream��
			inputStream = serialPort.getInputStream();
			int len =0 ;
			while((len = inputStream.read(readBuffer))!=-1) {
				//ȫ��д�꣬Ȼ�������������break
				test = new String(readBuffer, 0, len);
				
				if(test.equals("F")) {
					if(LineType.equals("S")) {
						WriteFile("1".getBytes());
					}else if(LineType.equals("D")) {
						WriteFile("2".getBytes());
					}else {
						System.out.println("FileOperate.HandleData�����ڴ����ļ�ʱ����·���͸�ֵ����");
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
	 * @description �رմ���
	 */
	public void closeSerialPort() {
		serialPort.close();
	}

	/**
	 * @description �򴮿ڷ�������
	 * @param information ���͵��ַ���
	 */
	public void sendMsg(String information) {
		//���ݴ��ݵ���Ϣ������·����,����ʼ����Ӧ���ļ�·��
		
		LineType = information.trim();
		InitFilePath();
		try {
			//ʵ���������
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
		Boolean FinishFlag = true;  //ѭ��������־λ 
		//��ʼ���˿�����
		init();
		//�������� ���أ�"S\n"����˫�أ� "D\n"
		sendMsg(Command);
		//���еȴ���β��ֹ����F��������Ȼ��ֹͣ
		try{
			while(FinishFlag) {
				Thread.sleep(10);  //ÿ10ms�������̲鿴
				if(test.equals("F")){
					FinishFlag = false;
				}
			}
		}catch (Exception e) {
			System.out.println("��λ���ȴ���ֹ��F����ʱ����");
		}
		
		
		//��ʼ����־λ
		FinishFlag = true;
		test = "";
		//�رմ���
		closeSerialPort();
		System.out.println("�ѳɹ��ɼ�����");

	}

	/*
	public static void main(String[] args)  {
		
    	SerialPortTest example = new SerialPortTest();
 	
		example.StartWorking("S\n");
		
		try {
			Thread.sleep(10000);  //����20s
		}catch (Exception e) {
			System.out.println("��ʱ����");
		}
		
		example.StartWorking("D\n");
	}
	*/
	
}
