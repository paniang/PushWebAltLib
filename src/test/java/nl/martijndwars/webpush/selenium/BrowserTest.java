package nl.martijndwars.webpush.selenium;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;
import nl.martijndwars.webpush.Subscription;
import org.apache.http.HttpResponse;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BrowserTest implements Executable {
    private PushService pushService;
    private TestingService testingService;
    private Configuration configuration;
    private int testSuiteId;

    public BrowserTest(PushService pushService, TestingService testingService, Configuration configuration, int testSuiteId) {
        this.pushService = pushService;
        this.configuration = configuration;
        this.testingService = testingService;
        this.testSuiteId = testSuiteId;
    }

    /**
     * Execute the test for the given browser configuration.
     *
     * @throws Throwable
     */
    @Override
    public void execute() throws Throwable {
        JsonObject test = testingService.getSubscription(testSuiteId, configuration);

        int testId = test.get("testId").getAsInt();

        Subscription subscription = new Gson().fromJson(test.get("subscription").getAsJsonObject(), Subscription.class);

        String message = "Hëllö, world!";
        Notification notification = new Notification(subscription, message);

        HttpResponse response = pushService.send(notification, closeableHttpAsyncClient);
        assertEquals(201, response.getStatusLine().getStatusCode());

        JsonArray messages = testingService.getNotificationStatus(testSuiteId, testId);
        assertEquals(1, messages.size());
        assertEquals(new JsonPrimitive(message), messages.get(0));
    }

    /**
     * The name used by JUnit to display the test.
     *
     * @return
     */
    public String getDisplayName() {
        return "Browser " + configuration.browser + ", version " + configuration.version + ", vapid " + configuration.isVapid();
    }
}
