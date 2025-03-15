package app.bettermetesttask.domainmovies.datasource

import app.bettermetesttask.domaincore.utils.Result
import app.bettermetesttask.domainmovies.entries.Movie
import kotlinx.coroutines.flow.Flow

interface MoviesLocalDataSource {
    suspend fun storeMovies(movies: List<Movie>)

    suspend fun getMovies(): Result<List<Movie>>

    suspend fun getMovie(id: Int): Result<Movie>

    fun observeLikedMovieIds(): Flow<List<Int>>

    suspend fun addMovieToFavorites(movieId: Int)

    suspend fun removeMovieFromFavorites(movieId: Int)

    suspend fun likeMovie(id: Int)

    suspend fun dislikeMovie(id: Int)
}
