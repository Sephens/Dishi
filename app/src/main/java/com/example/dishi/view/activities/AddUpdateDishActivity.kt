package com.example.dishi.view.activities

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Application
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.view.View
import android.widget.Toast
import com.example.dishi.R
import com.example.dishi.databinding.ActivityAddUpdateDishBinding
import com.example.dishi.databinding.DialogCustomImageSelectionBinding
import com.example.dishi.databinding.FragmentAllDishesBinding
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

class AddUpdateDishActivity : AppCompatActivity(), View.OnClickListener{
    private lateinit var mBinding: ActivityAddUpdateDishBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityAddUpdateDishBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setActionBar()

        mBinding.ivAddDishImage.setOnClickListener(this)
    }

    private fun setActionBar(){
        setSupportActionBar(mBinding.toolbarAddDishActivity)
        //add back button to the bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mBinding.toolbarAddDishActivity.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun onClick(p0: View?) {
        if (p0 != null) {
            when(p0.id){
                R.id.iv_add_dish_image->{
                   customImageSelectionDialog()
                    return
                }
            }
        }
    }


        val CAMERA = 1

    private fun customImageSelectionDialog(){
        val dialog = Dialog(this)
        val binding: DialogCustomImageSelectionBinding = DialogCustomImageSelectionBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)

        binding.tvCamera.setOnClickListener {
            Dexter.withContext(this).withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    report?.let{
//                        if (report.areAllPermissionsGranted()){
//                            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//                            startActivityForResult(intent, CAMERA)
//                        }
                        dispatchTakePictureIntent()
                    }

                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    showOnPermissionRationaleShouldBeShown()
                }

            }).onSameThread().check()

            dialog.dismiss()
        }





        //When a user clicks on  the gallery image
        binding.tvGallery.setOnClickListener {
           Dexter.withContext(this).withPermissions(
               Manifest.permission.READ_EXTERNAL_STORAGE,
               Manifest.permission.WRITE_EXTERNAL_STORAGE

           ).withListener(object : MultiplePermissionsListener {
               override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                   if (report!!.areAllPermissionsGranted()){
                       Toast.makeText(this@AddUpdateDishActivity,"Permission granted",Toast.LENGTH_SHORT).show()
                   }
               }

               override fun onPermissionRationaleShouldBeShown(
                   p0: MutableList<PermissionRequest>?,
                   p1: PermissionToken?
               ) {
                   showOnPermissionRationaleShouldBeShown()
               }

           }).onSameThread().check()


            dialog.dismiss()
        }

        dialog.show()
    }



    private fun showOnPermissionRationaleShouldBeShown(){
        AlertDialog.Builder(this).setMessage(" Looks like youve turned off permission for camera")
            .setPositiveButton("GO TO SETTINGS")
            {_,_->
                try {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                //add data to intent
                intent.data = uri
                startActivity(intent)
            }catch (e: ActivityNotFoundException){
                e.printStackTrace()
            }

            }
            .setNegativeButton("Cancel"){dialog,_ ->
                dialog.dismiss()

            }.show()
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, CAMERA)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == CAMERA && requestCode == Activity.RESULT_OK){
                data?.extras?.let {
                    val thumbail = data.extras!!.get("data") as Bitmap
                    mBinding.ivDishImage.setImageBitmap(thumbail)

            }
        }
    }


//    if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//        val imageBitmap = data.extras.get("data") as Bitmap
//        imageView.setImageBitmap(imageBitmap)
//    }


}