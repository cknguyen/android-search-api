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

package com.mclinic.search.adapter;

import java.text.SimpleDateFormat;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.mclinic.search.sample.domain.Observation;
import com.nribeka.search.R;

public class ObservationAdapter extends ArrayAdapter<Observation> {

    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");

    public ObservationAdapter(Context context, int textViewResourceId, List<Observation> items) {
        super(context, textViewResourceId, items);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.observation_list_item, null);
        }

        Observation obs = getItem(position);
        if (obs != null) {

            TextView textView = (TextView) v.findViewById(R.id.fieldname_text);
            if (textView != null)
                textView.setText(obs.getFieldName());

            textView = (TextView) v.findViewById(R.id.value_text);
            textView.setText(obs.getValueText());

            textView = (TextView) v.findViewById(R.id.encounterdate_text);
            textView.setText(dateFormat.format(obs.getObservationDate()));
        }
        return v;
    }
}
