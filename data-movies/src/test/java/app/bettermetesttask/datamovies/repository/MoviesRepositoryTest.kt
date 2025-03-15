package app.bettermetesttask.datamovies.repository

import app.bettermetesttask.domaincore.utils.Result
import app.bettermetesttask.domainmovies.datasource.MoviesLocalDataSource
import app.bettermetesttask.domainmovies.datasource.MoviesRemoteDataSource
import app.bettermetesttask.domainmovies.entries.Movie
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever

@ExtendWith(MockitoExtension::class)
internal class MoviesRepositoryTest {

    private lateinit var repository: MoviesRepositoryImpl
    private lateinit var localDataSource: MoviesLocalDataSource
    private lateinit var remoteDataSource: MoviesRemoteDataSource

    @BeforeEach
    fun setUp() {
        localDataSource = mock()
        remoteDataSource = mock()
        repository = MoviesRepositoryImpl(localDataSource, remoteDataSource)
    }

    @Test
    fun `getMovies() should return movies from remote when fetch is successful`() = runTest {
        val movies = listOf(
            Movie(
                id = 1,
                title = "Movie 1",
                description = "Movie description",
                posterPath = null
            )
        )
        whenever(remoteDataSource.fetchMovies()).thenReturn(Result.Success(movies))

        val result = repository.getMovies()

        assertTrue(result is Result.Success)
        assertEquals(movies, (result as Result.Success).data)
        verify(localDataSource).storeMovies(movies)
    }

    @Test
    fun `getMovies() should return movies from local when remote fetch fails`() = runTest {
        val movies = listOf(
        Movie(
            id = 1,
            title = "Movie 1",
            description = "Movie description",
            posterPath = null
        )
    )
        whenever(remoteDataSource.fetchMovies()).thenReturn(Result.Error(Exception("Network error")))
        whenever(localDataSource.getMovies()).thenReturn(Result.Success(movies))

        val result = repository.getMovies()

        assertTrue(result is Result.Success)
        assertEquals(movies, (result as Result.Success).data)
    }

    @Test
    fun `getMovie() should return movie from localDataSource`() = runTest {
        val movie =
            Movie(id = 1, title = "Movie 1", description = "Movie description", posterPath = null)
        whenever(localDataSource.getMovie(1)).thenReturn(Result.Success(movie))

        val result = repository.getMovie(1)

        assertTrue(result is Result.Success)
        assertEquals(movie, (result as Result.Success).data)
    }

    @Test
    fun `observeLikedMovieIds() should return flow from localDataSource`() = runTest {
        val likedIds = listOf(1, 2, 3)
        whenever(localDataSource.observeLikedMovieIds()).thenReturn(flowOf(likedIds))

        val result = repository.observeLikedMovieIds().first()

        assertEquals(likedIds, result)
    }

    @Test
    fun `addMovieToFavorites() should call likeMovie()`() = runTest {
        repository.addMovieToFavorites(1)

        verify(localDataSource).likeMovie(1)
    }

    @Test
    fun `removeMovieFromFavorites() should call dislikeMovie()`() = runTest {
        repository.removeMovieFromFavorites(1)

        verify(localDataSource).dislikeMovie(1)
    }

}