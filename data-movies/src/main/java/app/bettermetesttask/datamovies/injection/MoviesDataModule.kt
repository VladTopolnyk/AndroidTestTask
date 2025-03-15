package app.bettermetesttask.datamovies.injection

import android.content.Context
import androidx.room.Room
import app.bettermetesttask.datamovies.database.DB_NAME
import app.bettermetesttask.datamovies.database.MoviesDatabase
import app.bettermetesttask.datamovies.datasource.MoviesLocalDataSourceImpl
import app.bettermetesttask.datamovies.datasource.MoviesRemoteDataSourceImpl
import app.bettermetesttask.datamovies.repository.MoviesRepositoryImpl
import app.bettermetesttask.datamovies.repository.stores.MoviesRestStore
import app.bettermetesttask.domainmovies.datasource.MoviesLocalDataSource
import app.bettermetesttask.domainmovies.datasource.MoviesRemoteDataSource
import app.bettermetesttask.domainmovies.repository.MoviesRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class MoviesDataModule {

    companion object {
        @Provides
        @Singleton
        fun provideDatabase(context: Context): MoviesDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                MoviesDatabase::class.java,
                DB_NAME
            )
                .build()
        }

        @Provides
        @Singleton
        fun provideMoviesRemoteDataSource(): MoviesRemoteDataSource {
            return MoviesRemoteDataSourceImpl(MoviesRestStore())
        }
    }

    @Binds
    abstract fun bindMoviesRepository(repositoryImpl: MoviesRepositoryImpl): MoviesRepository

    @Binds
    abstract fun bindMoviesLocalDataSource(moviesLocalDataSourceImpl: MoviesLocalDataSourceImpl): MoviesLocalDataSource

}