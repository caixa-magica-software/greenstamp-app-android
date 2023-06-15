package com.example.greenstamp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
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

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

import io.reactivex.schedulers.Schedulers;
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
    private TextView textViewCategories;
    private TextView textViewCategoryPosition;
    private TableLayout tableLayout;

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
        textViewCategories = findViewById(R.id.textViewCategory);
        textViewCategoryPosition = findViewById(R.id.textViewCategoryPosition);
        tableLayout = findViewById(R.id.tableLayoutResults);

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
                .subscribe(apiResponse -> displayAppDetails(apiResponse.nodes.meta.data),
                        this::displayError)
        );
    }

    private void getAppAnalysis() {
        ApiAnalysisBody apiAnalysisBody = new ApiAnalysisBody(appName, packageName, version);

        compositeDisposable.add(aptoideAPI2.getAppAnalysis(apiAnalysisBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::displayAppAnalysis, this::displayErrorAnalysis)
        );
    }

    private void displayAppDetails(AppDetails appDetails) {
        Picasso.get().load(appDetails.icon).into(imageViewIcon);
        textViewName.setText(appDetails.name);

        textViewUname.setText(formatString(context.getResources().getString(R.string.uname,
                appDetails.uname),6));

        textViewSize.setText(formatString(context.getResources().getString(R.string.size,
                String.valueOf(appDetails.size)),5));

        textViewAge.setText(formatString(context.getResources().getString(R.string.age,
                appDetails.age.name, appDetails.age.title, appDetails.age.pegi,
                String.valueOf(appDetails.age.rating)),3));

        textViewDeveloper.setText(formatString(context.getResources().getString(R.string.developer,
                appDetails.developer.name, appDetails.developer.website, appDetails.developer.email,
                appDetails.developer.privacy),8));

        textViewStore.setText(formatString(context.getResources().getString(R.string.store,
                appDetails.store.name), 6));

        textViewVersion.setText(formatString(context.getResources().getString(R.string.version,
                appDetails.file.vername), 8));

        textViewKeywords.setText(formatString(context.getResources().getString(R.string.keywords,
                appDetails.media.keywords), 9));

        textViewDescription.setText(formatString(context.getResources().getString(R.string.description,
                appDetails.media.description), 11));

        textViewRating.setText(formatString(context.getResources().getString(R.string.rating,
                String.valueOf(appDetails.stats.rating.avg)), 7));

        textViewDownloads.setText(formatString(context.getResources().getString(R.string.downloads,
                String.valueOf(appDetails.stats.downloads)), 10));
    }

    private void displayAppAnalysis(ApiAnalysisResponse apiAnalysisResponse) {
        if(apiAnalysisResponse.data != null) {
            if(apiAnalysisResponse.data.categories.size() > 0) {
                StringBuilder category = new StringBuilder();
                for (int i = 0; i < apiAnalysisResponse.data.categories.size(); i++) {
                    if (i == 0) {
                        category.append(apiAnalysisResponse.data.categories.get(i));
                    } else{
                        category.append(", "+apiAnalysisResponse.data.categories.get(i));
                    }
                }

                textViewCategories.setVisibility(View.VISIBLE);
                textViewCategories.setText(formatString(context.getResources().getString(R.string.category,
                        category), 10));

                textViewCategoryPosition.setVisibility(View.VISIBLE);
                textViewCategoryPosition.setText(formatString(context.getResources().getString(R.string.position,
                        String.valueOf(apiAnalysisResponse.data.ranking)), 8)); //Change hard coded position to received
            }

            createTable();

            List<ApiAnalysisResponse.Data.Result> results = apiAnalysisResponse.data.results;

            for (int i = 0; i < results.size(); i++) {
                LinearLayout.LayoutParams tableRowParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);

                TableRow tableRow = new TableRow(this);
                tableRow.setLayoutParams(tableRowParams);

                TextView textViewName = new TextView(this);
                textViewName.setText(results.get(i).name);
                textViewName.setTextSize(16);
                textViewName.setPadding(10, 6, 0, 6);

                TextView textViewParameters = new TextView(this);
                textViewParameters.setText(results.get(i).parameters);
                textViewParameters.setTextSize(16);
                textViewParameters.setPadding(10, 6, 0, 6);

                TextView textViewResult = new TextView(this);
                textViewResult.setText(results.get(i).result);
                textViewResult.setTextSize(16);
                textViewResult.setPadding(10, 6, 0, 6);

                TextView textViewUnit = new TextView(this);
                textViewUnit.setText(results.get(i).unit);
                textViewUnit.setTextSize(16);
                textViewUnit.setPadding(10, 6, 0, 6);

                TableRow.LayoutParams cellParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT);

                cellParams.weight = 1;
                cellParams.leftMargin = 2;
                textViewName.setLayoutParams(cellParams);
                textViewParameters.setLayoutParams(cellParams);
                textViewResult.setLayoutParams(cellParams);
                textViewUnit.setLayoutParams(cellParams);

                tableRow.addView(textViewName);
                tableRow.addView(textViewParameters);
                tableRow.addView(textViewResult);
                tableRow.addView(textViewUnit);

                tableLayout.addView(tableRow);
            }
        }
    }

    private void displayError(Throwable t) {
        textViewUname.setText(context.getResources().getString(R.string.error));
        textViewSize.setText(context.getResources().getString(R.string.error_message, t.getMessage()));
    }

    private void displayErrorAnalysis(Throwable t) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0,12,0,0);

        TextView textViewError = new TextView(this);
        textViewError.setLayoutParams(params);
        textViewError.setText(context.getResources().getString(R.string.error_results));
        textViewError.setTextSize(16);

        TextView textViewErrorMessage = new TextView(this);
        textViewErrorMessage.setLayoutParams(params);
        textViewErrorMessage.setText(context.getResources().getString(R.string.error_message, t.getMessage()));
        textViewErrorMessage.setTextSize(16);

        linearLayout.addView(textViewError);
        linearLayout.addView(textViewErrorMessage);
    }

    private SpannableString formatString(String string, int endSpan) {
        SpannableString ss = new SpannableString(string);
        StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
        ss.setSpan(boldSpan, 0, endSpan, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    private void createTable() {
        LinearLayout.LayoutParams tableRowParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        TableRow tableRow = new TableRow(this);
        tableRow.setLayoutParams(tableRowParams);

        TextView textViewName = new TextView(this);
        textViewName.setText(context.getResources().getString(R.string.name));
        textViewName.setTextSize(16);
        textViewName.setTextColor(Color.WHITE);
        textViewName.setTypeface(null, Typeface.BOLD);
        textViewName.setBackgroundResource(R.color.teal_700);
        textViewName.setPadding(10, 6, 0, 6);

        TextView textViewParameters = new TextView(this);
        textViewParameters.setText(context.getResources().getString(R.string.parameters));
        textViewParameters.setTextSize(16);
        textViewParameters.setTextColor(Color.WHITE);
        textViewParameters.setTypeface(null, Typeface.BOLD);
        textViewParameters.setBackgroundResource(R.color.teal_700);
        textViewParameters.setPadding(10, 6, 0, 6);

        TextView textViewResult = new TextView(this);
        textViewResult.setText(context.getResources().getString(R.string.result));
        textViewResult.setTextSize(16);
        textViewResult.setTextColor(Color.WHITE);
        textViewResult.setTypeface(null, Typeface.BOLD);
        textViewResult.setBackgroundResource(R.color.teal_700);
        textViewResult.setPadding(10, 6, 0, 6);

        TextView textViewUnit = new TextView(this);
        textViewUnit.setText(context.getResources().getString(R.string.unit));
        textViewUnit.setTextSize(16);
        textViewUnit.setTextColor(Color.WHITE);
        textViewUnit.setTypeface(null, Typeface.BOLD);
        textViewUnit.setBackgroundResource(R.color.teal_700);
        textViewUnit.setPadding(10, 6, 0, 6);

        TableRow.LayoutParams cellParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT);

        cellParams.weight = 1;
        cellParams.leftMargin = 2;
        textViewName.setLayoutParams(cellParams);
        textViewParameters.setLayoutParams(cellParams);
        textViewResult.setLayoutParams(cellParams);
        textViewUnit.setLayoutParams(cellParams);

        tableRow.addView(textViewName);
        tableRow.addView(textViewParameters);
        tableRow.addView(textViewResult);
        tableRow.addView(textViewUnit);

        tableLayout.addView(tableRow);
    }
}