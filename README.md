# app-android

App android for the Greenstamp project.

The GreenStamp project aims to investigate and develop innovative mechanisms for analyzing and cataloging the energy efficiency of mobile applications integrated into app store processes.

```
https://greenstamp.caixamagica.pt/
```

## Developing with developer-service in localhost

```
if (retrofitInstance == null) {
    retrofitInstance = new Retrofit.Builder()
            .baseUrl("http://<localhost_ip>:3000/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();
}
```