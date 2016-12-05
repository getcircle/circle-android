package com.rhlabs.circle.fragments;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.rhlabs.circle.R;
import com.rhlabs.circle.common.AppPreferences;
import com.rhlabs.circle.services.MediaService;
import com.rhlabs.circle.services.ProfileService;
import com.rhlabs.circle.utils.BitmapUtils;
import com.rhlabs.circle.utils.ImageUtils;
import com.rhlabs.protobufs.services.profile.containers.ProfileV1;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.RetrofitError;

public class EditProfileImageFragment extends Fragment implements PopupMenu.OnMenuItemClickListener {

    @InjectView(R.id.btnNext)
    Button mNextButton;

    @InjectView(R.id.ivProfileImage)
    ImageView mProfileImageView;

    @InjectView(R.id.btnUpdateProfileImage)
    ImageButton mUpdateProfileImageButton;

    public final String APP_TAG = "Circle";
    public final static String LOG_TAG = EditProfileImageFragment.class.getSimpleName();
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public final static int PICK_PHOTO_REQUEST_CODE = 1046;
    public String photoFileName = "photo.png";

    private String mFilePath;
    private OnEditProfileImageListener mListener;
    private ProfileV1 mProfileV1;

    public static EditProfileImageFragment newInstance() {
        EditProfileImageFragment fragment = new EditProfileImageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public EditProfileImageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_edit_profile_image, container, false);
        ButterKnife.inject(this, rootView);
        initData(savedInstanceState);
        setupOnClickListeners();
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnEditProfileImageListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnEditProfileImageListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {

            case R.id.takeNewPicture:
                takeNewPicture();
                break;

            case R.id.chooseExistingPicture:
                pickPicture();
                break;

            default:
                break;
        }

        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri takenPhotoUri = getPhotoFileUri(photoFileName);
                setSelectedImage(takenPhotoUri.getPath());
                mNextButton.setEnabled(true);
            } else {
                Toast.makeText(getActivity(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == PICK_PHOTO_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                Uri photoUri = data.getData();
                Log.d(LOG_TAG, photoUri.toString());
                setSelectedImage(getRealPathFromURI(photoUri));
                mNextButton.setEnabled(true);
            } else {
                Toast.makeText(getActivity(), "Picture wasn't selected!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setSelectedImage(String filePath) {
        Bitmap fullSizeImageBitmap = ImageUtils.rotateBitmapOrientation(filePath);
        Bitmap scaledImageBitmap = BitmapUtils.scaleToFitWidth(fullSizeImageBitmap, mProfileImageView.getWidth());
        mProfileImageView.setImageBitmap(scaledImageBitmap);
        BitmapUtils.saveToCacheFile(scaledImageBitmap);
        mFilePath = BitmapUtils.getCacheFilename();
    }

    private String getRealPathFromURI(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public interface OnEditProfileImageListener {
        void onEditProfileImageCompleted();
    }

    private void initData(Bundle savedInstanceState) {
        ProfileV1 loggedInUserProfile = AppPreferences.getLoggedInUserProfile();
        if (loggedInUserProfile == null) {
            Toast.makeText(getActivity(), "Logged in user profile not found.", Toast.LENGTH_SHORT).show();
            return;
        }

        mProfileV1 = loggedInUserProfile;
        if (loggedInUserProfile.image_url != null && !loggedInUserProfile.image_url.isEmpty() && savedInstanceState == null) {
            Picasso.with(getActivity())
                    .load(loggedInUserProfile.image_url)
                    .fit()
                    .centerCrop()
                    .into(mProfileImageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            mNextButton.setEnabled(true);
                        }

                        @Override
                        public void onError() {

                        }
                    });

            mNextButton.setEnabled(false);
        }
    }

    private void setupOnClickListeners() {
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if image was uploaded
                if (mFilePath != null) {
                    mNextButton.setEnabled(false);
                    MediaService.uploadProfileImage(mProfileV1, mFilePath, new MediaService.CompleteImageUploadCallback() {
                        @Override
                        public void success(String mediaUrl, Map<String, String> errors) {
                            if (mediaUrl != null) {
                                ProfileV1.Builder updatedProfile = new ProfileV1.Builder(mProfileV1)
                                        .image_url(mediaUrl);

                                ProfileService.updateProfile(updatedProfile.build(), new ProfileService.UpdateProfileCallback() {
                                    @Override
                                    public void success(ProfileV1 updatedProfile, Map<String, String> errors) {
                                        AppPreferences.putLoggedInUserProfile(updatedProfile);
                                        mProfileV1 = updatedProfile;
                                        if (mListener != null) {
                                            mListener.onEditProfileImageCompleted();
                                        }
                                    }

                                    @Override
                                    public void failure(RetrofitError error) {
                                        Toast.makeText(getActivity(), "Error updating profile", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            mNextButton.setEnabled(true);
                            Toast.makeText(getActivity(), "Error uploading image", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else if (mProfileV1.image_url != null && !mProfileV1.image_url.isEmpty()) {
                    if (mListener != null) {
                        mListener.onEditProfileImageCompleted();
                    }
                }
                else {
                    Toast.makeText(getActivity(), "Please upload an image", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mUpdateProfileImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(getActivity(), view);
                popup.setOnMenuItemClickListener(EditProfileImageFragment.this);
                popup.inflate(R.menu.update_image_menu);
                popup.show();
            }
        });
    }

    private void takeNewPicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getPhotoFileUri(photoFileName));
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    private void pickPicture() {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

        startActivityForResult(chooserIntent, PICK_PHOTO_REQUEST_CODE);
    }

    private Uri getPhotoFileUri(String photoFileName) {
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), APP_TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(APP_TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        return Uri.fromFile(new File(mediaStorageDir.getPath() + File.separator + photoFileName));
    }
}
