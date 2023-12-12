package com.example.aplikasie_learning.presentation.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import androidx.core.widget.addTextChangedListener
import com.bumptech.glide.Glide
import com.example.aplikasie_learning.adapter.AksaraLontaraAdapter
import com.example.aplikasie_learning.adapter.KuisAdapter
import com.example.aplikasie_learning.adapter.MaterialsAdapter
import com.example.aplikasie_learning.databinding.ActivityMainBinding
import com.example.aplikasie_learning.model.AksaraLontara
import com.example.aplikasie_learning.model.Kuis
import com.example.aplikasie_learning.model.Materials
import com.example.aplikasie_learning.model.User
import com.example.aplikasie_learning.presentation.allmateri.AllMaterials
import com.example.aplikasie_learning.presentation.allaksaralontara.AllAksaraLontaraActivity
import com.example.aplikasie_learning.presentation.content.ContentActivity
import com.example.aplikasie_learning.presentation.kuis.KuisActivity
import com.example.aplikasie_learning.presentation.penerjemah.PenerjemahActivity
import com.example.aplikasie_learning.presentation.question.QuestionNewActivity
import com.example.aplikasie_learning.presentation.user.UserActivity
import com.example.aplikasie_learning.utils.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity() {
    companion object{
        const val EXTRA_POSITION = "extra_position"
    }

    private lateinit var aksaraLontaraAdapter: AksaraLontaraAdapter
    private lateinit var aksaraLontaraDatabase : DatabaseReference
    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var materialsAdapter: MaterialsAdapter
    private lateinit var userDatabase: DatabaseReference
    private lateinit var materialDatabase: DatabaseReference
    private lateinit var kuisAdapter: KuisAdapter
    private lateinit var kuisDatabase : DatabaseReference
    private var currentUser: FirebaseUser? = null
    private lateinit var kuisList : ArrayList<Kuis>


    private var listenerUser = object  : ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
            hideLoading()
            val user = snapshot.getValue(User::class.java)
            user?.let {
                mainBinding.apply {
                    tvNameUserMain.text = it.nameUser

                    Glide
                        .with(this@MainActivity)
                        .load(it.avatarUser)
                        .placeholder(android.R.color.white)
                        .into(ivAvatarMain)
                }
            }
        }

        override fun onCancelled(error: DatabaseError) {
            hideLoading()
            Log.e("MainActivity", "[onCancelled] - ${error.message}")
            showDialogError(this@MainActivity, error.message.toString())
        }
    }

    private var listenerMaterials = object : ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
            hideLoading()
            if (snapshot.value != null){
                showData()
                val json = Gson().toJson(snapshot.value)
                val type = object : TypeToken<MutableList<Materials>>() {}.type
                val materials = Gson().fromJson<MutableList<Materials>>(json, type)

                materials?.let { materialsAdapter.materials = it }
            }else{
                showEmptyData()
            }
        }

        override fun onCancelled(error: DatabaseError) {
            hideLoading()
            Log.e("MainActivity", "[OnCancelled] - ${error.message}")
            showDialogError(this@MainActivity, error.message)
        }

    }

    private var listenerAksaraLontara = object : ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
            hideLoading()
            if (snapshot.value != null){
                showData()
                val json = Gson().toJson(snapshot.value)
                val type = object : TypeToken<MutableList<AksaraLontara>>() {}.type
                val aksaraLontara = Gson().fromJson<MutableList<AksaraLontara>>(json, type)

                aksaraLontara?.let { aksaraLontaraAdapter.aksaraLontara = it }
            }else{
                showEmptyData()
            }
        }

        override fun onCancelled(error: DatabaseError) {
            hideLoading()
            Log.e("MainActivity", "[OnCancelled] - ${error.message}")
            showDialogError(this@MainActivity, error.message)
        }

    }

    private var listenerKuis = object : ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
            hideLoading()
            if (snapshot.exists()){
                kuisList.clear()
                showData()
                for (kuisSnap in snapshot.children){
                    val kuisData = kuisSnap.getValue(Kuis::class.java)
                    kuisList.add(kuisData!!)
                }
                kuisAdapter.notifyDataSetChanged()
            }else{
                showEmptyData()
            }
        }

        override fun onCancelled(error: DatabaseError) {
            hideLoading()
            Log.e("MainActivity", "[OnCancelled] - ${error.message}")
            showDialogError(this@MainActivity, error.message)
        }

    }

    private fun showData() {
        mainBinding.apply {
            ivEmptyData.gone()
            etSearchMain.enabled()
            rvMaterialsMain.visiable()
            rvAksaraLontara.visiable()
            rvKuisMain.visiable()
        }
    }

    private fun showEmptyData() {
        mainBinding.apply {
            ivEmptyData.visiable()
            etSearchMain.disabled()
            rvMaterialsMain.gone()
            rvAksaraLontara.gone()
            rvKuisMain.gone()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        // Init
        materialsAdapter = MaterialsAdapter()
        userDatabase = FirebaseDatabase.getInstance().getReference("users")
        materialDatabase = FirebaseDatabase.getInstance().getReference("materials")
        kuisDatabase = FirebaseDatabase.getInstance().getReference("kuisAdmin")
        kuisList = arrayListOf<Kuis>()
        kuisAdapter = KuisAdapter(kuisList)
        currentUser = FirebaseAuth.getInstance().currentUser
        aksaraLontaraAdapter = AksaraLontaraAdapter()
        aksaraLontaraDatabase = FirebaseDatabase.getInstance().getReference("aksaraLontara")

        getDataFirebase()
        onAction()


    }

    private fun getDataFirebase() {
        showLoading()
        userDatabase
            .child(currentUser?.uid.toString())
            .addValueEventListener(listenerUser)

        materialDatabase
            .addValueEventListener(listenerMaterials)

        mainBinding.rvMaterialsMain.adapter = materialsAdapter

        kuisDatabase
            .addValueEventListener(listenerKuis)

        mainBinding.rvKuisMain.adapter = kuisAdapter

        aksaraLontaraDatabase
            .addValueEventListener(listenerAksaraLontara)

        mainBinding.rvAksaraLontara.adapter = aksaraLontaraAdapter
    }

    override fun onResume() {
        super.onResume()
        if (intent != null){
            val position = intent.getIntExtra(EXTRA_POSITION, 0)
            mainBinding.rvMaterialsMain.smoothScrollToPosition(position)
        }
    }

    private fun onAction() {
        mainBinding.apply {
            ivAvatarMain.setOnClickListener{
                startActivity<UserActivity>()
            }
            etSearchMain.addTextChangedListener {
                materialsAdapter.filter.filter(it.toString())
                kuisAdapter.filter.filter(it.toString())
            }

            etSearchMain.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    val dataSearch = etSearchMain.text.toString().trim()
                    materialsAdapter.filter.filter(dataSearch)
                    kuisAdapter.filter.filter(dataSearch)
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }

            swipeMain.setOnRefreshListener {
                getDataFirebase()
            }

            btnSeeMoreLontara.setOnClickListener {
                startActivity<AllAksaraLontaraActivity>()
            }

            btnSeeMoreMateri.setOnClickListener {
                startActivity<AllMaterials>()
            }

            btnSeeMoreKuis.setOnClickListener {
                startActivity<KuisActivity>()
            }
            btnTranslate.setOnClickListener {
                startActivity<PenerjemahActivity>()
            }
        }

        materialsAdapter.onClick { material, position ->
            startActivity<ContentActivity>(
                ContentActivity.EXTRA_MATERIAL to material,
                ContentActivity.EXTRA_POSITION to position
            )
        }

        kuisAdapter.onClick { material, position ->
            startActivity<QuestionNewActivity>(
                QuestionNewActivity.EXTRA_KUIS to material,
                QuestionNewActivity.EXTRA_POSITION to position
            )
        }

    }

    private fun showLoading() {
        mainBinding.swipeMain.isRefreshing = true
    }

    private fun hideLoading() {
        mainBinding.swipeMain.isRefreshing = false
    }
}
