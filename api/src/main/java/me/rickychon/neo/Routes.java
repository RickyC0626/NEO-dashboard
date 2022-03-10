package me.rickychon.neo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Path("/api/v1")
@Component
public class Routes {
    @Value("${neo.NASA_API_KEY}")
    private String apiKey;

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
    public Response getNeos(
        @Context HttpServletRequest req,
        @DefaultValue("0") @QueryParam("page") int page,
        @DefaultValue("20") @QueryParam("size") int size
    ) throws JsonProcessingException {
        HttpSession session = req.getSession();
        Map<String, JsonNode> neodata = (Map<String, JsonNode>) session.getAttribute("neodata");

        if(neodata == null) neodata = new HashMap<>();
        // Refresh cache every hour
        session.setMaxInactiveInterval(60 * 60);

        // Get cached page or fetch new page
        if(!neodata.containsKey(String.valueOf(page))) {
            Client client = ClientBuilder.newClient();
            WebTarget target = client.target("https://api.nasa.gov/neo/rest/v1/neo/browse")
                .queryParam("api_key", apiKey)
                .queryParam("page", page)
                .queryParam("size", size);
            Response response = target.request(MediaType.APPLICATION_JSON).get();
            JsonNode json = new ObjectMapper().readTree(response.readEntity(String.class));
            if(response.getStatus() != 200) return Response.status(response.getStatus()).entity(json).build();
            neodata.put(String.valueOf(page), json);
        }

        // Remove links that show api key
        JsonNode data = neodata.get(String.valueOf(page));
        ((ObjectNode) data).remove("links");

        JsonNode neo = data.get("near_earth_objects");
        for(JsonNode node : neo)
            ((ObjectNode) node).remove("links");

        session.setAttribute("neodata", neodata);
        return Response.status(Response.Status.OK).entity(data).build();
    }
}
