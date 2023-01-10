package com.example.greenstamp;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.greenstamp.Controllers.AppsAnalysisController;
import com.example.greenstamp.Controllers.AppsController;
import com.example.greenstamp.Interfaces.AptoideAPI;
import com.example.greenstamp.Models.ApiAnalysisBody;
import com.example.greenstamp.Models.ApiAnalysisResponse;
import com.example.greenstamp.Models.AppDetails;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;
import retrofit2.Retrofit;

public class AppDetailsActivity extends AppCompatActivity {

    private AptoideAPI aptoideAPI;
    private AptoideAPI aptoideAPI2;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private final Context context = this;
    private LinearLayout linearLayout;
    private ImageView imageViewIcon;
    private TextView textViewName;
    private TextView textViewUname;
    private TextView textViewSize;
    private TextView textViewAge;
    private TextView textViewDeveloper;
    private TextView textViewStore;
    private TextView textViewVersion;
    private TextView textViewKeywords;
    private TextView textViewDescription;
    private TextView textViewRating;
    private TextView textViewDownloads;

    private String appName;
    private String packageName;
    private long version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_details);

        linearLayout = findViewById(R.id.linearLayout);
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
        appName = extras.getString("NAME");
        packageName = extras.getString("PACKAGE");
        version = extras.getLong("VERSION");

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setTitle(appName);

        Retrofit retrofit = AppsController.getInstance();
        aptoideAPI = retrofit.create(AptoideAPI.class);

        Retrofit retrofit2 = AppsAnalysisController.getInstance();
        aptoideAPI2 = retrofit2.create(AptoideAPI.class);

        getAppDetails();
        getAppAnalysis();
    }

    private void getAppDetails() {
        compositeDisposable.add(aptoideAPI.getAppDetails(packageName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(throwable -> {
                    if (throwable instanceof HttpException) {
                        HttpException httpException = (HttpException) throwable;
                        int code = httpException.code();
                        String errorMessage = Objects.requireNonNull(Objects.requireNonNull(
                                httpException.response()).errorBody()).string();
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

    private void getAppAnalysis() {
        ApiAnalysisBody apiAnalysisBody = new ApiAnalysisBody(appName, packageName, version);

        compositeDisposable.add(aptoideAPI2.getAppAnalysis(apiAnalysisBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(throwable -> {
                    if (throwable instanceof HttpException) {
                        HttpException httpException = (HttpException) throwable;
                        int code = httpException.code();
                        String errorMessage = Objects.requireNonNull(Objects.requireNonNull(
                                httpException.response()).errorBody()).string();
                        displayErrorAnalysis(code, errorMessage);
                    }
                    return Observable.empty();
                })
                .subscribe(this::displayAppAnalysis)
        );
    }

    private void displayAppAnalysis(ApiAnalysisResponse apiAnalysisResponse) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0,12,0,0);

        List<ApiAnalysisResponse.Data.Result> results = apiAnalysisResponse.data.results;

        for (int i = 0; i < results.size(); i++) {
            TextView textView = new TextView(this);
            textView.setLayoutParams(params);
            textView.setText(context.getResources().getString(R.string.result,
                    results.get(i).name, results.get(i).parameters, results.get(i).result));
            textView.setTextColor(Color.BLACK);
            textView.setTextSize(16);
            linearLayout.addView(textView);
        }
    }

    private void displayError(int code, String errorMessage) {
        textViewUname.setText(context.getResources().getString(R.string.error));
        textViewSize.setText(context.getResources().getString(R.string.error_message,
                String.valueOf(code), errorMessage));
    }

    private void displayErrorAnalysis(int code, String errorMessage) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0,12,0,0);

        TextView textViewError = new TextView(this);
        textViewError.setLayoutParams(params);
        textViewError.setText(context.getResources().getString(R.string.error));
        textViewError.setTextColor(Color.BLACK);
        textViewError.setTextSize(16);

        TextView textViewErrorMessage = new TextView(this);
        textViewErrorMessage.setLayoutParams(params);
        textViewErrorMessage.setText(context.getResources().getString(R.string.error_message,
                String.valueOf(code), errorMessage));
        textViewErrorMessage.setTextColor(Color.BLACK);
        textViewErrorMessage.setTextSize(16);

        linearLayout.addView(textViewError);
        linearLayout.addView(textViewErrorMessage);
    }
}