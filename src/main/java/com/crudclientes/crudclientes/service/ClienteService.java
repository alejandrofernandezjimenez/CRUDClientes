package com.crudclientes.crudclientes.service;

import com.crudclientes.crudclientes.entity.Cliente;
import com.crudclientes.crudclientes.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }

    public Optional<Cliente> obtenerClientePorId(Integer id) {
        return clienteRepository.findById(id);
    }

    public Cliente crearCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public Cliente actualizarCliente(Integer id, Cliente clienteActualizado) {
        Optional<Cliente> clienteExistente = clienteRepository.findById(id);
        if (clienteExistente.isPresent()) {
            Cliente cliente = clienteExistente.get();
            cliente.setNombre(clienteActualizado.getNombre());
            cliente.setEmail(clienteActualizado.getEmail());

            return clienteRepository.save(cliente);
        } else {
            throw new RuntimeException("Cliente no encontrado");
        }
    }

    public void eliminarCliente(Integer id) {
        clienteRepository.deleteById(id);
    }
}