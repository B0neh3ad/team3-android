package com.wafflestudio.wafflestagram.ui.signup

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import com.wafflestudio.wafflestagram.databinding.ActivitySignUpCompleteBinding
import com.wafflestudio.wafflestagram.network.dto.SignUpRequest
import dagger.hilt.android.AndroidEntryPoint
import java.util.regex.Pattern

@AndroidEntryPoint
class SignUpCompleteActivity : AppCompatActivity() {

    lateinit var binding: ActivitySignUpCompleteBinding
    private val viewModel: SignUpViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpCompleteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val phoneNumber = intent.getStringExtra(SIGNUP_ACTIVITY_EXTRA_PHONE_NUMBER)!!
        val name = intent.getStringExtra(SIGNUP_ACTIVITY_EXTRA_NAME)!!
        val password = intent.getStringExtra(SIGNUP_ACTIVITY_EXTRA_PASSWORD)!!
        val birthday = intent.getStringExtra(SIGNUP_ACTIVITY_EXTRA_BIRTHDAY)!!

        binding.editEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun afterTextChanged(p0: Editable?) {
                if(p0?.length!! > 0 && binding.editUsername.text.isNotEmpty()){
                    binding.buttonComplete.isClickable = true
                    binding.buttonComplete.isEnabled = true
                    binding.buttonComplete.setTextColor(Color.parseColor("#FFFFFFFF"))
                }else{
                    binding.buttonComplete.isClickable = false
                    binding.buttonComplete.isEnabled = false
                    binding.buttonComplete.setTextColor(Color.parseColor("#EAEAEA"))
                }
            }

        })
        binding.editUsername.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun afterTextChanged(p0: Editable?) {
                if(p0?.length!! > 0 && binding.editEmail.text.isNotEmpty()){
                    binding.buttonComplete.isClickable = true
                    binding.buttonComplete.isEnabled = true
                    binding.buttonComplete.setTextColor(Color.parseColor("#FFFFFFFF"))
                }else{
                    binding.buttonComplete.isClickable = false
                    binding.buttonComplete.isEnabled = false
                    binding.buttonComplete.setTextColor(Color.parseColor("#EAEAEA"))
                }
            }
        })
        binding.buttonComplete.setOnClickListener{
            if(android.util.Patterns.EMAIL_ADDRESS.matcher(binding.editEmail.text).matches()){
                binding.textInputLayoutEmail.error = null
                val request = SignUpRequest(
                    binding.editEmail.text.toString(),
                    password,
                    name,
                    binding.editUsername.text.toString(),
                    birthday,
                    phoneNumber
                )
                viewModel.signUp(request)
            }else{
                binding.textInputLayoutEmail.error = "올바른 이메일 주소를 입력하세요."
            }
        }

        viewModel.tokenResponse.observe(this, {response ->
            if(response.isSuccessful){
                binding.textInputLayoutUsername.error = null
                //token 저장
                //response.headers().get("Authentication") 를 이용
                //메인으로 이동

            }else if(response.code() == 409){
                //에러 메시지
                binding.textInputLayoutUsername.error = "이메일 또는 사용자 이름이 이미 사용 중입니다. 다시 입력해주세요."
            }else{
                binding.textInputLayoutUsername.error = "잘못된 접근입니다."
            }
        })
    }

    companion object{
        const val SIGNUP_ACTIVITY_EXTRA_PHONE_NUMBER = "phoneNumber"
        const val SIGNUP_ACTIVITY_EXTRA_NAME = "name"
        const val SIGNUP_ACTIVITY_EXTRA_PASSWORD = "password"
        const val SIGNUP_ACTIVITY_EXTRA_BIRTHDAY = "birthday"
    }
}