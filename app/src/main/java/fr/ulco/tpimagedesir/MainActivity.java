package fr.ulco.tpimagedesir;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends ImageLoader {

    private final String LAUNCH_IMAGE_INPUT = "image/*";

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

        registerForContextMenu(getImageView()); // Ajoute le menu contextuel à l'ImageView
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.image_menu, menu);
        return true;
    }

    public void onImageUpload(View view){
        mGetResult.launch(LAUNCH_IMAGE_INPUT);
    }
    
    public void onImageReset(View view){
        if(initialBitmap != null)
            getImageView().setImageBitmap(initialBitmap);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        ImageView imageView = getImageView();
        BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();

        if(!isImageLoaded(bitmapDrawable)) {
            return super.onOptionsItemSelected(item);
        }

        // Récupération du bitmap de l'image affichée dans l'ImageView
        Bitmap bitmap = bitmapDrawable.getBitmap();
        int id = item.getItemId();

       // Récupération de l'identifiant de l'item dans la classe R
        if (id == R.id.miroir_horizontal) {
            imageView.setImageBitmap(ImageUtils.mirrorHorizontal(bitmap));
            return true;
        }
        if(id == R.id.miroir_vertical) {
            imageView.setImageBitmap(ImageUtils.mirrorVertical(bitmap));
            return true;
        }
        if(id == R.id.rotate_90_right) {
            imageView.setImageBitmap(ImageUtils.rotate90Right(bitmap));
            return true;
        }
        if(id == R.id.rotate_90_left) {
            imageView.setImageBitmap(ImageUtils.rotate90Left(bitmap));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Menu contextuel
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.contextuel_menu, menu);
    }


    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        ImageView imageView = getImageView();
        BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();

        if(!isImageLoaded(bitmapDrawable))
            return super.onOptionsItemSelected(item);

        // Récupération du bitmap de l'image affichée dans l'ImageView
        Bitmap bitmap = bitmapDrawable.getBitmap();
        int id = item.getItemId();

        if (id == R.id.inverse_color) {
            imageView.setImageBitmap(ImageUtils.inversionColors(bitmap));
            return true;
        }
        if( id == R.id.gray_level) {
            imageView.setImageBitmap(ImageUtils.grayLevel(bitmap));
            return true;
        }
        return super.onContextItemSelected(item);
    }

    private final ActivityResultLauncher<String> mGetResult = registerForActivityResult(
            // classe de contrat pour une intention implicite
            new ActivityResultContracts.GetContent(),
            // callback pour une intention implicite
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    if (uri == null) return;
                    showUri(uri);
                    loadImage(uri);
                }// onActivityResult
            }// ActivityResultCallback
    );// registerForActivityResult
}