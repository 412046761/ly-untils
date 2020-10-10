
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * http url调用类  (POST GET方法)
 * @Author: liyue
 * @Date: 2020-06-22 17:26
 * @Version: 1.0
 **/
public class HttpUrlInvokerUtils {


    private static final String ENCODING = "UTF-8";

    private String url;
    private Integer timeoutSecond = 60;
    private String contentType = "text/xml; charset=" + ENCODING;
    private String soapAction = "http://goodwillcis.com/JHIPLIB.Util.Lookup.BS.Service.HIPMessageServer" ;
    private String cookie ="";

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public HttpUrlInvokerUtils(String url){
        this.url = url;
    }



    public HttpUrlInvokerUtils(String url, String contentType){
        this.url = url;
        this.contentType = contentType;
    }
    public HttpUrlInvokerUtils(String url, String contentType, String soapAction){
        this.url = url;
        if(StringUtils.isNotEmpty(contentType)){
            this.contentType = contentType;
        }
        this.soapAction = soapAction;
    }
    public HttpUrlInvokerUtils(String url, Integer second){
        this.url = url;
        if(second != null && second >= 1){
            this.timeoutSecond = second;
        }
    }

    /**
     * get请求
     * @return
     * @throws Exception
     */
    public String get() throws Exception{


        String result = null;
        URL url = null;
        HttpURLConnection conn = null;
        try {

            url = new URL(this.url);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            conn.setRequestProperty("Content-Type", contentType);
            conn.setRequestProperty("Connection", "Keep-Alive");
            if(StringUtils.isNotEmpty(cookie)){
                conn.setRequestProperty("cookie", cookie);
            }
            conn.setConnectTimeout(this.timeoutSecond * 500);
            conn.setReadTimeout(this.timeoutSecond * 500);
            DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());
            outStream.flush();
            outStream.close();

            int resultCode = conn.getResponseCode();
            if(resultCode == HttpURLConnection.HTTP_OK){
                StringBuilder sb = new StringBuilder();
                BufferedReader bufReader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                String readLine = null;
                while ((readLine = bufReader.readLine()) != null) {
                    sb.append(readLine).append("\n");
                }
                bufReader.close();
                result = sb.toString();
            }else{
                throw new Exception(String.format("%s调用异常，错误代码:%s", this.url,conn.getResponseCode()));
            }
        } catch (Exception e) {
            throw new Exception(e);
        }finally{
            if(conn != null){
                try {
                    conn.disconnect();
                } catch (Exception e2) {
                }
            }
        }

        return result;
    }

    /**
     * post请求
     * @param data
     * @return
     * @throws Exception
     */
    public String post(String data) throws Exception{
        if(StringUtils.isEmpty(data)){
            throw new Exception(String.format("%s请求内容为空", this.url));
        }

        String result = null;

        URL url = null;
        HttpURLConnection conn = null;
        try {
            byte[] bData = data.getBytes(ENCODING);

            url = new URL(this.url);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", contentType);
            conn.setRequestProperty("Content-Length", String.valueOf(bData.length));
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("SOAPAction",soapAction);
            conn.setConnectTimeout(this.timeoutSecond * 500);
            conn.setReadTimeout(this.timeoutSecond * 500);
            DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());
            outStream.write(bData);
            outStream.flush();
            outStream.close();

            int resultCode = conn.getResponseCode();
            if(resultCode == HttpURLConnection.HTTP_OK){
                StringBuilder sb = new StringBuilder();
                BufferedReader bufReader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                String readLine = null;
                while ((readLine = bufReader.readLine()) != null) {
                    sb.append(readLine).append("\n");
                }
                bufReader.close();
                result = sb.toString();
            }else{
                throw new Exception(String.format("%s调用异常，错误代码:%s", this.url,conn.getResponseCode()));
            }
        } catch (Exception e) {
            throw new Exception(e);
        }finally{
            if(conn != null){
                try {
                    conn.disconnect();
                } catch (Exception e2) {
                }
            }
        }

        return result;
    }





}
