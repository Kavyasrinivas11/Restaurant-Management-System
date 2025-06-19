package com.fab.restaurant.Network;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetResult {
    public static MyListener myListener;

    public void onNCHandle(Call<JsonObject> call, String res) {

        //IS NETWORK AVAILABLE PHASE FOR EACH CALLS IN FIRST PHASE SECURITY


        //ENQUEUE METHOD FOR API HANDLING RETROFIT
        call.enqueue(new Callback<JsonObject>() {
            @Override

            //SUCCESS INTERFACE
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                myListener.callback(response.body(), res);
            }

            //ERROR INTERFACE LISTENER
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                myListener.callback(null, res);
                call.cancel();
                t.printStackTrace();
            }
        });

    }


    public interface MyListener {

        public void callback(JsonObject result, String callNo);
    }

    public void setMyListener(MyListener Listener) {
        myListener = Listener;
    }

}
