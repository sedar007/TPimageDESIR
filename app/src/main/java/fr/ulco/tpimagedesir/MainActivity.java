package fr.ulco.tpimagedesir;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.image_menu, menu);
        return true;
    }

    private ActivityResultLauncher<String> mGetResult = registerForActivityResult(
            // classe de contrat pour une intention implicite
            new ActivityResultContracts.GetContent(),
            // callback pour une intention implicite
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    if (uri == null) return;
                    showUri(uri);
                    showImage(uri);
                }// onActivityResult
            }// ActivityResultCallback
    );// registerForActivityResult

    public void onImageUpload(View view){
        System.out.println("On Image Upload");
        mGetResult.launch("image/*");
    }

    private void showUri(Uri imageUri){
        String uriString = imageUri.toString();
        TextView textView = findViewById(R.id.uriTextView);
        textView.setText(uriString);
    }

    private void showImage(Uri imageUri){
        // ----- préparer les options de chargement de l’image
        BitmapFactory.Options option = new BitmapFactory.Options();
        option.inMutable = true; // l’image pourra ^etre modifi´ee
        // ------ chargement de l’image - valeur retourn´ee null en cas d’erreur
        try {
            Bitmap bm = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri), null, option);
            ImageView imageView = findViewById(R.id.imageView);
            imageView.setImageBitmap(bm);
        }
        catch (Exception e){
            // Affiche un message d'erreur au user
            Toast.makeText(this, getString(R.string.error_upload_image), Toast.LENGTH_SHORT).show();

        }
    }
}