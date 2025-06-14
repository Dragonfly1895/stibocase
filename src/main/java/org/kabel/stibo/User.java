package org.kabel.stibo;

/**
 * Record representing User including system generated unique id
 */
public record User(
    int id, // consider UUID

    String email,

    String firstName,

    String lastName,

    String role

) {}

