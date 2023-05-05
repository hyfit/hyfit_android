package com.example.hyfit_android


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sqrt

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var loginActivity: LoginActivity
    val PERMISSIONS_REQUEST_LOCATION = 1000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loginActivity=LoginActivity()
        Log.d("sibalhere", getJwt()!!)


        var showSetFragment = intent.getBooleanExtra("showSetFragment", false)


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
        if(showSetFragment){
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, SetFragment()).commit()
        }
        else {
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, MainFragment()).commit()
        }

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
        Log.d("babalogloglog", calDistance("35.522", "129.134", "42", "35.521", "129.134", "113").toString())
    }

    private fun calDistance(Lat1: String, Lon1: String, Alt1: String, Lat2: String, Lon2: String, Alt2: String): Double{
        // DCMA 알고리즘 사용
        val lat1 = Lat1.toDouble()
        val lat2 = Lat2.toDouble()
        val lon1 = Lon1.toDouble()
        val lon2 = Lon2.toDouble()
        val alt1 = kotlin.math.abs(Alt1.toDouble())/1000
        val alt2 = kotlin.math.abs(Alt2.toDouble())/1000

        val ML = (lat1 + lat2) / 2
        val KPD1 = 111.13209 - 0.56605 * cos(2 * ML) + 0.00120 * cos(4 * ML)
        val KPD2 = 111.41513 * cos(ML) - 0.09455 * cos(3 * ML) + 0.00012 * (5 * ML)
        val NS = KPD1 * (lat1 - lat2)
        val EW = KPD2 * (lon1 - lon2)
        val DISR = sqrt(NS.pow(2) + EW.pow(2))
        val DIFFalt = alt1 - alt2
        val DISTANCEmove = sqrt(DISR.pow(2) + DIFFalt.pow(2))

        return DISTANCEmove
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


