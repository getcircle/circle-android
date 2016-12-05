package com.rhlabs.circle.services;

import android.util.Log;

import com.rhlabs.protobufs.services.media.actions.complete_image_upload.CompleteImageUploadRequestV1;
import com.rhlabs.protobufs.services.media.actions.complete_image_upload.CompleteImageUploadResponseV1;
import com.rhlabs.protobufs.services.media.actions.start_image_upload.StartImageUploadRequestV1;
import com.rhlabs.protobufs.services.media.actions.start_image_upload.StartImageUploadResponseV1;
import com.rhlabs.protobufs.services.media.containers.UploadInstructionsV1;
import com.rhlabs.protobufs.services.media.containers.media.MediaTypeV1;
import com.rhlabs.protobufs.services.profile.containers.ProfileV1;
import com.rhlabs.protobufs.services.registry.requests.Ext_requests;
import com.rhlabs.protobufs.services.registry.responses.Ext_responses;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.mime.TypedFile;

/**
 * Created by anju on 6/18/15.
 */
public class MediaService {

    private static final String LOG_TAG = MediaService.class.getSimpleName();

    public interface StartImageUploadCallback {
        void success(UploadInstructionsV1 instructions, Map<String, String> error);

        void failure(RetrofitError error);
    }

    public interface CompleteImageUploadCallback {
        void success(String mediaUrl, Map<String, String> errors);

        void failure(RetrofitError error);
    }

    private static void startImageUpload(MediaTypeV1 mediaType, String mediaKey, final StartImageUploadCallback cb) {
        StartImageUploadRequestV1 request = new StartImageUploadRequestV1.Builder()
                .media_key(mediaKey)
                .media_type(mediaType)
                .build();

        ServiceClient serviceClient = new ServiceClient("media");
        serviceClient.callAction(
                Ext_requests.start_image_upload,
                Ext_responses.start_image_upload,
                request,
                new Callback<WrappedResponse>() {
                    @Override
                    public void success(WrappedResponse wrappedResponse) {
                        StartImageUploadResponseV1 response = (StartImageUploadResponseV1) wrappedResponse.getResponse();
                        UploadInstructionsV1 uploadInstructions = response != null ? response.upload_instructions : null;
                        cb.success(uploadInstructions, wrappedResponse.getErrors());
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        cb.failure(error);
                    }
                }
        );
    }

    private static void completeImageUpload(MediaTypeV1 mediaType, String mediaKey, String uploadId, String uploadKey, final CompleteImageUploadCallback cb) {
        CompleteImageUploadRequestV1 request = new CompleteImageUploadRequestV1.Builder()
                .media_type(mediaType)
                .media_key(mediaKey)
                .upload_id(uploadId)
                .upload_key(uploadKey)
                .build();

        ServiceClient serviceClient = new ServiceClient("media");
        serviceClient.callAction(Ext_requests.complete_image_upload, Ext_responses.complete_image_upload, request, new Callback<WrappedResponse>() {
            @Override
            public void success(WrappedResponse wrappedResponse) {
                CompleteImageUploadResponseV1 response = (CompleteImageUploadResponseV1) wrappedResponse.getResponse();
                String mediaUrl = response != null ? response.media_url : null;
                cb.success(mediaUrl, wrappedResponse.getErrors());
            }

            @Override
            public void failure(RetrofitError error) {
                cb.failure(error);
            }
        });
    }

    public static void uploadProfileImage(final ProfileV1 profile, final String imageFilePath, final CompleteImageUploadCallback cb) {
        startImageUpload(MediaTypeV1.PROFILE, profile.id, new StartImageUploadCallback() {
            @Override
            public void success(final UploadInstructionsV1 instructions, Map<String, String> error) {
                if (instructions != null) {
                    uploadFile(instructions.upload_url, imageFilePath, new retrofit.Callback<String>() {
                        @Override
                        public void success(String s, Response response) {
                            completeImageUpload(MediaTypeV1.PROFILE, profile.id, instructions.upload_id, instructions.upload_key, cb);
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            cb.failure(error);
                        }
                    });
                } else {
                    Log.d(LOG_TAG, "Did not get upload instructions back");
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    private static void uploadFile(String uploadUrl, String imageFilePath, final retrofit.Callback<String> cb) {

        String extension = imageFilePath.substring(imageFilePath.lastIndexOf(".") + 1);
        URL mediaUploadURL = null;
        try {
            mediaUploadURL = new URL(uploadUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        if (mediaUploadURL != null) {
            TypedFile file = new TypedFile("image/" + extension, new File(imageFilePath));
            RestAdapter adapter = new RestAdapter.Builder()
                    .setEndpoint(mediaUploadURL.getProtocol() + "://" + mediaUploadURL.getHost())
                    .build();
            FileUploader uploader = adapter.create(FileUploader.class);
            uploader.upload(mediaUploadURL.getFile().substring(1), file, cb);
        }
    }


    private interface FileUploader {

        @PUT("/{path}")
        void upload(@Path(value = "path", encode = false) String path, @Body TypedFile file, retrofit.Callback<String> callback);
    }
}
