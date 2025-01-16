package dev.alephany.fontpad

import android.view.View
import androidx.lifecycle.*
import androidx.savedstate.*

/**
 * Custom lifecycle owner for the keyboard view that implements necessary interfaces for Compose.
 * This class provides lifecycle management, ViewModel storage, and saved state handling for the
 * IME service, which doesn't inherently have these capabilities.
 *
 * This implementation is necessary because InputMethodService doesn't extend ComponentActivity
 * or other lifecycle-aware components, but Compose requires lifecycle awareness.
 */
class KeyboardViewLifecycleOwner : LifecycleOwner, ViewModelStoreOwner, SavedStateRegistryOwner {

    // Separate field for ViewModelStore to avoid naming conflicts with interface property
    private val internalViewModelStore = ViewModelStore()

    // Registry to handle lifecycle events
    private val lifecycleRegistry = LifecycleRegistry(this)

    // Controller for handling saved state
    private val savedStateRegistryController = SavedStateRegistryController.create(this)

    init {
        // Initialize saved state with null bundle since we don't restore any state
        savedStateRegistryController.performRestore(null)
    }

    override val lifecycle: Lifecycle
        get() = lifecycleRegistry

    override val viewModelStore: ViewModelStore
        get() = internalViewModelStore

    override val savedStateRegistry: SavedStateRegistry
        get() = savedStateRegistryController.savedStateRegistry

    /**
     * Lifecycle management methods
     * These should be called from corresponding IME service lifecycle methods
     */
    fun onCreate() {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
    }

    fun onResume() {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
    }

    fun onPause() {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    }

    fun onDestroy() {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        internalViewModelStore.clear()
    }

    /**
     * Attaches this lifecycle owner to the given view hierarchy.
     * This setup is required for Compose to function properly within the IME service.
     *
     * @param decorView The root view of the IME window
     */
    fun attachToDecorView(decorView: View?) {
        decorView?.let {
            // Set up the view tree owners required by Compose
            it.setViewTreeLifecycleOwner(this)
            it.setViewTreeViewModelStoreOwner(this)
            it.setViewTreeSavedStateRegistryOwner(this)
        }
    }
}