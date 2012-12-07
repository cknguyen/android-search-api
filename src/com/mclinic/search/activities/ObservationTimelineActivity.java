/**
 * Copyright 2012 Muzima Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mclinic.search.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.burkeware.search.api.RestAssuredService;
import com.burkeware.search.api.util.StringUtil;
import com.mclinic.search.adapter.EncounterAdapter;
import com.mclinic.search.sample.domain.Observation;
import com.mclinic.search.sample.domain.Patient;
import com.mclinic.search.util.Constants;
import com.mclinic.search.util.FileUtils;
import com.nribeka.search.R;

public class ObservationTimelineActivity extends ListActivity {

    private Patient patient;

    private String fieldUuid;

    private ArrayList<Observation> observations = new ArrayList<Observation>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.observation_timeline);

        if (!FileUtils.storageReady()) {
            showCustomToast(getString(R.string.error, R.string.storage_error));
            finish();
        }

        String patientUuid = getIntent().getStringExtra(Constants.KEY_PATIENT_ID);
        patient = getPatient(patientUuid);

        fieldUuid = getIntent().getStringExtra(Constants.KEY_OBSERVATION_FIELD_ID);
        String fieldName = getIntent().getStringExtra(Constants.KEY_OBSERVATION_FIELD_NAME);

        setTitle(getString(R.string.app_name) + " > " + getString(R.string.view_patient_detail));

        TextView textView = (TextView) findViewById(R.id.title_text);
        textView.setText(fieldName);
    }

    private Patient getPatient(final String uuid) {
        Patient patient = null;
        try {
            RestAssuredService service = com.burkeware.search.api.Context.getService();
            patient = service.getObject("uuid:" + StringUtil.quote(uuid), Patient.class);
        } catch (Exception e) {
            Log.e(this.getClass().getSimpleName(), "Exception when trying to load patient", e);
        }
        return patient;
    }

    private void getObservations(final String uuid, final String fieldUuid) {
        List<Observation> objects = new ArrayList<Observation>();
        try {
            RestAssuredService service = com.burkeware.search.api.Context.getService();
            objects =
                    service.getObjects(
                            "patient:" + StringUtil.quote(uuid) + " AND concept:" + StringUtil.quote(fieldUuid),
                            Observation.class);
        } catch (Exception e) {
            Log.e(this.getClass().getSimpleName(), "Exception when trying to load patient", e);
        }

        observations.addAll(objects);
        ArrayAdapter<Observation> observationAdapter = new EncounterAdapter(this, R.layout.encounter_list_item, observations);
        setListAdapter(observationAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (patient != null && fieldUuid != null) {
            getObservations(patient.getUuid(), fieldUuid);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void showCustomToast(String message) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.toast_view, null);

        // set the text in the view
        TextView tv = (TextView) view.findViewById(R.id.message);
        tv.setText(message);

        Toast t = new Toast(this);
        t.setView(view);
        t.setDuration(Toast.LENGTH_LONG);
        t.setGravity(Gravity.CENTER, 0, 0);
        t.show();
    }
}
