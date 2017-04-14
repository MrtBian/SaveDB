import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Save {
	String dbpath = "/home/wing/doc.db";
	String docpath = "/home/wing/ICCV_Papers";
	Connection conn;
	Statement stat;
	int docid;
	public Save(){
		docid = 1;
		conn = getConnection();
	}
	
    public Connection getConnection(){
        try
        {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:"+dbpath);
            //System.out.println("connect");
            return conn;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
	public void readDocs() throws IOException{// get all names of docs
		try {
			stat=conn.createStatement();
			String de = "delete from document;";
			stat.executeUpdate(de);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for(File docFile : new File(docpath).listFiles()){
			System.out.println("Get data from "+docFile.getName()+"...");
			BufferedReader reader = null;
	        try {
	            //System.out.println("以行为单位读取文件内容，一次读一整行：");
	            reader = new BufferedReader(new FileReader(docFile));
	            String tempString = null;
	            //int line = 1;
	            // 一次读入一行，直到读入null为文件结束
	            String title;
	            String author;
	            String abst;
	            String keywords;
	            String doi;
	            String url;
	            while ((tempString = reader.readLine()) != null) {
	            	if(tempString.length()>2){
	            		
	            		//if(docid>200)
	            			//break;
	            		//System.out.println(tempString);
	            		int index1=tempString.indexOf("\"");
	            		if(index1<2)//
	            		{
	            			while (reader.readLine().length()>2);
	            			continue;
	            		}
	            		int index2=tempString.indexOf("\"", index1+3);
	            		//System.out.println(index1+" "+index2);
	            		author=tempString.substring(0, index1-1);
	            		title=tempString.substring(index1+1, index2-1);
	            		tempString = reader.readLine();//doi
	            		if(tempString==null){
				            title="";
				            author="";
				            abst="";
				            keywords="";
				            doi="";
				            url="";
	            			break;
	            		}
	            		if(tempString.length()<2){

				            title="";
				            author="";
				            abst="";
				            keywords="";
				            doi="";
				            url="";
	            			continue;
	            		}
	            		doi = tempString.substring(5);
	            		tempString = reader.readLine();//abstract
	            		if(tempString==null){
				            title="";
				            author="";
				            abst="";
				            keywords="";
				            doi="";
				            url="";
	            			break;
	            		}
	            		if(tempString.length()<2){

				            title="";
				            author="";
				            abst="";
				            keywords="";
				            doi="";
				            url="";
	            			continue;
	            		}
	            		abst=tempString.substring(10);
	            		tempString = reader.readLine();//keywords
	            		if(tempString==null){
				            title="";
				            author="";
				            abst="";
				            keywords="";
				            doi="";
				            url="";
	            			break;
	            		}
	            		if(tempString.length()<2){

				            title="";
				            author="";
				            abst="";
				            keywords="";
				            doi="";
				            url="";
	            			continue;
	            		}
	            		keywords=tempString.substring(9);
	            		tempString = reader.readLine();//url
	            		if(tempString==null){
				            title="";
				            author="";
				            abst="";
				            keywords="";
				            doi="";
				            url="";
	            			break;
	            		}
	            		if(tempString.length()<2){

				            title="";
				            author="";
				            abst="";
				            keywords="";
				            doi="";
				            url="";
	            			continue;
	            		}
	            		url=tempString.substring(5);
	            		tempString = reader.readLine();//\r
	            		title=title.replaceAll("\"", "\\\\\"");
	            		author=author.replaceAll("\"", "\\\\\"");
	            		abst=abst.replaceAll("\"", "\\\\\"");
	            		keywords=keywords.replaceAll("\"", "\\\\\"");
	            		url=url.replaceAll("\"", "\\\\\"");
	            		title=title.replaceAll("\'", "\'\'");
	            		author=author.replaceAll("\'", "\'\'");
	            		abst=abst.replaceAll("\'", "\'\'");
	            		keywords=keywords.replaceAll("\'", "\'\'");
	            		url=url.replaceAll("\'", "\'\'");
	            		//String a="\"asd\"";
	            		//a=a.replaceAll("\"", "\\\\\"");
	            		//if(docid%1000==1)
	            			//System.out.println(docid);
	            		String sql="insert into document values (\'"+
									title+"\',\'"+author+"\',\'"+abst+"\',\'"+keywords+"\',\'"+
									doi+"\',\'"+url+"\',"+docid+");";
						docid++;
			            title="";
			            author="";
			            abst="";
			            keywords="";
			            doi="";
			            url="";
						//System.out.println(sql);
						try {
							stat.executeUpdate(sql);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
	            	}
	            	
	            }
	            reader.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            if (reader != null) {
	                try {
	                    reader.close();
	                } catch (IOException e1) {
	                }
	            }
	        }
		}
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String args[]) throws IOException{
		Save save = new Save();
		save.readDocs();
		System.out.println("end!");
	}
}
