package org.mattstep.platform.samples.contact;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.proofpoint.configuration.ConfigurationFactory;
import com.proofpoint.configuration.ConfigurationModule;
import com.proofpoint.http.client.ApacheHttpClient;
import com.proofpoint.http.client.HttpClient;
import com.proofpoint.http.client.StatusResponseHandler.StatusResponse;
import com.proofpoint.http.server.testing.TestingHttpServer;
import com.proofpoint.http.server.testing.TestingHttpServerModule;
import com.proofpoint.jaxrs.JaxrsModule;
import com.proofpoint.jmx.JmxHttpModule;
import com.proofpoint.jmx.JmxModule;
import com.proofpoint.json.JsonModule;
import com.proofpoint.node.testing.TestingNodeModule;
import com.proofpoint.testing.Assertions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.net.URI;
import java.util.Collections;
import java.util.List;

import static com.proofpoint.http.client.JsonResponseHandler.createJsonResponseHandler;
import static com.proofpoint.http.client.Request.Builder.prepareDelete;
import static com.proofpoint.http.client.Request.Builder.prepareGet;
import static com.proofpoint.http.client.Request.Builder.preparePut;
import static com.proofpoint.http.client.StatusResponseHandler.createStatusResponseHandler;
import static com.proofpoint.http.client.StringResponseHandler.createStringResponseHandler;
import static com.proofpoint.json.JsonCodec.listJsonCodec;
import static com.proofpoint.testing.Assertions.assertEqualsIgnoreOrder;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static javax.ws.rs.core.Response.Status.OK;
import static org.testng.Assert.assertEquals;

public class TestServer
{
    private HttpClient client;
    private TestingHttpServer server;
    private ContactStore contactStore;

    @BeforeMethod
    public void setup()
            throws Exception
    {
        // TODO: wrap all this stuff in a TestBootstrap class
        Injector injector = Guice.createInjector(
                new TestingNodeModule(),
                new TestingHttpServerModule(),
                new JsonModule(),
                new JaxrsModule(),
                new JmxHttpModule(),
                new JmxModule(),
                new MainModule(),
                new ConfigurationModule(new ConfigurationFactory(Collections.<String, String>emptyMap())));

        server = injector.getInstance(TestingHttpServer.class);

        contactStore = injector.getInstance(ContactStore.class);

        server.start();
        client = new ApacheHttpClient();
    }

    @AfterMethod
    public void teardown()
            throws Exception
    {
        if (server != null) {
            server.stop();
        }
    }

    @Test
    public void testGetAllContacts()
            throws Exception
    {
        ImmutableSet<String> contacts = ImmutableSet.of("martint", "electrum", "mattstep", "dphillips");
        String ownerId = "foo";

        for(String contactId : contacts) {
            contactStore.addContact(ownerId, contactId);
        }

        List<String> actualContacts = client.execute(
                prepareGet().setUri(uriFor("/v1/contact/" + ownerId)).build(),
                createJsonResponseHandler(listJsonCodec(String.class), OK.getStatusCode()));

        Assertions.assertEqualsIgnoreOrder(contacts, actualContacts);
    }

    @Test
    public void testGetAllContactsEmptyResults()
            throws Exception
    {
        List<String> actualContacts = client.execute(
                prepareGet().setUri(uriFor("/v1/contact/foo")).build(),
                createJsonResponseHandler(listJsonCodec(String.class), OK.getStatusCode()));

        Assertions.assertEqualsIgnoreOrder(ImmutableSet.of(), actualContacts);
    }

    @Test
    public void testPutContact()
    {
        StatusResponse putResponse = client.execute(
                preparePut().setUri(uriFor("/v1/contact/foo/bar")).build(),
                createStatusResponseHandler());

        assertEqualsIgnoreOrder(contactStore.getAllContactsForOwner("foo"), ImmutableSet.of("bar"));
        assertEquals(putResponse.getStatusCode(), NO_CONTENT.getStatusCode());
    }

    @Test
    public void testDeleteContact()
    {
        contactStore.addContact("foo", "bar");

        StatusResponse deleteResponse = client.execute(
                prepareDelete().setUri(uriFor("/v1/contact/foo/bar")).build(),
                createStatusResponseHandler());

        assertEqualsIgnoreOrder(contactStore.getAllContactsForOwner("foo"), ImmutableSet.of());
        assertEquals(deleteResponse.getStatusCode(), NO_CONTENT.getStatusCode());

        deleteResponse = client.execute(
                prepareDelete().setUri(uriFor("/v1/contact/foo/bar")).build(),
                createStatusResponseHandler());

        assertEqualsIgnoreOrder(contactStore.getAllContactsForOwner("foo"), ImmutableSet.of());
        assertEquals(deleteResponse.getStatusCode(), NOT_FOUND.getStatusCode());
    }


    @Test
    public void testGetContact()
    {
        StatusResponse getResponse = client.execute(
                prepareGet().setUri(uriFor("/v1/contact/foo/bar")).build(),
                createStatusResponseHandler());
        assertEquals(getResponse.getStatusCode(), NOT_FOUND.getStatusCode());

        contactStore.addContact("foo", "bar");

        getResponse = client.execute(
                prepareGet().setUri(uriFor("/v1/contact/foo/bar")).build(),
                createStatusResponseHandler());
        assertEquals(getResponse.getStatusCode(), NO_CONTENT.getStatusCode());
    }

    private URI uriFor(String path)
    {
        return server.getBaseUrl().resolve(path);
    }
}
