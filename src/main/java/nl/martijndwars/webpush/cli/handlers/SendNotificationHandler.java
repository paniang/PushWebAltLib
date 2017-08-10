package nl.martijndwars.webpush.cli.handlers;

import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;
import nl.martijndwars.webpush.Subscription;
import nl.martijndwars.webpush.cli.commands.SendNotificationCommand;
import org.apache.http.HttpResponse;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;

public class SendNotificationHandler implements HandlerInterface {
    private SendNotificationCommand sendNotificationCommand;

    public SendNotificationHandler(SendNotificationCommand sendNotificationCommand) {
        this.sendNotificationCommand = sendNotificationCommand;
    }

    @Override
    public void run() throws Exception {
        PushService pushService = new PushService()
            .setPublicKey(sendNotificationCommand.getPublicKey())
            .setPrivateKey(sendNotificationCommand.getPrivateKey())
            .setSubject("mailto:admin@domain.com");

        Subscription subscription = sendNotificationCommand.getSubscription();

        Notification notification = new Notification(subscription, sendNotificationCommand.getPayload());
        final CloseableHttpAsyncClient closeableHttpAsyncClient = HttpAsyncClients.createSystem();
        closeableHttpAsyncClient.start();
        HttpResponse response = pushService.send(notification, closeableHttpAsyncClient);

        System.out.println(response);
    }
}
