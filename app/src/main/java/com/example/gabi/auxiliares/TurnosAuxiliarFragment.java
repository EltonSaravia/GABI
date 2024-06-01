package com.example.gabi.auxiliares;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gabi.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TurnosAuxiliarFragment extends Fragment {

    private GridLayout calendarGrid;
    private TextView monthTextView;
    private Button prevMonthButton, nextMonthButton;
    private String token;
    private int trabajadorId;
    private Calendar currentMonth = Calendar.getInstance();
    private Map<String, String> turnosMap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_turnos_auxiliar, container, false);

        calendarGrid = view.findViewById(R.id.calendarGrid);
        monthTextView = view.findViewById(R.id.monthTextView);
        prevMonthButton = view.findViewById(R.id.prevMonthButton);
        nextMonthButton = view.findViewById(R.id.nextMonthButton);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyAppPrefs", getActivity().MODE_PRIVATE);
        token = sharedPreferences.getString("token", null);
        trabajadorId = sharedPreferences.getInt("trabajador_id", -1);

        prevMonthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentMonth.add(Calendar.MONTH, -1);
                updateMonthView();
            }
        });

        nextMonthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentMonth.add(Calendar.MONTH, 1);
                updateMonthView();
            }
        });

        updateMonthView();

        return view;
    }

    private void updateMonthView() {
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM yyyy", new Locale("es", "ES"));
        monthTextView.setText(monthFormat.format(currentMonth.getTime()).substring(0, 1).toUpperCase() + monthFormat.format(currentMonth.getTime()).substring(1));

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM", Locale.getDefault());
        obtenerTurnos(trabajadorId, dateFormat.format(currentMonth.getTime()));
    }

    private void obtenerTurnos(int trabajadorId, String mes) {
        String url = "https://residencialontananza.com/aux/mostrarTurnosMes.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        if (jsonResponse.getString("status").equals("success")) {
                            JSONArray turnos = jsonResponse.getJSONArray("turnos");
                            turnosMap = new HashMap<>();

                            for (int i = 0; i < turnos.length(); i++) {
                                JSONObject turno = turnos.getJSONObject(i);
                                String fechaInicioStr = turno.getString("fecha_inicio");
                                String tipo = turno.getString("tipo");

                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                                Date fechaInicio = sdf.parse(fechaInicioStr);

                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(fechaInicio);

                                String formattedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.getTime());
                                turnosMap.put(formattedDate, tipo);
                            }
                            populateCalendar();
                        } else {
                            Toast.makeText(getActivity(), jsonResponse.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException | java.text.ParseException e) {
                        Toast.makeText(getActivity(), "Error en la respuesta del servidor: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(getActivity(), "Error de red: " + error.getMessage(), Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("trabajador_id", String.valueOf(trabajadorId));
                params.put("mes", mes);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(stringRequest);
    }

    private void populateCalendar() {
        calendarGrid.removeAllViews();

        Calendar calendar = (Calendar) currentMonth.clone();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int i = 0; i < 42; i++) {
            TextView dayView = new TextView(getActivity());
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0;
            params.height = 0;
            params.rowSpec = GridLayout.spec(i / 7, 1f);
            params.columnSpec = GridLayout.spec(i % 7, 1f);
            params.setMargins(4, 4, 4, 4);
            dayView.setLayoutParams(params);
            dayView.setTextSize(16f);
            dayView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            if (i >= dayOfWeek && i < dayOfWeek + daysInMonth) {
                int dayOfMonth = i - dayOfWeek + 1;
                dayView.setText(String.valueOf(dayOfMonth));

                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String dateStr = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.getTime());

                if (turnosMap.containsKey(dateStr)) {
                    switch (turnosMap.get(dateStr)) {
                        case "diurno":
                            dayView.setBackgroundColor(Color.YELLOW);
                            break;
                        case "nocturno":
                            dayView.setBackgroundColor(Color.BLUE);
                            break;
                        case "vespertino":
                            dayView.setBackgroundColor(Color.GREEN);
                            break;
                        default:
                            dayView.setBackgroundColor(Color.LTGRAY);
                            break;
                    }
                } else {
                    dayView.setBackgroundColor(Color.LTGRAY);
                }
            } else {
                dayView.setText("");
                dayView.setBackgroundColor(Color.TRANSPARENT);
            }
            calendarGrid.addView(dayView);
        }
    }
}
