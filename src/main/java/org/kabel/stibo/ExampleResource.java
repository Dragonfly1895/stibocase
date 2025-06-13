package org.kabel.stibo;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/hello")
public class ExampleResource {

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public String hello() {
    return "Hello RESTEasy2";
  }
}

/*
Successfully created project 'stibocase' on GitHub, but initial commit failed:

Author identity unknown *** Please tell me who you are.
Run
git config --global user.email "you@example.com"
git config --global user.name "Your Name" to set your account's default identity.
Omit --global to set the identity only in this repository. unable to auto-detect email address (got 'Torben@LUZERN.(none)')


 */