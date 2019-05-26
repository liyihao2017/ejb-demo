package com.interfaces;

import com.model.Patient;

import javax.ejb.Remote;
import java.util.List;

@Remote
public interface PatientInterface {
    public List<Patient> findAllPatients();
}
