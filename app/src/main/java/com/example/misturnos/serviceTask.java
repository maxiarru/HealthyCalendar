package com.example.misturnos;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class serviceTask extends AsyncTask {
    private Context httpContext;
    ProgressDialog progressDialog;
    public String resultadoAPI = "";
    public String linkrequestAPI = "";

    public serviceTask(Context ctx, String linkAPI){
        this.httpContext = ctx;
        this.linkrequestAPI = linkAPI;
        }

    @Override
    protected Object doInBackground(Object[] objects) {
        return null;
    }

    protected void onPreExecute(){
        super.onPreExecute();
        progressDialog = ProgressDialog.show(httpContext, "Procesando Solicitud","porfavor, espere");
     }

}
