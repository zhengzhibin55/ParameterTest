package comm;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

import org.omg.CORBA.PUBLIC_MEMBER;

 //setBounds(int x, int y, int width, int height)
//X是左上角横坐标,Y是左上角纵坐标，windth是宽，height是高
//组件的坐标是相对于位置

//需要的监听事件
//线路类型的两个按钮，决定线路类型 jr1 jr2
//线路长度文本输入框，决定线路长度 jt1
//线路长度键和线路总长度线，决定从txt文件读来的信息如何进行显示 jb2 jb3
//开始测量键，决定信息发送 jb1

 public class MySwing extends JFrame{
	
	private JFrame frame = new JFrame();
	private JPanel panel = new JPanel(); //面板
	//标签
	private JLabel jl1 = new JLabel("选择端口:"); 
	private JLabel jl2 = new JLabel("线路类型:"); 
	private JLabel jl3 = new JLabel("线路长度(Km):"); 
	private JLabel jl4 = new JLabel("选择端口、线路类型和线路长度，点击确定开始测量"); 
	//下拉列表
	final String[] PORTLIST = {"COM1","COM2","COM3","COM4","COM5"};
	private JComboBox<String> jc = new JComboBox<>(PORTLIST);
	//普通按钮
	private JButton jb1 = new JButton("开始测量");
	private JButton jb2 = new JButton("线路单位长度参数");
	private JButton jb3 = new JButton("线路总长度参数");
	//单选按钮
	private JRadioButton jr1 = new JRadioButton("单回");
	private JRadioButton jr2 = new JRadioButton("双回");
	//单行文本
	private JTextField jt1 = new JTextField("1.0"); //显示线路长度
	//文本域
	private JTextArea jtr = new JTextArea(); //显示参数区域
	//保存result内容 总长度单位参数
	private List<Double> AllLineParameters = new ArrayList<>();
	//保存单位长度线路参数
	private List<Double> UnitLineParameters = new ArrayList<>();
	//实时显示当前状态的字符串
	private String Jl4String;
	
	/**
	 * 设置基本组件及其位置
	 */
	private void AppearanceSetting(){	
				//端口位置
				jl1.setBounds(30,130,80,30); //选择端口
				jc.setBounds(150,130,80,30); //端口
				
				//线路类型&单选按钮
				jl2.setBounds(30,180,80,30); //线路类型
				jr1.setBounds(150,180,80,30);  //单回
				jr2.setBounds(250,180,80,30);  //双回
				ButtonGroup group = new ButtonGroup();
				group.add(jr1);
				group.add(jr2);
				
				//线路长度  
				jl3.setBounds(30,230,100,30); //线路长度（Km）
				jt1.setBounds(150,230,80,30);//线路长度
				
				//开始测量
				jb1.setBounds(100,300,100,50); //开始测量
				
				//结果显示区
				jb2.setBounds(430,30,150,30); //线路单位长度参数
				jb3.setBounds(610,30,150,30); //线路总长度参数
				jtr.setBounds(350,80,550,300);//显示区间
				
				//状态实时显示
				jl4.setBounds(50,400,400,30);
				jl4.setForeground(Color.BLUE);
	}

	/**
	 * 选择端口
	 */
	private void JcListener(){

		jc.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SerialPortTest.PortType = jc.getSelectedItem().toString();
				System.out.println(SerialPortTest.PortType);
				jl4.setText("选择"+SerialPortTest.PortType+"端口");
			}
		});
	}
	
	/**
	 * 设置线路类型为单回
	 */
	private void Jr1Listener(){
		jr1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FileOperate.LineType = "S";
				jl4.setText("选择线路类型为单回");
				System.out.println(FileOperate.LineType);
			}
		});
	}
	
	/**
	 * 设置线路类型为双回
	 */
	private void Jr2Listener(){
		jr2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FileOperate.LineType = "D";
				jl4.setText("选择线路类型为双回");
				System.out.println(FileOperate.LineType);
			}
		});
	}
	
	/**
	 * 设置线路长度为指定长度
	 */
	private void Jt1Listener(){
		jt1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FileOperate.Linelength = Double.parseDouble(jt1.getText());
				jl4.setText(String.format("选择线路长度为%.3fKM",FileOperate.Linelength));
				System.out.println(FileOperate.Linelength);
			}
		});
	}
	

	/**
	 * 从result中读取data数据，并写入LineParameters中
	 */
	private void GetResult(){
		try{
			//清空AllLineParameters和UnitLineParameters
			AllLineParameters.clear();
			UnitLineParameters.clear();
    		//读取result文件内容
    		FileReader fr = new FileReader(FileOperate.DEFAULTFilePath+"/"+FileOperate.RESULT_NAME); 
    		BufferedReader br = new BufferedReader(fr);
    		String tep = br.readLine();
    		//将String转换成double添加到LineParameters中
    		while(tep!=null){
    			AllLineParameters.add(Double.parseDouble(tep));
    			tep = br.readLine();
    		}
    		br.close();
    		fr.close();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
		
		//将总长度的线路参数转换成单位长度的线路参数
    	for(int i=0;i<AllLineParameters.size();i++){
    		UnitLineParameters.add(AllLineParameters.get(i)/FileOperate.Linelength);
    	}
    	/*
		System.out.println("线路总参数："+AllLineParameters);
		System.out.println("线路分布参数："+UnitLineParameters);
		
		System.out.println("线路总参数个数："+AllLineParameters.size());
		System.out.println("线路单位参数个数："+UnitLineParameters.size());
		*/
	}
	
	/**
	 * @param L List<Double> 型的函数变量
	 * @return  在jtr中进行显示的字符串
	 */
	private String DispalyResults(List<Double> L){
		String resultString = null;
		if(FileOperate.LineType.equals("S")){
			resultString = String.format("线路参数为：\n" +
					"%4.3f+%4.3fi %4.3f+%4.3fi %4.3f+%4.3fi\n" +
					"%4.3f+%4.3fi %4.3f+%4.3fi %4.3f+%4.3fi\n" +
					"%4.3f+%4.3fi %4.3f+%4.3fi %4.3f+%4.3fi\n" +
					"线路序参数为：\n" +
					"%4.3f+%4.3fi %4.3f+%4.3fi %4.3f+%4.3fi\n" +
					"%4.3f+%4.3fi %4.3f+%4.3fi %4.3f+%4.3fi\n" +
					"%4.3f+%4.3fi %4.3f+%4.3fi %4.3f+%4.3fi\n" +
					"地阻抗为：\n" +
					"%4.3f", 
					L.get(0),L.get(1),L.get(2),L.get(3),L.get(4),L.get(5),
					L.get(6),L.get(7),L.get(8),L.get(9),L.get(10),L.get(11),
					L.get(12),L.get(13),L.get(14),L.get(15),L.get(16),L.get(17),
					
					L.get(18),L.get(19),L.get(20),L.get(21),L.get(22),L.get(23),
					L.get(24),L.get(25),L.get(26),L.get(27),L.get(28),L.get(29),
					L.get(30),L.get(31),L.get(32),L.get(33),L.get(34),L.get(35),
					
					L.get(36));
			
		}else if(FileOperate.LineType.equals("D")){
			resultString = String.format("线路参数为：\n" +
					"%4.3f+%4.3fi %4.3f+%4.3fi %4.3f+%4.3fi %4.3f+%4.3fi %4.3f+%4.3fi %4.3f+%4.3fi\n" +
					"%4.3f+%4.3fi %4.3f+%4.3fi %4.3f+%4.3fi %4.3f+%4.3fi %4.3f+%4.3fi %4.3f+%4.3fi\n" +
					"%4.3f+%4.3fi %4.3f+%4.3fi %4.3f+%4.3fi %4.3f+%4.3fi %4.3f+%4.3fi %4.3f+%4.3fi\n" +
					"%4.3f+%4.3fi %4.3f+%4.3fi %4.3f+%4.3fi %4.3f+%4.3fi %4.3f+%4.3fi %4.3f+%4.3fi\n" +
					"%4.3f+%4.3fi %4.3f+%4.3fi %4.3f+%4.3fi %4.3f+%4.3fi %4.3f+%4.3fi %4.3f+%4.3fi\n" +
					"%4.3f+%4.3fi %4.3f+%4.3fi %4.3f+%4.3fi %4.3f+%4.3fi %4.3f+%4.3fi %4.3f+%4.3fi\n" +
					"线路序参数为：\n" +
					"%4.3f+%4.3fi %4.3f+%4.3fi %4.3f+%4.3fi %4.3f+%4.3fi %4.3f+%4.3fi %4.3f+%4.3fi\n" +
					"%4.3f+%4.3fi %4.3f+%4.3fi %4.3f+%4.3fi %4.3f+%4.3fi %4.3f+%4.3fi %4.3f+%4.3fi\n" +
					"%4.3f+%4.3fi %4.3f+%4.3fi %4.3f+%4.3fi %4.3f+%4.3fi %4.3f+%4.3fi %4.3f+%4.3fi\n" +
					"%4.3f+%4.3fi %4.3f+%4.3fi %4.3f+%4.3fi %4.3f+%4.3fi %4.3f+%4.3fi %4.3f+%4.3fi\n" +
					"%4.3f+%4.3fi %4.3f+%4.3fi %4.3f+%4.3fi %4.3f+%4.3fi %4.3f+%4.3fi %4.3f+%4.3fi\n" +
					"%4.3f+%4.3fi %4.3f+%4.3fi %4.3f+%4.3fi %4.3f+%4.3fi %4.3f+%4.3fi %4.3f+%4.3fi\n" +
					"地阻抗为：\n" +
					"%4.3f", 
					L.get(0),L.get(1),L.get(2),L.get(3),L.get(4),L.get(5),L.get(6),L.get(7),L.get(8),L.get(9),L.get(10),L.get(11),
					L.get(12),L.get(13),L.get(14),L.get(15),L.get(16),L.get(17),L.get(18),L.get(19),L.get(20),L.get(21),L.get(22),L.get(23),
					L.get(24),L.get(25),L.get(26),L.get(27),L.get(28),L.get(29),L.get(30),L.get(31),L.get(32),L.get(33),L.get(34),L.get(35),
					L.get(36),L.get(37),L.get(38),L.get(39),L.get(40),L.get(41),L.get(42),L.get(43),L.get(44),L.get(45),L.get(46),L.get(47),
					L.get(48),L.get(49),L.get(50),L.get(51),L.get(52),L.get(53),L.get(54),L.get(55),L.get(56),L.get(57),L.get(58),L.get(59),
					L.get(60),L.get(61),L.get(62),L.get(63),L.get(64),L.get(65),L.get(66),L.get(67),L.get(68),L.get(69),L.get(70),L.get(71),
					
					L.get(72),L.get(73),L.get(74),L.get(75),L.get(76),L.get(77),L.get(78),L.get(79),L.get(80),L.get(81),L.get(82),L.get(83),
					L.get(84),L.get(85),L.get(86),L.get(87),L.get(88),L.get(89),L.get(90),L.get(91),L.get(92),L.get(93),L.get(94),L.get(95),
					L.get(96),L.get(97),L.get(98),L.get(99),L.get(100),L.get(101),L.get(102),L.get(103),L.get(104),L.get(105),L.get(106),L.get(107),
					L.get(108),L.get(109),L.get(110),L.get(111),L.get(112),L.get(113),L.get(114),L.get(115),L.get(116),L.get(117),L.get(118),L.get(119),
					L.get(120),L.get(121),L.get(122),L.get(123),L.get(124),L.get(125),L.get(126),L.get(127),L.get(128),L.get(129),L.get(130),L.get(131),
					L.get(132),L.get(133),L.get(134),L.get(135),L.get(136),L.get(137),L.get(138),L.get(139),L.get(140),L.get(141),L.get(142),L.get(143),

					L.get(144));	
		}else{
			resultString = "单位长度参数显示出错";
		}
		return resultString;
	}
	/**
	 * 设置从result.txt中读取的数据为单位长度参数
	 */
	private void Jb2Listener(){
		jb2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//从result.txt文件中读取结果UnitLineParameters
				GetResult();
				//将UnitLineParameters转换为jtr显示文本
				jtr.setText(DispalyResults(UnitLineParameters));
				jl4.setText("目前显示为单位长度线路参数");
			}
		});
	}
	/**
	 * 设置从result.txt中读取的数据为总长度参数
	 */
	private void Jb3Listener(){
		jb3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//从result.txt文件中读取结果AllLineParameters
				GetResult();
				//将UnitLineParameters转换为jtr显示文本
				jtr.setText(DispalyResults(AllLineParameters));
				jl4.setText("目前显示为总长度线路参数");
			}
		});
	}

	/**
	 * 开始测量，发出信号
	 */
	private void Jb1Listener(){
		jb1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				//新建SerialPortTest的对象，调用其函数
				//jl4.setText("正在测量中");
				SerialPortTest example = new SerialPortTest();
				//打开端口开始工作
				if(FileOperate.LineType.equals("S")) {
					example.StartWorking("S\n");
				}else if(FileOperate.LineType.equals("D")) {
					example.StartWorking("D\n");
				}else {
					System.out.println("MySwing.jb1Listener在判断线路类型时错误");
				}
				//调用Matlab函数求解问题
				MATLAB.CalculateImpedance();
				jl4.setText("本次测量结束");
			}
		});
	}

	/**
	 * 对Frame进行设置，关于Frame的设置需要放到最后
	 */
	private void FrameSetting(){
		//设置绝对布局 及各组件位置
		panel.setLayout(null);
		//将组件放入容器panel中
		panel.add(jl1);
		panel.add(jl2);
		panel.add(jl3);
		panel.add(jc);
		panel.add(jb1);
		panel.add(jb2);
		panel.add(jb3);
		panel.add(jr1);
		panel.add(jr2);
		panel.add(jt1);
		panel.add(jl4);
		panel.add(jtr);

		//设置Frame的样式和关闭方式
		frame.setTitle("线路参数测量");
		frame.setBackground(Color.white);
		frame.setBounds(300,100,950,500);
		//setFont("黑体",Font.BOLD,20);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setResizable(false); //去掉最大化
		
		frame.add(panel);
	}
	private void AllListener(){
		JcListener();
		Jr1Listener();
		Jr2Listener();
		Jt1Listener();
		Jb2Listener();
		Jb3Listener();
		Jb1Listener();
	}
	
	public void Demo(){
		//设置所有组件位置
		AppearanceSetting();
		//设置所有组件监听
		AllListener();
		//最后设置Frame
		FrameSetting();
	}
	
	public static void main(String[] args){
		new MySwing().Demo();	
	}
}
