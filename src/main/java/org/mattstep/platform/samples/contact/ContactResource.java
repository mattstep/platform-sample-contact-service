package org.mattstep.platform.samples.contact;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("/v1/contact/{ownerId: \\S+}")
public class ContactResource
{

    private final ContactStore contactStore;

    @Inject
    public ContactResource(ContactStore contactStore)
    {
        this.contactStore = contactStore;
    }

    @GET
    public Response getAllContacts(@PathParam("ownerId") String ownerId)
    {
        return Response.ok(contactStore.getAllContactsForOwner(ownerId)).build();
    }
}
