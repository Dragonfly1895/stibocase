package org.kabel.stibo;

import jakarta.inject.Inject;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.RestForm;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static java.lang.String.format;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

  // In-memory store of Users
  @Inject
  UserStore store;

  /**
   * Add new User - returns the created new User but only if the specified email
   * was not already present in the store.
   */
  @POST
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.APPLICATION_JSON)
  public Response addUser(@RestForm("email") @Email String email,
                          @RestForm("firstName") @NotBlank String firstName,
                          @RestForm("lastName") @NotBlank String lastName,
                          @RestForm("role") @NotBlank String role
  ) {
    User newUser = store.put(email, firstName, lastName, role);

    return (newUser == null)
        ? Response.status(Response.Status.NOT_FOUND)
        .entity(format("User with email '%s' already exists", email)).build()
        : Response.ok(newUser).build();
  }

  /**
   *  Return user for the specified id (path parameter)
   */
  @GET
  @Path("/{id}")
  public Response getUserForId(@PathParam("id") int id) {
    User user = store.forId(id);

    if (user == null) {
      return Response.status(Response.Status.NOT_FOUND)
          .entity(format("User not found for id: %s", id)).build();
    }
    return Response.ok(user).build();
  }

  /**
   * Returns all Users currently in the store or if the query parameter email is
   * specified then return the User with that email.
   * All results are returned as JSON formatted list of User objects.
   */
  @GET
  public Collection<User> getUsers(@QueryParam("email") String email) {
    if (email == null || email.isEmpty())
      return store.all();

    User user = store.forEmail(email);
    return (user == null) ? Collections.emptyList() : List.of(user);

  }
}
