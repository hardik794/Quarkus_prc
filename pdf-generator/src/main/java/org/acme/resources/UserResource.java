package org.acme.resources;

import org.acme.models.User;
import org.acme.repositories.UserRepository;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Path("/user")
public class UserResource {

    @Inject
    UserRepository userRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserList() {
        List<User> userList = userRepository.listAll();
        return Response.ok(userList).build();
    }

    @POST
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addUser(User user) {
        Optional<User> findUser = userRepository.listAll().stream().filter(checkUser -> checkUser.getMobile_no().equals(user.getMobile_no())).findFirst();
        if (findUser.isPresent()){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }else {
            userRepository.persist(user);
            if (userRepository.isPersistent(user)){
                return Response.ok(user).build();
            }else {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
        }
    }

    @PUT
    @Transactional
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateUser(@PathParam("id") Long id, User newUser) {
        Optional<User> user = userRepository.findByIdOptional(id);
        if (user.isPresent()){
            Optional<User> findUser = userRepository.listAll().stream().filter(checkUser -> checkUser.getMobile_no().equals(newUser.getMobile_no())).findFirst();
            if (findUser.isPresent()){
                return Response.status(Response.Status.BAD_REQUEST).build();
            }else {
                User dbUser = user.get();
                if (Objects.nonNull(newUser.getName())) {
                    dbUser.setName(newUser.getName());
                }
                if (Objects.nonNull(newUser.getMobile_no())) {
                    dbUser.setMobile_no(newUser.getMobile_no());
                }
                userRepository.persist(dbUser);
                return Response.ok(dbUser).build();

//                if (userRepository.isPersistent(dbUser)) {
//                    return Response.ok(dbUser).build();
//                } else {
//                    return Response.status(Response.Status.BAD_REQUEST).build();
//                }
            }
        }else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @DELETE
    @Transactional
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUser(@PathParam("id") Long id) {
//        Optional<User> userToDelete = userList.stream().filter(user -> user.getId() == id).findFirst();
        boolean isDeleted = userRepository.deleteById(id);
        if (isDeleted){
            return Response.noContent().build();
        }else{
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }    
    
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserById(@PathParam("id") Long id) {
        User user = userRepository.findById(id);
        return Response.ok(user).build();

    }
}