        package com.example.korey

        import android.content.Intent
        import android.graphics.Bitmap
        import android.os.Bundle
        import android.provider.MediaStore
        import android.view.LayoutInflater
        import android.view.View
        import android.view.ViewGroup
        import android.widget.Button
        import android.widget.EditText
        import android.widget.ImageView
        import androidx.fragment.app.Fragment
        import java.io.File
        import java.io.FileOutputStream
        import androidx.appcompat.app.AlertDialog


        private const val REQUEST_IMAGE_CAPTURE = 1

        class CreateCardFragment : Fragment() {


            private lateinit var imageView: ImageView
            private lateinit var nameEditText: EditText
            private lateinit var addButton: Button
            private lateinit var closeButton: Button
            private var selectedImage: Bitmap? = null

            override fun onCreateView(
                inflater: LayoutInflater, container: ViewGroup?,
                savedInstanceState: Bundle?
            ): View? {
                return inflater.inflate(R.layout.fragment_create_card, container, false)
            }

            override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
                super.onViewCreated(view, savedInstanceState)
                // Инициализация визуальных компонентов
                imageView = view.findViewById(R.id.imageView)
                nameEditText = view.findViewById(R.id.nameEditText)
                addButton = view.findViewById(R.id.addButton)
                closeButton = view.findViewById(R.id.closeButton)

                imageView.setOnClickListener {
                    dispatchTakePictureIntent()
                }

                closeButton.setOnClickListener {
                    // Обработка клика на кнопку "Close"
                    requireActivity().supportFragmentManager.popBackStack()
                }

                addButton.setOnClickListener {
                    val name = nameEditText.text.toString()
                    // Проверка, что пользователь выбрал фото и ввел имя
                    if (selectedImage != null && name.isNotEmpty()) {
                        // Сохранение данных карточки локально
                        saveCardLocally(name, selectedImage!!)
                    } else {
                        // Если поля не заполнены, показывает сообщение об ошибке
                        showErrorMessage("Please select a photo and enter the name with the position.")
                    }
                }

            }

            private fun showErrorMessage(message: String) {
                val alertDialogBuilder = AlertDialog.Builder(requireContext())
                alertDialogBuilder.apply {
                    setMessage(message)
                    setPositiveButton("OK") { dialog, _ ->
                        dialog.dismiss()
                    }
                }

                val alertDialog = alertDialogBuilder.create()
                alertDialog.show()
            }


            private fun dispatchTakePictureIntent() {
                val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(pickPhoto, REQUEST_IMAGE_CAPTURE)
            }

            private fun saveCardLocally(name: String, image: Bitmap) {
                val directory = File(requireContext().getExternalFilesDir(null), "cards")
                if (!directory.exists()) {
                    directory.mkdirs()
                }
                val file = File(directory, "$name.jpg")
                try {
                    val fos = FileOutputStream(file)
                    image.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                    fos.close()
                    // Успешное сохранение
                    showSuccessMessage()
                } catch (e: Exception) {
                    // Ошибка сохранения
                    showErrorMessage()
                }
            }

            private fun showSuccessMessage() {
                val alertDialogBuilder = AlertDialog.Builder(requireContext())
                alertDialogBuilder.apply {
                    setMessage("Card added successfully!")
                    setPositiveButton("OK") { dialog, _ ->
                        dialog.dismiss()
                    }
                }

                val alertDialog = alertDialogBuilder.create()
                alertDialog.show()
            }

            private fun showErrorMessage() {
                val alertDialogBuilder = AlertDialog.Builder(requireContext())
                alertDialogBuilder.apply {
                    setMessage("Failed to add card!")
                    setPositiveButton("OK") { dialog, _ ->
                        dialog.dismiss()
                    }
                }

                val alertDialog = alertDialogBuilder.create()
                alertDialog.show()
            }


            override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
                super.onActivityResult(requestCode, resultCode, data)
                if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == -1) {
                    val imageUri = data?.data
                    selectedImage = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, imageUri)
                    imageView.setImageBitmap(selectedImage)
                }
            }

            companion object {
                @JvmStatic
                fun newInstance() = CreateCardFragment()
            }
        }
