package com.mobileappeng.threegorgeous.projrutransit;

import com.google.gson.Gson;


import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import java.util.stream.Collectors;

public class History_data_Json {
/*CompletableFuture.runAsync(() -> {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url("http://history.muffinlabs.com/date").build();
        Call call = okHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("err: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody responseBody = response.body();
                String str = responseBody == null ? null : responseBody.string();

                Gson gson = new Gson();
                Map map = gson.fromJson(str, Map.class);
                ArrayList<HashMap<String, String>> list = (ArrayList<HashMap<String, String>>) ((List) ((Map) map.get("data")).get("Events")).for()
                        .map(m -> {
                            Map tMap = (Map) m;
                            HashMap<String, String> rMap = new HashMap<>();
                            rMap.put("year", (String) tMap.get("year"));
                            rMap.put("text", (String) tMap.get("text"));
                            return rMap;
                        }).collect(Collectors.toList());
                System.out.println(list);
            }
*/
}
