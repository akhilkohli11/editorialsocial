
package twitter.api;

import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LoadApp {
    public static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
    public static final String MYSQL_URL = "jdbc:mysql://localhost/social?"
            + "user=root";
    public static SocialMysqlLayer socialMysqlLayer;
    static int count=0;
    public static void initialize(int start,int end) throws Exception
    {

        try {
            TwitterDataRetriever.getTweets(5000, socialMysqlLayer,start,end);
            count++;
        }
        catch (Exception e)
        {
            System.out.println("Twitter stopped Twitter stopped");
            e.printStackTrace();
            while (count<100)
            {
                Thread.sleep(3000);
                initialize(start,end);
            }
        }

    }
    public static void main(String args[]) throws Exception
    {
        CloudSolrPersistenceLayer.getInstance().init();
        init();

    }
    public static void  init() throws Exception{

        BufferedReader br = null;

        try {

            String sCurrentLine;
            socialMysqlLayer=new SocialMysqlLayer(MYSQL_DRIVER,MYSQL_URL);

            br = new BufferedReader(new FileReader("/tmp/okbro"));
            int count=1;
            //  socialMysqlLayer.readData();
            while ((sCurrentLine = br.readLine()) != null) {

                String[] buffer= StringUtils.split(sCurrentLine, "#", 2);
                if(buffer.length>1) {
                    String[] hashtag = ("#" + buffer[1].trim()).split("#");

                    TwitterDataRetriever.populateShowIDToShowName(buffer[0].trim(), null, null, hashtag);
                    count++;
                }
            }

        } catch (Exception e) {
             e.printStackTrace();
        } finally {
            try {
                if (br != null) br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    public static SocialMysqlLayer getSocialMysqlLayer()
    {
        return socialMysqlLayer;
    }

}

