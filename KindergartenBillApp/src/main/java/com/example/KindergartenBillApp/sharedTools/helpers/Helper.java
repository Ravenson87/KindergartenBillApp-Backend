package com.example.KindergartenBillApp.sharedTools.helpers;


import jakarta.validation.constraints.NotBlank;


public class Helper {

    /**
     * Check if given email match email format or not
     * @param email String
     * @return boolean
     */
    public static boolean emailCheck(@NotBlank(message = "email can not be empty or null") String email) {
            String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(emailRegex);
    }

}
