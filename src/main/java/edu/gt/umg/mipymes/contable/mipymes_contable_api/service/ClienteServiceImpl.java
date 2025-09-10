package edu.gt.umg.mipymes.contable.mipymes_contable_api.service;

import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;

@Service
public class ClienteServiceImpl implements ClienteService {

    @Override
    public List<String> obtenerClientes() {
        // Aquí en el futuro llamarás al Repository para traer datos de la BD
        return Arrays.asList("Cliente A", "Cliente B", "Cliente C");
    }
}
