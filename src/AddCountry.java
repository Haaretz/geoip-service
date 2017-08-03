import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


























import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;


public class AddCountry {
	

	
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
	    String csvFile = "C:\\Users\\vladimir.stukalov\\Documents\\data_without_ip.csv";
        Reader reader = null;        
        Map<String,String[]> newFile = new HashMap<String, String[]>();
       // Map<String,String[]> polopolyData = readPolopolyFile();
        //FileInputStream fis = new FileInputStream(csvFile);
        try {
                /*FileInputStream inpReader =new FileInputStream(csvFile);
                InputStreamReader stReader = new InputStreamReader(inpReader, "UTF-8");
                BufferedReader myInput = new BufferedReader(stReader);               
                CSVReader csvReader = new CSVReader(myInput);
                //List<String[]> records = csvReader.readAll();                
                int lineNumber=0;
                String [] record;*/
        	int lineNumber=0;
        	String [] record = new String[4];
        	Reader in = new FileReader(csvFile);
        	Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(in);        	
            ArrayList<String[]> data = new ArrayList<String[]>();
                for (CSVRecord line :records) {
                	Iterator<String> it = line.iterator();
                	int columnNumber = 0;
                	while(it.hasNext() && columnNumber<record.length){
                		record[columnNumber]=it.next();
                		columnNumber++;
                	}
                //while((record = csvReader.readNext())!=null){
                	if(lineNumber==0){
                		data.add(concat(record,"Country"));
                		//data.add(concat(record,"State"));
                	}else{                		
                		if(record[2].contains(".")){
                			JSONObject json = getJsonFromServer("http://172.21.1.62:8080/json/"+record[2].trim());                		 
                			System.out.println(json.get("region_name")+" "+record[1]);
                			String[] tmp = concat(record,(String) json.get("country_name"));
                			data.add(concat(tmp,(String) json.get("region_name")));
                		}
                	}
                	lineNumber++;
                	Thread.sleep(10);
                	
                }
                String csv = "C:\\Users\\vladimir.stukalov\\Documents\\data_with_ip.csv";
                CSVWriter writer;
                try {
                        //writer = new CSVWriter(new FileWriter(csv));

            OutputStream os = new FileOutputStream(csv);
            os.write(239);
            os.write(187);
            os.write(191);
            PrintWriter w = new PrintWriter(new OutputStreamWriter(os, "UTF-8"));
            writer = new CSVWriter(w);
            writer.writeAll(data);
                writer.close();
                } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }

        }catch (Exception e){
        	System.out.print(e);
        }

	}
	
	public static JSONObject getJsonFromServer(String urlString) throws KeyManagementException, NoSuchAlgorithmException{
    	URL url;
		try {
			/*
			 *  fix for
			 *    Exception in thread "main" javax.net.ssl.SSLHandshakeException:
			 *       sun.security.validator.ValidatorException:
			 *           PKIX path building failed: sun.security.provider.certpath.SunCertPathBuilderException:
			 *               unable to find valid certification path to requested target
			 */
			TrustManager[] trustAllCerts = new TrustManager[] {
			   new X509TrustManager() {
			      public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			        return null;
			      }

			      public void checkClientTrusted1(X509Certificate[] certs, String authType) {  }

			      public void checkServerTrusted1(X509Certificate[] certs, String authType) {  }

				@Override
				public void checkClientTrusted(X509Certificate[] arg0,
						String arg1) throws CertificateException {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void checkServerTrusted(X509Certificate[] arg0,
						String arg1) throws CertificateException {
					// TODO Auto-generated method stub
					
				}

			   }
			};

			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

			// Create all-trusting host name verifier
			HostnameVerifier allHostsValid = new HostnameVerifier() {
			    public boolean verify(String hostname, SSLSession session) {
			      return true;
			    }

				public boolean verify1(String hostname, SSLSession session) {
					// TODO Auto-generated method stub
					return false;
				}
			};
			// Install the all-trusting host verifier
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
			/*
			 * end of the fix
			 */
			url = new URL(urlString);			
			/*HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
			con.setConnectTimeout(15 * 1000);
			con.setRequestMethod("GET");
			con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.0; en-US; rv:1.9.1.2) Gecko/20090729 Firefox/3.5.2 (.NET CLR 3.5.30729)");
			con.connect();*/
			//BufferedReader in = new BufferedReader(
			  //  new InputStreamReader(con.getInputStream()));	
			BufferedReader in = new BufferedReader(
					    new InputStreamReader(url.openStream()));
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(in);
			org.json.simple.JSONObject jsonObject = (JSONObject) obj;
			return jsonObject;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e){
			e.printStackTrace();
		}
		return null;
     
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
