package com.example.greenstamp;

import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.greenstamp.Controllers.AppsController;
import com.example.greenstamp.Interfaces.AptoideAPI;
import com.example.greenstamp.Models.AppDetails;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;
import retrofit2.Retrofit;

public class AppDetailsActivity extends AppCompatActivity {

    AptoideAPI aptoideAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    Context context = this;
    ImageView imageViewIcon;
    TextView textViewName;
    TextView textViewUname;
    TextView textViewSize;
    TextView textViewAge;
    TextView textViewDeveloper;
    TextView textViewStore;
    TextView textViewVersion;
    TextView textViewKeywords;
    TextView textViewDescription;
    TextView textViewRating;
    TextView textViewDownloads;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_details);

        imageViewIcon = findViewById(R.id.imageViewIcon);
        textViewName = findViewById(R.id.textViewName);
        textViewUname = findViewById(R.id.textViewUname);
        textViewSize = findViewById(R.id.textViewSize);
        textViewAge = findViewById(R.id.textViewAge);
        textViewDeveloper = findViewById(R.id.textViewDeveloper);
        textViewStore = findViewById(R.id.textViewStore);
        textViewVersion = findViewById(R.id.textViewVersion);
        textViewKeywords = findViewById(R.id.textViewKeywords);
        textViewDescription = findViewById(R.id.textViewDescription);
        textViewRating = findViewById(R.id.textViewRating);
        textViewDownloads = findViewById(R.id.textViewDownloads);

        Bundle extras = getIntent().getExtras();
        String appName = extras.getString("NAME");
        String packageName = extras.getString("PACKAGE");

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setTitle(appName);

        Retrofit retrofit = AppsController.getInstance();
        aptoideAPI = retrofit.create(AptoideAPI.class);

        getAppDetails(packageName);
    }

    private void getAppDetails(String packageName) {
        compositeDisposable.add(aptoideAPI.getAppDetails(packageName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(throwable -> {
                    if (throwable instanceof HttpException) {
                        HttpException httpException = (HttpException) throwable;
                        int code = httpException.code();
                        String errorMessage = httpException.getMessage();
                        displayError(code, errorMessage);
                    }
                    return Observable.empty();
                })
                .subscribe(apiResponse -> displayAppDetails(apiResponse.nodes.meta.data))
        );
    }

    private void displayAppDetails(AppDetails appDetails) {
        Picasso.get().load(appDetails.icon).into(imageViewIcon);
        textViewName.setText(appDetails.name);
        textViewUname.setText(context.getResources().getString(R.string.uname, appDetails.uname));
        textViewSize.setText(context.getResources().getString(R.string.size,
                String.valueOf(appDetails.size)));
        textViewAge.setText(context.getResources().getString(R.string.age, appDetails.age.name,
               appDetails.age.title, appDetails.age.pegi, String.valueOf(appDetails.age.rating)));
        textViewDeveloper.setText(context.getResources().getString(R.string.developer,
                appDetails.developer.name, appDetails.developer.website, appDetails.developer.email,
                appDetails.developer.privacy));
        textViewStore.setText(context.getResources().getString(R.string.store,
                appDetails.store.name));
        textViewVersion.setText(context.getResources().getString(R.string.version,
                appDetails.file.vername));
        textViewKeywords.setText(context.getResources().getString(R.string.keywords,
                appDetails.media.keywords));
        textViewDescription.setText(context.getResources().getString(R.string.description,
                appDetails.media.description));
        textViewRating.setText(context.getResources().getString(R.string.rating,
                String.valueOf(appDetails.stats.rating.avg)));
        textViewDownloads.setText(context.getResources().getString(R.string.downloads,
                String.valueOf(appDetails.stats.downloads)));
    }

    private void displayError(int code, String errorMessage) {
        textViewUname.setText(context.getResources().getString(R.string.error));
        textViewSize.setText(context.getResources().getString(R.string.error_message,
                String.valueOf(code), errorMessage));
    }
}