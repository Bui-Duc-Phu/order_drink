package com.example.codes.Activitys

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController

import com.example.codes.Firebase.FirebaseFunction

import com.example.codes.Adapters.AdapterViewPager
import com.example.codes.Chats.ChatMain
import com.example.codes.Firebase.DataHandler
import com.example.codes.Firebase.DataHandler.countItemsInCart
import com.example.codes.Firebase.FirebaseUpdate
import com.example.codes.Fragments.CartFragment
import com.example.codes.Fragments.HistoryFragment
import com.example.codes.Fragments.HomeFragment
import com.example.codes.Fragments.ProfileFragment

import com.example.codes.Models.ModeTheme
import com.example.codes.R
import com.example.codes.databinding.ActivityMainBinding


import com.example.sqlite.DBHelper
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.util.Locale


class Main : AppCompatActivity(){
    lateinit var auth: FirebaseAuth
    lateinit var googleSignInClient : GoogleSignInClient
    private lateinit var toggle: ActionBarDrawerToggle

    private val PREF_LANG_KEY = "Language"



    private val fragmentArrayList = ArrayList<Fragment>()

    private val binding : ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        DataHandler.getInforPDF {
            DataHandler.userInfo=it
        }
        devices()
        init_()














    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun init_() {
        navigationDrawer()
        setSupportActionBar(binding.toolbar)
        setupViewPagerAndBottomNav()
        setUpBadge()

        binding.chatBtn.setOnClickListener {
            startActivity(Intent(this@Main,ChatMain::class.java))
        }

    }

    private fun setUpBadge() {
        val badgeDrawable = binding.bottomNavigationView!!.getOrCreateBadge(R.id.cartFragment)
        countItemsInCart(DataHandler.CartItemCountCallback { count: Int ->
            if (count > 0) {
                badgeDrawable.number = count
                badgeDrawable.isVisible = true
            } else {
                badgeDrawable.isVisible = false
            }
        })
    }


    private fun setupViewPagerAndBottomNav() {
        fragmentArrayList.add(HomeFragment())
        fragmentArrayList.add(CartFragment())
        fragmentArrayList.add(HistoryFragment())
        fragmentArrayList.add(ProfileFragment())
        binding.viewPager2!!.setAdapter(AdapterViewPager(this, fragmentArrayList))
        binding.viewPager2!!.setUserInputEnabled(false)
        binding.bottomNavigationView!!.setOnItemSelectedListener { item: MenuItem ->
            if (item.itemId == R.id.homeFragment) {
                binding.viewPager2!!.setCurrentItem(0, true)
            }
            if (item.itemId == R.id.cartFragment) {
                binding.viewPager2!!.setCurrentItem(1, true)
            }
            if (item.itemId == R.id.historyFragment) {
                binding.viewPager2!!.setCurrentItem(2, true)
            }
            if (item.itemId == R.id.profileFragment) {
                binding.viewPager2!!.setCurrentItem(3, true)
            }
            true
        }
    }

    private fun buttonNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun navigationDrawer(){
        val themeItem = binding.navView.menu.findItem(R.id.lightMode)
        if (isDarkMode()) {
            themeItem.title = getString(R.string.light_mode)
            themeItem.setIcon(R.drawable.sun)
        } else {
            themeItem.title = getString(R.string.dark_mode)
            themeItem.setIcon(R.drawable.moon)
        }
        val languageItem  = binding.navView.menu.findItem(R.id.setLanguage)
        setSupportActionBar(binding.toolbar)
        toggle = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.open, R.string.close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.navView.setNavigationItemSelectedListener { menu ->
            when (menu.itemId) {
              R.id.changePassword ->  {
                  isAccountGoogle {
                      if(!it)startActivity(Intent(this@Main,ChangePassword::class.java))
                  }

              }
              R.id.setLanguage ->{
                  when{
                      isVietnameseLanguage(this) -> { changeLang(this,"en")
                      recreate()}
                      else ->{ changeLang(this,"vi")
                      recreate()}
                  }
              }
              R.id.lightMode ->{
                 toggleNightMode()
              }
              R.id.privacyPolicy ->{

              }
              R.id.notification -> loadNotification()
              R.id.nav_logout ->{
                  val firebaseUser : FirebaseUser = FirebaseAuth.getInstance().currentUser!!
                  val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                      .requestIdToken(getString(R.string.default_web_client_id))
                      .requestEmail()
                      .build()
                  googleSignInClient = GoogleSignIn.getClient(this, gso)
                  googleSignInClient.revokeAccess().addOnCompleteListener(this) {}
                  googleSignInClient.signOut().addOnCompleteListener(this){}
                  auth.signOut()

                  FirebaseUpdate.deleteDriver(firebaseUser.uid.toString()){
                      if(!it) Toast.makeText(applicationContext, "delete driver failse", Toast.LENGTH_SHORT).show()
                  }
                  startActivity(Intent(this, LoginOrSignUp::class.java))
              }
            }
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

    }
    private fun isAccountGoogle(callback : (Boolean)->Unit){
        val firebaseUser : FirebaseUser = FirebaseAuth.getInstance().currentUser!!
        FirebaseFunction.getUserDataWithUid(firebaseUser.uid){
            if(it.typeAccount.equals("3")){
                Toast.makeText(applicationContext, "Account google, not changed password", Toast.LENGTH_SHORT).show()
                callback(true)
            }else{
                callback(false)
            }
        }
    }

    private fun loadNotification() {

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun toggleNightMode() {
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        val newNightMode = when (currentNightMode) {
            Configuration.UI_MODE_NIGHT_NO -> AppCompatDelegate.MODE_NIGHT_YES
            Configuration.UI_MODE_NIGHT_YES -> AppCompatDelegate.MODE_NIGHT_NO
            else -> return
        }

        val confirmationDialog = AlertDialog.Builder(this)
            .setTitle(getString(R.string.c_p_nh_t_theme))
            .setMessage(getString(R.string.b_n_c_mu_n_c_p_nh_t_theme_kh_ng))
            .setPositiveButton(getString(R.string.yes)) { dialog, _ ->
                dialog.dismiss()
                AppCompatDelegate.setDefaultNightMode(newNightMode)
                updateDataMode()
                recreate()
            }
            .setNegativeButton(getString(R.string.no)) { dialog, _ ->
                dialog.dismiss()
            }
            .setOnCancelListener {
                it.dismiss()
            }
            .create()

        confirmationDialog.show()
    }

    private fun updateDataMode(){
        val dbHelper = DBHelper(this,null)
        if(dbHelper.getModeList()[0] == ModeTheme.dark.toString()){
            dbHelper.updateMode("1", ModeTheme.light.toString())
        }else{
            dbHelper.updateMode("1", ModeTheme.dark.toString())
        }
    }

    fun isDarkMode(): Boolean {
        val currentMode = this.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return currentMode == Configuration.UI_MODE_NIGHT_YES
    }




    fun changeLang(context: Context, lang: String) {
        val myLocale = Locale(lang)
        Locale.setDefault(myLocale)
        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(myLocale)
        context.resources.updateConfiguration(configuration, context.resources.displayMetrics)


        val dbHelper = DBHelper(this,null)
        if( lang == "vi") dbHelper.updateMode("2", "vi")
        else dbHelper.updateMode("2", "en")
    }

    fun isVietnameseLanguage(context: Context): Boolean {
        val currentLocale = context.resources.configuration.locale
        return currentLocale == Locale("vi")
    }



   private fun  devices(){
       val firebaseUser : FirebaseUser = FirebaseAuth.getInstance().currentUser!!
        FirebaseFunction.evenLogOut(applicationContext, firebaseUser.uid){
            if(!it){
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build()
                googleSignInClient = GoogleSignIn.getClient(this, gso)
                googleSignInClient.revokeAccess().addOnCompleteListener(this) {}
                googleSignInClient.signOut().addOnCompleteListener(this){}
                auth.signOut()
                FirebaseUpdate.deleteDriver(firebaseUser.uid.toString()){
                    if(!it) Toast.makeText(applicationContext, "delete driver failse", Toast.LENGTH_SHORT).show()
                }
                startActivity(Intent(this, LoginOrSignUp::class.java))
            }
        }
   }

















}