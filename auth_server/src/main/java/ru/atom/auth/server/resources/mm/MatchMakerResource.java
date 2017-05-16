package ru.atom.auth.server.resources.mm;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.atom.auth.server.base.Match;
import ru.atom.auth.server.base.PersonalResult;
import ru.atom.auth.server.dao.MatchDao;
import ru.atom.auth.server.dao.PersonalResultDao;
import ru.atom.auth.server.resources.Authorized;
import ru.atom.auth.server.service.MatchMakerException;
import ru.atom.auth.server.service.MatchMakerService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;


@Path("/")
public class MatchMakerResource {
    private static final Logger logger = LogManager.getLogger(MatchMakerResource.class);

    public static MatchMakerService matchMakerService = new MatchMakerService();

    @GET
    @Consumes("application/x-www-form-urlencoded")
    @Path("/join")
    @Produces("text/plain")
    public Response join(@QueryParam("name") String name, @QueryParam("token") String token) {
        String url = "wtfis.ru:8090/gs/";
        logger.info(name + " " + token);

        long gameSessionId = matchMakerService.join(name, token);
        if (gameSessionId == -1) {
            return Response.status(Response.Status.OK).entity("Please, wait :)").build();
        }


        return Response.status(Response.Status.OK).entity(url + gameSessionId).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/finish")
    @Produces("text/plain")
    public Response finish(String jsonString) {
        try {
            matchMakerService.finish(jsonString);
        } catch (RuntimeException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Неверный формат данных! " +
                    "Сохранение результатов невозможно!").build();
        }
        return Response.status(Response.Status.OK).entity("Игра успешно завершена!").build();
    }

    //TODO maybe do it post request
    @Authorized
    @GET
    @Consumes("application/x-www-form-urlencoded")
    @Path("/add-match")
    @Produces("text/plain")
    public Response addMatch() {
        Integer id = matchMakerService.saveMatch(new Match());
        if (id == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Can not get info about user match").build();
        }
        return Response.status(Response.Status.OK).entity(id).build();
    }

    @Authorized
    @POST
    @Consumes("application/x-www-form-urlencoded")
    @Path("/add-result")
    @Produces("text/plain")
    public Response addResult(@FormParam("match") Integer id,
                              @FormParam("name") String name,
                              @FormParam("result") Integer result) {
        if (name == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("name can not be null").build();
        }

        if (id == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("match can not be null").build();
        }

        if (result == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("result can not be null").build();
        }

        try {
            matchMakerService.addResult(id, name, result);
        } catch (MatchMakerException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Can not add info about user match").build();
        }
        return Response.status(Response.Status.OK).entity("").build();
    }

    @Authorized
    @GET
    @Consumes("application/x-www-form-urlencoded")
    @Path("/get-result")
    @Produces("text/plain")
    public Response getResult(@QueryParam("name") String name) {
        if (name == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("name can not be null").build();
        }
        List<PersonalResult> list = matchMakerService.getResults(name);
        if (list == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Can not get info about user match").build();
        }
        //TODO add show info
        return Response.status(Response.Status.OK).entity("Nice!").build();
    }

}
