package org.mattstep.platform.samples.contact;

import com.google.common.collect.ImmutableSet;
import org.mattstep.platform.samples.contact.ContactStore.RemovalStatus;
import org.testng.annotations.Test;

import static com.proofpoint.testing.Assertions.assertEqualsIgnoreOrder;
import static org.testng.Assert.assertEquals;

public class TestContactStore
{
    @Test
    public void testGetAllContactsByOwner()
    {
        ImmutableSet<String> contacts = ImmutableSet.of("martint", "electrum", "mattstep", "dphillips");
        String ownerId = "foo";

        ContactStore contactStore = new ContactStore();

        for(String contactId : contacts) {
            contactStore.addContact(ownerId, contactId);
        }

        assertEqualsIgnoreOrder(contactStore.getAllContactsForOwner(ownerId), contacts);
    }

    @Test
    public void testAddContact()
    {
        ContactStore contactStore = new ContactStore();

        contactStore.addContact("foo", "bar");

        assertEqualsIgnoreOrder(contactStore.getAllContactsForOwner("foo"), ImmutableSet.of("bar"));
    }

    @Test
    public void testRemoveContact()
    {
        ContactStore contactStore = new ContactStore();

        contactStore.addContact("foo", "bar");
        assertEqualsIgnoreOrder(contactStore.getAllContactsForOwner("foo"), ImmutableSet.of("bar"));

        assertEquals(contactStore.removeContact("foo", "bar"), RemovalStatus.REMOVED);
        assertEqualsIgnoreOrder(contactStore.getAllContactsForOwner("foo"), ImmutableSet.of());

        assertEquals(contactStore.removeContact("foo", "bar"), RemovalStatus.NOT_PRESENT);
    }
}
