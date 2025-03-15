package app.bettermetesttask.datamovies.datasource

import app.bettermetesttask.datamovies.repository.stores.MoviesRestStore
import app.bettermetesttask.domaincore.utils.Result
import app.bettermetesttask.domainmovies.datasource.MoviesRemoteDataSource
import app.bettermetesttask.domainmovies.entries.Movie
import javax.inject.Inject

class MoviesRemoteDataSourceImpl @Inject constructor(
    private val restStore: MoviesRestStore
) : MoviesRemoteDataSource {
    override suspend fun fetchMovies(): Result<List<Movie>> {
        return Result.of { restStore.getMovies() }
    }
}