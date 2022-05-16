package com.example.surveyapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class Home : AppCompatActivity() {

    internal var dbHelper = DatabaseHelper(this)
    lateinit var appExecutors: AppExecutors

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        appExecutors = AppExecutors()

        sendEmail() // call function to send email
        insertJson()  // call function to insert data to SQLite DB
    }

//    Send email
    private fun sendEmail() {
        appExecutors.diskIO().execute {
            val props = System.getProperties()
            props.put("mail.smtp.host", "smtp.gmail.com")
            props.put("mail.smtp.socketFactory.port", "465")
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory")
            props.put("mail.smtp.auth", "true")
            props.put("mail.smtp.port", "465")

            val session = Session.getInstance(props,
                object : javax.mail.Authenticator() {
                    //Authenticating the password
                    override fun getPasswordAuthentication(): PasswordAuthentication {
                        return PasswordAuthentication(Credentials.EMAIL, Credentials.PASSWORD)
                    }
                })

            try {
                //Creating MimeMessage object
                val mm = MimeMessage(session)
                val emailId = "tech@pula.io" // Email to sent tp


                mm.setFrom(InternetAddress(Credentials.EMAIL))  // Set sender address
                //Adding receiver
                mm.addRecipient(
                    Message.RecipientType.TO, InternetAddress(emailId)  //
                )
                //Email Subject
                mm.subject = "Android Engineer Coding Challenge."
                //Email  message
                mm.setText("A user has successfully installed survey app")
                //Sending email
                Transport.send(mm)

                appExecutors.mainThread().execute {
                    //Something that should be executed on main thread.
                    Toast.makeText(this@Home,"Email sent", Toast.LENGTH_LONG).show()
                }

            } catch (e: MessagingException) {
                e.printStackTrace()
                Toast.makeText(this@Home,e.printStackTrace().toString(), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun insertJson() {


        // URL to get the JSON API
        var url = "https://run.mocky.io/v3/d628facc-ec18-431d-a8fc-9c096e00709a"  //
        var rq: RequestQueue = Volley.newRequestQueue(this)
        var sr = StringRequest(
            Request.Method.GET, url, { response ->
                var jsonResponse = JSONObject(response)
                Log.e("RESPONSE", jsonResponse.toString())
                var jsonArray: JSONArray = jsonResponse.getJSONArray("questions")
                for (i in 0..jsonArray.length() - 1) {

                    var jsonObject: JSONObject = jsonArray.getJSONObject(i)

                    var id = jsonObject.getString("id")
                    var question_type = jsonObject.getString("question_type")
                    var ans_type = jsonObject.getString("answer_type")
                    var question_text = jsonObject.getString("question_text")
                    var options = jsonObject.getString("options")

//                    Insert into sqlite DB
                    dbHelper.insertData(id, question_type, ans_type,question_text,options)
//
                }
            },
            { error ->

                Toast.makeText(applicationContext, error.message, Toast.LENGTH_LONG).show()
            })
        rq.add(sr)
    }
}