package ParameterTest;



import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import test.SwingThreadTest2;


//setBounds(int x, int y, int width, int height)
//X是左上角横坐标,Y是左上角纵坐标，windth是宽，height是高 
//组件的坐标是相对于位置

/**
 * @author zheng
 * 负责开场动画和登录界面
 */
public class SplashWindow extends JFrame {
	//定义属性
	private JFrame frame = new JFrame();  
	private JPanel panel = new JPanel(); //面板
	//标签
	private JLabel jl1 = new JLabel("抗高感应电压线路参数测试仪"); 
	private JLabel jl2 = new JLabel("线路名称:"); 
	private JLabel jl3 = new JLabel("测试人员姓名:"); 
	//普通按钮
	private JButton jb1 = new JButton("登录");
	private JButton jb2 = new JButton("退出");
	//单行文本
	private JTextField jt1 = new JTextField("未输入线路名称"); //输入线路名称
	private JTextField jt2 = new JTextField("未输入测试人员姓名"); //输入测试姓名
	//其他两个界面
	private MySwing mw=new MySwing();//主界面
	private JWindow jw=new JWindow(frame);
	
	
	/**
	 * 设置基本组件及其位置
	 */  
	private void AppearanceSetting(){
		Font font = new Font("微软雅黑", Font.BOLD, 21);//标题
		Font font2 = new Font("微软雅黑", Font.PLAIN, 17);//按钮
		Font font3 = new Font("宋体", Font.PLAIN, 15);//文字
		
		jl1.setFont(font);
		
		jb1.setFont(font2);
		jb2.setFont(font2);
		
		jl2.setFont(font3);
		jl3.setFont(font3);
		//抗高感应电压线路参数测试仪
		jl1.setBounds(45,40,300,30); 
		//线路名称: 
		jl2.setBounds(50,120,100,30); 
		//测试人员姓名:
		jl3.setBounds(50,170,100,30); 
		//线路名称
		jt1.setBounds(160,120,150,30);
		//测试人员姓名
		jt2.setBounds(160,170,150,30);
		//登录
		jb1.setBounds(50,250,80,40); 
		//退出
		jb2.setBounds(220,250,80,40); 
		
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
		panel.add(jb1);
		panel.add(jb2);
		panel.add(jt1);
		panel.add(jt2);
		
		//设置Frame的样式和关闭方式
		frame.setTitle("请输入以下信息");
		frame.setBackground(Color.white);
		frame.setBounds(450,200,400,400);
		
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		//frame.setVisible(true);
		frame.setResizable(false); //去掉最大化   
		frame.add(panel);

	}
	
	/**
	 * 设置开场动画
	 */
	private void SplashSetting(){
        Container co=jw.getContentPane();
        //图画
        JLabel label = new JLabel(new ImageIcon("C:/Users/zheng/Downloads/t2.jpg"));
        //添加画图
        co.add(label);
        jw.setLocation(400, 200);
        jw.pack();//设置大小，如果没有使用setsize函数就必须使用pack函数
        jw.setVisible(true);
        
        new Thread(new Runnable() {
			public void run() {
				// TODO Auto-generated method stub
				//延时时间
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				//将页面更改操作发送到EDT的可执行队列中
				SwingUtilities.invokeLater(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						 jw.setVisible(false);
						 frame.setVisible(true);
					}
				});
			}
		}).start();

	}

	/**
	 * 设置线路名称
	 */
	private void Jt1Listener(){
		jt1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});
	}
	/**
	 * 设置测试人员名称
	 */
	private void Jt2Listener(){
		jt2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});
	}
	/**
	 * 进入主界面
	 */
	private void Jb1Listener(){
		jb1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
				mw.Demo();
				//System.out.println("单击登录");
			}
		});
	}
	/**
	 * 退出
	 */
	private void Jb2Listener(){
		jb2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose(); 
			}
		});
	}

	/**
	 * 设置所有监听器
	 */
	private void AllListener(){

		Jt1Listener();
		Jt2Listener();
		Jb1Listener();
		Jb2Listener();
	}
	
	
	/**
	 * 运行流程
	 */
	public void Demo(){
		//设置所有组件位置
		AppearanceSetting();
		//设置所有组件监听
		AllListener();
		//最后设置Frame
		FrameSetting();
		//开场动画
		SplashSetting();
		
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		SwingUtilities.invokeLater(new Runnable(){
			public void run() {
				 new SplashWindow().Demo();
			}
		});

		

	}

}
