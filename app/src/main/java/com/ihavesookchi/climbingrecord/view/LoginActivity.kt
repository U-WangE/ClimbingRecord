package com.ihavesookchi.climbingrecord.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.ihavesookchi.climbingrecord.R
import com.ihavesookchi.climbingrecord.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var googleSignInClient: GoogleSignInClient

    private lateinit var googleLoginLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        googleLoginResult()

        googleLoginLauncher()
    }



    private fun googleLoginResult() {
        googleLoginLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    try {
                        GoogleSignIn
                            .getSignedInAccountFromIntent(result.data)
                            .getResult(ApiException::class.java)
                            .idToken
                            .let {
                                firebaseAuthWithGoogle(it)
                            }
                    } catch (e: ApiException) {
                        Log.d("여기3", e.toString())
                    }
                } else {
                    Log.d("여기", "Google Sign-In failed. Result code: ${result.resultCode}")
                    // 사용자가 로그인을 취소하거나 다른 이유로 실패한 경우의 처리를 추가할 수 있습니다.
                }
            }
    }

    private fun firebaseAuthWithGoogle(idToken: String?) {
        firebaseAuth
            .signInWithCredential(GoogleAuthProvider.getCredential(idToken, null))
            .addOnCompleteListener(this) { result ->
                if (result.isSuccessful) {
                    startActivity(Intent(this, BaseActivity::class.java))
                    finish()
                }
                else
                    Log.d("여기", "$result")
            }
    }

    private fun googleLoginLauncher() {
        binding.sbGoogleLoginButton.setOnClickListener {
            googleLoginLauncher.launch(
                googleSignInClient.signInIntent
            )
        }
    }
}