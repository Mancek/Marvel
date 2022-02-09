package hr.algebra.marvel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import hr.algebra.marvel.databinding.ActivityLoginBinding
import hr.algebra.marvel.framework.startActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkIfLogged()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupListeners()
    }


    private fun checkIfLogged() {
        FirebaseAuth.getInstance().currentUser?.let {
            showMainScreen()
        }
    }

    private fun setupListeners() {
        binding.btnSign.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            signIn(email, password)
        }
    }

    private fun signIn(email: String, password: String) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { login ->
                if(login.isSuccessful) {
                    showMainScreen()
                } else {
                    register(email, password)
                }
            }
    }

    private fun register(email: String, password: String) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { register ->
                if(register.isSuccessful) {
                    showMainScreen()
                } else {
                    Toast.makeText(this, register.exception!!.message.toString(), Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun showMainScreen() {
        startActivity<SplashScreenActivity>()
        finish()
    }
}