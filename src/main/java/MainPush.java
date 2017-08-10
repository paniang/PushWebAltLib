import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;
import org.apache.http.HttpResponse;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jose4j.lang.JoseException;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.util.concurrent.ExecutionException;

public class MainPush {
    public static void main(String... args) throws IOException {
        String endPoint = "https://updates.push.services.mozilla.com/wpush/v1/gAAAAABZiv2CIvYMyh0jbzfU2AixyjKdE1VNBug8qYrxmQCuXoQWmKyQHjAd_l3ho6cWO23AWjw5NmXVXW1FnfoDKuG6jp-h-E84uG1EjGlI0MAqoAtbId5lNLs9yXrbbjjpoUyiRaTa";
        String userPublicKey = "BJEXEPZdkQtH4cw_kepXdLu9bZrJVnUAhs__CXv0-rApLehmb-zg5B2soO13y4z39-hH9zn2fEPy6Z4_l8EFVtw";
        PushService pushService = new PushService();
        Notification notification = null;
        Security.addProvider(new BouncyCastleProvider());
        try {
            notification = new Notification(endPoint, userPublicKey, "aMSO4U24QeOC5eqsFZRyQw", "Houston we've got a problem!".getBytes());
        } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        final CloseableHttpAsyncClient closeableHttpAsyncClient = HttpAsyncClients.createSystem();
        try {
            closeableHttpAsyncClient.start();
            HttpResponse response = pushService.sendAsync(notification, closeableHttpAsyncClient).get();
            pushService.sendAsync(notification, closeableHttpAsyncClient).get();
            pushService.sendAsync(notification, closeableHttpAsyncClient).get();
            System.out.println(response);
        } catch (GeneralSecurityException | IOException | JoseException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            closeableHttpAsyncClient.close();
        }

    }
}
