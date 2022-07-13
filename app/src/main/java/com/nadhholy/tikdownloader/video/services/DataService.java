package com.nadhholy.tikdownloader.video.services;


    import com.nadhholy.tikdownloader.video.models.AwemRData;
    import com.nadhholy.tikdownloader.video.models.Aweme;


    import retrofit2.Call;
    import retrofit2.http.Body;
    import retrofit2.http.POST;

public interface DataService {
    @POST("/tik/v2/video/metadata")
    Call<Aweme> getVideoData(@Body AwemRData data);
}

