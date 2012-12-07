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

package com.mclinic.search.task;

import java.io.IOException;
import java.util.List;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;
import com.burkeware.search.api.Context;
import com.burkeware.search.api.RestAssuredService;
import com.burkeware.search.api.resource.Resource;
import com.burkeware.search.api.util.StringUtil;
import com.mclinic.search.sample.domain.Cohort;
import com.mclinic.search.sample.domain.Patient;
import org.apache.lucene.queryParser.ParseException;

public class PatientLoaderTask extends AsyncTask<String, String, String> {

    private ProgressBar progressBar;

    public PatientLoaderTask(final ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar.setVisibility(ProgressBar.VISIBLE);
    }

    @Override
    protected void onPostExecute(final String s) {
        super.onPostExecute(s);
        progressBar.setVisibility(ProgressBar.INVISIBLE);
    }

    @Override
    protected String doInBackground(String... strings) {

        try {
            RestAssuredService service = Context.getService();
            Resource resource = Context.getResource("Cohort Resource");
            service.loadObjects(StringUtil.EMPTY, resource);
            List<Cohort> cohorts = service.getObjects(StringUtil.EMPTY, Cohort.class);
            for (Cohort cohort : cohorts) {
                resource = Context.getResource("Cohort Member Resource");
                service.loadObjects(cohort.getUuid(), resource);
                List<Patient> patients = service.getObjects(StringUtil.EMPTY, Patient.class);
                for (Patient patient : patients) {
                    resource = Context.getResource("Observation Resource");
                    service.loadObjects(patient.getUuid(), resource);
                }
            }
        } catch (ParseException e) {
            Log.e(this.getClass().getSimpleName(), "ParseException when trying to load patient", e);
        } catch (IOException e) {
            Log.e(this.getClass().getSimpleName(), "IOException when trying to load patient", e);
        }
        return "Success";
    }
}
