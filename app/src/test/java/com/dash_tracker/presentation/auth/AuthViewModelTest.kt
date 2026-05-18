package com.dash_tracker.presentation.auth

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dash_tracker.domain.model.Usuario
import com.dash_tracker.domain.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {

    // Regla para que las tareas de arquitectura (como LiveData si hubiera) se ejecuten instantáneamente
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: AuthViewModel
    private val authRepository: AuthRepository = mockk()
    
    // Dispatcher de prueba para controlar el tiempo de las corrutinas
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        // Forzamos a que el Dispatcher Main use nuestro testDispatcher
        Dispatchers.setMain(testDispatcher)
        viewModel = AuthViewModel(authRepository)
    }

    @After
    fun tearDown() {
        // Limpiamos el Dispatcher Main
        Dispatchers.resetMain()
    }

    @Test
    fun `login con email y password exitoso actualiza el estado a success`() = runTest {
        // Given (Dado)
        val email = "usuario@test.com"
        val password = "password123"
        val usuarioMock = Usuario(id = "uid123", nombre = "Test User", email = email)
        
        // Mockeamos la respuesta del repositorio
        coEvery { authRepository.login(email, password) } returns Result.success(usuarioMock)

        // When (Cuando)
        viewModel.login(email, password)
        
        // Avanzamos hasta que todas las corrutinas terminen
        advanceUntilIdle()

        // Then (Entonces)
        assertEquals(false, viewModel.isLoading.value)
        assertEquals(null, viewModel.error.value)
        assertEquals(true, viewModel.isSuccess.value)
    }

    @Test
    fun `login con google exitoso actualiza el estado a success`() = runTest {
        // Given
        val idToken = "google-token-xyz"
        val usuarioMock = Usuario(id = "google123", nombre = "Google User", email = "google@test.com")
        
        coEvery { authRepository.loginConGoogle(idToken) } returns Result.success(usuarioMock)

        // When
        viewModel.loginGoogle(idToken)
        
        advanceUntilIdle()

        // Then
        assertEquals(false, viewModel.isLoading.value)
        assertEquals(null, viewModel.error.value)
        assertEquals(true, viewModel.isSuccess.value)
    }

    @Test
    fun `login fallido actualiza el estado de error`() = runTest {
        // Given
        val email = "error@test.com"
        val password = "wrong"
        val mensajeError = "Credenciales incorrectas"
        
        coEvery { authRepository.login(email, password) } returns Result.failure(Exception(mensajeError))

        // When
        viewModel.login(email, password)
        
        advanceUntilIdle()

        // Then
        assertEquals(false, viewModel.isLoading.value)
        assertEquals(mensajeError, viewModel.error.value)
        assertEquals(false, viewModel.isSuccess.value)
    }

    @Test
    fun `registro exitoso actualiza el estado a success`() = runTest {
        // Given
        val nombre = "Nuevo Usuario"
        val email = "nuevo@test.com"
        val password = "password123"
        val usuarioMock = Usuario(id = "uid456", nombre = nombre, email = email)
        
        coEvery { authRepository.registrar(nombre, email, password) } returns Result.success(usuarioMock)

        // When
        viewModel.register(nombre, email, password)
        
        advanceUntilIdle()

        // Then
        assertEquals(false, viewModel.isLoading.value)
        assertEquals(null, viewModel.error.value)
        assertEquals(true, viewModel.isSuccess.value)
    }

    @Test
    fun `registro fallido actualiza el estado de error`() = runTest {
        // Given
        val nombre = "Usuario Error"
        val email = "error@test.com"
        val password = "password123"
        val mensajeError = "El email ya está en uso"
        
        coEvery { authRepository.registrar(nombre, email, password) } returns Result.failure(Exception(mensajeError))

        // When
        viewModel.register(nombre, email, password)
        
        advanceUntilIdle()

        // Then
        assertEquals(false, viewModel.isLoading.value)
        assertEquals(mensajeError, viewModel.error.value)
        assertEquals(false, viewModel.isSuccess.value)
    }

    @Test
    fun `registro con campos vacios muestra error de validacion`() = runTest {
        // When
        viewModel.register("", "", "")
        
        // Then
        assertEquals("Todos los campos son obligatorios", viewModel.error.value)
        assertEquals(false, viewModel.isSuccess.value)
    }
}
