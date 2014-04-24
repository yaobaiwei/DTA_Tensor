package Bigdata;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import DTA.DTAT;

import com.mathworks.toolbox.javabuilder.MWArray;
import com.mathworks.toolbox.javabuilder.MWClassID;
import com.mathworks.toolbox.javabuilder.MWNumericArray;

public class XLSparse {
	
	public List<Integer> re_uid = new ArrayList<Integer>();  
	public List<Integer> re_pid = new ArrayList<Integer>();  
	public List<Integer> re_type = new ArrayList<Integer>();  
	
	public int DTensor[][] = null;
	private int uidsize;
	private int pidsize;
	private int typesize;
	private int num = 0;
	
	private String URL= new String();
	
	public XLSparse(String URL){
		this.URL = URL;
	}
	
	public List<Integer> removeDuplicateWithOrder(List<Integer> list) {
        HashSet set = new HashSet();
        List<Integer> newList = new ArrayList<Integer>();
        for (Iterator<Integer> iter = list.iterator(); iter.hasNext();) {
            Integer element = iter.next();
            if (set.add(element))
                newList.add(element);
        }
      return newList;
    }
	
	public int[][] getModelOne(int Tensor[][][]){
		int L=Tensor.length;
		int M=Tensor[0].length;
		int N=Tensor[0][0].length;
		int [][] modelone = new int[M][L*N];
		for(int i=0;i<L;i++){
			for(int j=0;j<N;j++){
				for(int k=0;k<M;k++){
					modelone[k][i*N+j] = Tensor[i][k][j];
				}
			}
		}
		return modelone;
	}
    
	public void getdata(){
    	
    	List<Integer> uid = new ArrayList<Integer>(); 
    	List<Integer> pid = new ArrayList<Integer>();  
    	List<Integer> type = new ArrayList<Integer>(); 
        try { 
            File csv = new File(this.URL); // CSV文件
            BufferedReader br = new BufferedReader(new FileReader(csv));
            String line = ""; 
            int i=0;
            while ((line = br.readLine()) != null) { 
            	if(i++ ==0){
            		continue;
            	}
            	String [] data = line.split(",");
            	uid.add(Integer.parseInt(data[0]));
            	pid.add(Integer.parseInt(data[1]));
            	type.add(Integer.parseInt(data[2]));
            } 
            br.close();
            
            this.num = i-1;
    		this.re_uid = removeDuplicateWithOrder(uid);
    		this.re_pid = removeDuplicateWithOrder(pid);
    		this.re_type = removeDuplicateWithOrder(type);
    		this.uidsize = re_uid.size();
    		this.pidsize = re_pid.size();
    		this.typesize = re_type.size();
    		uid=null;
    		pid=null;
    		type=null;
            
        } catch (FileNotFoundException e) { 
            // 捕获File对象生成时的异常 
            e.printStackTrace(); 
        } catch (IOException e) { 
            // 捕获BufferedReader对象关闭时的异常 
            e.printStackTrace(); 
        } 
    } 
    
	public int[][] maketensormodelone(int count){
		int[][][] Tensor = new int [this.typesize][this.pidsize][this.uidsize];
        for(int i=0;i<this.typesize;i++){
        	for(int j=0;j<this.pidsize;j++){
        		for(int k=0;k<this.uidsize;k++){
        			Tensor[i][j][k]=0;
        		}
        	}
        }
        
        try { 
            File csv = new File(this.URL); // CSV文件
            BufferedReader br = new BufferedReader(new FileReader(csv));

            String line = ""; 
            int i=0;
            while ((line = br.readLine()) != null) { 
            	if(i++ ==0){
            		continue;
            	}
            	String [] data = line.split(",");
            	String day = data[3].replace("月", ",");
            	day = day.replace("日", "");
            	String [] num = day.split(",");
            	if((Integer.parseInt(num[0])*100+Integer.parseInt(num[1])-15)/100 - 3 == count){
        			int L = 0;
        			int M = 0;
        			int N = 0;
        			for(L=0;L<typesize;L++){
        				Integer t = new Integer(Integer.parseInt(data[2]));
        				if(t.equals(re_type.get(L))){
        					break;
        				}
        			}	

        			for(N=0;N<pidsize;N++){
        				Integer t = new Integer(Integer.parseInt(data[1]));
        				if(t.equals(re_pid.get(N))){
        					break;
        				}
        			}

        			for(M=0;M<uidsize;M++){
        				Integer t = new Integer(Integer.parseInt(data[0]));
        				if(t.equals(re_uid.get(M))){
        					break;
        				}
        			}
        			Tensor[L][N][M]++;
            	}
            } 
            br.close();            
        } catch (FileNotFoundException e) { 
            // 捕获File对象生成时的异常 
            e.printStackTrace(); 
        } catch (IOException e) { 
            // 捕获BufferedReader对象关闭时的异常 
            e.printStackTrace(); 
        } 
        int [][] modelone = this.getModelOne(Tensor);
        Tensor = null;
        return modelone;
	}
	
		
    public static void main(String[] args) { 
    	XLSparse xls = new XLSparse("E:/workspace/Alibaba/t_alibaba_data.csv");
		xls.getdata();
    	Object result[] = null;
    	Object C =null;
		MWNumericArray R = null;
		MWNumericArray SIZE = null;
		MWNumericArray AP = null;

		int [] r = {xls.pidsize,xls.uidsize,xls.typesize};
		int [] size = {xls.pidsize/300,xls.uidsize/50,xls.typesize};
		
		List<String> list = new ArrayList<String>();
	
    	try{
    		DTAT dta = new DTAT();
			R = new MWNumericArray(r, MWClassID.SINGLE);
			SIZE = new MWNumericArray(size, MWClassID.SINGLE);
			AP = new MWNumericArray(0.25, MWClassID.DOUBLE);

    		for(int i=1;i<=4;i++){
				xls.DTensor = xls.maketensormodelone(i);
    			if(i==1){
    				System.out.print("!!!!\n");
    				MWNumericArray B = new MWNumericArray(xls.DTensor, MWClassID.SINGLE);
    				xls.DTensor=null;
    				result = dta.dtaf(2,B,R,SIZE);
    				C = result[1];
    				result = null;
    			}else if(i<4){
    				System.out.print("!!!!\n");
    				MWNumericArray B = new MWNumericArray(xls.DTensor, MWClassID.SINGLE);
    				xls.DTensor=null;
    				result = dta.dtas(2,B,R,SIZE,C,AP);
    				C = result[1];
    				result=null;
    			}else{
    				System.out.print("!!!!\n");
    				MWNumericArray B = new MWNumericArray(xls.DTensor, MWClassID.SINGLE);
    				xls.DTensor=null;
    				result = dta.dtat(1,B,R,SIZE,C,AP);
    				C = result[0];
    				result=null;
    			}
    		}
    		
    		double[][][] SimTensor =(double[][][])((MWNumericArray)C).toDoubleArray();
    		C=null;
  		
    		int h=0;
			for(int k=0;k<SimTensor[0].length;k++){
				String line = new String(""); 
				boolean check = false;
				for(int j=0;j<SimTensor.length;j++){
					if(SimTensor[j][k][0]*0.04+SimTensor[j][k][1]*0.5+SimTensor[j][k][2]*0.9+SimTensor[j][k][3]>=0.1)
					{
						if(check==false){
							line = xls.re_uid.get(k).toString()+"\t";
							check = true;
						}
						h++;
						line += xls.re_pid.get(j)+",";
						//System.out.print(xls.re_uid.get(k)+" "+xls.re_pid.get(j)+" "+SimTensor[j][k][3]+"\n");
					}	
				}
				if(check==true){
					list.add(line);
				}	
			}	
			System.out.print("\n"+h+"\n");
			SimTensor = null;
			xls.re_pid=null;
			xls.re_type=null;
			xls.re_uid=null;
    	}
	    catch (Exception e){
			 e.getStackTrace();
	    }	

    	try{
    		File file = new File("D:/add.txt");
    		if(!file.exists())
    			file.createNewFile();
    		
            FileWriter fileWritter = new FileWriter(file,false);
            BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
            StringBuffer sb=new StringBuffer();
    		for(int i =0;i<list.size();i++){
    			String s = list.get(i);
    			int index = s.lastIndexOf(",");
    			s = s.substring(0,index)+"\r\n";
    			System.out.print(s);
                sb.append(s);
    		}
            bufferWritter.write(sb.toString());
            bufferWritter.close();
    	}
	    catch (Exception e){
			 e.getStackTrace();
	    }
		finally
		{
		    MWArray.disposeArray(result);
		    MWArray.disposeArray(R);
		    MWArray.disposeArray(SIZE);
		    MWArray.disposeArray(AP);
			System.out.print("END!\n");
		}
		
    }
}