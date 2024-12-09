package com.example.mobileappproject.repository

import com.example.mobileappproject.states.UserState
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UserRepository(private val firebaseDatabase: FirebaseDatabase) {
    private val usersRef: DatabaseReference = firebaseDatabase.reference.child("ToDos")

    // Firebase에 사용자 데이터를 저장
    fun saveUserData(userState: UserState, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        // Firebase UID를 key로 사용하여 데이터 저장
        val userRef = usersRef.child(userState.nickname) // nickname을 UID로 사용
        userRef.setValue(userState)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception ->
                onFailure(exception.message ?: "Unknown error")
            }
    }

    // Firebase에서 사용자 데이터를 로드
    fun loadUserData(userId: String, onSuccess: (UserState) -> Unit = {}, onFailure: (String) -> Unit = {}) {
        val userRef = usersRef.child(userId) // userId를 사용하여 데이터 로드
        userRef.get()
            .addOnSuccessListener { snapshot ->
                val userState = snapshot.getValue(UserState::class.java)
                if (userState != null) {
                    onSuccess(userState)
                } else {
                    onFailure("User data not found")
                }
            }
            .addOnFailureListener { exception ->
                onFailure(exception.message ?: "Unknown error")
            }
    }
}
