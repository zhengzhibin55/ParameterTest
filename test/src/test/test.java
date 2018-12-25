package test;
 
import com.mathworks.toolbox.javabuilder.MWArray;  
import com.mathworks.toolbox.javabuilder.MWClassID;  
import com.mathworks.toolbox.javabuilder.MWComplexity;  
import com.mathworks.toolbox.javabuilder.MWNumericArray;  
  
import demo.plotter;  
  
public class test {  
  
    /**  
     * @param args  
     */  
    public static void main(String[] args) {  
        // TODO Auto-generated method stub  
        MWNumericArray x = null; // ���xֵ������  
        MWNumericArray y = null; // ���yֵ������  
        plotter thePlot = null; // plotter���ʵ������MatLab����ʱ���½����ࣩ  
        int n = 20; // ��ͼ����  
  
        try {  
            // ����x��y��ֵ  
            int[] dims = { 1, n };  
            x = MWNumericArray.newInstance(dims, MWClassID.DOUBLE,  
                    MWComplexity.REAL);  
            y = MWNumericArray.newInstance(dims, MWClassID.DOUBLE,  
                    MWComplexity.REAL);  
  
            // ���� y = x^2  
            for (int i = 1; i <= n; i++) {  
                x.set(i, i);  
                y.set(i, i * i);  
            }  
  
            // ��ʼ��plotter�Ķ���  
            thePlot = new plotter();  
  
            // ��ͼ  
            thePlot.drawplot(x, y);  
            thePlot.waitForFigures();  
        }  
  
        catch (Exception e) {  
            System.out.println("Exception: " + e.toString());  
        }  
  
        finally {  
            // �ͷű�����Դ  
            MWArray.disposeArray(x);  
            MWArray.disposeArray(y);  
            if (thePlot != null)  
                thePlot.dispose();  
        }  
    }  
  
}  