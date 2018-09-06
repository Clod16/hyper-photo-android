package photo.hyper.it.hyperphoto.feature;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "HyperPhoto" ;
    private TextView mTextMessage;
    static final int PICTURE_RESULT = 1;
    String currentPhotoPath;
    ContentValues contentValues;
    private Uri file;
    ImageView imageView;
    Bitmap help1;
    ThumbnailUtils thumbnailUtils;

    /*private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.navigation_home) {
                mTextMessage.setText(R.string.title_home);

                return true;
            } else if (id == R.id.navigation_dashboard) {
                mTextMessage.setText(R.string.title_dashboard);
                return true;
            } else if (id == R.id.navigation_notifications) {
                mTextMessage.setText(R.string.title_notifications);
                return true;
            }
            return false;
        }
    };
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button TakePhoto = findViewById(R.id.button);
        mTextMessage = findViewById(R.id.message);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        //navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        TakePhoto.setOnClickListener((View.OnClickListener) mTextMessage);
            try {
                launch_camera(mTextMessage);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }

        imageView = (ImageView) findViewById(R.id.imageView);
        contentValues = new ContentValues();
    }


    public void launch_camera(View v) {
        // the intent is my camera
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //getting uri of the file
        file = Uri.fromFile(getFile());

        //Setting the file Uri to my photo
        intent.putExtra(MediaStore.EXTRA_OUTPUT, file);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, PICTURE_RESULT);
        }
    }
    //this method will create and return the path to the image file

    public File getFile() {
        File folder = Environment.getExternalStoragePublicDirectory("/From_camera/imagens");// the file path
        //if it doesn't exist the folder will be created
        if (!folder.exists()) {
            folder.mkdir();
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File image_file = null;
        try {
            image_file = File.createTempFile(imageFileName, ".jpg", folder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        currentPhotoPath = image_file.getAbsolutePath();

        return image_file;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICTURE_RESULT) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    help1 = MediaStore.Images.Media.getBitmap(getContentResolver(), file);
                    imageView.setImageBitmap(thumbnailUtils.extractThumbnail(help1, help1.getWidth(), help1.getHeight()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
