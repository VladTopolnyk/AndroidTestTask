package app.bettermetesttask.datamovies.datasource

import app.bettermetesttask.datamovies.repository.stores.MoviesLocalStore
import app.bettermetesttask.datamovies.repository.stores.MoviesMapper
import app.bettermetesttask.domaincore.utils.Result
import app.bettermetesttask.domainmovies.datasource.MoviesLocalDataSource
import app.bettermetesttask.domainmovies.entries.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MoviesLocalDataSourceImpl @Inject constructor(
    private val localStore: MoviesLocalStore,
    private val mapper: MoviesMapper
) : MoviesLocalDataSource {
    override suspend fun storeMovies(movies: List<Movie>) = withContext(Dispatchers.IO) {
        localStore.storeMovies(movies.map { mapper.mapToLocal(it) })
    }

    override suspend fun getMovies(): Result<List<Movie>> {
        return Result.of { localStore.getMovies().map { mapper.mapFromLocal(it) } }
    }

    override suspend fun getMovie(id: Int): Result<Movie> {
        return Result.of { mapper.mapFromLocal(localStore.getMovie(id)) }
    }

    override fun observeLikedMovieIds(): Flow<List<Int>> {
        return localStore.observeLikedMoviesIds()
    }

    override suspend fun addMovieToFavorites(movieId: Int) {
        localStore.likeMovie(movieId)
    }

    override suspend fun removeMovieFromFavorites(movieId: Int) {
        localStore.dislikeMovie(movieId)
    }

    override suspend fun likeMovie(id: Int) {
        localStore.likeMovie(id)
    }

    override suspend fun dislikeMovie(id: Int) {
        localStore.dislikeMovie(id)
    }
}