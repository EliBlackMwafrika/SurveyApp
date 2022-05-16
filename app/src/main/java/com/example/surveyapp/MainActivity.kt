package com.example.surveyapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.example.surveyapp.databinding.ActivityMainBinding



class MainActivity : AppCompatActivity() {

    private  lateinit var binding:ActivityMainBinding

     var phoneText=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener{v ->submitForm()}


        phoneFocusListener()
    }

    private fun phoneFocusListener(){
        binding.edtPhoneNo.setOnFocusChangeListener{_, focused ->
            if(!focused){
                binding.phoneContainer.helperText= validPhone()
            }
        }
    }

    private fun validPhone(): String? {
         phoneText = binding.edtPhoneNo.text.toString().trim() // get phone number values
       var  pass = binding.edtPassword.text.toString().trim() // get password values

//        Check if input is empty
        if(phoneText.isEmpty()){
            return "Phone number is required"
        }
//        Phone number should have 13 digits number
        if(phoneText.length<13){
            return "Invalid phone number"
        }
//        Phone number should have digits number
        if(!phoneText.matches(".*[0-9].*".toRegex())){
            return "must contain digits"
        }
//        Phone number must have + character
        if(!phoneText.matches(".*[+].*".toRegex())){
            return "must contain +  character"
        }
//        Hardcoded password
        if(!pass.equals("1234GYD%$")){
            return "Please confirm your password"
        }

        return null
    }

    private fun invalidForm() {
        var message =""

        if(binding.phoneContainer.helperText !=null)
            message+= binding.phoneContainer.helperText


//        Show Alert with error message if validation fails
        AlertDialog.Builder(this)
            .setTitle("ERROR")
            .setMessage(message)
            .setPositiveButton( "Close"){ _,_ ->

            }.show()

    }


    private fun submitForm() {
        binding.phoneContainer.helperText= validPhone()

        val validPhoneNo = binding.phoneContainer.helperText==null

        if( validPhoneNo ) {

            // on successful login go to home Activity
            val intent = Intent(this, Home::class.java)
            startActivity(intent)



        }else {
            invalidForm()   // called when inputs fail validation
        }
    }

}