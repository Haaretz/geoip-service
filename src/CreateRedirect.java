import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;


public class CreateRedirect {
	public static void main(String[] args) {
        try {
               addCountryColumn();
       } catch (FileNotFoundException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
       }
}

	private static void addCountryColumn()  throws FileNotFoundException{
		// TODO Auto-generated method stub
	    String csvFile = "C:\\Users\\vladimir.stukalov\\Downloads\\redirects1.csv";
        Reader reader = null;        
        Map<String,String[]> newFile = new HashMap<String, String[]>();
       // Map<String,String[]> polopolyData = readPolopolyFile();
        //FileInputStream fis = new FileInputStream(csvFile);
        try {
                FileInputStream inpReader =new FileInputStream(csvFile);
                InputStreamReader stReader = new InputStreamReader(inpReader, "UTF-8");
                CSVReader csvReader = new CSVReader(stReader);
                List<String[]> records = csvReader.readAll();
                PrintWriter writer = new PrintWriter("C:\\Users\\vladimir.stukalov\\Downloads\\newredirects1.txt", "UTF-8");                                
                int lineNumber=0;                
                for (String[] record:records) {
                	if(lineNumber==0){                		
                	}else if(!record[1].contains("*")){                		                		 
                		//writer.println("RewriteRule ^/"+record[0] +"(/.*) "+record[1]+"/$1 [R=permanent,L]");
                		writer.println("RewriteRule ^/"+record[0] +" "+record[1]+" [R=permanent,L]");
                	}
                	lineNumber++;                	
                	
                }              
                writer.close();
          

        }catch (Exception e){
        	System.out.print(e);
        }

	}
	
	  public static String [] concat(String[] a,String b){
          int aLen = a.length;          
          String[] c= new String[aLen+1];
          System.arraycopy(a, 0, c, 0, aLen);
          //System.arraycopy(b, 0, c, aLen, bLen);
          c[aLen]=b;
          return c;
}
	
}
