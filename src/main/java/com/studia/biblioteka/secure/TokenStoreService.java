package com.studia.biblioteka.secure;

import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenStoreService {
    private final ConcurrentHashMap<String, Date> validTokens = new ConcurrentHashMap<>();

    public void storeToken(String token, Date expiryDate) {
        validTokens.put(token, expiryDate);
    }

    public boolean isTokenValid(String token) {
        Date expiryDate = validTokens.get(token);

        if (expiryDate == null) {
            return false;
        }
        Date now = new Date();
        return expiryDate.after(now);
    }

    public void invalidateToken(String token) {
        validTokens.remove(token);
    }
}
