package com.crudclientes.crudclientes.controller;

import com.crudclientes.crudclientes.entity.Cliente;
import com.crudclientes.crudclientes.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClienteControllerTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteController clienteController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testObtenerTodos() {
        Cliente c1 = new Cliente(1, "Juan", "juan@email.com");
        Cliente c2 = new Cliente(2, "Ana", "ana@email.com");

        when(clienteRepository.findAll()).thenReturn(Arrays.asList(c1, c2));

        List<Cliente> resultado = clienteController.obtenerTodos();

        assertEquals(2, resultado.size());
        verify(clienteRepository, times(1)).findAll();
    }

    @Test
    void testObtenerPorId_existente() {
        Cliente cliente = new Cliente(1, "Pepe", "pepe@email.com");
        when(clienteRepository.findById(1)).thenReturn(Optional.of(cliente));

        ResponseEntity<Cliente> response = clienteController.obtenerPorId(1);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Pepe", response.getBody().getNombre());
    }

    @Test
    void testObtenerPorId_inexistente() {
        when(clienteRepository.findById(99)).thenReturn(Optional.empty());

        ResponseEntity<Cliente> response = clienteController.obtenerPorId(99);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testCrearCliente() {
        Cliente nuevo = new Cliente(null, "Nuevo", "nuevo@email.com");
        Cliente guardado = new Cliente(1, "Nuevo", "nuevo@email.com");

        when(clienteRepository.save(nuevo)).thenReturn(guardado);

        Cliente resultado = clienteController.crearCliente(nuevo);

        assertNotNull(resultado);
        assertEquals("Nuevo", resultado.getNombre());
        verify(clienteRepository).save(nuevo);
    }

    @Test
    void testActualizarCliente_existente() {
        Cliente existente = new Cliente(1, "Viejo", "viejo@email.com");
        Cliente detalles = new Cliente(null, "Actualizado", "nuevo@email.com");

        when(clienteRepository.findById(1)).thenReturn(Optional.of(existente));
        when(clienteRepository.save(any(Cliente.class))).thenAnswer(inv -> inv.getArgument(0));

        ResponseEntity<Cliente> response = clienteController.actualizarCliente(1, detalles);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Actualizado", response.getBody().getNombre());
        assertEquals("nuevo@email.com", response.getBody().getEmail());
    }

    @Test
    void testActualizarCliente_inexistente() {
        Cliente detalles = new Cliente(null, "Nada", "nada@email.com");

        when(clienteRepository.findById(99)).thenReturn(Optional.empty());

        ResponseEntity<Cliente> response = clienteController.actualizarCliente(99, detalles);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testEliminarCliente_existente() {
        when(clienteRepository.existsById(1)).thenReturn(true);

        ResponseEntity<Void> response = clienteController.eliminarCliente(1);

        assertEquals(204, response.getStatusCodeValue());
        verify(clienteRepository).deleteById(1);
    }

    @Test
    void testEliminarCliente_inexistente() {
        when(clienteRepository.existsById(99)).thenReturn(false);

        ResponseEntity<Void> response = clienteController.eliminarCliente(99);

        assertEquals(404, response.getStatusCodeValue());
        verify(clienteRepository, never()).deleteById(any());
    }
}
