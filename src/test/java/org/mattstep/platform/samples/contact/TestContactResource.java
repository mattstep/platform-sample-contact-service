package org.mattstep.platform.samples.contact;

import com.google.common.collect.ImmutableSet;
import org.testng.annotations.Test;

import java.util.Set;

import static com.proofpoint.testing.Assertions.assertEqualsIgnoreOrder;
import static javax.ws.rs.core.Response.Status.OK;
import static org.testng.Assert.assertEquals;

public class TestContactResource
{

    @Test
    public void testGetAllContacts()
    {
        ContactResource resource = new ContactResource(new ContactStore());

        assertEqualsIgnoreOrder((Set<String>) resource.getAllContacts("foo").getEntity(), ImmutableSet.of("martint", "electrum", "mattstep", "dphillips"));
        assertEquals(resource.getAllContacts("foo").getStatus(), OK.getStatusCode());
    }
}
