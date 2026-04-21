package com.dash_tracker.di

import android.content.Context
import androidx.room.Room
import com.dash_tracker.data.local.DashTrackerDatabase
import com.dash_tracker.data.local.dao.HabitoDao
import com.dash_tracker.data.local.dao.RegistroHabitoDao
import com.dash_tracker.data.repository.AuthRepositoryImpl
import com.dash_tracker.data.repository.HabitoRepositoryImpl
import com.dash_tracker.domain.repository.AuthRepository
import com.dash_tracker.domain.repository.HabitoRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // Estos objetos vivirán mientras la app esté abierta
object AppModule {

    // --- 1. PROVEER FIREBASE ---
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    // --- 2. PROVEER BASE DE DATOS LOCAL (ROOM) ---
    @Provides
    @Singleton
    fun provideDashTrackerDatabase(@ApplicationContext context: Context): DashTrackerDatabase {
        return Room.databaseBuilder(
            context,
            DashTrackerDatabase::class.java,
            "dash_tracker_db"
        ).build()
    }

    // --- 3. PROVEER DAOs ---
    @Provides
    @Singleton
    fun provideHabitoDao(db: DashTrackerDatabase): HabitoDao {
        return db.habitoDao
    }

    @Provides
    @Singleton
    fun provideRegistroHabitoDao(db: DashTrackerDatabase): RegistroHabitoDao {
        return db.registroHabitoDao
    }

    // --- 4. PROVEER REPOSITORIOS ---
    @Provides
    @Singleton
    fun provideAuthRepository(firebaseAuth: FirebaseAuth): AuthRepository {
        return AuthRepositoryImpl(firebaseAuth)
    }

    @Provides
    @Singleton
    fun provideHabitoRepository(habitoDao: HabitoDao): HabitoRepository {
        return HabitoRepositoryImpl(habitoDao)
    }
}