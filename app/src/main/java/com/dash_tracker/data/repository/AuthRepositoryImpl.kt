package com.dash_tracker.data.repository

import com.dash_tracker.domain.model.Usuario
import com.dash_tracker.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import java.util.Date

class AuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<Usuario> {
        return try {
            // await() convierte la llamada de Firebase (Callbacks) a Corrutinas de Kotlin
            val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val user = authResult.user

            if (user != null) {
                Result.success(Usuario(
                    id = user.uid,
                    nombre = user.displayName ?: "Usuario",
                    email = user.email ?: "",
                    fotoPerfil = user.photoUrl?.toString(),
                    fechaRegistro = Date()
                ))
            } else {
                Result.failure(Exception("Error al obtener usuario de Firebase"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun registrar(nombre: String, email: String, password: String): Result<Usuario> {
        return try {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val user = authResult.user

            // TODO: Más adelante, aquí guardaremos el "nombre" en Firestore,
            // ya que Auth de Firebase solo pide email y password al crear.

            if (user != null) {
                Result.success(Usuario(
                    id = user.uid,
                    nombre = nombre,
                    email = email,
                    fechaRegistro = Date()
                ))
            } else {
                Result.failure(Exception("Error al crear cuenta"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getCurrentUser(): Usuario? {
        val user = firebaseAuth.currentUser ?: return null
        return Usuario(
            id = user.uid,
            nombre = user.displayName ?: "Usuario",
            email = user.email ?: "",
            fotoPerfil = user.photoUrl?.toString()
        )
    }

    override fun logout() {
        firebaseAuth.signOut()
    }
}