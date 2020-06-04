package personal.project.souravtiwary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

// this class will make request and store result in the form of stringbuilder
public class VolleyClass extends AppCompatActivity {
    private TextView volley_info_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volley_class);
        volley_info_tv = findViewById(R.id.volley_info_tv);
        getRequestVolley();
    }

    private void getRequestVolley(){
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                "http://dummy.restapiexample.com/api/v1/employees",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            StringBuilder sb = new StringBuilder();
                            JSONArray jsonArray = (response.getJSONArray("data"));
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                sb.append("ID: " + jsonObject.getString("id") + "\n");
                                sb.append("Name: " + jsonObject.getString("employee_name") + "\n");
                                sb.append("Salary: " + jsonObject.getString("employee_salary") + "\n");
                                sb.append("Age: " + jsonObject.getString("employee_age") + "\n");
                                sb.append("Image: " + jsonObject.getString("profile_image") + "\n");
                                sb.append("\n");
                            }
                            if(sb.toString() != null){
                                String x = response.getString("status");
                                Toast.makeText(VolleyClass.this, x, Toast.LENGTH_SHORT).show();
                                volley_info_tv.setText(sb.toString());
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("VolleyError", "onErrorResponse: "+ error.getMessage());
                Toast.makeText(VolleyClass.this, "SomeThing went wrong", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}