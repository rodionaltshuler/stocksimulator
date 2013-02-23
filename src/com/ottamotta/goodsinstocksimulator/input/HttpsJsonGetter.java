package com.ottamotta.goodsinstocksimulator.input;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.util.Base64;
import android.util.Log;


public class HttpsJsonGetter {
	/**
	 * 
	 * Класс осуществляет HTTPS-соединение и получает JSON-объект с перечнем задач для отображения
	 * Пока не работает!
	 */

		
		private final static String LOGTAG = "SIMULATION HttpBasicAuth";	
		
		
		public String getData(String urlToJson) {
			
			String data=null;
			try {	        
				
				//final String username = "estafeta@62e12548-0a68-4999-960b-a3bb3c44675c";
				//final String password = "11111";
					
				//URL url = new URL("https://sites.google.com/site/coffeerides/contacts/json.txt?attredirects=0&d=1");			
				URL url = new URL(urlToJson);
				//String encoded = Base64.encodeToString((username+":"+password).getBytes("UTF-8"), Base64.NO_WRAP);			
				//String basicAuth = "Basic " + encoded;
				
				HttpClient client = getNewHttpClient();
				HttpGet get = new HttpGet(url.toString());
			
				//get.addHeader("ClientVersion", "2.0.49.14000");
				//get.addHeader("Authorization", basicAuth);
			
				
				HttpResponse response = client.execute(get);		
				HttpEntity entity = response.getEntity();
		        data = EntityUtils.toString(entity);
				
		        Log.d(LOGTAG, "Содержимое страницы:");
		        Log.d(LOGTAG, data);
		        
		        //сохраняем содержимое в JSON - когда будет что сохранять 
				//json = (JSONObject) new JSONObject(data);		
				
			} catch(Exception e) {
		        e.printStackTrace();	        
		    }
			
			return data;
			
		    
		}
		

		//Фабрика для создания HttpClient-а, доверяещего всем ssl-сертификатам
		//источник:
		//http://stackoverflow.com/questions/2642777/trusting-all-certificates-using-httpclient-over-https
		
		public HttpClient getNewHttpClient() {
		    try {
		        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
		        trustStore.load(null, null);

		        SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
		        sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

		        HttpParams params = new BasicHttpParams();
		        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

		        SchemeRegistry registry = new SchemeRegistry();
		        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		        registry.register(new Scheme("https", sf, 443));

		        ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

		        return new DefaultHttpClient(ccm, params);
		    } catch (Exception e) {
		        return new DefaultHttpClient();
		    }
		}
		
		public class MySSLSocketFactory extends SSLSocketFactory {
		    SSLContext sslContext = SSLContext.getInstance("TLS");

		    public MySSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
		        super(truststore);

		        TrustManager tm = new X509TrustManager() {
		            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		            }

		            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		            }

		            public X509Certificate[] getAcceptedIssuers() {
		                return null;
		            }
		        };

		        sslContext.init(null, new TrustManager[] { tm }, null);
		    }

		    @Override
		    public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
		        return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
		    }

		    @Override
		    public Socket createSocket() throws IOException {
		        return sslContext.getSocketFactory().createSocket();
		    }
		}
	

}
