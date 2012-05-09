package org.mattstep.platform.samples.contact;

import com.google.common.collect.ImmutableList;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/v1/contact/{ownerId: \\S+}")
public class ContactResource
{

    private static final List CONTACTS = ImmutableList.of("martint","electrum","mattstep","dphillips");

    @Inject
    public ContactResource()
    {
    }

    @GET
    public Response getAllContacts()
    {
        return Response.ok(CONTACTS).build();
    }
}
