package me.rickychon.neo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Date;

@Path("/api/v1")
public class Routes {
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String date() {
        return new Date().toString();
    }

    @Path("/news")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getNews() throws IOException {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("https://api.spaceflightnewsapi.net/v3/articles?_limit=3");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        JsonNode json = new ObjectMapper().readTree(response.readEntity(String.class));
        ObjectNode node = new ObjectMapper().createObjectNode();
        node.set("news", json);
        return Response.status(Response.Status.OK).entity(node).build();
    }

    @Path("/browse")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getNeos() {
        return null;
    }
}
