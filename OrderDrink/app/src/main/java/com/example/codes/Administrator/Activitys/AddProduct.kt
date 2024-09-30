package com.example.codes.Administrator.Activitys

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.codes.Administrator.model.Size
import com.example.codes.Administrator.model.coffeModel
import com.example.codes.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage


class AddProduct : AppCompatActivity() {
    var imageProduct: ImageView? = null
    var btnAddImage: Button? = null
    var nameProduct: EditText? = null
    var priceProduct: EditText? = null
    var size1: EditText? = null
    var size2: EditText? = null
    var size3: EditText? = null
    var size4: EditText? = null
    var priceSize1: EditText? = null
    var priceSize2: EditText? = null
    var priceSize3: EditText? = null
    var priceSize4: EditText? = null
    var discount: EditText? = null
    var btnSave: Button? = null
    var imageUri: Uri? = null
    var spinnerCategory: Spinner? = null
    var btnBack: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)
        setControl()
        loadCategory()
        btnBack!!.setOnClickListener { v: View? -> onBackPressed() }
        btnSave!!.setOnClickListener { v: View? ->
            uploadImage()
            finish()
        }
        btnAddImage!!.setOnClickListener { v: View? -> openGallery() }
    }

    private fun loadCategory() {
    val categoryReference = FirebaseDatabase.getInstance().getReference().child("Categories")
    categoryReference.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val categories: MutableList<String?> = ArrayList()
            for (snapshot in dataSnapshot.getChildren()) {
                val category = snapshot.child("name").getValue(String::class.java)
                categories.add(category)
            }
            val adapter =
                ArrayAdapter(this@AddProduct, android.R.layout.simple_spinner_item, categories)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerCategory!!.setAdapter(adapter)
        }

        override fun onCancelled(databaseError: DatabaseError) {
            Log.e("Firebase", "Error: " + databaseError.message)
        }
    })
}

    private fun uploadImage() {
        val storageRef = FirebaseStorage.getInstance().getReference()
        val fileName = "images/" + nameProduct!!.getText() + ".jpg"
        val fileRef = storageRef.child(fileName)
        fileRef.putFile(imageUri!!)
            .addOnSuccessListener {
                fileRef.getDownloadUrl().addOnSuccessListener { uri ->
                    themDataVaoFirebase(uri.toString())
                    Toast.makeText(this@AddProduct,
                        getString(R.string.t_i_l_n_th_nh_c_ng), Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(
                    this@AddProduct,
                    getString(R.string.t_i_l_n_th_t_b_i),
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            imageUri = data.data
            imageProduct!!.setImageURI(imageUri)
        }
    }

    private fun themDataVaoFirebase(imageUrl: String) {
        val mDatabase = FirebaseDatabase.getInstance().getReference().child("Products")
        val product = coffeModel()
        product.imageUrl = imageUrl
        product.category = spinnerCategory!!.getSelectedItem().toString()
        product.name = nameProduct!!.getText().toString()
        product.price = priceProduct!!.getText().toString().toInt()
        product.discount = discount!!.getText().toString().toInt()
        val sizes = mutableMapOf<String, Size>()
        priceSize1!!.text.toString().takeIf { it.isNotEmpty() }?.let {
            sizes["S"] = Size(it.toInt(), "S")
        }
        priceSize2!!.text.toString().takeIf { it.isNotEmpty() }?.let {
            sizes["M"] = Size(it.toInt(), "M")
        }
        priceSize3!!.text.toString().takeIf { it.isNotEmpty() }?.let {
            sizes["L"] = Size(it.toInt(), "L")
        }
        priceSize4!!.text.toString().takeIf { it.isNotEmpty() }?.let {
            sizes["XL"] = Size(it.toInt(), "XL")
        }
        product.sizes = sizes
        mDatabase.child(product.name!!).setValue(product)
    }

    private fun setControl() {
        imageProduct = findViewById(R.id.imageProduct)
        btnAddImage = findViewById(R.id.btnAddImage)
        btnBack = findViewById(R.id.btnBack)
        nameProduct = findViewById(R.id.nameProduct)
        priceProduct = findViewById(R.id.priceProduct)
        size1 = findViewById(R.id.size1)
        size2 = findViewById(R.id.size2)
        size3 = findViewById(R.id.size3)
        size4 = findViewById(R.id.size4)
        priceSize1 = findViewById(R.id.priceSize1)
        priceSize2 = findViewById(R.id.priceSize2)
        priceSize3 = findViewById(R.id.priceSize3)
        priceSize4 = findViewById(R.id.priceSize4)
        discount = findViewById(R.id.Discount)
        btnSave = findViewById(R.id.btnSave)
        spinnerCategory = findViewById(R.id.spinnerCategory)
    }

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }
}
