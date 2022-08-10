package com.example.finalyearproject.Utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.finalyearproject.R;
import com.github.ybq.android.spinkit.SpinKitView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import de.hdodenhof.circleimageview.CircleImageView;

public class Utils {
    //set dropdown data
    public static void SpinnerAdapter(ArrayList<?> arrayList, Spinner spinner, int selectPos, Context context) {
        if (arrayList != null) {
            ArrayAdapter<?> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, arrayList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setSelection(selectPos);
        }
    }





    public static void loadimageNormal(String url, final ImageView imageView, final Context context,
                                       final SpinKitView progressBar) {
        if (context != null) {
            progressBar.setVisibility(View.VISIBLE);
            if (url == null || url.equals("")) {
                imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.no_image));
                progressBar.setVisibility(View.GONE);

            } else {
                Log.e("loadingimageurl", url);

                RequestOptions requestOptions = new RequestOptions();
                requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
                requestOptions.placeholder(context.getResources().getDrawable(R.drawable.loading_image)).dontAnimate();
                Glide.with(context)
                        .load(url)
                        .apply(requestOptions)
                        .error(R.drawable.no_image)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                Bitmap icon = BitmapFactory.decodeResource(context.getResources(),
                                        R.drawable.no_image);
                                progressBar.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                                progressBar.setVisibility(View.GONE);
                                return false;
                            }


                        })
                        .into(imageView);
            }

        }

    }

    public static void loadcircularimageNormal(String url, final CircleImageView imageView, final Context context,
                                               final SpinKitView progressBar) {
        if (context != null) {
            progressBar.setVisibility(View.VISIBLE);
            if (url == null || url.equals("")) {
                imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.no_image));
                progressBar.setVisibility(View.GONE);

            } else {
                Log.e("loadingimageurl", url);

                RequestOptions requestOptions = new RequestOptions();
                requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
                requestOptions.placeholder(context.getResources().getDrawable(R.drawable.loading_image)).dontAnimate();
                Glide.with(context)
                        .load(url)
                        .apply(requestOptions)
                        .error(R.drawable.no_image)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                Bitmap icon = BitmapFactory.decodeResource(context.getResources(),
                                        R.drawable.no_image);
                                progressBar.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                                progressBar.setVisibility(View.GONE);
                                return false;
                            }


                        })
                        .into(imageView);
            }

        }

    }



    public static void checknullandSetValueofString(String datavalue, TextView tobesetOn) {
        if (datavalue != null && !datavalue.equals("")) {
            tobesetOn.setText("" + datavalue);
        } else {
            tobesetOn.setText("Not Available");

        }

    }

    public static void startActivity(Context context, final Class<?> activityClass) {
        Intent intent = new Intent(context, activityClass);
        context.startActivity(intent);
    }

    public static void e(String tag, String msg) {

        Log.e(tag, msg);
    }

   /* public static boolean isInternetConnected(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo connection = manager.getActiveNetworkInfo();
        if (connection != null && connection.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }*/

    public static Dialog progressDialog(Context context) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progress_layout);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return dialog;
    }

    public static void printHashKey(Context pContext) {
        try {
            PackageInfo info = pContext.getPackageManager().getPackageInfo(pContext.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i("key", "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e("key", "printHashKey()", e);
        } catch (Exception e) {
            Log.e("key", "printHashKey()", e);
        }
    }

    public static void showToastMessage(String message, Context context) {

        if (message == null || message.equals("")) return;
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void dismissDialog(Dialog dialog) {
        if (dialog != null && dialog.isShowing()) dialog.dismiss();
    }

    public static void showDialog(Dialog dialog) {
        if (dialog != null && !dialog.isShowing()) dialog.show();
    }

    public static boolean isValidEmailed(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }


    public static void FragemntTransactionNormal(FragmentManager Manager, int containerID, Fragment fragment,
                                                 boolean isaddtoBackStack, String tagtext) {

        FragmentManager fragmentManager = Manager;
        if (fragmentManager != null) {

            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(containerID, fragment, tagtext);
            if (isaddtoBackStack) {
                transaction.addToBackStack(null);
            }
            transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
            transaction.commit();


        }

    }

    /*public static void FragemntTransactionId(Integer Menuid, FragmentManager Manager, int containerID, Fragment fragment,
                                             boolean isaddtoBackStack, String tagtext) {

        FragmentManager fragmentManager = Manager;
        if (fragmentManager != null) {
            Bundle bundle = new Bundle();
            bundle.putInt(Constants.PARAM_ID, Menuid);
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(containerID, fragment, tagtext);
            fragment.setArguments(bundle);
            if (isaddtoBackStack) {
                transaction.addToBackStack(null);
            }
            transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
            transaction.commit();


        }
    }

*/





}
