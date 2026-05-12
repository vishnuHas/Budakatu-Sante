package com.budakattu.sante.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "settings")

class PreferenceManager(private val context: Context) {

    companion object {
        val ONBOARDING_COMPLETE = booleanPreferencesKey("onboarding_complete")
        val USER_LOGGED_IN = booleanPreferencesKey("user_logged_in")
        val USER_ROLE = stringPreferencesKey("user_role")
        val CART_ITEMS = stringPreferencesKey("cart_items")
        val WISHLIST_ITEMS = stringPreferencesKey("wishlist_items")
        val USER_FULLNAME = stringPreferencesKey("user_fullname")
        val USER_EMAIL = stringPreferencesKey("user_email")
        val USER_LOCATION = stringPreferencesKey("user_location")
    }

    val isOnboardingComplete: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[ONBOARDING_COMPLETE] ?: false
        }

    suspend fun setOnboardingComplete(complete: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[ONBOARDING_COMPLETE] = complete
        }
    }

    val isUserLoggedIn: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[USER_LOGGED_IN] ?: false
        }

    val userRole: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[USER_ROLE]
        }

    suspend fun setUserLoggedIn(loggedIn: Boolean, role: String? = null) {
        context.dataStore.edit { preferences ->
            preferences[USER_LOGGED_IN] = loggedIn
            if (role != null) {
                preferences[USER_ROLE] = role
            }
            // Clear session data on logout if necessary
            if (!loggedIn) {
                // optional: keep keys or clear. User wants "localstorage enough", we'll keep them for now
            }
        }
    }

    val userFullName: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[USER_FULLNAME] }

    val userEmail: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[USER_EMAIL] }

    val userLocation: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[USER_LOCATION] }

    suspend fun saveUserDetails(fullName: String, email: String, location: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_FULLNAME] = fullName
            preferences[USER_EMAIL] = email
            preferences[USER_LOCATION] = location
        }
    }

    val cartItems: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[CART_ITEMS]
        }

    suspend fun saveCartItems(json: String) {
        context.dataStore.edit { preferences ->
            preferences[CART_ITEMS] = json
        }
    }

    val wishlistItems: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[WISHLIST_ITEMS]
        }

    suspend fun saveWishlistItems(json: String) {
        context.dataStore.edit { preferences ->
            preferences[WISHLIST_ITEMS] = json
        }
    }
}
