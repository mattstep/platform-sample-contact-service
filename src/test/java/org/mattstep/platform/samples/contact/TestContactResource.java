package org.mattstep.platform.samples.contact;

import com.google.common.collect.ImmutableList;
import com.proofpoint.testing.Assertions;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.ws.rs.core.Response;
import java.util.List;

import static com.proofpoint.testing.Assertions.assertEqualsIgnoreOrder;
import static javax.ws.rs.core.Response.Status.OK;
import static org.testng.Assert.assertEquals;

public class TestContactResource
{

    @Test
    public void testGetAllContacts()
    {
        ContactResource resource = new ContactResource();

        assertEqualsIgnoreOrder((List<String>) resource.getAllContacts().getEntity(), ImmutableList.of("martint", "electrum", "mattstep", "dphillips"));
        assertEquals(resource.getAllContacts().getStatus(), OK.getStatusCode());
    }
}
