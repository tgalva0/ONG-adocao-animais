package com.project.adocao.service;

import com.project.adocao.model.Animal;
import com.project.adocao.repository.AnimalDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnimalServiceTest {

    @Mock
    private AnimalDAO animalDAOMock;

    @InjectMocks
    private AnimalService animalService;

    private Animal animalValido;

    @BeforeEach
    void setUp() {
        animalValido = new Animal();
        animalValido.setIdAnimal(1);
        animalValido.setNomeAnimal("Rex");
    }

    @Test
    void deveCriarAnimalComSucesso() {
        animalService.criarAnimal(animalValido);
        verify(animalDAOMock, times(1)).salvar(animalValido);
    }

    @Test
    void naoDeveCriarAnimalComNomeVazio() {
        Animal animalInvalido = new Animal();
        animalInvalido.setNomeAnimal(""); // Nome inválido
        assertThrows(IllegalArgumentException.class, () -> {
            animalService.criarAnimal(animalInvalido);
        });
        verify(animalDAOMock, never()).salvar(any(Animal.class));
    }

    @Test
    void deveBuscarAnimalPorId() {
        when(animalDAOMock.buscarPorId(1)).thenReturn(Optional.of(animalValido));
        Optional<Animal> animalEncontrado = animalService.buscarAnimalPorId(1);
        assertTrue(animalEncontrado.isPresent());
        assertEquals("Rex", animalEncontrado.get().getNomeAnimal());
    }

    @Test
    void deveRetornarVazioSeAnimalNaoExiste() {
        when(animalDAOMock.buscarPorId(99)).thenReturn(Optional.empty());
        Optional<Animal> animalEncontrado = animalService.buscarAnimalPorId(99);
        assertFalse(animalEncontrado.isPresent());
    }
}