package com.example.application.data.service;

import com.example.application.data.entity.Alimento;
import com.example.application.data.entity.Contact;
import com.example.application.data.entity.Ingesta;
import com.example.application.data.repository.AlimentoRepository;
import com.example.application.data.repository.ContactRepository;
import com.example.application.data.repository.IngestaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class CrmService {

    private ContactRepository cR;
    private AlimentoRepository aR;
    private IngestaRepository iR;

    @Autowired
    public CrmService(ContactRepository cR, AlimentoRepository aR, IngestaRepository iR) {
        this.aR=aR;
        this.cR = cR;
        this.iR = iR;
    }

    public List<Alimento> findAllAlimentos() {
        return aR.findAll();
    }

    public List<Contact> findAllContacts(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return cR.findAll();
        } else {
            return cR.search(stringFilter);
        }
    }

    public Collection<Ingesta> findAllIngestas() {
        return iR.findAll();
    }

    public Alimento findAlimentoByNombre(String nombre) {
        return aR.findByNombre(nombre);
    }
}
