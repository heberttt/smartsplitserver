package com.smartsplit.splitservice.Model;

import lombok.Data;

@Data
public class Friend {
    private String id; // leave null for guest, and use username instead
    private String username; // leave null for registered user, and use id instead
}
