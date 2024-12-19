package e.helper;

import java.util.Objects;
import java.security.cert.X509Certificate;
import java.security.NoSuchAlgorithmException;
import java.security.KeyManagementException;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class NetHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(NetHelper.class);

    public static void fixSunCertPathBuilderException() {
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
 
           public void checkClientTrusted(X509Certificate[] certs, String authType) {  }
 
           public void checkServerTrusted(X509Certificate[] certs, String authType) {  }
 
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
     };
     // Install the all-trusting host verifier
     HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
     /*
      * end of the fix
      */

        } catch (NoSuchAlgorithmException | KeyManagementException exception) {
            LOGGER.error(StringHelper.getStackTrace(exception));
        }
    }
}
