package test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class temp {
	
	public static void main(String[] args) {
		 String filePath = "C:\\Users\\sunda\\Desktop\\test.txt";
		 // System.out.println(readTxtFile(filePath));
		 List<String> list = splitStr(readTxtFile(filePath));
		// System.out.println(readTxtFile(filePath));
		 for (String string : list) {
			 System.out.println("----------");
			// System.out.println(getTable(string));
			 contentToTxt("C:\\Users\\sunda\\Desktop\\result.txt", getTable(string));
		}
		 //System.out.println(list.get(0)+"----------");
		// System.out.println(getTable(list.get(0)));
	}
	
	
	 public static String readTxtFile(String filePath){
		 String str = "";
	        try {

	                String encoding="utf-8";

	                File file=new File(filePath);

	                if(file.isFile() && file.exists()){ //判断文件是否存在

	                    InputStreamReader read = new InputStreamReader(

	                    new FileInputStream(file),encoding);//考虑到编码格式

	                    BufferedReader bufferedReader = new BufferedReader(read);

	                    String lineTxt = null;
	                    

	                    while((lineTxt = bufferedReader.readLine()) != null){
	                    	str = str+lineTxt+"\r\n";

	                        //System.out.println(lineTxt);

	                    }

	                    read.close();

	        }else{

	            System.out.println("找不到指定的文件");

	        }

	        } catch (Exception e) {

	            System.out.println("读取文件内容出错");

	            e.printStackTrace();

	        }

	     
	        return str;
	    }

	 public static List<String> splitStr(String str){
		 ArrayList<String> list = new ArrayList<String>();
		 String[] arr = str.split("prompt\\r\\n");
		 for(int i=0;i<arr.length;i++){
			 if(arr[i].trim()==""||arr[i].contains("Creating")||arr[i].contains("==")||arr[i].contains("spool")){
				 
			 }else{
				 list.add(arr[i]);
			 }
		 }
		 System.out.println(arr.length+"========"+list.size());
		 return list;
	 }  
	 
	 public static String getTable(String str){
		 str = str.toUpperCase();
		// System.out.println(str+"----------");
		 String createTable = "";
		 String fieldStr = "";
		 String primaryKey = "";
		 String tableName = "";
		 Map<String, String> comments = new HashMap<String,String>();
		 if(str.contains("CREATE TABLE")){
			 Pattern p = Pattern.compile("CREATE TABLE\\s+[A-Z0-9\\._\\s]+\\("); 
			 Matcher m = p.matcher(str); 
			 if(m.find()){				 
				 createTable = m.group();
				// System.out.println("找到了======");
			 }
			// System.out.println(createTable);			 
		 }
		 tableName = createTable.split("\\s+")[2];
		 String dropTable = "DROP TABLE "+tableName+";\r\n";
		 System.out.println(dropTable);
		 String s = str.replace(createTable, "");
		// System.out.println(s+"----------");
		 
		 if(str.contains("COMMENT ON")){
			 str = str.replaceAll("\\'", "");
			// System.out.println(str);
			 Pattern p = Pattern.compile("COMMENT ON [A-Z0-9_\\.\\s\\']*[\\u4e00-\\u9fa5]+"); 
			 Matcher m = p.matcher(str); 
			 while(m.find()){				 
				String comment = m.group().split("\\.")[m.group().split("\\.").length-1];
				String[] com = comment.split("\\s+IS\\s");
				System.out.println(com[1]);
				comments.put(com[0], com[1]);
			 }			 		 
		 }
		 
		 if(str.contains("PRIMARY KEY")){
			 Pattern p = Pattern.compile("PRIMARY\\s+KEY\\s+\\([A-Z0-9_,\\s]+\\)"); 
			 Matcher m = p.matcher(str); 
			 if(m.find()){				 
				 primaryKey = m.group();
				// System.out.println("找到了----------");
			 }
			// System.out.println(primaryKey);
			 
		 }
		 
		 StringBuilder sb = new StringBuilder();
		 
		 if(s.contains("(")){
			 Pattern p = Pattern.compile("[A-Z0-9_,\\(\\)\\s\\']+"); 
			 Matcher m = p.matcher(s); 
			 if(m.find()){				 
				 fieldStr = m.group();
				 fieldStr = fieldStr.split("TABLESPACE")[0].replaceAll(" NOT NULL", "");
				 String[] fields = fieldStr.split("\\r\\n");
				 if(!primaryKey.equals("")){
					 fields[fields.length-2] = fields[fields.length-2]+",";
					 fields[fields.length-1] = "  "+primaryKey+"\r\n)";
				 }
				 for(int i=0;i<fields.length-1;i++){
					 if(fields[i].trim().equals("")){
						 continue;
					 }else{			
						 fields[i] = fields[i]+"/*";
						 for (String key : comments.keySet()) {
							 if(fields[i].contains(key)){
								 fields[i] = fields[i]+comments.get(key);
								 continue;
							 }						   
						 }
						 fields[i] = fields[i]+"*/";
					 }
				 }
				 
				 for(int i = 0;i<fields.length;i++){
					// System.out.println(fields[i]);	
					 sb.append(fields[i]+"\r\n");
				 }
				 
			 }
			 
			
		 }	 
		 
	    String title = "/*"+tableName+"对照表"+"*/\r\n";
		return 	title+dropTable+createTable+sb.toString();
		 
	 }

	  public static void contentToTxt(String filePath, String content) {

	        try{

	            BufferedWriter writer = new BufferedWriter(new FileWriter(new File(filePath),true));

	            writer.write("\n"+content);

	            writer.close();

	        }catch(Exception e){

	            e.printStackTrace();

	        }

	    }

}
