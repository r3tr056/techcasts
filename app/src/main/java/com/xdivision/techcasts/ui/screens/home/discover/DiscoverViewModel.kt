package com.xdivision.techcasts.ui.screens.home.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

import com.xdivision.techcasts.Graph
import com.xdivision.techcasts.data.Category
import com.xdivision.techcasts.data.CategoryStore

data class DiscoverViewState(
    val categories: List<Category> = emptyList(),
    val selectedCategory: Category? = null
)

class DiscoverViewModel(
    private val categoryStore: CategoryStore = Graph.categoryStore
) : ViewModel() {
    // currently selected category
    private val _selectedCategory = MutableStateFlow<Category?>(null)
    // view state which the UI collects via [state]
    private val _state = MutableStateFlow(DiscoverViewState())

    val state: StateFlow<DiscoverViewState>
        get() = _state

    init {

    }
}