package moviles.pruebajson;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import moviles.pruebajson.data.*;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListPostres#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListPostres extends Fragment {

    private ListView listaPostresView;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ListPostres() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListPostres.
     */
    // TODO: Rename and change types and number of parameters
    public static ListPostres newInstance(String param1, String param2) {
        ListPostres fragment = new ListPostres();
        Bundle args = new Bundle();
        args.putString( ARG_PARAM1, param1 );
        args.putString( ARG_PARAM2, param2 );
        fragment.setArguments( args );
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        if (getArguments() != null) {
            mParam1 = getArguments().getString( ARG_PARAM1 );
            mParam2 = getArguments().getString( ARG_PARAM2 );
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate( R.layout.fragment_list_postres, container, false );
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        listaPostresView = (ListView) getView().findViewById( R.id.listViewPostres );
        fetchHttpData();
        //new JsonTask().execute();
    }

    public class JsonTask extends AsyncTask<Void, Void, List<Postre>>{

        @Override
        protected List doInBackground(Void... voids) {
            List result = new ArrayList<>();
            HttpURLConnection con = null;
            try {
                // Establecer la conexión
                URL url = new URL("https://nubecolectiva.com/blog/tutos/demos/leer_json_android_java/datos/postres.json");
                con = (HttpURLConnection) url.openConnection();
                con.setConnectTimeout(15000);
                con.setReadTimeout(10000);
                // Obtener el estado del recurso
                int statusCode = con.getResponseCode();
                if(statusCode==200){
                    // Parsear el flujo con formato JSON
                    InputStream in = new BufferedInputStream(con.getInputStream());
                    GsonPostreParser parser = new GsonPostreParser();
                    result = parser.readJsonStream(in);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                if (con != null) {
                    con.disconnect();
                }
            }
            return result;
        }

        protected void onPostExecute(List<Postre> result) {
            /*
            Asignar los objetos de Json parseados al adaptador
             */
            if(result!=null) {
                ArrayAdapter postresAdapter;
                postresAdapter = new ArrayAdapter<>(
                        getActivity(),
                        android.R.layout.simple_list_item_1,
                        result);
                listaPostresView.setAdapter( postresAdapter );
            }
        }
    }

    private void fetchHttpData() {
        Future<List<Postre>> future = executorService.submit(new Callable<List<Postre>>() {
            @Override
            public List<Postre> call() throws Exception {
                List<Postre> result = new ArrayList<>();
                HttpURLConnection urlConnection = null;
                try {
                    URL url = new URL("https://nubecolectiva.com/blog/tutos/demos/leer_json_android_java/datos/postres.json");
                    urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    GsonPostreParser parser = new GsonPostreParser();
                    result = parser.readJsonStream(in);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
                return result;
            }
        });

        // Obtener el resultado del Future y actualizar la UI
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                try {
                    ArrayAdapter postresAdapter;
                    postresAdapter = new ArrayAdapter<>(
                            getActivity(),
                            android.R.layout.simple_list_item_1,
                            future.get());
                    listaPostresView.setAdapter( postresAdapter );
                    // Aquí puedes, por ejemplo, actualizar un adaptador de un RecyclerView o ListView
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}