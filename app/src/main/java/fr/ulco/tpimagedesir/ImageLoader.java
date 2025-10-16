package fr.ulco.tpimagedesir;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ImageLoader extends AppCompatActivity {

    protected Bitmap initialBitmap;
    protected TextView getUriTextView(){
        return findViewById(R.id.uriTextView);
    }
    protected ImageView getImageView(){
        return findViewById(R.id.imageView);
    }
    protected void showUri(final Uri imageUri){
        final String uriString = imageUri.toString();
        getUriTextView().setText(uriString);
    }

    protected void loadImage(final Uri imageUri){
        // ----- préparer les options de chargement de l’image
        BitmapFactory.Options option = new BitmapFactory.Options();
        option.inMutable = true; // l’image pourra ^etre modifi´ee
        // ------ chargement de l’image - valeur retourn´ee null en cas d’erreur
        try {
            Bitmap bm = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri), null, option);
            getImageView().setImageBitmap(bm);
            initialBitmap = bm;
        }
        catch (Exception e){
            // Affiche un message d'erreur au user
            Toast.makeText(this, getString(R.string.error_upload_image), Toast.LENGTH_SHORT).show();
        }
    }

    // Méthode pour vérifier si une image est chargée dans l'ImageView et afficher un message d'erreur si ce n'est pas le cas
    protected Boolean isImageLoaded(final BitmapDrawable bitmapDrawable) {
        if(bitmapDrawable == null) {
            Toast.makeText(this, getString(R.string.error_no_image), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}