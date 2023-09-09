package com.zilch.interview;

import com.zilch.interview.model.Block;
import lombok.extern.java.Log;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Log()
public class BlockHashCalculator {
    public static String calculate(Block block) {
        String dataToHash = block.getPreviousHash()
                + block.getTimeStamp()
                + block.getNonce()
                + block.getData();
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(dataToHash.getBytes(StandardCharsets.UTF_8));
            StringBuilder buffer = new StringBuilder();
            for (byte b : bytes) {
                buffer.append(String.format("%02x", b));
            }
            return buffer.toString();
        } catch (NoSuchAlgorithmException ex) {
            log.severe(ex.getMessage());
            throw new RuntimeException("Failed to calculate hash.");
        }
    }
}
