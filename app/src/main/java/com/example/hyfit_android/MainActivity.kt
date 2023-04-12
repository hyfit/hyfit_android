package com.example.hyfit_android


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.hyfit_android.Join.JoinActivity1
import com.example.hyfit_android.Login.LoginActivity
import com.example.hyfit_android.Login.LogoutActivity
import com.example.hyfit_android.community.CommunityFragment
import com.example.hyfit_android.databinding.ActivityMainBinding
import com.example.hyfit_android.goal.GoalFragment

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var loginActivity: LoginActivity
    val PERMISSIONS_REQUEST_LOCATION = 1000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loginActivity=LoginActivity()


        // BottomNavigationView 초기화
        binding.navigationView.selectedItemId = R.id.MainFragment
        binding.navigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.GoalFragment -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, GoalFragment()).commit()
                    true
                }
                R.id.ReportFragment -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, ReportFragment()).commit()
                    true
                }
                R.id.MainFragment -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, MainFragment()).commit()
                    true
                }
                R.id.CommunityFragment -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, CommunityFragment()).commit()
                    true
                }
                R.id.SetFragment -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, SetFragment()).commit()
                    true
                }
                else -> false
            }
        }
        // 초기 fragment 설정
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, MainFragment()).commit()

        // permission code
        if ((ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) ||
            (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACTIVITY_RECOGNITION
            ) != PackageManager.PERMISSION_GRANTED)
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACTIVITY_RECOGNITION),
                PERMISSIONS_REQUEST_LOCATION
            )
            return
        }
    }
    // 권한 요청
    private fun requestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACTIVITY_RECOGNITION), PERMISSIONS_REQUEST_LOCATION)
        }
    }

    override fun onStart() {
        super.onStart()
        requestPermissions() // 액티비티가 시작되면 권한 요청 실행
    }


    private fun getJwt():String?{
        val spf = getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        return spf!!.getString("jwt","0")
    }
    // permission code
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode === PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults.size > 0) {
                for (grant in grantResults) {
                    if (grant != PackageManager.PERMISSION_GRANTED) System.exit(0)
//                    else {
//                        binding.MoveButton.setOnClickListener{
//                            startActivity(Intent(this@MainActivity, PinnacleActivity::class.java))
//                        }
                    }
                }
            }
        }

//    // fragment에서 다른 fragment로 화면전환
//    public fun replaceFragment(fragment: Fragment) {
//        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
//    }

    }


