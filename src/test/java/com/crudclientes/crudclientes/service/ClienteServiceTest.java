package com.crudclientes.crudclientes.service;

import com.crudclientes.crudclientes.entity.Cliente;
import com.crudclientes.crudclientes.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListarClientes() {
        Cliente c1 = new Cliente(1, "Lola", "lola@mail.com");
        Cliente c2 = new Cliente(2, "Paco", "paco@mail.com");

        when(clienteRepository.findAll()).thenReturn(Arrays.asList(c1, c2));

        List<Cliente> resultado = clienteService.listarClientes();

        assertEquals(2, resultado.size());
        verify(clienteRepository, times(1)).findAll();
    }

    @Test
    void testObtenerClientePorId_existente() {
        Cliente cliente = new Cliente(1, "Manolo", "manolo@mail.com");

        when(clienteRepository.findById(1)).thenReturn(Optional.of(cliente));

        Optional<Cliente> resultado = clienteService.obtenerClientePorId(1);

        assertTrue(resultado.isPresent());
        assertEquals("Manolo", resultado.get().getNombre());
    }

    @Test
    void testObtenerClientePorId_inexistente() {
        when(clienteRepository.findById(99)).thenReturn(Optional.empty());

        Optional<Cliente> resultado = clienteService.obtenerClientePorId(99);

        assertFalse(resultado.isPresent());
    }

    @Test
    void testCrearCliente() {
        Cliente nuevo = new Cliente(null, "Nuevo", "nuevo@mail.com");
        Cliente guardado = new Cliente(1, "Nuevo", "nuevo@mail.com");

        when(clienteRepository.save(nuevo)).thenReturn(guardado);

        Cliente resultado = clienteService.crearCliente(nuevo);

        assertEquals(1, resultado.getId());
        assertEquals("Nuevo", resultado.getNombre());
        verify(clienteRepository).save(nuevo);
    }

    @Test
    void testActualizarCliente_existente() {
        Cliente existente = new Cliente(1, "Viejo", "viejo@mail.com");
        Cliente actualizado = new Cliente(null, "NuevoNombre", "nuevo@mail.com");

        when(clienteRepository.findById(1)).thenReturn(Optional.of(existente));
        when(clienteRepository.save(any(Cliente.class))).thenAnswer(inv -> inv.getArgument(0));

        Cliente resultado = clienteService.actualizarCliente(1, actualizado);

        assertEquals("NuevoNombre", resultado.getNombre());
        assertEquals("nuevo@mail.com", resultado.getEmail());
    }

    @Test
    void testActualizarCliente_inexistente() {
        Cliente actualizado = new Cliente(null, "Nada", "nada@mail.com");

        when(clienteRepository.findById(999)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                clienteService.actualizarCliente(999, actualizado)
        );

        assertEquals("Cliente no encontrado", ex.getMessage());
    }

    @Test
    void testEliminarCliente() {
        doNothing().when(clienteRepository).deleteById(1);

        clienteService.eliminarCliente(1);

        verify(clienteRepository).deleteById(1);
    }
}
