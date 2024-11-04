package uk.ac.tees.mad.univid.authentication

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uk.ac.tees.mad.univid.authentication.repository.AuthRepository
import uk.ac.tees.mad.univid.authentication.repository.AuthRepositoryImple
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirestore() = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideRepository(auth: FirebaseAuth, firestore: FirebaseFirestore): AuthRepository{
        return AuthRepositoryImple(auth, firestore)
    }
}