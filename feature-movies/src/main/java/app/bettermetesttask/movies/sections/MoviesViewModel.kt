package app.bettermetesttask.movies.sections

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.bettermetesttask.domaincore.utils.Result
import app.bettermetesttask.domainmovies.entries.Movie
import app.bettermetesttask.domainmovies.interactors.AddMovieToFavoritesUseCase
import app.bettermetesttask.domainmovies.interactors.ObserveMoviesUseCase
import app.bettermetesttask.domainmovies.interactors.RemoveMovieFromFavoritesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class MoviesViewModel @Inject constructor(
    private val observeMoviesUseCase: ObserveMoviesUseCase,
    private val likeMovieUseCase: AddMovieToFavoritesUseCase,
    private val dislikeMovieUseCase: RemoveMovieFromFavoritesUseCase,
    private val adapter: MoviesAdapter
) : ViewModel() {
    var activeMovie by mutableStateOf<Movie?>(null)
        private set

    private val moviesMutableFlow: MutableStateFlow<MoviesState> =
        MutableStateFlow(MoviesState.Initial)

    val moviesStateFlow: StateFlow<MoviesState>
        get() = moviesMutableFlow.asStateFlow()
            .onStart {
                loadMovies()
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                MoviesState.Initial
            )

    private fun loadMovies() = viewModelScope.launch {
        observeMoviesUseCase()
            .collect { result ->
                if (result is Result.Success) {
                    moviesMutableFlow.emit(MoviesState.Loaded(result.data))
                    adapter.submitList(result.data)
                }
            }
    }

    fun showMovieDetails(movie: Movie) {
        updateActiveMovie(movie)
    }

    fun hideMovieDetails() {
        activeMovie = null
    }

    private fun updateActiveMovie(movie: Movie) {
        activeMovie = movie
    }


    fun likeMovie(movie: Movie) = viewModelScope.launch {
        if (movie.liked) {
            dislikeMovieUseCase(movie.id)
        } else {
            likeMovieUseCase(movie.id)
        }
    }
}