package com.bussinesslogic;

import com.interfaces.PatientInterface;
import com.model.Patient;

import javax.ejb.LocalBean;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.enterprise.context.SessionScoped;
import java.util.ArrayList;
import java.util.List;

@Stateless
@LocalBean
@Remote(PatientInterface.class)
public class PatientInterfaceImpl implements PatientInterface {


    @Override
    public List<Patient> findAllPatients() {
        List<Patient> patients = new ArrayList<>();
        Patient patient = new Patient();
        patient.setId(1);
        patient.setName("eason");
        patient.setAge(27);

        patients.add(patient);
        return patients;
    }
}
