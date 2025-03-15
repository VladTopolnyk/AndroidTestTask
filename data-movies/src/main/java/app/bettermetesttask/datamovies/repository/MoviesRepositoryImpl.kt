package app.bettermetesttask.datamovies.repository

import app.bettermetesttask.domainmovies.datasource.MoviesLocalDataSource
import app.bettermetesttask.domainmovies.datasource.MoviesRemoteDataSource
import app.bettermetesttask.domaincore.utils.Result
import app.bettermetesttask.domainmovies.entries.Movie
import app.bettermetesttask.domainmovies.repository.MoviesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
    private val localDataSource: MoviesLocalDataSource,
    private val remoteDataSource: MoviesRemoteDataSource
) : MoviesRepository {

    override suspend fun getMovies(): Result<List<Movie>> {
        val moviesFromRemote = remoteDataSource.fetchMovies()
        val movies = if (moviesFromRemote is Result.Success) {
            storeMoviesToLocal(moviesFromRemote.data)
            moviesFromRemote
        } else {
            localDataSource.getMovies()
        }
        return movies
    }

    private suspend fun storeMoviesToLocal(movies: List<Movie>){
        localDataSource.storeMovies(movies)
    }

    override suspend fun getMovie(id: Int): Result<Movie> {
        return localDataSource.getMovie(id)
    }

    override fun observeLikedMovieIds(): Flow<List<Int>> {
        return localDataSource.observeLikedMovieIds()
    }

    override suspend fun addMovieToFavorites(movieId: Int) {
        localDataSource.likeMovie(movieId)
    }

    override suspend fun removeMovieFromFavorites(movieId: Int) {
        localDataSource.dislikeMovie(movieId)
    }
}