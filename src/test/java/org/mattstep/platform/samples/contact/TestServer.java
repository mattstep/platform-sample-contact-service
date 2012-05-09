package org.mattstep.platform.samples.contact;

import com.google.common.collect.ImmutableList;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.proofpoint.configuration.ConfigurationFactory;
import com.proofpoint.configuration.ConfigurationModule;
import com.proofpoint.jmx.JmxHttpModule;
import com.proofpoint.http.client.ApacheHttpClient;
import com.proofpoint.http.client.HttpClient;
import com.proofpoint.http.server.testing.TestingHttpServer;
import com.proofpoint.http.server.testing.TestingHttpServerModule;
import com.proofpoint.jaxrs.JaxrsModule;
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
import static com.proofpoint.http.client.Request.Builder.prepareGet;
import static com.proofpoint.json.JsonCodec.listJsonCodec;
import static javax.ws.rs.core.Response.Status.OK;

public class TestServer
{
    private HttpClient client;
    private TestingHttpServer server;

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
        List<String> contacts = client.execute(
                prepareGet().setUri(uriFor("/v1/contact/foo")).build(),
                createJsonResponseHandler(listJsonCodec(String.class), OK.getStatusCode()));

        Assertions.assertEqualsIgnoreOrder(ImmutableList.of("martint","electrum","mattstep","dphillips"), contacts);
    }

    private URI uriFor(String path)
    {
        return server.getBaseUrl().resolve(path);
    }
}
