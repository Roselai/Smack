package co.cantina.smack.Controller

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import co.cantina.smack.R
import co.cantina.smack.Services.AuthService
import kotlinx.android.synthetic.main.activity_create_user.*
import kotlin.random.Random

class CreateUserActivity : AppCompatActivity() {

    var userAvatar = "profileDefault"
    var avatarColor = "[0.5, 0.5, 0.5, 1]"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)
        createSpinner.visibility = View.INVISIBLE
    }

    fun generateUserAvatar(view: View){
        val random = Random
        val color = random.nextInt(2)
        val avatar = random.nextInt(28)

        if (color == 0){
            userAvatar = "light$avatar"
        } else {
            userAvatar = "dark$avatar"
        }

        val resourceId = resources.getIdentifier(userAvatar, "drawable", packageName)
        createAvatarImageView.setImageResource(resourceId)

    }


    fun generateColorClicked(view: View){
        val random = Random
        val r = random.nextInt(255)
        val g = random.nextInt(255)
        val b = random.nextInt(255)

        createAvatarImageView.setBackgroundColor(Color.rgb(r,g,b))

        val savedR = r.toDouble()/255
        val savedG = g.toDouble()/255
        val savedB = b.toDouble()/255

        avatarColor = "[$savedR, $savedG, $savedB, 1]"
        println(avatarColor)
    }

    fun createUserClicked(view: View){
        enableSpinner(true)
        val userName = createUsernameText.text.toString()
        val email = createEmailText.text.toString()
        val password = createPasswordText.text.toString()


        AuthService.registerUser(this, email, password) {registerSuccess ->
            if (registerSuccess) {
                AuthService.loginUser(this, email, password){loginSuccess ->
                    if (loginSuccess) {
                        AuthService.createUser(this, userName, email, userAvatar, avatarColor ){createUserSuccess ->
                            if (createUserSuccess){
                                enableSpinner(false)
                                finish()
                            }
                        }
                    }

                }
            }
        }
    }


    fun enableSpinner(enable: Boolean){
        if (enable){
            createSpinner.visibility = View.VISIBLE

        } else {
            createSpinner.visibility = View.INVISIBLE
        }
        createUserButton.isEnabled = !enable
        createAvatarImageView.isEnabled = !enable
        backgroundColorButton.isEnabled = !enable

    }
}
