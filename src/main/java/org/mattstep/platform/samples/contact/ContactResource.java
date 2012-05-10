package org.mattstep.platform.samples.contact;

import com.google.common.base.Preconditions;
import org.mattstep.platform.samples.contact.ContactStore.RemovalStatus;

import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("/v1/contact/{ownerId: \\w+}")
public class ContactResource
{

    private final ContactStore contactStore;

    @Inject
    public ContactResource(ContactStore contactStore)
    {
        Preconditions.checkNotNull(contactStore);

        this.contactStore = contactStore;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllContacts(@PathParam("ownerId") String ownerId)
    {
        Preconditions.checkNotNull(ownerId);

        return Response.ok(contactStore.getAllContactsForOwner(ownerId)).build();
    }

    @Path("/{contactId: \\w+}")
    @PUT
    public Response putContact(@PathParam("ownerId") String ownerId, @PathParam("contactId") String contactId)
    {
        Preconditions.checkNotNull(ownerId);
        Preconditions.checkNotNull(contactId);

        contactStore.addContact(ownerId, contactId);

        return Response.noContent().build();
    }

    @Path("/{contactId: \\w+}")
    @DELETE
    public Response deleteContact(@PathParam("ownerId") String ownerId, @PathParam("contactId") String contactId)
    {
        Preconditions.checkNotNull(ownerId);
        Preconditions.checkNotNull(contactId);

        if(contactStore.removeContact(ownerId, contactId) == RemovalStatus.REMOVED) {
            return Response.noContent().build();
        }
        return Response.status(Status.NOT_FOUND).build();
    }
}
