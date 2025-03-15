package app.bettermetesttask.domainmovies.datasource

import app.bettermetesttask.domaincore.utils.Result
import app.bettermetesttask.domainmovies.entries.Movie

interface MoviesRemoteDataSource {
    suspend fun fetchMovies(): Result<List<Movie>>
}