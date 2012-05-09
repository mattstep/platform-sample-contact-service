package org.mattstep.platform.samples.contact;

import com.google.common.collect.ImmutableSet;
import com.proofpoint.testing.Assertions;
import org.testng.annotations.Test;

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

        Assertions.assertEqualsIgnoreOrder(contactStore.getAllContactsForOwner(ownerId), contacts);
    }
}
