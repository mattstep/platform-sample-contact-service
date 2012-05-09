package org.mattstep.platform.samples.contact;

import com.google.common.collect.ImmutableSet;
import com.proofpoint.testing.Assertions;
import org.testng.annotations.Test;

public class TestContactStore
{
    @Test
    public void testGetAllContactsByOwner()
    {
        ContactStore store = new ContactStore();

        Assertions.assertEqualsIgnoreOrder(store.getAllContactsForOwner("foo"), ImmutableSet.of("martint", "electrum", "mattstep", "dphillips"));
    }
}
