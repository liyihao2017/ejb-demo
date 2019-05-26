package com.client;

import com.interfaces.PatientInterface;
import com.model.Patient;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import java.util.List;

@Stateless
@LocalBean
@Named
public class LocalTestClient{

    @EJB(lookup = "java:global/ejb-demo-1.0-SNAPSHOT/PatientInterfaceImpl!com.interfaces.PatientInterface")
    private PatientInterface patientInterfaceImpl;

    public List<Patient> getPatients() {
       return patientInterfaceImpl.findAllPatients();
    }

}
