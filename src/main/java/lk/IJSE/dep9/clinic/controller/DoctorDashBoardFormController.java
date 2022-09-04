package lk.IJSE.dep9.clinic.controller;

import lk.IJSE.dep9.clinic.security.SecurityContextHolder;

public class DoctorDashBoardFormController {
    public void initialize(){
        System.out.println(SecurityContextHolder.getPrinciple());
    }
}

