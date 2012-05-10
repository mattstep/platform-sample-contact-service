package org.mattstep.platform.samples.contact;

import com.google.common.collect.ImmutableSet;
import org.testng.annotations.Test;

import javax.ws.rs.core.Response;
import java.util.Set;

import static com.proofpoint.testing.Assertions.assertEqualsIgnoreOrder;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static javax.ws.rs.core.Response.Status.OK;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

public class TestContactResource
{

    @Test
    public void testGetAllContacts()
    {
        ImmutableSet<String> contacts = ImmutableSet.of("martint", "electrum", "mattstep", "dphillips");
        String ownerId = "foo";

        ContactStore contactStore = new ContactStore();

        for(String contactId : contacts) {
            contactStore.addContact(ownerId, contactId);
        }

        ContactResource resource = new ContactResource(contactStore);

        assertEqualsIgnoreOrder((Set<String>) resource.getAllContacts(ownerId).getEntity(), contacts);
        assertEquals(resource.getAllContacts(ownerId).getStatus(), OK.getStatusCode());
    }

    @Test
    public void testPutContact()
    {
        ContactStore contactStore = new ContactStore();
        ContactResource resource = new ContactResource(contactStore);

        Response response = resource.putContact("foo", "bar");

        assertEqualsIgnoreOrder(contactStore.getAllContactsForOwner("foo"), ImmutableSet.of("bar"));
        assertEquals(response.getStatus(), NO_CONTENT.getStatusCode());
        assertNull(response.getEntity());
    }

    @Test
    public void testDeleteContact()
    {
        ContactStore contactStore = new ContactStore();
        ContactResource resource = new ContactResource(contactStore);

        contactStore.addContact("foo", "bar");
        Response response = resource.deleteContact("foo", "bar");

        assertEqualsIgnoreOrder(contactStore.getAllContactsForOwner("foo"), ImmutableSet.of());
        assertEquals(response.getStatus(), NO_CONTENT.getStatusCode());
        assertNull(response.getEntity());
    }

    @Test
    public void testDeleteContactMissing()
    {
        ContactStore contactStore = new ContactStore();
        ContactResource resource = new ContactResource(contactStore);

        Response response = resource.deleteContact("foo", "bar");

        assertEqualsIgnoreOrder(contactStore.getAllContactsForOwner("foo"), ImmutableSet.of());
        assertEquals(response.getStatus(), NOT_FOUND.getStatusCode());
        assertNull(response.getEntity());
    }
}
