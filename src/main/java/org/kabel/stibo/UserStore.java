package org.kabel.stibo;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * In memory store of User objects. New users gets added with a unique ID, and all
 * Users are ensured to have unique email addresses.
 * The store is backed by a ConcurrentHashMap so thread-safe (for now)
 */
@ApplicationScoped
public class UserStore {

  private final Map<Integer, User> id2users = new ConcurrentHashMap<>();
  private final Map<String, User> email2users = new ConcurrentHashMap<>();

  private final AtomicInteger idCounter = new AtomicInteger(0);

  /** Return new User if new user was created and inserted successfully otherwise null */
  public User put(String email, String firstName, String lastName,  String role) {
    // Only create and insert new User if email address is not already present
    // Note: computeIfAbsent may return existing user if present and new user if not

    int nextId = idCounter.incrementAndGet();
    User newUser = email2users.computeIfAbsent(email,
        email_ -> new User(nextId,
            email,
            firstName,
            lastName,
            role));

    // Detect if id of newUser actually matches the ezpected new id - otherwise
    // user with the specified email alredy exists
    if (email2users.get(email).id() != nextId)
      return null;

    // otherwise we actually got a new user
    id2users.put(newUser.id(), newUser);
    return newUser;
  }

  /** Return User for the specified id */
  public User forId(int id) {
    return id2users.get(id);
  }

  /** Return User for the specified email */
  public User forEmail(String email) {
    return email2users.get(email);
  }

  /** Return all Users in this UserStore */
  public Collection<User> all() {
    return id2users.values();
  }

}
